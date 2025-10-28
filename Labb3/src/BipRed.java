import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Exempel på in- och utdatahantering för maxflödeslabben i kursen
 * ADK.
 *
 * Använder Kattio.java för in- och utläsning.
 * Se http://kattis.csc.kth.se/doc/javaio
 *
 * @author: Per Austrin
 */
public class BipRed {
    Kattio io;
	int X;
	int Y;
	int[][] bipEdges;
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

    void writeFlowGraph() {
		int v = X + Y;
		int s = v + 1;
		int t = v + 2;
		int e = bipEdges.length;

		// Skriv ut antal hörn och kanter samt källa och sänka
		io.println(v + 2);
		io.println(s + " " + t);
		io.println(e);
		for (int i = 0; i < e; ++i) {
			int a = bipEdges[i][0];
			int b = bipEdges[i][1];
			int c = 1;
			// Kant från a till b med kapacitet c
			io.println(a + " " + b + " " + c);
		}
		for (int i = 0; i < X; i++) {
			int c = 1;
			io.println(s + " " + i + 1 + " " + c);
		}
		for (int i = X; i < X + Y; i++) {
			int c = 1;
			io.println(i + 1 + " " + t + " " + c);
		}
		// Var noggrann med att flusha utdata när flödesgrafen skrivits ut!
		io.flush();

		// Debugutskrift
		System.err.println("Skickade iväg flödesgrafen");
    }

	void solveFlowProblem() {
		int v = io.getInt();
		int s = io.getInt();
		int t = io.getInt();
		int e = io.getInt();

		int[][] edges = new int[e][3];

		for (int i = 0; i < e; i++) {
			int a = io.getInt();
			int b = io.getInt();
			int c = io.getInt();
			edges[i] = new int[]{a, b, c};
		}

		int[][] f = edmondsKarp(s, t, v, edges);

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
				if (f[a][b] == 1) {
					io.println(a + " " + b + " " + 1);
				}
			}
		}
	}

	int[][] edmondsKarp(int v, int s, int t, int[][] edges) {
		int[][] c = new int[v][v];
		int[][] f = new int[v][v];
		int[][] cf = new int[v][v];

		int[][] adjMatrix = new int[v][v];

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

			int r = 1; // Skippar check för det enda kapaciteten kan vara är 0 eller 1 och i et att > 1 pga. BFS.

			int b = t;
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

    void readMaxFlowSolution() {
		// Läs in antal hörn, kanter, källa, sänka, och totalt flöde
		// (Antal hörn, källa och sänka borde vara samma som vi i grafen vi
		// skickade iväg)
		int v = io.getInt();
		int s = io.getInt();
		int t = io.getInt();
		int totflow = io.getInt();
		int e = io.getInt();

		maximumMatch = totflow;
		bipMatch = new int[maximumMatch][];

		for (int i = 0; i < e; ++i) {
			// Flöde f från a till b
			int a = io.getInt();
			int b = io.getInt();
			int f = io.getInt();

			if (b != t) {
				bipMatch[i] = new int[]{a, b};
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
    
    BipRed() {
		io = new Kattio(System.in, System.out);

		readBipartiteGraph();

		writeFlowGraph();

		solveFlowProblem();

		readMaxFlowSolution();

		writeBipMatchSolution();

		// debugutskrift
		System.err.println("Bipred avslutar\n");

		// Kom ihåg att stänga ner Kattio-klassen
		io.close();
    }
    
    public static void main(String args[]) {
		new BipRed();
    }
}

