import java.util.*;

public class HangmanManager {
    private String patternDisplayed; // keeps track of the pattern displayed
    private Set<String> wordsConsidered; // keeps track of the possibilities for words
    private int guessCount; // keeps track of guesses the user has left
    private SortedSet<Character> lettersGuessed; // keeps track of the letters guessed

    // Takes in a dictionary of words, a target word length,
    // and the maximum number of wrong guesses the player is allowed to make.
    // This method uses the values to initialize the state of the game.
    // The set of words initially contains all words from the dictionary
    // of the given length, eliminating any duplicates.
    // Throws an IllegalArgumentException if length is less than 1 or if max is less
    // than 0.
    public HangmanManager(Collection<String> dictionary, int length, int max) {
        if (length < 1 || max < 0) {
            throw new IllegalArgumentException();
        }
        guessCount = max;
        wordsConsidered = new TreeSet<String>();
        lettersGuessed = new TreeSet<Character>();
        for (String word : dictionary) {
            if (word.length() == length) {
                wordsConsidered.add(word);
            }
        }
        patternDisplayed = "";
        for (int i = 0; i < length; i++) {
            patternDisplayed += "- ";
        }
    }

    // The client calls this method to get access to the current set of words being
    // considered by the hangman manager.
    public Set<String> words() {
        return wordsConsidered;
    }

    // The client calls this method to find out how many guesses the player has
    // left.
    public int guessesLeft() {
        return guessCount;
    }

    // The client calls this method to find out the current set of letters that have
    // been guessed by the user.
    public Set<Character> guesses() {
        return lettersGuessed;
    }

    // This method returns the current pattern to be displayed for the hangman game taking
    // into account guesses that have been made. Letters that have not yet been guessed
    // will be displayed as a dash and there will be spaces separating the letters.
    // There will be no leading or trailing spaces.
    // Throws an IllegalStateException if the set of words is empty.
    public String pattern() {
        if (wordsConsidered.isEmpty()) {
            throw new IllegalStateException();
        }
        return patternDisplayed;
    }

    // This method does most of the work by recording the next guess made by the user.
    // Using the guess, it decides what set of words to use going forward.
    // It returns the number of occurrences of the guessed letter in the new pattern
    // and appropriately updates the number of guesses left.
    // Throws an IllegalStateException if the number of guesses left is not at least 1
    // or if the set of words is empty. If the previous exception is not thrown
    // Throws an IllegalArgumentException if the character being guessed was guessed
    // previously.
    public int record(char guess) {
        if (wordsConsidered.isEmpty() || guessCount < 1) {
            throw new IllegalStateException();
        } else if (!wordsConsidered.isEmpty() && lettersGuessed.contains(guess)) {
            throw new IllegalArgumentException();
        }
        lettersGuessed.add(guess);
        Map<String, Set<String>> findFamily = new TreeMap<String, Set<String>>();
        for (String word : wordsConsidered) {
            String pattern = patternMaker(guess, word);
            Set<String> setOfWords = new TreeSet<String>();
            if (!findFamily.containsKey(pattern)) {
                findFamily.put(pattern, setOfWords);
            }
            findFamily.get(pattern).add(word);
        }
        int maximumSize = 0;
        if (!findFamily.isEmpty()) {
            for (String currentPattern : findFamily.keySet()) {
                if (findFamily.get(currentPattern).size() > maximumSize) {
                    wordsConsidered.clear();
                    wordsConsidered.addAll(findFamily.get(currentPattern));
                    patternDisplayed = currentPattern;
                    maximumSize = findFamily.get(currentPattern).size();
                }
            }
        }
        return decision(guess);
    }

    // This method keeps updating the pattern with each input
    // dashes represent unidentified letter
    // returns the pattern going to be displayed
    private String patternMaker(char guess, String word) {
        String pattern = "";
        int index = 0;
        for (int i = 0; i < word.length(); i++) {
            if (word.charAt(i) != guess) {
                pattern += patternDisplayed.substring(index, index + 2);
            } else {
                pattern += guess + " ";
            }
            index += 2;
        }
        return pattern;
    }

    // Tracks the occurences of the letters in the pool
    // If the occurence is zero it gets reduced by one
    // returns the maximum occurences of the letters
    private int decision(char guess) {
        int maxOccurence = 0;
        for (int i = 0; i < patternDisplayed.length(); i++) {
            if (patternDisplayed.charAt(i) == guess) {
                maxOccurence++;
            }
        }
        lettersGuessed.add(guess);
        if (maxOccurence == 0) {
            guessCount--;
        }
        return maxOccurence;
    }
}