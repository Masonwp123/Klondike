// Move card from board to another board place
public class MoveCommand {
    protected Cards cardType;
    protected int columnName;
    protected int targetColumnName;//MoveAt //MoveAtTo

    // Form 'Move [Card] at [Column Name] to [Column Name]'
    // Ex: 'Move Two at Column 5 to Column 2'
    public static MoveCommand parse(String string) {
        MoveCommand moveCommand = new MoveCommand();

        // Split using space as a delimiter
        String[] splitString = string.split(" ");

        // Length check to ensure enough arguments
        if (splitString.length != 6) {
            PrintUtils.printError("Move Command doesn't have correct number of arguments");
            return null;
        }

        moveCommand.cardType = Cards.stringToCard(splitString[1]);
        if (moveCommand.cardType == Cards.NONE) {
            PrintUtils.printError("Card Type was not valid");
            return moveCommand;
        }

        if (!splitString[2].equalsIgnoreCase("at") || !splitString[4].equalsIgnoreCase("to")) {
            PrintUtils.printError("Move Command Formatted Improperly");
            return null;
        }
        try {
            moveCommand.columnName = Integer.parseInt(splitString[3]);
            moveCommand.targetColumnName = Integer.parseInt(splitString[5]);
        } catch (NumberFormatException exception) {
            PrintUtils.printError("Column Number was not valid");
            return null;
        }

        return moveCommand;
    }

    public Cards getCardType() {
        return cardType;
    }

    public int getColumnName() {
        return columnName;
    }

    public int getTargetColumnName() {
        return targetColumnName;
    }
}