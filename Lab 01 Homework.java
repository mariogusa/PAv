import java.util.Random;

public class GraphGenerator {
    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Usage: GraphGenerator <n> <k>");
            return;
        }
        
        int n = Integer.parseInt(args[0]);
        int k = Integer.parseInt(args[1]);
        
        // pentru k > 1, avem nevoie de cel putin 2*k noduri pentru a avea o clica si un set independent
        if (k > 1 && n < 2 * k) {
            System.err.println("Impossible to have a clique and a stable set of size " + k + " in a graph with " + n + " vertices.");
            return;
        }
        
        boolean[][] matrix = new boolean[n][n];
        Random rand = new Random();
        long startTime = System.currentTimeMillis();
        
        // clica pe nodurile de la 0 la k-1 (complet conectat)
        for (int i = 0; i < k; i++) {
            for (int j = i + 1; j < k; j++) {
                matrix[i][j] = true;
                matrix[j][i] = true;
            }
        }

        // completam perechile ramase cu muchii aleatorii
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (i < k && j < k) continue;
                if (i >= k && i < 2 * k && j >= k && j < 2 * k) continue;
                boolean edge = rand.nextBoolean();
                matrix[i][j] = edge;
                matrix[j][i] = edge;
            }
        }
        
        // numaram muchiile si calculam gradele
        int edgeCount = 0;
        int[] degrees = new int[n];
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                if (matrix[i][j]) {
                    edgeCount++;
                    degrees[i]++;
                    degrees[j]++;
                }
            }
        }
        
        int maxDegree = 0;
        int minDegree = n; // gradul maxim este n
        int sumDegrees = 0;
        for (int i = 0; i < n; i++) {
            sumDegrees += degrees[i];
            if (degrees[i] > maxDegree) {
                maxDegree = degrees[i];
            }
            if (degrees[i] < minDegree) {
                minDegree = degrees[i];
            }
        }
        
        System.out.println("Number of edges m: " + edgeCount);
        System.out.println("Maximum degree Δ(G): " + maxDegree);
        System.out.println("Minimum degree δ(G): " + minDegree);
        System.out.println("Verification: Sum of degrees = " + sumDegrees + " and 2*m = " + (2 * edgeCount));
    }
}
