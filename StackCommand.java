// Stack a card onto an ace pile //TODO: can only stack from end of column, so card type shouldnt be needed
public class StackCommand {

    public static StackCommand parse(String string) {
        // Split using space as a delimiter
        String[] splitString = string.split(" ");

        // Parse based on length
        switch (splitString.length) {
            case 1 -> { //'Stack'
                return new StackCommand();
            }
            case 3 -> { //'Stack at [Column Name]'
                return StackAtCommand.parse(string);
            }
        }

        PrintUtils.printError("Stack Command doesn't have correct number of arguments");
        return null;
    }

    public static class StackAtCommand extends StackCommand {

        protected int columnName;

        // Form 'Stack [Card] at [Column Name]'
        public static StackAtCommand parse(String string) {
            StackAtCommand stackAtCommand = new StackAtCommand();

            // Split using space as a delimiter
            String[] splitString = string.split(" ");

            if (!splitString[1].equalsIgnoreCase("at")) {
                PrintUtils.printError("Stack At Command Formatted Improperly");
                return null;
            }
            try {
                stackAtCommand.columnName = Integer.parseInt(splitString[2]);
            } catch (NumberFormatException exception) {
                PrintUtils.printError("Column Number was not valid");
                return null;
            }
            return stackAtCommand;
        }

        public int getColumnName() {
            return columnName;
        }
    }
}