public class BruteForce {
    private static int N = 3;
    private boolean[] jobstatus = new boolean[N];
    private int[] jobAssignedToWorker = new int[N];
    private int[][] costMatrix = {{7, 4, 3}, {3, 1, 2}, {3, 0, 0}};


    private static int indexOfMin(int[] a, int cnt) {
        int loc = 0;
        int min = a[loc];
        for (int i = 1; i < cnt; i++) {
            if (a[i] < min) {
                min = a[i];
                loc = i;
            }
        }
        return loc;
    }

    int kthSmallest(int arr[], int l, int r, int k) {
        if (k > 0 && k <= r - l + 1) {
            int pos = partition(arr, l, r);
            if (pos - l == k - 1)
                return arr[pos];
            if (pos - l > k - 1)
                return kthSmallest(arr, l, pos - 1, k);
            return kthSmallest(arr, pos + 1, r, k - pos + l - 1);
        }
        return Integer.MAX_VALUE;
    }

    void swap(int a, int b) {
        int temp = a;
        a = b;
        b = temp;
    }

    int partition(int arr[], int l, int r) {
        int x = arr[r], i = l;
        for (int j = l; j <= r - 1; j++) {
            if (arr[j] <= x) {
                swap(arr[i], arr[j]);
                i++;
            }
        }
        swap(arr[i], arr[r]);
        return i;
    }

    private void hungarian() {
        int[] costVector;
        for (int i = 0; i < N; i++) {
            costVector = costMatrix[i];
            int k=1;
            while (true) {
                int kthSmallestIndex =  kthSmallest(costVector, 0, N-1, k);
                if (jobstatus[kthSmallestIndex] != true && kthSmallestIndex < N) {
                    jobstatus[kthSmallestIndex] = true;
                    jobAssignedToWorker[i] = kthSmallestIndex;
                    break;
                }
                k++;
            }
        }
    }

    private int optimalCost() {
        int cost = 0;
        for (int i = 0; i < N; i++) {
            cost += costMatrix[i][jobAssignedToWorker[i]];
        }
        return cost;
    }

    public static void main(String[] args) {
        BruteForce obj = new BruteForce();
        obj.hungarian();
        for (int i = 0; i < N; i++) {
            System.out.println(obj.jobAssignedToWorker[i]);
        }
        System.out.println(obj.optimalCost());
    }

}
