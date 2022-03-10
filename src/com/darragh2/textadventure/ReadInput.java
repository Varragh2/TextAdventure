package com.darragh2.textadventure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;
import java.util.function.Consumer;

public class ReadInput <T> implements Cloneable {

    /****IMPORTANT********
    Any change to these instance variables must be reflected in clone() **/
    private HashMap<String, Consumer<T>> commands;
    private final Scanner scanner = new Scanner(System.in);
    private Scanner output;
    private Consumer<T> defaultCommand = s -> System.out.println("Unknown command, type 'help' for a list of commands");
    private String prompt = "Enter command: ";


    public ReadInput() {this(new HashMap<>());}

    public ReadInput(HashMap<String, Consumer<T>> commands) {
        this.commands = commands;

        //adds these default commands only if they haven't been set by the user
        //commands.putIfAbsent("help", s -> System.out.println("Commands: " + String.join( ", ", getCommands().keySet().stream().filter(key -> !key.equals("help") ).collect(Collectors.toSet()))));
    }

    public void setCommands(HashMap<String, Consumer<T>> commands) {
        this.commands = commands;
    }

    /**
     * Registers a new command
     * @param name the name of the command that the user enters. In lower case
     * @param command the action associated with a given name
     * @return the command that was replaced or null if no command was replaced. As defined in HashMap.put()
     */
    public Consumer<T> putCommand(String name, Consumer<T> command) {
        return this.commands.put(name, command);
    }

    /**
     * Adds all of newCommands.getCommands() to commands only if they are absent.
     * This method prioritizes commands over newCommands, it never overrides prompt or defaultCommand.
     * @param newCommands ReadInput to be added to commands
     */
    public void putIfAbsentAllCommands(ReadInput<T> newCommands) {
        if (newCommands != null) {
            newCommands.getCommands().forEach(commands::putIfAbsent);
        }
    }

    /**
     * Calls commands.put() on every item in newCommands.
     * This method prioritizes newCommands over commands, it overrides defaultCommand and prompt.
     * @param newCommands ReadInput to be added to commands
     */
    public void putAllCommands(ReadInput<T> newCommands) {
        if (newCommands == null) {
            return;
        }
        commands.putAll(newCommands.getCommands());
        setDefaultCommand(newCommands.getDefaultCommand());
        setPrompt(newCommands.getPrompt());
    }

    public HashMap<String, Consumer<T>> getCommands() {
        return commands;
    }

    public void andThen (Consumer<T> method) {
        //adds a method to do after any command is run
        commands.forEach( (string, command) -> {
            commands.put(string, command.andThen(method));
        });
    }
    public void andThen (Consumer<T> method, ArrayList<String> exceptions) {
        //adds a method to do after any command has run
        //except for any exceptions
        commands.forEach( (string, command) -> {
            if (!exceptions.contains(string)) {
                commands.put(string, command.andThen(method));
            }
            commands.putIfAbsent(string, command.andThen(method));
        });
    }

    /**
     * Prompts, as set by MapSite.prompt, the user to input a command and runs that command or MapSite.defaultCommand on T. Only runs once
     * @param T the instance of T that commands are run on
     */
    public void runCommands(T T) {
        System.out.print(prompt);
        String input = scanner.nextLine().toLowerCase().trim();
        commands.getOrDefault(input, defaultCommand).accept(T);
    }

    public void runCommands(T T, ArrayList<String> exceptions) {
        System.out.print(prompt);
        String input = scanner.nextLine().toLowerCase().trim();
        HashMap<String, Consumer<T>> newCommands = commands;
        for (String str : commands.keySet()) {
            if (!exceptions.contains(str)) {
                newCommands.put(str, commands.get(str));
            }
        }
        newCommands.getOrDefault(input, defaultCommand).accept(T);
    }

    public Consumer<T> getDefaultCommand() {
        return defaultCommand;
    }

    /**
     * Sets the command that is run if the user's input is not recognized. By default, it is, s -> System.out.println("Unknown command, type 'help' for a list of commands").
     * More formally if commands.get(userInput) == null, defaultCommand.accept(T) is run
     * @param defaultCommand the new defaultCommand
     */
    public void setDefaultCommand(Consumer<T> defaultCommand) {
        commands.put("default", defaultCommand);
        this.defaultCommand = defaultCommand;
    }

    public String getPrompt() {
        return prompt;
    }

    /**
     * Defines what ReadInput will prompt the user with before asking for input in ReadInput.runCommands().
     * More formally, in runCommands(): System.out.print(prompt); String = scanner.nextString();
     * @param prompt the prompt
     */
    public void setPrompt(String prompt) {
        this.prompt = prompt;
    }

    /**
    public void readCommands()
    {
        //Want to revamp readInput to be able to take a dynamic number of commands
        //that all have their own functionality
        System.out.print("Enter a command: \n");
        String input = scanner.nextLine().toLowerCase();
        input = input.toLowerCase();
        switch (input) {
            case "move" -> player.move();
            case "turn left" -> player.turnLeft();
            case "turn right" -> player.turnRight();
            case "direction" -> System.out.println("You are facing " + player.direction);
            case "help" -> System.out.println("Commands: 'move', 'turn left', 'turn right', 'direction'");
            default -> System.out.println("Unknown command, type 'help' for a list of commands");
        }
    }

    public boolean readYesNo(String input)
    {
        while (true)
        {
            if (input.equalsIgnoreCase("yes"))
            {
                return true;
            }
            else if (input.equalsIgnoreCase("no"))
            {
                return false;
            }
        }
    }

    public void readTrivia()
    {
        if (!readYesNo(readLine("Do you want to solve some trivia?\n")))
        {
            return;
        }
        //Ask trivia questions
        Hashtable <String, String> trivia = new Hashtable<>();
        trivia.put("\"Don't ever tell anybody anything. If you do, you start missing everybody.\" This is a quote from which book: \"Great Expectations\"(A) \"Animal Farm\" (B) \"1984\"(C) \"The Catcher in the Rye\"(D)", "d");
        trivia.put("Which Greek god is the goddess of corn, grain and harvest? Demeter(A) Dionysus(B) Poseidon(C) Pan(D)", "a");
        trivia.put("\"Cattleya\" could also be described as which color? Red(A) Blue(B) Purple(C) Pink(D)", "c");

        for (Map.Entry<String, String> entry : trivia.entrySet())
        {
            String a = readLine( (String) entry.getKey() + "\n");
            if (a.equalsIgnoreCase(entry.getValue()))
            {
                System.out.println("You got it right!");
            }
            else
            {
                System.out.println("You failed, good luck next time.");
                return;
            }
        }
        System.out.println("The cat hands you a key, this might be useful later.");
        player.hasKey = true;
        TextAdventure.board.get(1).get(2).createAdjacentRoom(Direction.EAST, new Room(new String[] {"", "You won!!!", "", ""}, 6));

    }

    public String endGame()
    {
        if (!T.alive)
        {
            return readLine("You just died, do you want to keep playing? \n");
        }
        if (T.won)
        {
            return readLine("Congratulations, do you want to play again? \n");
        }
        return null;
    }

    private String readLine(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }
     **/

    /**
     * Creates and returns a copy of this object.  The precise meaning
     * of "copy" may depend on the class of the object. The general
     * intent is that, for any object {@code x}, the expression:
     * <blockquote>
     * <pre>
     * x.clone() != x</pre></blockquote>
     * will be true, and that the expression:
     * <blockquote>
     * <pre>
     * x.clone().getClass() == x.getClass()</pre></blockquote>
     * will be {@code true}, but these are not absolute requirements.
     * While it is typically the case that:
     * <blockquote>
     * <pre>
     * x.clone().equals(x)</pre></blockquote>
     * will be {@code true}, this is not an absolute requirement.
     * <p>
     * By convention, the returned object should be obtained by calling
     * {@code super.clone}.  If a class and all of its superclasses (except
     * {@code Object}) obey this convention, it will be the case that
     * {@code x.clone().getClass() == x.getClass()}.
     * <p>
     * By convention, the object returned by this method should be independent
     * of this object (which is being cloned).  To achieve this independence,
     * it may be necessary to modify one or more fields of the object returned
     * by {@code super.clone} before returning it.  Typically, this means
     * copying any mutable objects that comprise the internal "deep structure"
     * of the object being cloned and replacing the references to these
     * objects with references to the copies.  If a class contains only
     * primitive fields or references to immutable objects, then it is usually
     * the case that no fields in the object returned by {@code super.clone}
     * need to be modified.
     *
     * @return a clone of this instance.
     * @throws CloneNotSupportedException if the object's class does not
     *                                    support the {@code Cloneable} interface. Subclasses
     *                                    that override the {@code clone} method can also
     *                                    throw this exception to indicate that an instance cannot
     *                                    be cloned.
     * @implSpec The method {@code clone} for class {@code Object} performs a
     * specific cloning operation. First, if the class of this object does
     * not implement the interface {@code Cloneable}, then a
     * {@code CloneNotSupportedException} is thrown. Note that all arrays
     * are considered to implement the interface {@code Cloneable} and that
     * the return type of the {@code clone} method of an array type {@code T[]}
     * is {@code T[]} where T is any reference or primitive type.
     * Otherwise, this method creates a new instance of the class of this
     * object and initializes all its fields with exactly the contents of
     * the corresponding fields of this object, as if by assignment; the
     * contents of the fields are not themselves cloned. Thus, this method
     * performs a "shallow copy" of this object, not a "deep copy" operation.
     * <p>
     * The class {@code Object} does not itself implement the interface
     * {@code Cloneable}, so calling the {@code clone} method on an object
     * whose class is {@code Object} will result in throwing an
     * exception at run time.
     * @see Cloneable
     */
    @Override
    public ReadInput<T> clone() {
        ReadInput<T> clone = new ReadInput<>();
        clone.setPrompt(prompt);
        clone.setDefaultCommand(defaultCommand);
        clone.setCommands(new HashMap<>(commands));
        return clone;
    }
}

