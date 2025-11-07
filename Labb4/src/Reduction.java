import java.util.Arrays;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Reduction {
    Kattio io;

    int v; // Antal noder
    int e; // Antal kanter
    int m; // Antal färger
    int[][] edges;

    int n; // Antal roller
    int s; // Antal scener
    int k; // Antal skådespelare
    int[][] roles; // Skådespelare som kan spela en viss roll.
    int[][] scenes; // Roller som medverkar i en viss scen.

    public void readGraph() {
        v = io.getInt();
        e = io.getInt();
        m = io.getInt();

        edges = new int[e][];

        for (int i = 0; i < e; i++) {
            edges[i] = new int[2];
            edges[i][0] = io.getInt();
            edges[i][1] = io.getInt();
        }
    }

    public void reduce() {
        int numNodesWithEdges = 0;
        boolean[] hasEdge = new boolean[v];
        int[] subNum = new int[v];

        for (int i = 0; i < e; i++) {
            int a = edges[i][0];
            int b = edges[i][1];
            hasEdge[a-1] = true;
            hasEdge[b-1] = true;
        }


        if (!hasEdge[0]) {
            subNum[0] = 1;
        }
        for (int i = 1; i < v; i++) {
            subNum[i] = subNum[i-1];
            if (!hasEdge[i]) {
                subNum[i] += 1;
            }
            else {
                numNodesWithEdges++;
            }
        }

        n = numNodesWithEdges + 3;
        s = e + 2;
        k = m + 2;

        scenes = new int[s][];
        scenes[0] = new int[]{1, 3};
        scenes[1] = new int[]{2, 3};
        for (int i = 2; i < s; i++) {
            int a = edges[i-2][0];
            int b = edges[i-2][1];
            // +2 pga. vi lägger till divorna först, sedan tar vi bort antal
            // ensamma noder med lägre "nodtal" än denna nod.
            scenes[i] = new int[]{a + 2 - subNum[a-1], b + 2 - subNum[b-1]};
        }
    }

    public void writeRoleAssignment() {
        io.println(n);
        io.println(s);
        io.println(k);

        for (int i = 1; i < 4; i++) {
            io.println(1 + " " + i);
        }

        int[] allActors = new int[m]; // Alla skådespelare utom divorna.
        for (int i = 0; i < m; i++) {
            allActors[i] = i + 3;
        }
        for (int i = 3; i < n; i++) {
            printRow(allActors, allActors.length);
        }

        for (int i = 0; i < s; i++) {
            int rolesInScene = scenes[i].length;
            printRow(scenes[i], rolesInScene);
        }
        io.flush();
    }

    public void printRow(int[] row, int numInRow) {
        StringBuilder printStr = new StringBuilder("" + numInRow);
        for (int i = 0; i < numInRow; i++) {
            printStr.append(" ").append(row[i]);
        }
        io.println(printStr);
    }

    public Reduction() {
        io = new Kattio(System.in, System.out);
        readGraph();
        reduce();
        writeRoleAssignment();
    }

    public static void main(String[] args) {
        new Reduction();
    }
}