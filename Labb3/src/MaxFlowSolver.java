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
        io.println(3*flow);
        for (int a = 0; a < v; a++) {
            for (int b = 0; b < v; b++) {
                if (f[a][b] > 0) {
                    io.println(a + " " + b + " " + f[a][b]);
                }
            }
        }
        io.flush();
    }

    int[][] edmondsKarp(int v, int s, int t, int[][] edges) {
        int[][] c = new int[v][v];
        int[][] f = new int[v][v];
        int[][] cf = new int[v][v];

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
            int[] parents = new int[v];
            if (!bfs(v, s, t, cf, parents)) {
                break;
            }

            int r = -1;
            int b = t;
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

        return f;
    }

    boolean bfs(int v, int s, int t, int[][] cf, int[] parents) {
        boolean[] visited = new boolean[v];
        Queue<Integer> queue = new PriorityQueue<>();
        queue.add(s);
        visited[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.poll();

            for (int i = 0; i < v; i++) {
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
