PrintUtils.class: PrintUtils.java
	javac -g PrintUtils.java

MoveCommand.class: MoveCommand.java PrintUtils.class
	javac -g MoveCommand.java

PlayCommand.class: PlayCommand.java PrintUtils.class
	javac -g PlayCommand.java

StackCommand.class: StackCommand.java PrintUtils.class
	javac -g StackCommand.java

CommandParser.class: CommandParser.java MoveCommand.class PlayCommand.class StackCommand.class PrintUtils.class
	javac -g CommandParser.java

IRenderable.class: IRenderable.java
	javac -g IRenderable.java

Cards.class: Cards.java
	javac -g Cards.java

CardSuits.class: CardSuits.java
	javac -g CardSuits.java

Card.class: Card.java Cards.class CardSuits.class IRenderable.class
	javac -g Card.java

Node.class: Node.java
	javac -g Node.java

Stack.class: Stack.java Node.class
	javac -g Stack.java

CardStack.class: CardStack.java Stack.class Card.class IRenderable.class
	javac -g CardStack.java

Klondike.class: Klondike.java IRenderable.class CommandParser.class  MoveCommand.class PlayCommand.class StackCommand.class PrintUtils.class
	javac -g Klondike.java

run: Klondike.class
	java Klondike

clean:
	rm *.class