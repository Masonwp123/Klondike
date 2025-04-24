public enum CardSuits {
    NONE(false), SPADE(false), HEART(true), DIAMOND(true), CLUB(false);

    private final boolean isRed;

    CardSuits(boolean isRed) {
        this.isRed = isRed;
    }

    // Test both Spade and Spades by concatting an s
    public static CardSuits stringToSuit(String string) {
        for (CardSuits cardType : CardSuits.values()) {
            if (cardType.name().equalsIgnoreCase(string) ||
                cardType.name().concat("s").equalsIgnoreCase(string)) {
                return cardType;
            }
        }
        return CardSuits.NONE;
    }

    public boolean isSameColor(CardSuits otherSuit) {
        return this.isRed == otherSuit.isRed;
    }
}