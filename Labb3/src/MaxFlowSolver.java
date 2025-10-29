import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.Queue;

public class MaxFlowSolver {
    Kattio io;
    int v;
    int s;
    int t;
    int[][] edges;
    int[][] f;

    void readFlowGraph() {
        v = io.getInt();
        s = io.getInt();
        t = io.getInt();
        int e = io.getInt();

        edges = new int[e][3];

        for (int i = 0; i < e; i++) {
            int a = io.getInt();
            int b = io.getInt();
            int c = io.getInt();
            edges[i] = new int[]{a, b, c};
        }
    }

    void writeFlowSolution() {
        io.println(v);
        int flow = 0;
        for (int i = 0; i < v; i++) {
            flow += f[t][i];
        }
        flow = -flow;

        io.println(s + " " + t + " " + flow);
        int numPosEdges = 0;
        for (int a = 1; a < v+1; a++) {
            for (int b = 1; b < v+1; b++) {
                if (f[a][b] > 0) {
                    numPosEdges++;
                }
            }
        }
        io.println(numPosEdges);
        for (int a = 1; a < v+1; a++) {
            for (int b = 1; b < v+1; b++) {
                if (f[a][b] > 0) {
                    io.println(a + " " + b + " " + f[a][b]);
                }
            }
        }
        io.flush();
    }

    int[][] edmondsKarp(int v, int s, int t, int[][] edges) {
        int[][] c = new int[v+1][v+1];
        int[][] f = new int[v+1][v+1];
        int[][] cf = new int[v+1][v+1];

        for (int i = 0; i < edges.length; i++) {
            int a = edges[i][0];
            int b = edges[i][1];
            c[a][b] = edges[i][2];
            c[b][a] = -c[a][b];
            f[a][b] = 0;
            f[b][a] = 0;
            cf[a][b] = c[a][b];
            cf[b][a] = c[b][a];
        }

        while (true) {
            int[] parents = new int[v+1];
            if (!bfs(v, s, t, cf, parents)) {
                break;
            }

            int r = -1;
            int b = t;
            // Hitta flaskhals för flödet genom den funna stigen.
            while (b != s) {
                int a = parents[b];
                if (r == -1 || cf[a][b] < r) {
                    r = cf[a][b];
                }
                b = a;
            }

            b = t;
            while (b != s) {
                int a = parents[b];

                f[a][b] += r;
                f[b][a] = -f[a][b];
                cf[a][b] = c[a][b] -f[a][b];
                cf[b][a] = c[b][a] -f[b][a];

                b = a;
            }
        }

        tempPrintFlow(f);

        return f;
    }

    void tempPrintFlow(int[][] f) {
        for (int i = 0; i < f.length; i++) {
            System.out.println(Arrays.toString(f[i]));
        }
        System.out.println("=====");
    }

    boolean bfs(int v, int s, int t, int[][] cf, int[] parents) {
        boolean[] visited = new boolean[v+1];
        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(s);
        visited[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int i = 1; i < v+1; i++) {
                if (!visited[i] && cf[u][i] > 0) {
                    queue.add(i);
                    visited[i] = true;
                    parents[i] = u;
                }
            }
        }

        return visited[t];
    }

    MaxFlowSolver() {
        io = new Kattio(System.in, System.out);

        readFlowGraph();

        f = edmondsKarp(v, s, t, edges);

        writeFlowSolution();

        io.close();
    }

    public static void main(String args[]) {
        new MaxFlowSolver();
    }
}
