package comp3506.assn2.application;


import java.util.List;

import comp3506.assn2.utils.Pair;
import comp3506.assn2.utils.Triple;


/**
 * Interface that will be used by the test driver for your submitted assignment.
 * All methods have a default implementation that indicates that they have not been implemented.
 * 
 * @author Richard Thomas <richard.thomas@uq.edu.au>
 */
public interface Search {

	/**
	 * Determines the number of times the word appears in the document.
	 * 
	 * @param word The word to be counted in the document.
	 * @return The number of occurrences of the word in the document.
	 * @throws IllegalArgumentException if word is null or an empty String.
	 */
	default int wordCount(String word) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.wordCount() Not Implemented.");
	}

	/**
	 * Finds all occurrences of the phrase in the document.
	 * A phrase may be a single word or a sequence of words.
	 * 
	 * @param phrase The phrase to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column number of each occurrence of the phrase.
	 *         Returns an empty list if the phrase is not found in the document.
	 * @throws IllegalArgumentException if phrase is null or an empty String.
	 */
	default List<Pair<Integer,Integer>> phraseOccurrence(String phrase) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.phraseOccurrence() Not Implemented.");
	}
	
	/**
	 * Finds all occurrences of the prefix in the document.
	 * A prefix is the start of a word. It can also be the complete word.
	 * For example, "obscure" would be a prefix for "obscure", "obscured", "obscures" and "obscurely".
	 * 
	 * @param prefix The prefix of a word that is to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column number of each occurrence of the prefix.
	 *         Returns an empty list if the prefix is not found in the document.
	 * @throws IllegalArgumentException if prefix is null or an empty String.
	 */
	default List<Pair<Integer,Integer>> prefixOccurrence(String prefix) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.prefixOccurrence() Not Implemented.");
	}
	
	/**
	 * Searches the document for lines that contain all the words in the 'words' parameter.
	 * Implements simple "and" logic when searching for the words.
	 * The words do not need to be contiguous on the line.
	 * 
	 * @param words Array of words to find on a single line in the document.
	 * @return List of line numbers on which all the words appear in the document.
	 *         Returns an empty list if the words do not appear in any line in the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in the array are null or empty.
	 */
	default List<Integer> wordsOnLine(String[] words) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.wordsOnLine() Not Implemented.");
	}
	
	/**
	 * Searches the document for lines that contain any of the words in the 'words' parameter.
	 * Implements simple "or" logic when searching for the words.
	 * The words do not need to be contiguous on the line.
	 * 
	 * @param words Array of words to find on a single line in the document.
	 * @return List of line numbers on which any of the words appear in the document.
	 *         Returns an empty list if none of the words appear in any line in the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in the array are null or empty.
	 */
	default List<Integer> someWordsOnLine(String[] words) throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.someWordsOnLine() Not Implemented.");
	}
	
	/**
	 * Searches the document for lines that contain all the words in the 'wordsRequired' parameter
	 * and none of the words in the 'wordsExcluded' parameter.
	 * Implements simple "not" logic when searching for the words.
	 * The words do not need to be contiguous on the line.
	 * 
	 * @param wordsRequired Array of words to find on a single line in the document.
	 * @param wordsExcluded Array of words that must not be on the same line as 'wordsRequired'.
	 * @return List of line numbers on which all the wordsRequired appear 
	 *         and none of the wordsExcluded appear in the document.
	 *         Returns an empty list if no lines meet the search criteria.
	 * @throws IllegalArgumentException if either of wordsRequired or wordsExcluded are null or an empty array 
	 *                                  or any of the Strings in either of the arrays are null or empty.
	 */
	default List<Integer> wordsNotOnLine(String[] wordsRequired, String[] wordsExcluded) 
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.wordsNotOnLine() Not Implemented.");
	}
	
	/**
	 * Searches the document for sections that contain all the words in the 'words' parameter.
	 * Implements simple "and" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param words Array of words to find within a defined section in the document.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in either of the arrays are null or empty.
	 */
	default List<Triple<Integer,Integer,String>> simpleAndSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.simpleAndSearch() Not Implemented.");
	}
	
	/**
	 * Searches the document for sections that contain any of the words in the 'words' parameter.
	 * Implements simple "or" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param words Array of words to find within a defined section in the document.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if words is null or an empty array 
	 *                                  or any of the Strings in either of the arrays are null or empty.
	 */
	default List<Triple<Integer,Integer,String>> simpleOrSearch(String[] titles, String[] words)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.simpleOrSearch() Not Implemented.");
	}
	
	/**
	 * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
	 * and none of the words in the 'wordsExcluded' parameter.
	 * Implements simple "not" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param wordsRequired Array of words to find within a defined section in the document.
	 * @param wordsExcluded Array of words that must not be in the same section as 'wordsRequired'.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the required words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if wordsRequired is null or an empty array 
	 *                                  or any of the Strings in any of the arrays are null or empty.
	 */
	default List<Triple<Integer,Integer,String>> simpleNotSearch(String[] titles, String[] wordsRequired, 
			                                                     String[] wordsExcluded)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.simpleNotSearch() Not Implemented.");
	}
	
	/**
	 * Searches the document for sections that contain all the words in the 'wordsRequired' parameter
	 * and at least one of the words in the 'orWords' parameter.
	 * Implements simple compound "and/or" logic when searching for the words.
	 * The words do not need to be on the same lines.
	 * 
	 * @param titles Array of titles of the sections to search within, 
	 *               the entire document is searched if titles is null or an empty array.
	 * @param wordsRequired Array of words to find within a defined section in the document.
	 * @param orWords Array of words, of which at least one, must be in the same section as 'wordsRequired'.
	 * @return List of triples, where each triple indicates the line and column number and word found,
	 *         for each occurrence of one of the words.
	 *         Returns an empty list if the words are not found in the indicated sections of the document, 
	 *         or all the indicated sections are not part of the document.
	 * @throws IllegalArgumentException if wordsRequired is null or an empty array 
	 *                                  or any of the Strings in any of the arrays are null or empty.
	 */
	default List<Triple<Integer,Integer,String>> compoundAndOrSearch(String[] titles, String[] wordsRequired, 
			                                                         String[] orWords)
			throws IllegalArgumentException {
		throw new UnsupportedOperationException("Search.compoundAndOrSearch() Not Implemented.");
	}

}
