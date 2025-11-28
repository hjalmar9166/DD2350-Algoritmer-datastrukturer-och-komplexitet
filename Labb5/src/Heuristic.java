import com.sun.tools.javac.Main;

import javax.sound.sampled.Line;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;
import java.util.HashMap;

public class Heuristic {
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
        io.println(givenRoles.size());

        for (Map.Entry<Integer, LinkedList<Integer>> actor : givenRoles.entrySet()) {
            int actNum = actor.getKey();
            LinkedList<Integer> roles = actor.getValue();
            io.println(actNum + " " + roles.size() + " " + getActorsRoles(roles));
        }
        io.flush();
    }

    /**
     * Sätter ihop en textsträng med en av rollerna som en skådespelare tilldelats.
     * @param roles Int-lista med rollnummer.
     * @return Textsträng av rollistan.
     */
    String getActorsRoles(LinkedList<Integer> roles) {
        String returnString = "";
        for (int role : roles) {
            returnString += role + " ";
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
        givenRoles = createListOfActorsWithRoles(actorsPlayingRoles);
    }

    boolean[][] createRoleAdjacencyLists() {
        boolean[][] shareScene = new boolean[n][n];
        for (int[] rolesInScene : scenes) {
            addSceneToAdjList(shareScene, rolesInScene);
        }
        return shareScene;
    }

    void addSceneToAdjList(boolean[][] shareScene, int[] rolesInScene) {
        for (int i = 0; i < rolesInScene.length-1; i++) {
            for (int j = i + 1; j < rolesInScene.length; j++) {
                int role1 = rolesInScene[i]-1;
                int role2 = rolesInScene[j]-1;
                shareScene[role1][role2] = true;
                shareScene[role2][role1] = true;
            }
        }
    }

    void giveTheDivasTheirRoles(int[] actorsPlayingRoles, boolean[][] shareScene) {
        for (int i = 0; i < n; i++) {
            if (!canBePlayedByDiva(roles[i], 1)) {
                continue;
            }

            for (int j = 0; j < n; j++) {
                if (j != i && canBePlayedByDiva(roles[j], 2) && !shareScene[i][j]) {
                    actorsPlayingRoles[i] = 1;
                    actorsPlayingRoles[j] = 2;
                    return;
                }
            }
        }
    }

    boolean canBePlayedByDiva(int[] actors, int divaNum){
        for (int i = 0; i < actors.length; i++) {
            if (actors[i] == divaNum) {
                return true;
            }
        }

        return false;
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
                if ((actor == 1 || actor == 2) && shareScene[roleNum][j] && (actorsPlayingRoles[j] == 1 || actorsPlayingRoles[j] == 2)) {
                    invalidAssignment = true;
                }
                if (shareScene[roleNum][j] && actorsPlayingRoles[j] == actor) {
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
            LinkedList<Integer> actorsRoles = rolesOfActors.get(actorsPlayingRoles[i]);
            if (actorsRoles == null) {
                actorsRoles = new LinkedList<>();
            }
            actorsRoles.add(i+1);
            rolesOfActors.put(actorsPlayingRoles[i], actorsRoles);
        }

        return rolesOfActors;
    }

    Heuristic() {
        io = new Kattio(System.in, System.out);
        read();
        solve();
        write();
    }

    public static void main(String[] args) {
        new Heuristic();
    }
}
