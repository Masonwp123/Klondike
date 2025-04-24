import javax.swing.*;
import java.util.HashMap;

public class Klondike implements IRenderable, Runnable {

    private boolean dirty = false;

    private final CommandParser commandParser = new CommandParser();

    // Defines a full stack of cards (52 cards)
    protected static final CardStack fullDeck = new CardStack();

    // Shuffled card stack for the deck to pull out of
    protected final CardStack.ShuffledCardStack shuffledCardStack = new CardStack.ShuffledCardStack();
    // The cards that have been looked through using 'next'
    protected final CardStack.UsableCardStack usableCardStack = new CardStack.UsableCardStack();

    // Ordered card stack for cards on the board
    public static final int boardLength = 7;
    protected final CardStack.BoardCardStack[] boardCardStacks = new CardStack.BoardCardStack[boardLength];

    // Reverse ordered card stack initialized with length - 1 to account for CardSuits.NONE
    protected final HashMap<CardSuits, CardStack.AceCardStack> aceStacks = new HashMap<>(CardSuits.values().length - 1);

    public static void main(String[] args) {
        Klondike klondike = new Klondike();
        klondike.run();
    }

    public Klondike() {
        // Initialize fullDeck with cards
        for (Cards cardType : Cards.values()) {
            if (cardType.equals(Cards.NONE)) continue;
            for (CardSuits cardSuit : CardSuits.values()) {
                if (cardSuit.equals(CardSuits.NONE)) continue;
                Klondike.fullDeck.push(new Card(cardType, cardSuit));
            }
        }

        // Initialize board stacks with empty stacks
        for (int i = 0; i < Klondike.boardLength; ++i) {
            this.boardCardStacks[i] = new CardStack.BoardCardStack();
        }

        // Initialize ace stacks with suits
        for (CardSuits cardSuit : CardSuits.values()) {
            if (cardSuit.equals(CardSuits.NONE)) continue;
            this.aceStacks.put(cardSuit, new CardStack.AceCardStack(cardSuit));
        }
    }

    @Override
    public void run() {

        // Begin New game
        newGame();

        // Render initially
        markAsDirty();

        boolean keepGoing = true;

        while (keepGoing) {

            // Render deck before prompting commands
            render();

            Object command = commandParser.getCommand();

            // Null commands indicate an error inside a command, default indicates a different object
            if (command instanceof CommandParser.HelpCommand) {
                help((CommandParser.HelpCommand)command);
            } else if (command instanceof CommandParser.NewGameCommand) {
                newGame();
            } else if (command instanceof CommandParser.QuitGameCommand) {
                keepGoing = false;
            } else if (command instanceof CommandParser.NextCommand) {
                next();
            } else if (command instanceof PlayCommand.PlayAtCommand) {
                playAt((PlayCommand.PlayAtCommand)command);
            } else if (command instanceof PlayCommand) {
                play((PlayCommand)command);
            } else if (command instanceof MoveCommand) {
                move((MoveCommand)command);
            } else if (command instanceof StackCommand.StackAtCommand) {
                stackAt((StackCommand.StackAtCommand)command);
            } else if (command instanceof StackCommand) {
                stack((StackCommand)command);
            }

            // Test if the head of each ace stack is a king, if they are, the game has finished
            boolean bShouldContinue = false;
            for (CardStack.AceCardStack aceStack : aceStacks.values()) {
                bShouldContinue |= aceStack.isEmpty() || !aceStack.top().getCardType().equals(Cards.KING);
            }
            if (!bShouldContinue) {
                keepGoing = false;
                System.out.println("Congrats! You Win!");
                System.out.println();
            }
        }
    }

    // Render the cards in order
    @Override
    public void render() {
        // No need to re-render if not dirty
        if (!isDirty()) return;
        this.dirty = false;

        System.out.println("On Deck");

        this.shuffledCardStack.render();
        this.usableCardStack.render();
        System.out.println();

        System.out.println("On Board");

        for (int i = 0; i < Klondike.boardLength; ++i) {
            CardStack.BoardCardStack boardCardStack = this.boardCardStacks[i];
            System.out.print(i);
            System.out.print(": ");
            boardCardStack.render();
        }
        System.out.println();

        for (CardSuits cardSuit : CardSuits.values()) {
            if (cardSuit.equals(CardSuits.NONE)) continue;
            System.out.println(cardSuit.name().toLowerCase());
            this.aceStacks.get(cardSuit).render();
            System.out.println();
        }
    }

    public void markAsDirty() {
        this.dirty = true;
    }

    public boolean isDirty() {
        return this.dirty;
    }

    public void help(CommandParser.HelpCommand helpCommand) {
        System.out.println("Help");
        System.out.println();
        System.out.println("-Play");
        System.out.println();
        System.out.println("Play");
        System.out.println("Play at [Column Name]");
        System.out.println();
        System.out.println("-Move");
        System.out.println();
        System.out.println("Move [Card] at [Column Name] to [Column Name]");
        System.out.println();
        System.out.println("-Stack");
        System.out.println();
        System.out.println("Stack");
        System.out.println("Stack at [Column Name]");
        System.out.println();
        PrintUtils.waitForNextInput();
        markAsDirty();
    }

    // Get card from a specified card type and column
    public Card getCard(Cards cardType, CardStack.BoardCardStack boardCardStack) {
        return boardCardStack.getFirst(card -> {
            return card.getCardType().equals(cardType);
        });
    }

    // Play outermost card from randomized deck to any position available
    public void play(PlayCommand playCommand) {

        if (this.usableCardStack.isEmpty()) {
            return;
        }

        // Get first card that is not hidden
        Card current = this.usableCardStack.getFirst(card -> {
            return !card.isHidden();
        });

        // Get column name from command or auto find a valid spot
        int column = -1;
        for (int i = 0; i < Klondike.boardLength; ++i) {
            CardStack.BoardCardStack boardCardStack = this.boardCardStacks[i];
            if (boardCardStack.canPush(current)) {
                column = i;
                break;
            }
        }

        // Make sure column name is valid
        if (column < 0) {
            System.out.println("No place to put card.");
            return;
        }

        playCard(column);
    }

    // Play outermost card from randomized deck
    public void playAt(PlayCommand.PlayAtCommand playAtCommand) {
        playCard(playAtCommand.getColumnName());
    }

    public void playCard(int column) {

        if (this.usableCardStack.isEmpty()) {
            return;
        }

        // Get first card that is not hidden
        Card current = this.usableCardStack.getFirst(card -> {
            return !card.isHidden();
        });

        CardStack.BoardCardStack boardCardStack = this.boardCardStacks[column];
        this.usableCardStack.swapTo(boardCardStack, current);
        markAsDirty();
    }

    // Move cards around on board
    public void move(MoveCommand moveCommand) {
        CardStack.BoardCardStack boardCardStack = this.boardCardStacks[moveCommand.getColumnName()];
        CardStack.BoardCardStack targetBoardCardStack = this.boardCardStacks[moveCommand.getTargetColumnName()];

        // Get first card of card type
        Card current = boardCardStack.getFirst(card -> {
            return card.getCardType().equals(moveCommand.getCardType());
        });

        // Swap card from board stack to target board stack
        boardCardStack.swapChildrenTo(targetBoardCardStack, current);

        markAsDirty();
    }

    // 'Stack'
    // Stacks to an ace pile if it can
    public void stack(StackCommand stackCommand) {

        if (this.usableCardStack.isEmpty()) {
            return;
        }

        // Get first card that is not hidden
        Card current = this.usableCardStack.getFirst(card -> {
            return !card.isHidden();
        });

        // Get column name from command or auto find a valid spot
        CardSuits cardSuit = CardSuits.NONE;
        for (CardSuits testSuit : CardSuits.values()) {
            if (testSuit.equals(CardSuits.NONE)) continue;
            if (this.aceStacks.get(testSuit).canPush(current)) {
                cardSuit = testSuit;
                break;
            }
        }

        if (cardSuit.equals(CardSuits.NONE)) {
            System.out.println("No place to stack card.");
            return;
        }

        this.usableCardStack.swapTo(this.aceStacks.get(cardSuit), current);

        markAsDirty();
    }

    //'Stack at [Column Name]'
    // Stacks the outermost card to an ace pile if it can
    public void stackAt(StackCommand.StackAtCommand stackAtCommand) {

        CardStack.BoardCardStack boardCardStack = this.boardCardStacks[stackAtCommand.getColumnName()];
        Card card = boardCardStack.top();

        // Get column name from command or auto find a valid spot
        CardSuits cardSuit = CardSuits.NONE;
        for (CardSuits testSuit : CardSuits.values()) {
            if (testSuit.equals(CardSuits.NONE)) continue;
            if (this.aceStacks.get(testSuit).canPush(card)) {
                cardSuit = testSuit;
                break;
            }
        }

        if (cardSuit.equals(CardSuits.NONE)) {
            System.out.println("No place to stack card.");
            return;
        }

        boardCardStack.swapTo(this.aceStacks.get(cardSuit), card);

        markAsDirty();
    }

    // Move to next position in shuffled deck
    public void next() {

        // If the shuffled deck has none left, put all usable cards back into shuffled deck
        if (this.shuffledCardStack.isEmpty()) {
            while (!this.usableCardStack.isEmpty()) {
                this.shuffledCardStack.push(this.usableCardStack.pop());
            }
        }

        // Push latest 3 cards from shuffled deck into usable deck
        for (int i = 0; i < 3; ++i) {
            // Ensure we aren't popping more than shuffled deck has
            if (this.shuffledCardStack.isEmpty()) break;
            this.usableCardStack.push(this.shuffledCardStack.pop());
        }
        markAsDirty();
    }

    public void newGame() {

        // Empty out all cards from locations

        this.shuffledCardStack.empty();
        this.usableCardStack.empty();

        for (CardStack.BoardCardStack boardCardStack : this.boardCardStacks) {
            boardCardStack.empty();
        }

        for (CardSuits cardSuit : CardSuits.values()) {
            if (cardSuit.equals(CardSuits.NONE)) continue;
            this.aceStacks.get(cardSuit).empty();
        }

        CardStack fullDeck = (CardStack) Klondike.fullDeck.clone();
        fullDeck.shuffle();

        // Pushes 28 cards, should be 24 left
        for (int i = 0; i < Klondike.boardLength; ++i) {
            // The amount of cards in board row is 1-7 (i + 1)
            int cardAmount = i + 1;
            CardStack.BoardCardStack boardCardStack = this.boardCardStacks[i];
            for (int v = 0; v < cardAmount; ++v) {
                Card card = fullDeck.pop();

                // Hide all cards but head
                if (v < i) {
                    card.hide();
                }

                boardCardStack.push(card);
            }
        }

        // Push the rest of the cards into shuffled deck
        fullDeck.iterate(card -> {
            card.hide();
            this.shuffledCardStack.push(card);
        });

        // Pushes latest from shuffled deck into usable deck
        next();

        // Ensure re-print
        markAsDirty();
    }
}
