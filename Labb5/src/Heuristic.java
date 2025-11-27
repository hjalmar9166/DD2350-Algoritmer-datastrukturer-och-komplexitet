import com.sun.tools.javac.Main;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public class Heuristic {
    Random rnd = new Random();
    Kattio io;

    int n; // Antal roller
    int s; // Antal scener
    int k; // Antal skådespelare
    int[][] roles; // Skådespelare som kan spela en viss roll.
    int[][] scenes; // Roller som medverkar i en viss scen.

    Map<Integer, LinkedList<Integer>> givenRoles;

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
        boolean[][] shareScene = createRoleAdjacencyLists();
        giveTheDivasTheirRoles(actorsPlayingRoles, shareScene);
        giveActorsToRemainingRoles(actorsPlayingRoles, shareScene);
        givenRoles = createListOfActorsWithRoles();
    }

    boolean[][] createRoleAdjacencyLists() {
        boolean[][] shareScene = new boolean[n][n];
        for (int[] scene : scenes) {
            addSceneToAdjList(shareScene, scene);
        }
        return shareScene;
    }

    void addSceneToAdjList(boolean[][] shareScene, int[] scene) {
        for (int i = 0; i < scene.length-1; i++) {
            for (int j = i + 1; j < scene.length; j++) {
                shareScene[i][j] = true;
                shareScene[j][i] = true;
            }
        }
    }

    void giveTheDivasTheirRoles(int[] actorsPlayingRoles, boolean[][] shareScene) {
        for (int i = 0; i < n; i++) {
            if (roles[i][0] != 1) {
                continue;
            }

            for (int j = 0; j < n; j++) {
                if (j != i && (roles[j][0] == 2 || roles[j][1] == 2) && !shareScene[i][j]) {
                    actorsPlayingRoles[i] = 1;
                    actorsPlayingRoles[j] = 2;
                    return;
                }
            }
        }
    }

    void giveActorsToRemainingRoles(int[] actorsPlayingRoles, boolean[][] shareScene) {
        int superActor = k + 1;
        for (int i = 0; i < n; i++) {
            if (actorsPlayingRoles[i] != 0) {
                continue;
            }

            assignActorToRole(actorsPlayingRoles, shareScene, i, roles[i].length);

            if (actorsPlayingRoles[i] == 0) {
                actorsPlayingRoles[i] = superActor;
                superActor++;
            }
        }
    }

    void assignActorToRole(int[] actorsPlayingRoles, boolean[][] shareScene, int roleNum, int numActorsForRole) {
        for (int i = 0; i < numActorsForRole; i++) {
            int actor = roles[roleNum][i];
            boolean invalidAssignment = false; //shares scene with self or divas sharing scene
            for (int j = 0; j < n; j++) {
                if ((actor == 1 || actor == 2) && (actorsPlayingRoles[j] == 1 || actorsPlayingRoles[j] == 2)) {
                    invalidAssignment = true;
                }
                if (shareScene[actor-1][j] && actorsPlayingRoles[j] == actor) {
                    invalidAssignment = true;
                }
            }

            if (!invalidAssignment) {
                actorsPlayingRoles[roleNum] = actor;
                break;
            }
        }
    }

    Map<Integer, LinkedList<Integer>> createListOfActorsWithRoles(int[] actorsPlayingRoles) {
        Map<Integer, LinkedList<Integer>> rolesOfActors = new HashMap<>();
        for (int i = 0; i < n; i++) {
            LinkedList actorsRoles = rolesOfActors.get(actorsPlayingRoles[i]);
            if (actorsRoles == null) {
                actorsRoles = new LinkedList<>();
            }
            actorsRoles.add(i+1);
        }

        return rolesOfActors;
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
