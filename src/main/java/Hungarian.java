import java.util.Arrays;

import static java.lang.Math.max;
import static java.lang.Math.min;

public class Hungarian {
    private  final int N = 55;
    private  final int INF = Integer.MAX_VALUE;
    private  int n=3, max_match;
    private  int[][] costMatrix = {{7, 4, 3}, {3, 1, 2}, {3, 0, 0}};
    private  int[] xLabels = new int[N];
    private  int[] yLabels = new int[N];
    private  int[] vertexMatchedWithElementsInX = new int[N];
    private  int[] vertexMatchedWithElementsInY = new int[N];
    private  boolean[] S = new boolean[N];
    private  boolean[] T = new boolean[N];
    private  int[] slack = new int[N];
    private  int[] slackX = new int[N];
    private  int[] alternatingPaths = new int[N];

    public Hungarian() {
    }

    private  void init_labels() {
        Arrays.fill(xLabels, 0);
        Arrays.fill(yLabels, 0);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                xLabels[i] = max(xLabels[i], costMatrix[i][j]);
            }
        }
    }

    private  void update_labels() {
        int delta = INF;
        for (int j = 0; j < n; j++) {
            if (!T[j]) {
                delta = min(delta, slack[j]);
            }
        }
        for (int i = 0; i < n; i++) {
            if (S[i]) xLabels[i] -= delta;
        }
        for (int j = 0; j < n; j++) {
            if (T[j]) yLabels[j] += delta;
        }
        for (int j = 0; j < n; j++) {
            if (!T[j]) {
                slack[j] -= delta;
            }
        }
    }

     private void add_to_tree(int currentVertex, int prevCurrentVertex) {
        S[currentVertex] = true;
        alternatingPaths[currentVertex] = prevCurrentVertex;
        for (int j = 0; j < n; j++) {
            if (xLabels[currentVertex] + yLabels[j] - costMatrix[currentVertex][j] < slack[j]) {
                slack[j] = xLabels[currentVertex] + yLabels[j] - costMatrix[currentVertex][j];
                slackX[j] = currentVertex;
            }
        }
    }

    private  void augment() {
        if (max_match == n) return;
        int writePos = 0, readPos = 0, root = 0,i,j;// check this.
        int[] bfsQueue = new int[N];
        Arrays.fill(S, false);
        Arrays.fill(T, false);
        Arrays.fill(alternatingPaths, -1);
        for (i = 0; i < n; i++) {
            if (vertexMatchedWithElementsInX[i] == -1) {
                bfsQueue[writePos++] = root = i;
                alternatingPaths[i] = -2;
                S[i] = true;
                break;
            }
        }

        for (j = 0; j < n; j++) {
            slack[j] = xLabels[root] + yLabels[j] - costMatrix[root][j];
            slackX[j] = root;
        }

        while (true) {
            while (readPos < writePos) {
                i = bfsQueue[readPos++];
                for (j = 0; j < n; j++) {
                    if (costMatrix[i][j] == xLabels[i] + yLabels[j] && !T[j]) {
                        if (vertexMatchedWithElementsInY[j] == -1) {
                            break;
                        }
                        T[j] = true;
                        bfsQueue[writePos++] = vertexMatchedWithElementsInY[j];
                        add_to_tree(vertexMatchedWithElementsInY[j], i);
                    }
                }
                if (j < n) break;
            }
            if (j < n) break;

            update_labels();

            writePos = readPos = 0;
            for (j = 0; j < n; j++) {
                if (!T[j] && slack[j] == 0) {
                    if (vertexMatchedWithElementsInY[j] == -1) {
                        i = slackX[j];
                        break;
                    } else {
                        T[j] = true; //else just add y to T,
                        if (!S[vertexMatchedWithElementsInY[j]]) {
                            bfsQueue[writePos++] = vertexMatchedWithElementsInY[j];
                            add_to_tree(vertexMatchedWithElementsInY[j], slackX[j]);
                        }
                    }
                }
            }
            if (j < n) {
                break;
            }
        }

        if (j < n) {
            max_match++;
            for (int cx = i, cy = j, ty; cx != -2; cx = alternatingPaths[cx], cy = ty) {
                ty = vertexMatchedWithElementsInX[cx];
                vertexMatchedWithElementsInY[cy] = cx;
                vertexMatchedWithElementsInX[cx] = cy;
            }
            augment();
        }
    }

    public static void main(String[] args) {
        Hungarian hungarian = new Hungarian();
        int minimalCost = 0;
        hungarian.max_match = 0;
        Arrays.fill(hungarian.vertexMatchedWithElementsInX, -1);
        Arrays.fill(hungarian.vertexMatchedWithElementsInY, -1);
        hungarian.init_labels();
        hungarian.augment();
        for (int i = 0; i < hungarian.n; i++) {
            minimalCost += hungarian.costMatrix[i][hungarian.vertexMatchedWithElementsInX[i]];
        }
        System.out.println(minimalCost);
        System.out.println("Hello Hungarian");
    }
}


