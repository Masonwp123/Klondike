// Play a card from the randomized deck
public class PlayCommand {

    public static PlayCommand parse(String string) {
        // Split using space as a delimiter
        String[] splitString = string.split(" ");

        // Parse based on length, format for length of 3 is assumed to be played at
        switch (splitString.length) {
            case 1: { //'Play'
                return new PlayCommand();
            }
            case 3: { //'Play at [Column Name]'
                return PlayAtCommand.parse(string);
            }
        }

        PrintUtils.printError("Play Command doesn't have correct number of arguments");
        return null;
    }

    public static class PlayAtCommand extends PlayCommand {

        protected int columnName;

        public static PlayAtCommand parse(String string) {
            PlayAtCommand playAtCommand = new PlayAtCommand();

            // Split using space as a delimiter
            String[] splitString = string.split(" ");

            // If length is three the assumed format is 'Play at [Column Name]'
            if (!splitString[1].equalsIgnoreCase("at")) {
                PrintUtils.printError("Play At Command Formatted Improperly");
                return null;
            }
            try {
                playAtCommand.columnName = Integer.parseInt(splitString[2]);
            } catch (NumberFormatException exception) {
                PrintUtils.printError("Column Number was not valid");
                return null;
            }
            if (playAtCommand.columnName < 0 || playAtCommand.columnName >= Klondike.boardLength) {
                PrintUtils.printError("Column Number needs to be between 0 and 7");
                return null;
            }

            return playAtCommand;
        }

        public int getColumnName() {
            return columnName;
        }
    }
}