package comp3506.assn2.application;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

import comp3506.assn2.utils.CompressedTrie;
import comp3506.assn2.utils.Pair;

/**
 * Hook class used by automated testing tool. The testing tool will instantiate
 * an object of this class to test the functionality of your assignment. You
 * must implement the constructor stub below and override the methods from the
 * Search interface so that they call the necessary code in your application.
 * 
 * @author
 */
public class AutoTester implements Search {
	// The data structure of choice
	private CompressedTrie trie;

	/**
	 * Create an object that performs search operations on a document. If
	 * indexFileName or stopWordsFileName are null or an empty string the
	 * document should be loaded and all searches will be across the entire
	 * document with no stop words. All files are expected to be in the files
	 * sub-directory and file names are to include the relative path to the
	 * files (e.g. "files\\shakespeare.txt").
	 * 
	 * @param documentFileName
	 *            Name of the file containing the text of the document to be
	 *            searched.
	 * @param indexFileName
	 *            Name of the file containing the index of sections in the
	 *            document.
	 * @param stopWordsFileName
	 *            Name of the file containing the stop words ignored by most
	 *            searches.
	 * @throws FileNotFoundException
	 *             if any of the files cannot be loaded. The name of the file(s)
	 *             that could not be loaded should be passed to the
	 *             FileNotFoundException's constructor.
	 * @throws IllegalArgumentException
	 *             if documentFileName is null or an empty string.
	 */
	public AutoTester(String documentFileName, String indexFileName,
			String stopWordsFileName)
			throws FileNotFoundException, IllegalArgumentException {

		// Create Data Structure

		trie = new CompressedTrie();

		// Add text to data structure
		File file = new File(documentFileName);

		// We'll scan the document line by line
		Scanner lineScanner = new Scanner(file);
		lineScanner.useDelimiter(System.getProperty("line.separator"));
		int lineCounter = 1;

		String line = "";
		int lineLength = line.length();
		int lineIndex = 0;

		// We'll scan the line char by char
		int wordColumn = 0;
		String word = "";
		char nextChar = '0';

		// Used to determine if a word has been completed and should be added to
		// the Data Structure
		boolean startOfWordToggle = true;

		while (lineScanner.hasNext()) {
			// Get the line and length of line
			line = lineScanner.next();
			lineLength = line.length();

			// For every char in the line...
			for (lineIndex = 0; lineIndex < line.length(); lineIndex++) {
				// Get the char
				nextChar = line.charAt(lineIndex);

				// Word characters are either ' or [a-z]
				if (nextChar == '\'' || Character.isLetter(nextChar)) {
					// last char of the line is a ' so it's not part of the word
					if (nextChar == '\'' && lineIndex == line.length() - 1) {
						continue;
					}
					// char is a ' and we're either at the start of a word or
					// the char after ' is not a letter (more punctuation, etc)
					if (nextChar == '\'' && (word.length() < 1 || !(Character
							.isLetter(line.charAt(lineIndex + 1))))) {
						continue;
					}
					// Char is hereafter added to the word, so set column and
					// toggle
					if (startOfWordToggle) {
						wordColumn = lineIndex;
						startOfWordToggle = false;
					}
					// Add char to word
					word += nextChar;
				}

				// Is the char whitespace, or not a ' or [a-z], or at the
				// end of a line?
				if (Character.isWhitespace(nextChar)
						|| !(nextChar == '\'' || Character.isLetter(nextChar))
						|| lineIndex + 1 == lineLength) {
					// We're at the end of word
					if (word.length() > 0) {
						// Add to data structer (O(log n) see method javadoc)
						trie.add(word, lineCounter, wordColumn + 1);
						word = "";
					}
					// reset toggle
					startOfWordToggle = true;
				}
				//Next char in line
			}
			//next line
			++lineCounter;
		}
		// All added!
		lineScanner.close();

	}

	// See javadoc of CDT
	public int wordCount(String word) throws IllegalArgumentException {
		if (word == "" || word == null)
			throw new IllegalArgumentException();
		return trie.count(word);
	}
	// See javadoc of CDT
	public List<Pair<Integer, Integer>> phraseOccurrence(String phrase)
			throws IllegalArgumentException {
		if (phrase == "" || phrase == null)
			throw new IllegalArgumentException();
		// delimit the phrase using space
		String[] splitPhrase = phrase.split(" ");

		return trie.findPhrase(splitPhrase);
	}
	// See javadoc of CDT
	public List<Pair<Integer, Integer>> prefixOccurrence(String prefix)
			throws IllegalArgumentException {
		if (prefix == "" || prefix == null)
			throw new IllegalArgumentException();

		return trie.findPrefix(prefix);
	}
	// See javadoc of CDT
	public List<Integer> wordsOnLine(String[] words)
			throws IllegalArgumentException {
		if (words == null)
			throw new IllegalArgumentException();
		// Check validity of words in array
		for (String word : words) {
			if (word == null || word == "")
				throw new IllegalArgumentException();
		}

		return trie.wordsOnLine(words);

	}
	// See javadoc of CDT
	public List<Integer> someWordsOnLine(String[] words)
			throws IllegalArgumentException {
		if (words == null)
			throw new IllegalArgumentException();
		// Check validity of words in array
		for (String word : words) {
			if (word == null || word == "")
				throw new IllegalArgumentException();
		}

		return trie.someWordsOnLine(words);

	}

}
