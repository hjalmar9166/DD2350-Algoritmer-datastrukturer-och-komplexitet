import java.util.LinkedList;

public class Node {
    int name;
    LinkedList<Node> neighbours;
    LinkedList<Integer> weights;

    public Node(int name) {
        this.name = name;
        this.neighbours = new LinkedList<>();
        this.weights = new LinkedList<>();
    }
}
