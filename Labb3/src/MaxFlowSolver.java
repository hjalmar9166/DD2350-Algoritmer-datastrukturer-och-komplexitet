import java.util.*;
import java.util.stream.IntStream;

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
        for (int i = 0; i < v+1; i++) {
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

        LinkedList<Integer>[] adjLists = new LinkedList[v+1];
        for (int i = 0; i < adjLists.length; i++) {
            adjLists[i] = new LinkedList<>();
        }

        for (int i = 0; i < edges.length; i++) {
            int a = edges[i][0];
            int b = edges[i][1];

            adjLists[a].add(b);
            adjLists[b].add(a);

            c[a][b] = edges[i][2];
            f[a][b] = 0;
            f[b][a] = 0;
            cf[a][b] = c[a][b];
            cf[b][a] = c[b][a];
        }

        while (true) {
            int[] parents = new int[v+1];
            if (!bfs(v, s, t, cf, parents, adjLists)) {
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

        return f;
    }

    boolean bfs(int v, int s, int t, int[][] cf, int[] parents, LinkedList<Integer>[] adjLists) {
        boolean[] visited = new boolean[v+1];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.addLast(s);
        visited[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.removeFirst();

            for (int i : adjLists[u]) {
                if (!visited[i] && cf[u][i] > 0) {
                    queue.addLast(i);
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
