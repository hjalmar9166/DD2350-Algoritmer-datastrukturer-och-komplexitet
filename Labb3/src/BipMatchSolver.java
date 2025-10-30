import java.util.*;

public class BipMatchSolver {
    Kattio io;
    int X;
    int Y;
    int[][] bipEdges;

    int v;
    int s;
    int t;
    int[][] flowEdges;

    int maximumMatch;
    int[][] bipMatch;

    void readBipartiteGraph() {
        // Läs antal hörn och kanter
        int x = io.getInt();
        int y = io.getInt();
        int e = io.getInt();

        X = x;
        Y = y;
        bipEdges = new int[e][];

        // Läs in kanterna
        for (int i = 0; i < e; ++i) {
            int a = io.getInt();
            int b = io.getInt();
            bipEdges[i] = new int[]{a, b};
        }
    }

    void createFlowGraph() {
        v = X + Y + 2; // Två nya noder: sours och sink
        int e = bipEdges.length; // Antal kanter i bipartita grafen.

        s = v - 1;
        t = v;

        flowEdges = new int[e+X+Y][]; // Tillkommer X + Y nya kanter.

        // Lägg till alla kanter från bip.-grafen.
        for (int i = 0; i < e; i++) {
            int a = bipEdges[i][0];
            int b = bipEdges[i][1];
            int c = 1;
            flowEdges[i] = new int[]{a, b, c};
        }
        // Lägg till kanter från s till X.
        for (int i = 0; i < X; i++) {
            int c = 1;
            flowEdges[e+i] = new int[]{s, i+1, c};
        }
        // Lägg till kanter från Y till t.
        for (int i = X; i < X + Y; i++) {
            int c = 1;
            flowEdges[e+i] = new int[]{i+1, t, c};
        }
    }

    Hashtable<Integer, Integer>[] edmondsKarp(int v, int s, int t, int[][] edges) {
        // Arrayer av hashtabeller så vi kan få tillgång till c[a, b] på kort tid!
        Hashtable<Integer, Integer>[] c = new Hashtable[v+1];
        Hashtable<Integer, Integer>[] f = new Hashtable[v+1];
        Hashtable<Integer, Integer>[] cf = new Hashtable[v+1];


        for (int i = 0; i < edges.length; i++) {
            int a = edges[i][0];
            int b = edges[i][1];
            int capacity = edges[i][2];

            // Skapa hashtabell om ej finns.
            if (c[a] == null) {
                c[a] = new Hashtable<>();
                f[a] = new Hashtable<>();
                cf[a] = new Hashtable<>();
            }
            // c[a, b] = kapaciteten av kanten a till b.
            c[a].put(b, capacity);

            // Skapa hashtabell om ej finns.
            if (c[b] == null) {
                c[b] = new Hashtable<>();
                f[b] = new Hashtable<>();
                cf[b] = new Hashtable<>();
            }

            // Kapacitet åt andra hållet. Om värde redan finns ska det stå kvar. Annars sätt till 0.
            // Händer om grafen har kanter åt båda hållen (spelar ingen roll för bip.-graf).
            int revCapacity = 0;
            if (c[b].containsKey(a) && c[b].get(a) != 0) {
                revCapacity = c[b].get(a);
            }
            c[b].put(a, revCapacity);

            // f[a, b] = 0, f[b, a] = 0.
            f[a].put(b, 0);
            f[b].put(a, 0);

            // cf[a, b] = c[a, b], cf[b, a] = c[b, a].
            cf[a].put(b, capacity);
            cf[b].put(a, revCapacity);
        }

        // Edmons–Karps algoritm:
        while (true) {
            // Om vi hittar en stig från s till t.
            int[] parents = new int[v+1];
            if (!bfs(v, s, t, cf, parents)) {
                break;
            }

            int r = -1;
            int b = t;
            // Hitta flaskhals för flödet genom den funna stigen.
            while (b != s) {
                int a = parents[b];
                int cap = cf[a].get(b);
                if (r == -1 || cap < r) {
                    r = cap;
                }
                b = a;
            }

            b = t;
            // Ändra flöde och restkapacitet längs den funna vägen.
            while (b != s) {
                int a = parents[b];

                int fab = f[a].get(b) + r;
                f[a].put(b, fab);
                f[b].put(a, -fab);
                cf[a].put(b, c[a].get(b) - fab);
                cf[b].put(a, c[b].get(a) + fab);

                b = a;
            }
        }

        return f;
    }

    boolean bfs(int v, int s, int t, Hashtable<Integer, Integer>[] cf, int[] parents) {
        boolean[] visited = new boolean[v+1];
        Deque<Integer> queue = new ArrayDeque<>();
        queue.addLast(s);
        visited[s] = true;

        while (!queue.isEmpty()) {
            int u = queue.removeFirst();

            for (int i : cf[u].keySet()) {
                // Om nod ej besökts och kantens restkapacitet är större än noll kan mer flöda längs kanten
                // i riktning från u till i.
                if (!visited[i] && cf[u].get(i) > 0) {
                    queue.addLast(i);
                    visited[i] = true;
                    parents[i] = u;
                }
            }
        }

        return visited[t];
    }

    void createBipMatch(Hashtable<Integer, Integer>[] f) {
        // Flödet in i t är samma som negativa flödet ut från t.
        int flow = 0;
        for (int edgeFlow : f[t].values()) {
            flow += edgeFlow;
        }
        flow = -flow;
        // Flödet in i t är också den maximala matchningen.
        maximumMatch = flow;

        bipMatch = new int[maximumMatch][];
        int ind = 0;
        for (int a = 1; a < v+1; a++) {
            for (int b : f[a].keySet()) {
                // Ta endast med en kant med positivt flöde och som inte har en ändpunkt i s eller t.
                if (f[a].get(b) > 0 && a != s && b != t) {
                    bipMatch[ind] = new int[]{a, b};
                    ind++;
                }
            }
        }
    }

    void writeBipMatchSolution() {
        int x = X;
        int y = Y;
        int maxMatch = maximumMatch;

        // Skriv ut antal hörn och storleken på matchningen
        io.println(x + " " + y);
        io.println(maxMatch);

        for (int i = 0; i < maxMatch; ++i) {
            int a = bipMatch[i][0];
            int b = bipMatch[i][1];
            // Kant mellan a och b ingår i vår matchningslösning
            io.println(a + " " + b);
        }
        io.flush();
    }

    public BipMatchSolver() {
        io = new Kattio(System.in, System.out);
        readBipartiteGraph();
        createFlowGraph();
        Hashtable<Integer, Integer>[] f = edmondsKarp(v, s, t, flowEdges);
        createBipMatch(f);
        writeBipMatchSolution();
        io.close();
    }

    public static void main(String[] args) {
        new BipMatchSolver();
    }
}