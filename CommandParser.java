import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.function.Function;

public class CommandParser {

    protected final HashMap<String, Function<String, Object>> CommandMap = new HashMap<>();

    // Add Commands and a function to create new commands with the string
    public CommandParser() {
        CommandMap.put("help", string -> HelpCommand.parse(string));
        CommandMap.put("new game", string -> NewGameCommand.parse(string));
        CommandMap.put("quit", string -> QuitGameCommand.parse(string));
        CommandMap.put("q", string -> QuitGameCommand.parse(string));
        CommandMap.put("next", string -> NextCommand.parse(string));
        CommandMap.put("n", string -> NextCommand.parse(string));
        CommandMap.put("play", string -> PlayCommand.parse(string));
        CommandMap.put("move", string -> MoveCommand.parse(string));
        CommandMap.put("stack", string -> StackCommand.parse(string));
    }

    public Object getCommand() {
        Scanner input = new Scanner(System.in);

        // Get input from next line
        String nextLine = input.nextLine();

        // For any given entry, test if the commands are equal or has an equal beginning and parse
        for (Map.Entry<String, Function<String, Object>> entry : CommandMap.entrySet()) {
            if (nextLine.equals(entry.getKey()) || nextLine.startsWith(entry.getKey().concat(" "))) {
                return entry.getValue().apply(nextLine);
            }
        }

        PrintUtils.printError("No Such Command");
        return null;
    }

    // Display information for playing the game
    public static class HelpCommand {
        public static HelpCommand parse(String string) {
            return new HelpCommand();
        }
    }

    // Starts a new game
    public static class NewGameCommand {
        public static NewGameCommand parse(String string) {
            return new NewGameCommand();
        }
    }

    // Quits Program
    public static class QuitGameCommand {
        public static QuitGameCommand parse(String string) {
            return new QuitGameCommand();
        }
    }

    // Displays the next cards in the shuffled deck
    public static class NextCommand {
        public static NextCommand parse(String string) {
            return new NextCommand();
        }
    }
}
