import com.sun.tools.javac.Main;
import java.util.Arrays;
import java.util.Random;

public class Heuristic {
    Random rnd = new Random();
    Kattio io;

    int n; // Antal roller
    int s; // Antal scener
    int k; // Antal skådespelare
    int[][] roles; // Skådespelare som kan spela en viss roll.
    int[][] scenes; // Roller som medverkar i en viss scen.

    int[][] givenRoles;

    /**
     * Läser in en probleminstans och lagrar nödvändig data.
     */
    void read() {

        n = io.getInt();
        s = io.getInt();
        k = io.getInt();

        roles = new int[n][];

        for (int i = 0; i < n; i++) {
            int numActorsForRole = io.getInt();
            roles[i] = new int[numActorsForRole];
            for (int j = 0; j < numActorsForRole; j++) {
                roles[i][j] = io.getInt();
            }
        }

        scenes = new int[s][];

        for (int i = 0; i < s; i++) {
            int numRolesForScene = io.getInt();
            scenes[i] = new int[numRolesForScene];
            for (int j = 0; j < numRolesForScene; j++) {
                scenes[i][j] = io.getInt();
            }
        }
    }

    /**
     * Skriver ut certifikat för probleminstans.
     */
    void write() {
        int numActors = givenRoles.length;

        io.println(numActors);

        for (int i = 0; i < numActors; i++) {
            int numRoles = givenRoles[i].length;
            if (numRoles > 0) {
                io.println((i + 1) + " " + numRoles + " " + getActorsRoles(givenRoles[i]));
            }
        }
    }

    /**
     * Sätter ihop en textsträng med en av rollerna som en skådespelare tilldelats.
     * @param givenRoles Int-lista med rollnummer.
     * @return Textsträng av rollistan.
     */
    String getActorsRoles(int[] givenRoles) {
        String returnString = "";
        for (int i = 0; i < givenRoles.length; i++) {
            returnString += givenRoles[i] + " ";
        }

        return returnString;
    }

    /**
     * Löser problemet!
     */
    void solve() {
        int[] actorsPlayingRoles = new int[n];

        setup(actorsPlayingRoles);
        

    }

    void setup(int[] actorsPlayingRoles) {
        for (int i = 0; i < n; i++) {
            actorsPlayingRoles[i] = roles[i][rnd.nextInt(roles[i].length)];
        }
    }

    Heuristic() {
        read();
        solve();
        write();
    }

    void main(String[] args) {
        io = new Kattio(System.in, System.out);
    }
}
