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
	}

	void edmondsKarps(int[][] edges) {
		for (int[] e : edges) {
			
		}
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

