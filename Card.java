
public class Card implements IRenderable, Comparable<Card>, Cloneable {

    protected boolean hidden;
    protected final Cards cardType;
    protected final CardSuits cardSuit;

    public Card(boolean hidden, Cards cardType, CardSuits cardSuit) {
        this.hidden = hidden;
        this.cardType = cardType;
        this.cardSuit = cardSuit;
    }

    public Card(Cards cardType, CardSuits cardSuit) {
        this(false, cardType, cardSuit);
    }

    public boolean isHidden() {
        return hidden;
    }

    public Cards getCardType() {
        return cardType;
    }

    public CardSuits getCardSuit() {
        return cardSuit;
    }

    public void show() {
        hidden = false;
    }

    public void hide() {
        hidden = true;
    }

    @Override
    public void render() {
        if (isHidden()) {
            System.out.print("*");
            return;
        }
        System.out.print(cardType.name().toLowerCase());
        System.out.print(" of ");
        System.out.print(cardSuit.name().toLowerCase());
        System.out.print("s");
    }

    @Override
    public int compareTo(Card o) {
        return this.cardType.compareTo(o.cardType);
    }

    @Override
    public Card clone() {
        return new Card(this.hidden, this.cardType, this.cardSuit);
    }
}
