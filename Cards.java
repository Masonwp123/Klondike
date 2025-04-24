public enum Cards {
    NONE, ACE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, JACK, QUEEN, KING;

    public static Cards stringToCard(String string) {
        // Check for numbers first, if failed, try direct conversion
        try {
            int Number = Integer.parseInt(string);
            if (Number >= 1 && Number <= 10) {
                return Cards.values()[Number];
            }
            return Cards.NONE;
        } catch (NumberFormatException exception) {
            for (Cards cardType : Cards.values()) {
                if (cardType.name().equalsIgnoreCase(string)) {
                    return cardType;
                }
            }
        }

        return Cards.NONE;
    }

    public boolean isOneLess(Cards otherType) {
        return ordinal() + 1 == otherType.ordinal();
    }

    public boolean isOneGreater(Cards otherType) {
        return ordinal() - 1 == otherType.ordinal();
    }

}