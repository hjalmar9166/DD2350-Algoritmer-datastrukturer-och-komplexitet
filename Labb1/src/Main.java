import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner inp = new Scanner(System.in);
        PersistentArray array = new PersistentArray();

        //while(!(något =inp.nextLine()).isEmpty())
        while (inp.hasNextLine()) {
            String input = inp.nextLine();
            String[] inputArguments = input.split(" ");
            String command = inputArguments[0];

            // Givet kommando:
            // "set" -> kör set-metoden med två parametrar: index och värde. Sätter värde på index i arrayen.
            // "get" -> kör get-metoden med en parameter: index. Får ut värde på index i arrayen.
            // "unset" -> gör unset-metoden. Återställer arrayen till föregående stadie.
            if(command.equals("set")) {
                int index = Integer.parseInt(inputArguments[1]);
                int value = Integer.parseInt(inputArguments[2]);
                array.set(index, value);
            } else if (command.equals("get")) {
                int index = Integer.parseInt(inputArguments[1]);
                System.out.println(array.get(index));
            } else if (command.equals("unset")) {
                array.unset();
            }
        }
    }
}