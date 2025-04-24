# Klondike
Finally a command line version, such an upgrade...

## Classes

### interface IRenderable
```
void render()
```

### class PrintUtils
Contains Basic Printing functions for convenience

### public enum Cards

**stringToCard**
```
public static Cards stringToCard(String string):
    if string is a number and number is greater than 0 and less than 11:
        return number of string converted to card number (ex: 1 is ace, 2 is two)
    for each cardType:
        if string equals cardType as string:
            return cardType
    return NONE
```

**isOneLess**
```
public boolean isOneLess(Cards otherType):
    return this as int + 1 == otherType as int
```

**isOneGreater**
```
public boolean isOneGreater(Cards otherType):
    return this as int - 1 == otherType as int
```

### public enum CardSuits

**stringToSuit**
```
public static CardSuits stringToSuit(String string):
    for each cardSuit:
        if string equals cardSuit as string:
            return cardSuit
     return NONE
```

### public class Node<T>
Contains basic node things like a T payload and Node<T> next

### public class Stack<T>
All push functions add one to the length and set a new node as head like a standard stack
All pop/pull functions remove one from length and
Pop removes from head, Pull removes from a specified location (index, or specific object)

**pullFirst**
Pulls first node that returns true in the predicate

**pull(index)**
Pulls after a number of loops

**popTo and swapTo**
pops from current and pushes to another stack
swap can do any payload (if it exists in current)

**getFirst**
returns first payload that returns true in the predicate

**iterate and iterateUntil**
iterates through the stack
iterateUntil stops when predicate returns false

### public class Card implements IRenderable, Comparable<Card>, Cloneable

**render**
```
public void render():
    if is hidden print "*"
    print cardtype "of" cardsuit "s"
```

### public class CardStack extends Stack<Card> implements IRenderable

**shuffle**
Creates a temp stack and pushes random nodes from self
remaps temp to self

**render**
iterates through cards and calls their render function

### public static class ShuffledCardStack extends CardStack

**push**
hides any cards pushed into

### public static class UsableCardStack extends CardStack

**recalculateVisibility()**
Hides all current cards
Shows latest three cards in stack

**pop**
recalculates after pop

**push**
recalculates after push

### public static class BoardCardStack extends CardStack

**popNode**
sets head visible after pop

**pullFirst**
sets head visible after pull

**swapChildrenTo**
swaps a card (and all cards to its left up to head) into another board stack

**canPush**
prevents pushing cards that cannot be on board in that position
cards that are not the opposite color of the current head and are not one less cannot be pushed unless head is null

### public static class AceCardStack extends CardStack

**canPush**
Only cards of like suit and that are one greater can be pushed into

### public class CommandParser

**CommandParser()**
Puts commands into the CommandMap
ex: "help", string -> HelpCommand.parse(string)

**getCommand()**
gets input via Scanner
tests the input if it starts with the string from a command in commandMap
runs function from commandMap
otherwise prints error

### public static class HelpCommand
Just returns a new instance of itself

### public static class NewGameCommand
Just returns a new instance of itself

### public static class QuitGameCommand
Just returns a new instance of itself

### public static class NextCommand
Just returns a new instance of itself

### public class MoveCommand

**parse**
creates a new move command
checks for proper formatting of command
gets cardType, columnName, and targetColumnName from input
sets values of movecommand with parsed values
return movecommand

### public class PlayCommand

**parse**
if length is 1, returns new PlayCommand
if length is 3, returns PlayAtCommand.parse(string)
otherwise prints error

### public static class PlayAtCommand

**parse**
creates new playAtCommand
checks for proper formatting of command
gets columnName from input
sets values of playAtCommand with parsed values
return playAtCommand

### public class StackCommand

**parse**
if length is 1, returns new StackCommand
if length is 3, returns StackAtCommand.parse(string)
otherwise prints error

### public static class StackAtCommand

**parse**
creates new stackAtCommand
checks for proper formatting of command
gets columnName from input
sets values of stackAtCommand with parsed values
return stackAtCommand

### public class Klondike implements IRenderable, Runnable

**main**
creates new instance of Klondike
calls Klondike.run()

**Klondike()**
initializes fulldeck with a full deck of cards (for each type for each suit)
create new instances of boardCardStacks using Klondike.boardLength (default 7)
create new instances of aceStacks using each Suit

**run**
calls newGame() to start a new game
calls markAsDirty() to allow an initial render
creates a while loop
calls commandParser.getCommand() to get an Object command
depending on the object calls a function
HelpCommand calls help
NewGameCommand calls newgame
QuitGameCommand stops the loop
NextCommand calls next
PlayAtCommand calls playAt
PlayCommand calls play
MoveCommand calls move
StackAtCommand calls stackAt
StackCommand calls stack
If the heads of each ace stack is a king, stop the game and deliver a you win message

**render**
if is dirty continue, set dirty to false
print shuffled deck and usable deck by calling render
print board stacks by calling render
print ace stacks by calling render

**help**
prints helpful things, like how to format each command

**getCard**
gets a card matching cardType from a boardStack
useful for determining where a card should play

**play**
gets first playable card
if playable card can be played anywhere on board, do so

**playAt**
gets first playable card
if card can be played on specified board stack, do so

**move**
moves a specified card from a specified board stack to another board stack if it can

**stack**
stacks to an ace pile if it can

**stackAt**
will stack the outermost card from a board stack to an ace pile if it can

**next**
will move to next position in shuffled deck (next 3 cards)

**newGame**
empties all card stacks
creates a temp full deck and shuffles it
pushes 28 cards into the board stacks based on their board number (0 gets 1 card, 1 gets 2)
pushes the rest of the cards into the shuffled deck
calls next to push 3 latest cards from shuffled deck into usable deck
marks dirty