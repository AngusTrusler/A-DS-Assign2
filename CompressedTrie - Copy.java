package comp3506.assn2.utils;

import java.util.LinkedList;
import java.util.List;

public class CompressedTrie {
	private trieNode root;
	private trieNode lastAddedWord;
	private int uniqueWordsCount;

	public CompressedTrie() {
		root = new trieNode();
		lastAddedWord = null;
	}

	/**
	 * Adds the next word in the text to the trie.
	 *
	 * O(log n) where n is the length of the text document
	 *
	 * or
	 * 
	 * O(a * l) where a is the length of the alphabet and l is the length of the
	 * word
	 *
	 * @param word
	 *            The next word in the text
	 * @param line
	 *            The line on which the word is located
	 * @param column
	 *            The column at which the word begins
	 */
	public void add(String word, int line, int column) {
		// Make case in-sensitive
		word = word.toLowerCase();
		// If there is word currently in the trie, make this the first child
		// (adam!)
		if (root.child == null) {
			root.child = new trieNode(word, line, column);
			lastAddedWord = root.child;
			root.child.parent = root;
			++uniqueWordsCount;
			return;
		}

		// There are nodes in the trie already, so prepare to traverse trie
		trieNode spider = root.child; // "Spider" because it's like a spider
										// crawling over a web
		// The new node with word to be added
		trieNode newWord = new trieNode(word, line, column);
		// The position in the newWord / Spider
		int wordIndex = 0;
		// The characters at these positions
		char wordPoint = newWord.value.charAt(wordIndex);
		char nodePoint = spider.value.charAt(wordIndex);
		// The length of the smaller node
		int wordsMin = Math.min(word.length(), spider.value.length()) - 1;

		// Continue looking until we've reached the end of the word or node
		for (wordIndex = 0; wordIndex <= wordsMin; wordIndex++) {
			// Load the next char
			wordPoint = newWord.value.charAt(wordIndex);
			nodePoint = spider.value.charAt(wordIndex);

			// Are the chars the same? (word not found yet?)
			if (wordPoint != nodePoint) {
				// Which char lexicographically comes first?
				if (nodePoint < wordPoint) {
					// Have we matched any chars yet?
					if (wordIndex == 0) {
						// No matched chars yet
						// search sibling node
						if (spider.nextSibling == null) {
							// No sibling node: word not found: add node after
							// spider
							nodeSlot(spider, newWord);
							return;
						}
						// Sibling found: appropriate node could be after this
						// point. Restart search with next node word.
						spider = spider.nextSibling;
						wordsMin = Math.min(newWord.value.length(),
								spider.value.length()) - 1;
						wordIndex = -1;
						continue;
					} else {
						// We have matched chars, but differ at some point in
						// the node. This is as close as we can get: split node
						nodeSplitTriple(wordIndex, spider, newWord);
						return;
					}
				} else {
					// Have we matched any chars yet?
					if (wordIndex == 0) {
						// No matched chars
						// node can't be split: insert node between spider
						// and prev sibling
						nodeSlot(spider, newWord);
						return;
					} else {
						// this is as close as we can get:
						nodeSplitTriple(wordIndex, spider, newWord);
						return;
					}
				}

			}

			// end of word/node reached
			if (wordIndex == wordsMin) {
				if (newWord.value.length() == spider.value.length()) {
					// Length and chars match: identical word found
					spider.newLocation(line, column);
					setWordNeighbour(spider);
					return;
				}
				if (newWord.value.length() < spider.value.length()) {
					// word is a prefix of node, split node.
					nodeSplitDouble(spider, newWord);
					return;
				} else {
					// node is a prefix of word
					// check word isn't already child of spider
					if (spider.child == null) {
						nodeSplitDouble(spider, newWord);
						return;
					}
					// children found. substring newWord and restart search
					// with children
					newWord.value = newWord.value.substring(++wordIndex);
					spider = spider.child;
					wordsMin = Math.min(newWord.value.length(),
							spider.value.length()) - 1;
					wordIndex = -1;
					continue;
				}
			}
		}
	}

	/**
	 * This method splits an existing node into three nodes.
	 * 
	 * E.g. existing node: "race" (spider), want to add: "rise" (newWord)
	 * 
	 * Node is split into "r" with "ace" and "ise" as children.
	 * 
	 * "r" is the parent node as "ace" lexicographically preceeds "ise", "ace"
	 * becomes "r"'s child, and "ise" becomes "ace"'s nextSibling
	 * 
	 * O(1)
	 *
	 * @param matchIndex
	 *            The position within the existing node at which the two words
	 *            no longer match. Using above example, the second character is
	 *            the first character that no longer matches. r == r a != i.
	 *            Because zero indexing, this is at position 1
	 * @param spider
	 *            Existing node
	 * @param newWord
	 *            New Node
	 */
	private void nodeSplitTriple(int matchIndex, trieNode spider,
			trieNode newWord) {

		// Create new node of shared letters.
		trieNode newInternal = new trieNode(
				spider.value.substring(0, matchIndex), spider.parent);

		// Assume spider's siblings as its own
		newInternal.nextSibling = spider.nextSibling;
		newInternal.prevSibling = spider.prevSibling;

		// Is spider the first child of its parent?
		if (spider.parent.child == spider) {
			// Let it know who its new first child is
			spider.parent.child = newInternal;
		}
		// Does Spider have a next sibling?
		if (spider.nextSibling != null) {
			// Let it know who its new succeeding sibling is
			spider.nextSibling.prevSibling = newInternal;
		}
		// Does Spider have a prev sibling?
		if (spider.prevSibling != null) {
			// Let it know who its new preceding sibling is
			spider.prevSibling.nextSibling = newInternal;
		}

		// Remove now redundant characters from spider
		spider.value = spider.value.substring(matchIndex);
		// Set spider's new parent
		spider.parent = newInternal;

		// Remove now redundant characters from newWord
		newWord.value = newWord.value.substring(matchIndex);
		// Set newWord's new parent
		newWord.parent = newInternal;

		// Decide who gets to be the new parent's first child
		// (who lexicographically comes first)
		if (newWord.value.compareTo(spider.value) > 0) {
			// Spider wins
			newInternal.child = spider;
			spider.nextSibling = newWord;
			spider.prevSibling = null;
			newWord.prevSibling = spider;
		} else {
			// newWord wins
			newInternal.child = newWord;
			newWord.nextSibling = spider;
			spider.prevSibling = newWord;
			spider.nextSibling = null;
		}
		// Set the new position of the most recently added word for nearest
		// neighbour searches (phraseOccurence)
		setWordNeighbour(newWord);
		++uniqueWordsCount;
	}

	/**
	 * This method is used for dealing with prefixes. E.g. existing node: "race"
	 * (spider), want to add: "races" (newWord) Neither node will have children
	 * at this point (if it did, we would be nodeSlotting)
	 * 
	 * O(1)
	 * 
	 * @param spider
	 *            Existing Node
	 * @param newWord
	 *            New Node
	 */
	private void nodeSplitDouble(trieNode spider, trieNode newWord) {

		// Who is the prefix of who?
		if (spider.value.length() < newWord.value.length()) {
			// spider is a prefix of newWord, so remove now redundant characters
			// from newWord
			newWord.value = newWord.value.substring(spider.value.length());
			// Set up appropriate child/parent relationship
			newWord.parent = spider;
			spider.child = newWord;
		} else {
			// newWord is a prefix of spider. Remove redundant characters
			spider.value = spider.value.substring(newWord.value.length());
			newWord.parent = spider.parent;

			// Because spider could already have siblings, we need to make sure
			// newWord fits in with its new siblings, and spider properly says
			// goodbye to its current siblings: as spider will now become a
			// child of newWord (one level deeper on the tree)
			newWord.nextSibling = spider.nextSibling;
			newWord.prevSibling = spider.prevSibling;
			if (spider.prevSibling != null) {
				spider.prevSibling.nextSibling = newWord;
			}
			if (spider.nextSibling != null) {
				spider.nextSibling.prevSibling = newWord;
			}
			// Is spider the first of its parent's children? If so, fix that.
			if (spider.parent.child == spider) {
				spider.parent.child = newWord;
			}
			newWord.child = spider;
			spider.parent = newWord;
			// Now a level deeper and the first (and only) child of newWord,
			// spider must now forget its siblings (lucky spider :P )
			spider.nextSibling = null;
			spider.prevSibling = null;
		}
		// Set the new position of the most recently added word for nearest
		// neighbour searches (phraseOccurence)
		setWordNeighbour(newWord);
		++uniqueWordsCount;
	}

	/**
	 * This method is for dealing with bog-standard lexicographical order
	 * amongst sibling nodes. E.g. Existing node: "that" (spider), want to add
	 * "with" (newWord)
	 * 
	 * O(1)
	 * 
	 * @param spider
	 *            Sibling node
	 * @param newWord
	 *            Sibling node
	 */
	private void nodeSlot(trieNode spider, trieNode newWord) {
		// Does spider come after newWord?
		if (spider.value.compareTo(newWord.value) > 0) {
			newWord.prevSibling = spider.prevSibling;
			newWord.nextSibling = spider;
			newWord.parent = spider.parent;
			if (newWord.prevSibling != null) {
				newWord.prevSibling.nextSibling = newWord;
			} else {
				spider.parent.child = newWord;
			}
			spider.prevSibling = newWord;

		} else {
			// Spider comes before newWord
			newWord.nextSibling = spider.nextSibling;
			newWord.prevSibling = spider;
			newWord.parent = spider.parent;
			if (newWord.nextSibling != null) {
				newWord.nextSibling.prevSibling = newWord;
			}
			if (newWord.prevSibling == null) {
				spider.parent.child = newWord;
			}
			spider.nextSibling = newWord;
		}
		// O(1)
		setWordNeighbour(newWord);
		++uniqueWordsCount;
	}

	/**
	 * This method links two word instances together, preserving the order in
	 * which they appear in the text document.
	 *
	 * O(1)
	 *
	 * @param newWord
	 *            Newly added word in the trie
	 */
	private void setWordNeighbour(trieNode newWord) {
		// Is the last added word the the same word as the newWord?
		// Example: "the the" "0,1"
		if (lastAddedWord.equals(newWord)) {
			// This method is called after the new location is already added to
			// the node's list of locations
			// 1.prev = 0
			lastAddedWord.locations.prevNeighbourCoordinates = lastAddedWord.locations.nextCoordinate;
			// 0.next = 1
			lastAddedWord.locations.nextCoordinate.nextNeighbourCoordinates = lastAddedWord.locations;
			return;
		}
		// Linking the last word to the current (latest) word
		lastAddedWord.locations.nextNeighbourCoordinates = newWord.locations;
		// Check we're not at the first added word
		if (lastAddedWord != null) {
			// Linking the current (latest) word to the last word
			newWord.locations.prevNeighbourCoordinates = lastAddedWord.locations;
		}
		// Set up for next word
		lastAddedWord = newWord;
	}

	/**
	 * Searches the Compressed Trie for the node at which the given string
	 * terminates.
	 * 
	 * O(log n) where n is the length of the text document. Because Zipf's Law
	 * and Heap's Law (see readme.pdf)
	 * 
	 * or
	 * 
	 * O(n): O(a * l) where a is the length of the alphabet and l is the length
	 * of the word
	 * 
	 * @param word
	 *            The word to search for
	 * @param mode
	 *            Flag parameter: determines whether or not prefixes should be
	 *            returned.
	 * 
	 *            0 = only return a node if the string exists as a whole word
	 *            within the text document, null otherwise
	 * 
	 *            1 = return node when end of word reached regardless (all
	 *            subsequent children with counts >= 1 will be words that have
	 *            word as prefix)
	 * @return trieNode The node at which the given string terminates, or null,
	 *         if no such node found
	 */
	private trieNode findWord(String word, int mode) {
		//

		trieNode spider = root.child;
		int wordIndex = 0;
		char wordPoint = word.charAt(wordIndex);
		char nodePoint = spider.value.charAt(wordIndex);
		// Minimum of word.length and spider.value.length
		int wordsMin = Math.min(word.length(), spider.value.length()) - 1;
		// Local variable instead of running string.length() every time.
		int wordLength = word.length();

		// While we're not at the end of either the word or the spider's length,
		// do...
		for (wordIndex = 0; wordIndex <= wordsMin; ++wordIndex) {
			// Get next character of both
			wordPoint = word.charAt(wordIndex);
			nodePoint = spider.value.charAt(wordIndex);

			// If the character's don't match...
			if (wordPoint != nodePoint) {
				// If word's character lexicographically preceeds node's
				// character, the word can't be in the trie ("b" will never be
				// after "c" in dictionary)
				if (wordPoint < nodePoint) {
					// word not in trie
					return null;
				} else {
					// Not there yet, but are there remaining words in the trie?
					if (spider.nextSibling != null) {
						// Yes, appropriate node could still be in remaining
						// children. Move to next child, and reset loop
						// parameters.
						spider = spider.nextSibling;
						wordsMin = Math.min(word.length(),
								spider.value.length()) - 1;
						wordIndex = -1;
						continue;
					} else {
						// No words left
						return null;
					}
				}
			}
			// Are we at the end of either the word or node?
			if (wordIndex == wordsMin) {
				// Reached end of word?
				if (wordLength < spider.value.length()) {
					// word is a prefix of spider
					// are we looking for prefixes?
					if (mode == 1) {
						// Yes, return node
						return spider;
					}
					// Not looking for prefixes, so word not found
					return null;
				}
				// Or reached end of node?
				if (wordLength > spider.value.length()) {
					// Are there more words to search?
					if (spider.child != null) {
						// Yes, appropriate node could still be in children.
						// Move to child, and reset loop parameters.
						spider = spider.child;
						word = word.substring(++wordIndex);
						wordLength = word.length();
						wordsMin = Math.min(word.length(),
								spider.value.length()) - 1;
						wordIndex = -1;
						continue;
					}
					// No more words, word not found
					return null;
				}

				// as all letters match, and length is same, node must be found
				return spider;
			}
			// If not at the end of either word or node, and current letters
			// match, continue to next character in both
		}
		return null;
	}

	/**
	 * Determines the number of times the word appears in the document.
	 *
	 * O(log n) (see findWord javaDoc)
	 * 
	 * @param word
	 *            The word to be counted in the document.
	 * @return The number of occurrences of the word in the document.
	 */
	public int count(String word) {
		trieNode node = findWord(word, 0);
		if (node != null) {
			return node.count;
		}
		return 0;
	}

	/**
	 * Finds all occurrences of the phrase in the document. A phrase may be a
	 * single word or a sequence of words.
	 * 
	 * O(n) (typical): O(f * l) where f is the number of times the phrase's
	 * first word occurs in the document, and l is the length of the phrase
	 * 
	 * or
	 * 
	 * O(n^2) (pathological): If the text document consists of only one word
	 * many times, and the phrase length is the length of the document, the run
	 * time is proportional to n^2 as f == l
	 * 
	 * @param phrase
	 *            The phrase to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column
	 *         number of each occurrence of the phrase. Returns an empty list if
	 *         the phrase is not found in the document.
	 */
	public List<Pair<Integer, Integer>> findPhrase(String[] phrase) {
		// Count of words in phrase
		int wordCount = phrase.length;

		// Used exclusively because search.java returns a list of pairs.
		// Not used as part of method behaviour
		List<Pair<Integer, Integer>> phraseLocations = new LinkedList<Pair<Integer, Integer>>();

		// find all instances of the first word in the phrase
		// O(log n) (see method javadoc)
		trieNode firstWord = findWord(phrase[0], 0);

		if (firstWord == null) {
			// first word NOT found == phrase doesn't exist in text
			return phraseLocations;
		}

		// all instances of word (coordinate form)
		textCoordinates wordLocations = firstWord.locations;

		// If there's only one word, then all instances of the word count as the
		// phrase, so return them all. O(f) f = frequency of word
		if (wordCount == 1) {
			for (int i = 0; i < firstWord.count; ++i) {
				phraseLocations.add(new Pair<Integer, Integer>(
						wordLocations.line, wordLocations.column));
				wordLocations = wordLocations.nextCoordinate;
			}
			return phraseLocations;
		}

		// While there is another instance of word, do...
		// O(f) f = frequency of word
		while (wordLocations.nextCoordinate != null) {
			// For every word in the phrase, do...
			for (int i = 1; i < wordCount; ++i) {
				// Get the nthWord n words away from our word
				textCoordinates nthNextNeighbour = wordLocations
						.nthNextNeighbour(i);
				// If we're off the end of the document, try the next instance
				if (nthNextNeighbour == null) {
					break;
				}
				// Get the word's full value from the coordinate's node
				// O(n) (see method javadoc)
				String nthWord = nthNextNeighbour.word.getWord();

				// Test if node's word matches word in phrase
				if (!(nthWord.equals(phrase[i]))) {
					break;
				}
				// Test if we're at the last word in the phrase
				if (i == wordCount - 1) {
					// Phrase Found!
					Pair<Integer, Integer> phraseLocation = new Pair<Integer, Integer>(
							wordLocations.line, wordLocations.column);
					phraseLocations.add(phraseLocation);
				}
			}
			// Move to next instance of phrase's first word
			wordLocations = wordLocations.nextCoordinate;
		}

		return phraseLocations;
	}

	/**
	 * Searches the document for lines that contain all the words in the 'words'
	 * parameter. Implements simple "and" logic when searching for the words.
	 * The words do not need to be contiguous on the line.
	 * 
	 * O(n) where n is the number of times the rarest word in the array appears
	 * in the text
	 * 
	 * @param words
	 *            Array of words to find on a single line in the document.
	 * @return List of line numbers on which all the words appear in the
	 *         document. Returns an empty list if the words do not appear in any
	 *         line in the document.
	 *
	 */
	public List<Integer> wordsOnLine(String[] words) {
		// If there's only one word to find, we can utilise our logical 'or'
		// method just fine
		// O(n) (see method javadoc)
		if (words.length == 1) {
			return someWordsOnLine(words);
		}

		// Used exclusively to return results, not used for method behaviour
		List<Integer> linesWithAllWords = new LinkedList<Integer>();

		// Need to store array words in a way to omits duplicates and gives us
		// an accurate count of unique words
		CompressedTrie wordsTrie = new CompressedTrie();

		// Because this is a logical 'and' search, only checking lines with the
		// rarest word dramatically cuts down on possible searches to do
		trieNode rarestWord = null;

		for (String word : words) {
			// Find the word in the main document
			trieNode wordsNode = findWord(word, 0);
			if (wordsNode == null) {
				// If this word doesn't occur anywhere, no point in checking the
				// rest of the document
				return linesWithAllWords;
			}
			// Add the word to our new phrase storage type
			wordsTrie.add(word, 1, 1);

			// Determine which word is the rarest of them all
			if (rarestWord == null) {
				rarestWord = wordsNode;
			} else {
				if (rarestWord.count > wordsNode.count) {
					rarestWord = wordsNode;
				}
			}
		}

		textCoordinates rarestWordInstance = rarestWord.locations;
		textCoordinates spider = rarestWordInstance;

		// Check every line on which the rarest word is found
		while (spider != null) {
			// Need to store the line's words in a way to omits duplicates and
			// gives us an accurate count of unique words
			CompressedTrie lineCandidateTrie = new CompressedTrie();

			int currentLine = spider.line;

			// Navigate to line's first word
			while (true) {
				if (spider.prevNeighbourCoordinates != null) {
					if (spider.prevNeighbourCoordinates.line == currentLine) {
						spider = spider.prevNeighbourCoordinates;
					} else {
						break;
					}
				} else {
					break;
				}
			}

			// Move forward across the line, adding only words that are in
			// words[]
			do {
				String spiderWord = spider.word.getWord();
				// O(log n) (see method javadoc)
				if (wordsTrie.count(spiderWord) > 0) {
					// O(log n) (see method javadoc)
					lineCandidateTrie.add(spiderWord, 1, 1);
				}
				spider = spider.nextNeighbourCoordinates;
				if (spider.nextNeighbourCoordinates == null) {
					break;
				}
			} while (spider.prevNeighbourCoordinates.line == spider.line);

			// Because wordsTrie has exclusively all the words in the phrase
			// and because a word is only added to lineCadidateTrie if it exists
			// in wordsTre, if the counts are the same, then this is a valid
			// line
			if (wordsTrie.uniqueWordsCount == lineCandidateTrie.uniqueWordsCount) {
				// O(1) (adding to unordered linked list is always O(1))
				linesWithAllWords.add(rarestWordInstance.line);
			}

			// Move to the next line on which the rarest word occurs and start
			// search again
			rarestWordInstance = rarestWordInstance.nextCoordinate;
			spider = rarestWordInstance;
		}

		return linesWithAllWords;
	}

	/**
	 * Finds all occurrences of the prefix in the document. A prefix is the
	 * start of a word. It can also be the complete word. For example, "obscure"
	 * would be a prefix for "obscure", "obscured", "obscures" and "obscurely".
	 * 
	 * O(n): O(i + (n * c)) where i is the number of internal nodes, n is the
	 * number of nodes with valid words, and c is the number of instances of
	 * that valid word
	 * 
	 * @param prefix
	 *            The prefix of a word that is to be found in the document.
	 * @return List of pairs, where each pair indicates the line and column
	 *         number of each occurrence of the prefix. Returns an empty list if
	 *         the prefix is not found in the document.
	 */
	public List<Pair<Integer, Integer>> findPrefix(String prefix) {
		// Used exclusively because search.java returns a list of pairs.
		// Not used as part of method behaviour
		List<Pair<Integer, Integer>> prefixLocations = new LinkedList<Pair<Integer, Integer>>();
		// find node representing the prefix
		trieNode prefixNode = findWord(prefix, 1);

		if (prefixNode == null) {
			// no such prefix exists
			return prefixLocations;
		}
		// O(n) (see method javadoc)
		// While it is in preOrder that the (n * c) part occurs, because
		// preOrder is more of a generic method, I have that part here
		preOrder(0, prefixLocations, null, prefixNode);

		return prefixLocations;
	}

	/**
	 * Searches the document for lines that contain any of the words in the
	 * 'words' parameter. Implements simple "or" logic when searching for the
	 * words. The words do not need to be contiguous on the line.
	 * 
	 * O(n) where n is the number of word instances to find.
	 * 
	 * @param words
	 *            Array of words to find on a single line in the document.
	 * @return List of line numbers on which any of the words appear in the
	 *         document. Returns an empty list if none of the words appear in
	 *         any line in the document.
	 */
	public List<Integer> someWordsOnLine(String[] words) {

		// Because we don't want to return duplicated lines, the actual line
		// numbers of all word instances will be converted to Strings, and added
		// to a CompressedTrie instance. Later, to return all lines, we just
		// have to get each line number.
		CompressedTrie lines = new CompressedTrie();

		// Not used for method behaviour, only for returning results.
		List<Integer> foundLines = new LinkedList<Integer>();

		// Find every instance of each word within the document
		for (String word : words) {
			// O(log n) / O(n) (see method javadoc)
			trieNode wordNode = findWord(word, 0);
			if (wordNode == null) {
				continue;
			}
			textCoordinates instance = wordNode.locations;
			// Add every instance of word to trie (O(n) where in is the number
			// of instances of the word)
			while (instance != null) {
				lines.add("" + instance.line, 1, 1);
				instance = instance.nextCoordinate;
			}
		}
		// O(n) (see javadoc)
		preOrder(1, null, foundLines, lines.root);
		return foundLines;
	}

	/**
	 * Searches the trie in a depth-first pre-order fashion, adding to a List as
	 * it goes.
	 * 
	 * O(n) where n is the found number of words with the word at 'start' as its
	 * prefix + the number of nodes between them and 'start'
	 * 
	 * n is also linear because maximum number of nodes in a compressed trie
	 * storing S words is 2*S (still linear and therefore proportional to word
	 * count))
	 *
	 * @param command
	 *            Is this for (0) prefixOccurrence or (1) someWordsOnLine?
	 * @param prefixLocations
	 *            The list to be added to for prefixOccurence
	 * @param lines
	 *            The list to be added to for someWordsOnLine
	 * @param start
	 *            The trieNode at which the search should begin
	 */
	private void preOrder(int command,
			List<Pair<Integer, Integer>> prefixLocations, List<Integer> lines,
			trieNode start) {

		trieNode spider = start;
		while (true) {
			// Are there words on the current node?
			if (spider.count > 0) {
				// If we're searching for findPrefix()
				if (command == 0) {
					// Get their locations
					textCoordinates wordInstance = spider.locations;
					do {
						// Add them all to the list (O(n), n = number of word
						// instances)
						prefixLocations.add(new Pair<Integer, Integer>(
								wordInstance.line, wordInstance.column));
						wordInstance = wordInstance.nextCoordinate;
					} while (wordInstance != null);
				} else {
					// When using this for someWordsOnLine, the lines are stored
					// as strings so duplicate lines aren't added to the list
					lines.add(Integer.parseInt(spider.getWord()));
				}
			}
			// Does this node have children?
			if (spider.child != null) {
				// Move down to child
				spider = spider.child;
				continue;
			}

			// No children; are we still at the 'root' node?
			if (spider == start) {
				return;
			}

			// No children, and not at the 'root' node; does this node have a
			// sibling?
			if (spider.nextSibling != null) {
				// Move across to sibling
				spider = spider.nextSibling;
				continue;
			}

			// No children, and not at the 'root' node, and no siblings; Is our
			// parent the 'root' node?
			// (Are we at the top of the last subtree?)
			if (spider.parent == start) {
				return;
			}

			// Not at the top of the last subtree; move back up the tree to
			// another unsearched sibling, or if there aren't any, return the
			// list
			while (spider.parent.nextSibling == null) {
				// No unsearched subtrees found yet...
				spider = spider.parent;
				// Is this node the top of the last subtree?
				if (spider.parent == start) {
					// It is, return list
					return;
				}
			}
			// Unsearched subtree/node found, continue search.
			spider = spider.parent.nextSibling;
		}
	}

	// ##############################################################

	private class trieNode {
		// How many times this word occurs in the document.
		// If trieNode doesn't represent the end of a word count == 0 always
		private int count;
		// The ordered series of characters this node represents
		private String value;
		// The lexicographically next child of this node's parent
		private trieNode nextSibling;
		// The lexicographically previous child of this node's parent
		private trieNode prevSibling;
		// The lexicographically first child of this node
		private trieNode child;
		// This node's parent node
		private trieNode parent;
		// The last identified occurrence of this word within the text
		// (Plural because it's an ordered linked list of all occurrences)
		private textCoordinates locations;

		/**
		 * Creates extraneous (root) node
		 * 
		 * O(1)
		 */
		public trieNode() {
			count = 0;
			value = "";
		}

		//
		/**
		 * Creates node that does not represent the end of a word.
		 * 
		 * O(1)
		 * 
		 * @param value
		 *            The string representing the word section.
		 * @param parent
		 *            The node represeting the preceding word section.
		 */
		public trieNode(String value, trieNode parent) {
			count = 0;
			this.parent = parent;
			this.value = value;
		}

		/**
		 * Creates end of word node.
		 * 
		 * O(1)
		 * 
		 * @param value
		 *            The string representing the word (or end of word).
		 * @param line
		 *            Line number of the word.
		 * @param column
		 *            Column number of the word.
		 */
		public trieNode(String value, int line, int column) {
			++count;
			this.value = value;
			locations = new textCoordinates(this, line, column);
		}

		/**
		 * Adds a new location of the word within the document
		 * 
		 * O(1)
		 *
		 * @param line
		 *            Line number of the word.
		 * @param column
		 *            Column number of the word.
		 */
		public void newLocation(int line, int column) {
			++count;
			textCoordinates newer = new textCoordinates(this, line, column);
			newer.nextCoordinate = this.locations;
			this.locations = newer;
		}

		/**
		 * Get the word represented by this node.
		 * 
		 * O(n) where n is the length of the word or O(log n) where n is the
		 * variety of words in the text
		 * 
		 * @return The word represented by this node.
		 */
		public String getWord() {
			String word = value;
			trieNode spider = this.parent;
			// Spider climbs from this node, to the root node, building the word
			// as
			// it goes.
			while (spider != null) {
				word = spider.value + word;
				spider = spider.parent;
			}
			return word;
		}
	}

	// ################################################################

	private class textCoordinates {
		// Line on which this word may be found
		private int line;
		// Column on which this word begins
		private int column;
		// The next occurrence of the same word within the document
		// Example: The first "the" in "The next occurrence" would have
		// "the" in "the same word" as its nextCoordiante
		private textCoordinates nextCoordinate;
		// The word that immediately succeeds this word within the document
		// Example: "word" in "word within" would have "within" as its
		// nextNeighbourCoordinates
		private textCoordinates nextNeighbourCoordinates;
		// Same as nextNeighbourCoordinates, but with word that *precedes* this
		// word
		private textCoordinates prevNeighbourCoordinates;
		// Link back to the node within the CompressedTrie.
		private trieNode word;

		/**
		 * Creates an instance of a word's location.
		 * 
		 * O(1)
		 * 
		 * @param word
		 *            The node representing the end of the word
		 * @param line
		 *            Line number of the word.
		 * @param column
		 *            Column number of the word.
		 */
		public textCoordinates(trieNode word, int line, int column) {
			this.word = word;
			this.line = line;
			this.column = column;
		}

		/**
		 * Returns the word n words after the current word. Example: "current"
		 * is 3 words after "words".
		 * 
		 * O(n) where n is the desired number of hops
		 * 
		 * @param n
		 *            The desired number of hops
		 * @return textCoordinates of the word located n words after the current
		 *         word.
		 */
		public textCoordinates nthNextNeighbour(int n) {
			textCoordinates spider = this;
			for (int i = 0; i < n; ++i) {
				if (spider.nextNeighbourCoordinates == null)
					return null;
				spider = spider.nextNeighbourCoordinates;
			}
			return spider;
		}

		public String toString() {
			return "(" + line + ", " + column + ")";
		}

	}
}
