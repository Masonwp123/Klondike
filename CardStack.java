import java.util.function.Predicate;

public class CardStack extends Stack<Card> implements IRenderable {

    // Get random from deck and add to temp then set this to those shuffled values
    public void shuffle() {
        CardStack temp = new CardStack();
        while (!isEmpty()) {
            int length = getLength();
            int rand = (int)(Math.random() * length);
            temp.push(this.pull(rand));
        }

        temp.iterate(card -> this.push(card));
    }

    @Override
    public Object clone() {
        CardStack newStack = new CardStack();
        iterate(o -> {
            newStack.push(o.clone());
        });
        return newStack;
    }

    @Override
    public void render() {
        iterate(card -> {
            card.render();
            System.out.print(" | ");
        });
        System.out.println();
    }

    public static class ShuffledCardStack extends CardStack {

        public ShuffledCardStack() {
            super();
        }

        @Override
        public void push(Card nextPayload) {

            // All cards in shuffled deck are hidden
            nextPayload.hide();

            super.push(nextPayload);
        }
    }

    public static class UsableCardStack extends CardStack {

        public UsableCardStack() {
            super();
        }

        @Override
        public Card pop() {
            Card card = super.pop();
            recalculateVisibility();
            return card;
        }

        @Override
        public void push(Card nextPayload) {
            super.push(nextPayload);
            recalculateVisibility();
        }

        private void recalculateVisibility() {
            // Hide previous cards
            iterate(card -> card.hide());

            // Show latest 3 cards
            Node<Card> current = topNode();
            for (int i = 0; i < 3; ++i) {
                if (current == null) break;
                current.get().show();
                current = current.getNext();
            }
        }
    }

    public static class BoardCardStack extends CardStack {

        public BoardCardStack() {
            super();
        }

        @Override
        public Object clone() {
            BoardCardStack newStack = new BoardCardStack();
            iterate(o -> {
                newStack.push(o.clone());
            });
            return newStack;
        }

        @Override
        public Node<Card> popNode() {
            Node<Card> node = super.popNode();
            this.setHeadVisible();
            return node;
        }

        @Override
        public Card pullFirst(Predicate<Card> predicate) {
            Card card = super.pullFirst(predicate);
            this.setHeadVisible();
            return card;
        }

        // Will swap a card (and all cards to its left) to another board stack
        public void swapChildrenTo(BoardCardStack other, Card card) {
            // If cannot push original card, cannot push the rest
            if (!other.canPush(card)) {
                return;
            }

            Stack<Card> temp = new Stack<>();

            // Pop all cards to temp until given card
            while (!this.head.get().equals(card)) {
                popTo(temp);
            }
            // Pop given card to temp
            popTo(temp);

            // Push all temp cards to other
            temp.iterate(card1 -> {
                other.push(card1);
            });
        }

        private void setHeadVisible() {
            if (this.head != null)
                this.head.get().show();
        }

        // In a card stack that is ordered we can ONLY add to the end with cards smaller by 1
        // Only cards of opposite color from previous can be added irrespective of suit
        // Unlike google solitaire we allow any card in an empty slot (like DS solitaire)
        // Cards can be randomized when undiscovered
        @Override
        public boolean canPush(Card nextPayload) {
            return this.head == null || (this.top().isHidden() || (nextPayload.getCardType().isOneLess(this.top().getCardType()) && !this.top().getCardSuit().isSameColor(nextPayload.getCardSuit())));
        }
    }

    public static class AceCardStack extends CardStack {

        // Ace stacks have a suit that can only be played
        private final CardSuits stackSuit;

        public AceCardStack(CardSuits stackSuit) {
            this.stackSuit = stackSuit;
        }

        @Override
        public Object clone() {
            AceCardStack newStack = new AceCardStack(stackSuit);
            iterate(o -> {
                newStack.push(o.clone());
            });
            return newStack;
        }

        // In a card stack that is ordered we can ONLY add to the end with cards larger by 1
        // Special case where the first card is ALWAYS an ace
        // Only cards of same suit can be added
        @Override
        public boolean canPush(Card nextPayload) {
            if (!stackSuit.equals(nextPayload.getCardSuit())) {
                return false;
            }
            return this.head == null ? nextPayload.cardType.equals(Cards.ACE) : nextPayload.getCardType().isOneGreater(this.top().getCardType());
        }
    }
}
