# EvilHangman

This program implements a class that keeps track of the state of a game of hangman.  But this won’t be any ordinary game of hangman.  Our hangman program is going to cheat.

If you aren’t familiar with the general rules of hangman, you should review the wikipedia entry for it:

http://en.wikipedia.org/wiki/Hangman_%28game%29

In a normal game of hangman, the computer picks a word that the user is supposed to guess.  In our game of hangman, the computer is going to delay picking a word until it is forced to.  As a result, at any given point in time there will be a set of words that are currently being used by the computer.  Each of those words will have the same pattern to be displayed to the user.  
