/* Labb 2 i DD2350 Algoritmer, datastrukturer och komplexitet    */
/* Se labbinstruktionerna i kursrummet i Canvas                  */
/* Ursprunglig författare: Viggo Kann KTH viggo@nada.kth.se      */
import java.util.LinkedList;
import java.util.List;

public class ClosestWords {
    LinkedList<String> closestWords = null;

    int closestDistance = -1;
    static final int MAX_WORD_LENGTH = 40;
    int[][] M;

    int partDist(String w1, String w2, int w1len, int w2len, int startRow) {
        // Hitta värdena i varje cell i matrisen. Om förra ordet och detta överlappar kommer startRow
        // vara större än 1 och de först startRow rader kommer redan vara beräknade.
        for (int i = startRow; i < w2len + 1; i++) {
            for (int j = 1; j < w1len + 1; j++) {
                // Om bokstäverna redan är samma behöver vi inte byta bokstav.
                if(w1.charAt(j-1) == w2.charAt(i-1)) {
                    M[i][j] = M[i-1][j-1];
                }
                else {
                    int res = M[i-1][j-1];
                    // Se om det editeringsavståndet är kortare för "lägga till en bokstav".
                    if (M[i-1][j] < res) {
                        res = M[i-1][j];
                    }
                    // Se om det editeringsavståndet är kortare för "ta bort en bokstav".
                    if (M[i][j-1] < res) {
                        res = M[i][j - 1];
                    }
                    // I alla fall lägger vi till ett då editeringssteget är 1 mer än föregående.
                    M[i][j] = res + 1;
                }
            }
        }

        return M[w2len][w1len];
    }

    int distance(String w1, String w2, int startRow) {
        return partDist(w1, w2, w1.length(), w2.length(), startRow);
    }

    public ClosestWords(String w, List<String> wordList) {
        M = new int[MAX_WORD_LENGTH][MAX_WORD_LENGTH];
        setBaseCase();
        String prevWord = "";

        for (String s : wordList) {
            int startRow = comparePrevWordToNewWord(prevWord, s);
            int dist = distance(w, s, startRow);
            prevWord = s;
            // System.out.println("d(" + w + "," + s + ")=" + dist);
            if (dist < closestDistance || closestDistance == -1) {
                closestDistance = dist;
                closestWords = new LinkedList<String>();
                closestWords.add(s);
            }
            else if (dist == closestDistance)
                closestWords.add(s);
        }
    }

    private void setBaseCase() {
        // Sätt basfall för rader och kolumner.
        for (int i = 0; i < M.length; i++) {
            M[i][0] = i;
            M[0][i] = i;
        }
    }

    private int comparePrevWordToNewWord(String prevWord, String newWord) {
        // Om det inte finns något tidigare ord: börja på rad 1.
        if (prevWord.isEmpty()) {
            return 1;
        }

        // Gå igenom orden bokstav för bokstav och hitta första tecken som skiljer.
        for (int i = 1; i < prevWord.length() + 1; i++) {
            // Om bokstav i är olika.
            if(prevWord.charAt(i-1) != newWord.charAt(i-1)){
                return i;
            }
        }
        return prevWord.length();
    }

    int getMinDistance() {
        return closestDistance;
    }

    List<String> getClosestWords() {
        return closestWords;
    }

    private void printMatrix() {
        StringBuilder str = new StringBuilder();
        for (int[] row : M) {
            for (int dist : row) {
                str.append(dist).append(" ");
            }
            str.append("\n");
        }
        System.out.println(str);
    }
}