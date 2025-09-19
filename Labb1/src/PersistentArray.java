public class PersistentArray {
    private static class Node {
        public Node leftChild;
        public Node rightChild;
        public int data;

        public Node(){}
    }

    private static class LinkedList {
        public Node treeRoot;
        public LinkedList previousRoot;
        public int length;
        public int logMaxIndex;

        public LinkedList(LinkedList previousRoot, int length, int logMaxIndex) {
            this.previousRoot = previousRoot;
            this.length = length;
            this.logMaxIndex = logMaxIndex;
        }
    }

    private LinkedList currentRoot; // Aktuella versionen av arrayen.

    /**
     * Skapar en ny beständig array. Arrayen har inga noder, och dess längd och maxindex är båda 0.
     */
    public PersistentArray() {
        this.currentRoot = new LinkedList(null,0, 0);
    }

    /**
     * Sätter värdet ett givet värde på given plats (index) i arrayn. Då arrayn är beständig skapas en
     * ny instans av arrayn där gamla versionen förblir intakt.
     * @param index Index på vilket värdet ska sättas in.
     * @param value Värde som ska sättas in i arrayen.
     */
    public void set(int index, int value) {
        int logIndex = (int) Math.floor(log2(index));
        int treeHeight = Math.max(currentRoot.logMaxIndex, logIndex + 1);

        Node nextOldNode = currentRoot.treeRoot;
        Node newTreeRoot = new Node();
        Node nextNewNode = newTreeRoot;

        nextOldNode = createLeftNodes(logIndex, nextNewNode, nextOldNode);

        // Går igenom trädet nivå för nivå och följer stigen from roten till det givna indexet.
        // Elementet på det sökta indexet genom att följa stigen som uppstår genom att gå till
        // siffra för siffra genom indexet (i binära talbasen) från vänster till höger (med start
        // i den mest signifikanta ettan) och gå till vänster i trädet om en siffta är 0 och till
        // höger om en siffra är 1. Om man vill åt elementet på index 2 i trädet (arrayen) nedan
        // går man först höger sedan vänster (2 (DEC) = 10 (BIN) -> 1 (höger), 0 (vänster)).
        //        o
        //      /   \
        //    o       o
        //   / \     / \
        // [0] [1] [2] [3]
        for (int j = treeHeight - 1; j >= 0; j--) {
            int direction = ((index >> j) & 1);
            // Om biten är 1, gå till höger i trädet
            if(direction == 1) {
                // Om gamla arrayen inte har noden finns inget att peka på för nya trädet.
                if (nextOldNode != null) {
                    nextNewNode.leftChild = nextOldNode.leftChild;
                    nextOldNode = nextOldNode.rightChild;
                }

                nextNewNode.rightChild = new Node();
                nextNewNode = nextNewNode.rightChild;
            }
            // Om biten är 0, gå till vänster i trädet.
            else {
                // Om gamla arrayen inte har noden finns inget att peka på för nya trädet.
                if (nextOldNode != null) {
                    nextNewNode.rightChild = nextOldNode.rightChild;
                    nextOldNode = nextOldNode.leftChild;
                }

                nextNewNode.leftChild = new Node();
                nextNewNode = nextNewNode.leftChild;
            }
        }

        nextNewNode.data = value; // Sätt det givna värdet på givet index.

        // Sätt längd på den nya arrayen.
        int newLength = currentRoot.length;
        // Om noden/lövet inte finns är indexet oanvänt. I så fall ökar arrayens längd.
        if (nextOldNode == null) {
            newLength += 1;
        }

        currentRoot = new LinkedList(currentRoot, newLength, treeHeight);
        currentRoot.treeRoot = newTreeRoot;
    }

    /**
     * Skapa nya noder till vänster om roten när trädet växer.
     * @param logIndex BIN-log av index för det nya noden (trädets nya höjd).
     * @param nextLeftNode Roten från vilken nya noder läggs till åt vänster.
     * @param nextOldNode Gamla rotnoden.
     * @return Null om nya noder läggs till, annas gamla roten.
     */
    private Node createLeftNodes(int logIndex, Node nextLeftNode, Node nextOldNode) {
        // Om index kräver fler nivåer än vad som finns i trädet måste noder läggas till
        // som pekar på rotnoden till det gamla trädet.
        // Ex: o                                     o <- Nya roten
        //    / \    -> Lägg till på index 2 ->    /   \
        //  [0] [1]               Gamla roten -> o       o
        //                                      / \     /
        //                                    [0] [1] [2]
        if(logIndex >= currentRoot.logMaxIndex) {
            int numNewNodes = logIndex - currentRoot.logMaxIndex;
            // Om trädet inte har några element behöver inga noder skapas.
            if (currentRoot.length != 0) {
                // Skapa antal noder som krävs för att det gamla trädets löv ska hamna längst
                // ner i det nya trädet.
                for (int j = numNewNodes; j > 0; j--) {
                    nextLeftNode.leftChild = new Node();
                    nextLeftNode = nextLeftNode.leftChild;
                }
            }

            // Sätt det gamla trädets rot till den sista noden.
            nextLeftNode.leftChild = nextOldNode;
            return null; // Inga av det gamla trädets noder är med i stigen från nya roten till index.
        }

        return nextOldNode;
    }

    /**
     * Letar efter värdet på givet index och returnera det.finns. Returnerar 0 om inget värde satts.
     * @param index Index för elementet som önskas.
     * @return Elementet lagrat på givet index.
     */
    public int get(int index) {
        int treeHeight = currentRoot.logMaxIndex;
        int logIndex = (int)Math.floor(log2(index));

        // Om index kräver fler nivåer än vad som finns i trädet finns garanterat
        // inget element på det givna indexet.
        if (logIndex >= treeHeight) {
            return 0;
        }

        Node nextNode = currentRoot.treeRoot;

        // Går igenom trädet nivå för nivå och följer stigen from roten till det givna indexet.
        // Elementet på det sökta indexet genom att följa stigen som uppstår genom att gå till
        // siffra för siffra genom indexet (i binära talbasen) från vänster till höger (med start
        // i den mest signifikanta ettan) och gå till vänster i trädet om en siffta är 0 och till
        // höger om en siffra är 1. Om man vill åt elementet på index 2 i trädet (arrayen) nedan
        // går man först höger sedan vänster (2 (DEC) = 10 (BIN) -> 1 (höger), 0 (vänster)).
        //        R
        //      /   \
        //    o       o
        //   / \     / \
        // [0] [1] [2] [3]
        for (int j = treeHeight - 1; j >= 0; j--) {
            // Om biten är 1, gå till höger i trädet.
            if(((index >> j) & 1) == 1) {
                // Om det inte finns en nod i trädet har den inte heller barn (grannar längre ner i trädet) och
                // därmed inte heller några löv, alltså finns inget element på givet index.
                if (nextNode == null) {
                    return 0;
                }
                nextNode = nextNode.rightChild;
            }
            // Om biten är 0, gå till vänster i trädet.
            else {
                // Om det inte finns en nod i trädet har den inte heller barn (grannar längre ner i trädet) och
                // därmed inte heller några löv, alltså finns inget element på givet index.
                if (nextNode == null) {
                    return 0;
                }
                nextNode = nextNode.leftChild;
            }
        }

        // Om inget löv finns i slutet på stigen finns inget element på sökt index.
        if (nextNode == null) {
            return 0;
        }

        return nextNode.data;
    }

    /**
     * Återställer arrayen till föregående tillstånd (innan senaste set-anropet).
     */
    public void unset() {
        if (currentRoot.previousRoot != null) {
            currentRoot = currentRoot.previousRoot;
        }
    }

    /**
     * Returnerar längden på arrayen.
     * @return längden på arrayen.
     */
    public int size() {
        return currentRoot.length;
    }

    /**
     * Hjälpfunktion för att beräkna BIN-logaritmen av givet tal.
     * @param n Tal att hitta BIN-logaritmen av.
     * @return BIN-logaritmen av n (log n).
     */
    private static double log2(int n) {
        // Returnera 0 om värdet inte är definierat.
        if (n <= 0) {
            return  0;
        }

        return Math.log(n) / Math.log(2);
    }
}