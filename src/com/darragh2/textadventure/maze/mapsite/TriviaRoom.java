package com.darragh2.textadventure.maze.mapsite;

import com.darragh2.textadventure.Direction;
import com.darragh2.textadventure.Player;
import com.darragh2.textadventure.ReadInput;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class TriviaRoom extends Room {

    private HashMap<String, String> trivia = new HashMap<>();
    private boolean askingTrivia = true;
    private ReadInput<MapSite> playerCommands;

    public TriviaRoom() {
        super(new String[]{"This is a TriviaRoom North", "This is a TriviaRoom East", "This is a TriviaRoom South", "This is a TriviaRoom West"});
        setAddCommandsOnEnter(false);
        ReadInput<MapSite> firstQuestions = new ReadInput<>();
        firstQuestions.putCommand("yes", mapSite -> {
            TriviaRoom triviaRoom = (TriviaRoom) mapSite;
            triviaRoom.setAskingTrivia(true);
        });
        firstQuestions.putCommand("no", mapSite -> {
            TriviaRoom triviaRoom = (TriviaRoom) mapSite;
            triviaRoom.setAskingTrivia(false);
            System.out.println(triviaRoom.getDescription(triviaRoom.getPlayer().getDirection()));
        });
        //firstQuestions.putCommand("help", mapSite -> System.out.println("Commands: " + mapSite.getCurrentCommands().getCommands().keySet().stream().filter(key -> !key.equals("help")).collect(Collectors.joining(", "))) );
        getDefaultCommands().putAllCommands(firstQuestions);

        trivia.put("\"Don't ever tell anybody anything. If you do, you start missing everybody.\" This is a quote from which book: \"Great Expectations\"(A) \"Animal Farm\" (B) \"1984\"(C) \"The Catcher in the Rye\"(D)\n", "d");
        trivia.put("Which Greek god is the goddess of corn, grain and harvest? Demeter(A) Dionysus(B) Poseidon(C) Pan(D)\n", "a");
        trivia.put("\"Cattleya\" could also be described as which color? Red(A) Blue(B) Purple(C) Pink(D)\n", "c");

    }

    public void askTrivia() {
        askingTrivia = true;
        List<String> choices = Arrays.stream(new String[]{"a", "b", "c", "d"}).toList();
        for (Map.Entry<String, String> entry : trivia.entrySet()) {
            String triviaQ = entry.getValue();
            ReadInput<MapSite> question = triviaQuestion(triviaQ, choices);
            question.setPrompt(entry.getKey());
            question.setDefaultCommand(mapSite -> System.out.println("Choose 'a', 'b', 'c', or 'd'"));
            setCurrentCommands(question);
            runCommands();
            while (getCurrentCommands().getLastRanCommand() == getCurrentCommands().getDefaultCommand()) {
                runCommands();
            }
            if (!askingTrivia) {
                break;
            }
        }
        if (askingTrivia) {
            System.out.println("The cat hands you a key, this might be useful later.");
            setAskingTrivia(false);
        }
    }

    private  ReadInput<MapSite> triviaQuestion(String answer, List<String> choices) {
        ReadInput<MapSite> triviaQuestion = new ReadInput<>();
        Consumer<MapSite> failureCommand = mapSite -> {
            System.out.println("You failed, good luck next time.");
            TriviaRoom triviaRoom = (TriviaRoom) mapSite;
            triviaRoom.setAskingTrivia(false);
        };
        for (String choice : choices) {
            triviaQuestion.putCommand(choice, failureCommand);
        }
        triviaQuestion.setDefaultCommand(failureCommand);
        triviaQuestion.putCommand(answer, mapSite -> System.out.println("You got it right!"));
        return triviaQuestion;
    }

    /**
     * Interrupts askTrivia() if set to false otherwise calls askTrivia()
     * @param askingTrivia boolean
     */
    public void setAskingTrivia(boolean askingTrivia) {
        this.askingTrivia = askingTrivia;
        if (askingTrivia) {
            askTrivia();
        }
        else {
            setCurrentCommands(playerCommands);
        }
    }

    /**
     * Trivia is defined as a HashMap where the first String is the question and the second String is the answer
     * @param trivia HashMap<Question, Answer>
     */
    public void setTrivia(HashMap<String, String> trivia) {
        this.trivia = trivia;
    }

    /**
     * Called when a Player enters the MapSite.
     * Must readInput.putCommand() in this method to add commands in this MapSite
     *
     * @param player         the Player instance that entered
     * @param playerCommands the list of commands that by default are added without overwriting any existing to MapSite.defaultCommands
     * @return true if the Player can enter the MapSite, false otherwise
     */
    @Override
    public boolean enter(Player player, ReadInput<MapSite> playerCommands) {
        super.enter(player, playerCommands);
        this.playerCommands = playerCommands;
        return true;
    }

    /**
     * Called when a Player leaves the MapSite.
     * Sets this.Player == null and sets currentCommands to defaultCommands
     *
     * @return true if player can leave, false otherwise
     */
    @Override
    public boolean leave() {
        askingTrivia = true;
        return super.leave();
    }

    @Override
    public String getDescription(Direction direction) {
        if (askingTrivia) {
            return "Do you want to solve some trivia?";
        }
        else {
            return super.getDescription(direction);
        }
    }
}
