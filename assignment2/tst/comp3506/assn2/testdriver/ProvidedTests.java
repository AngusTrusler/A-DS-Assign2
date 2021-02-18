package comp3506.assn2.testdriver;


import static org.hamcrest.collection.IsIterableContainingInAnyOrder.containsInAnyOrder;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.hasItem;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import org.junit.BeforeClass;
import org.junit.Test;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import comp3506.assn2.application.AutoTester;
import comp3506.assn2.application.Search;
import comp3506.assn2.utils.TestingTriple;
import comp3506.assn2.utils.TestingPair;
import comp3506.assn2.utils.Triple;
import comp3506.assn2.utils.Pair;


/**
 * Sample tests for each method in comp3506.assn2.application.Search
 * 
 * @author Richard Thomas <richard.thomas@uq.edu.au>
 *
 */
public class ProvidedTests {

	private static Search searchApplication;
	
	// All occurrences of the word "obscure" in shakespeare.txt.
	private final static List<TestingPair<Integer,Integer>> obscureOccurrences = 
			Arrays.asList(new TestingPair<>(27960,25), new TestingPair<>(44217,19), new TestingPair<>(46802,12), 
					      new TestingPair<>(69473,10), new TestingPair<>(69674,29), new TestingPair<>(72415,41),
                          new TestingPair<>(78318,33), new TestingPair<>(92223,45), new TestingPair<>(100957,31), 
                          new TestingPair<>(122883,31), new TestingPair<>(131918,38), new TestingPair<>(148012,31));

	
	@BeforeClass
	public static void openFiles() {
		try {
			searchApplication = new AutoTester("files\\shakespeare.txt", "files\\shakespeare-index.txt", "files\\stop-words.txt");
		} catch (FileNotFoundException | IllegalArgumentException e) {
			System.out.println("Opening files failed!");
			e.printStackTrace();
		}
	}

	@Test(timeout=500)
	public void testWordCount() {
		assertThat("Word count of 'obscure' should have been 12.", searchApplication.wordCount("obscure"), is(equalTo(12)));
	}

	@Test(timeout=500)
	public void testPhraseOccurrence_FirstOccurrence() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("obscure"));
		assertThat("First occurrence of 'obscure' was not found.", searchResult, hasItem(obscureOccurrences.get(0)));
	}

	@Test(timeout=500)
	public void testPhraseOccurrence_LastOccurrence() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("obscure"));
		assertThat("Last occurrence of 'obscure' was not found.", searchResult, hasItem(obscureOccurrences.get(obscureOccurrences.size()-1)));
	}

	@Test(timeout=500)
	public void testPhraseOccurrence_MiddleOccurrence() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("obscure"));
		assertThat("A middle occurrence of 'obscure' was not found.", searchResult, hasItem(obscureOccurrences.get(obscureOccurrences.size()/2)));
	}

	@Test(timeout=500)
	public void testPhraseOccurrence_AllOccurrences() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("obscure"));
		assertThat("Locations of 'obscure' were not expected.", searchResult, containsInAnyOrder(obscureOccurrences.toArray()));
		assertThat("Search for 'obscure' returned wrong number of results.", searchResult, hasSize(obscureOccurrences.size()));
	}

	@Test(timeout=500)
	public void testPhraseOccurrence_MultipleWords() {
		List<TestingPair<Integer, Integer>> searchResult = makeTestingPair(searchApplication.phraseOccurrence("that is the question"));
		assertThat("Phrase 'that is the question' was not found where expected.", searchResult, hasItem(new TestingPair<>(25779, 22)));
		assertThat("Search for 'that is the question' returned wrong number of results.", searchResult, hasSize(1));
	}

	@Test(timeout=500)
	public void testPrefixOccurrence() {
		List<TestingPair<Integer,Integer>> expected = new ArrayList<>(obscureOccurrences);                                    // obscure
		expected.addAll(Arrays.asList(new TestingPair<>(12574,5), new TestingPair<>(12870,12), new TestingPair<>(64754, 7),   // obscured 
					                  new TestingPair<>(78736,5), new TestingPair<>(148585,19),                               // obscures
					                  new TestingPair<>(58643,38), new TestingPair<>(146994,25)));                            // obscurely
		List<TestingPair<Integer,Integer>> searchResult = makeTestingPair(searchApplication.prefixOccurrence("obscure"));
		assertThat("Locations of 'obscure' prefix were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=500)
	public void testWordsOnLine() {
		String [] searchTerm = {"riper", "decease"};
		List<Integer> expected = Arrays.asList(13);
		assertThat("Location of 'riper' && 'decease' were not expected.", searchApplication.wordsOnLine(searchTerm), is(expected));
	}

	@Test(timeout=500)
	public void testSomeWordsOnLine() {
		String [] searchTerm = {"boggler", "carlot"};
		List<Integer> expected = Arrays.asList(8416, 11839);
		List<Integer> searchResult = searchApplication.someWordsOnLine(searchTerm);
		assertThat("Locations of 'boggler' || 'carlot' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'boggler' || 'carlot' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=500)
	public void testWordsNotOnLine() {
		String [] requiredWords = {"riper"};
		String [] excludedWords = {"decease"};
		List<Integer> expected = Arrays.asList(1837, 11852);
		List<Integer> searchResult = searchApplication.wordsNotOnLine(requiredWords, excludedWords);
		assertThat("Locations of 'riper' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'riper' && !'decease' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=500)
	public void testSimpleAndSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
				            "THE SECOND PART OF KING HENRY THE SIXTH", "KING RICHARD THE SECOND", "VENUS AND ADONIS"};
		String [] requiredWords = {"obscure", "rusty"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(25300,28,"rusty"),     // Hamlet
				                                                             new TestingTriple<>(27960,25,"obscure"),
				                                                             new TestingTriple<>(100683,31,"rusty"),    // King Richard the Second
				                                                             new TestingTriple<>(100957,31,"obscure"));
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleAndSearch(titles, requiredWords));
		assertThat("Locations of 'obscure' && 'rusty' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' && 'rusty' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=500)
	public void testSimpleOrSearch() {
		String [] titles = {"AS YOU LIKE IT", "CYMBELINE", "THE TRAGEDY OF HAMLET", 
				            "PERICLES PRINCE OF TYRE", "THE PASSIONATE PILGRIM"};
		String [] words = {"riper", "rusty"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(11852,14,"riper"), 
				                                                             new TestingTriple<>(25300,28,"rusty"),
				                                                             new TestingTriple<>(95975,28,"rusty"),
				                                                             new TestingTriple<>(96140,40,"rusty"),
				                                                             new TestingTriple<>(96160,12,"rusty"),
				                                                             new TestingTriple<>(145236,36,"rusty"));
		List<TestingTriple<Integer,Integer,String>> searchResult = makeTestingTriple(searchApplication.simpleOrSearch(titles, words));
		assertThat("Locations of 'riper' || 'rusty' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'riper' || 'rusty' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=500)
	public void testSimpleNotSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE FIRST PART OF KING HENRY THE FOURTH", 
	                        "THE SECOND PART OF KING HENRY THE SIXTH", "KING RICHARD THE SECOND", 
	                        "THE TRAGEDY OF ROMEO AND JULIET"};
		String [] requiredWords = {"obscure", "rusty"};
		String [] excludedWords = {"beaver"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(100683,31,"rusty"),    // King Richard the Second
                                                                             new TestingTriple<>(100957,31,"obscure"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.simpleNotSearch(titles, requiredWords, excludedWords));
		assertThat("Locations of 'obscure' && 'rusty' were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' && 'rusty' && !'beaver' returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	@Test(timeout=500)
	public void testCompoundAndOrSearch() {
		String [] titles = {"CYMBELINE", "THE TRAGEDY OF HAMLET", "THE LIFE OF KING HENRY THE FIFTH", 
				            "THE FIRST PART OF HENRY THE SIXTH", "THE SECOND PART OF KING HENRY THE SIXTH", 
				            "KING RICHARD THE SECOND", "VENUS AND ADONIS"};
		String [] requiredWords = {"obscure"};
		String [] orWords = {"beaver", "hoof"};
		List<TestingTriple<Integer,Integer,String>> expected = Arrays.asList(new TestingTriple<>(23709,29,"beaver"),    // Hamlet
                                                                             new TestingTriple<>(27960,25,"obscure"),
                                                                             new TestingTriple<>(148012,31,"obscure"),  // Venus and Adonis
                                                                             new TestingTriple<>(148047,33,"hoof"));
		List<TestingTriple<Integer,Integer,String>> searchResult = 
				makeTestingTriple(searchApplication.compoundAndOrSearch(titles, requiredWords, orWords));
		assertThat("Locations of 'obscure' && ('beaver' || 'hoof') were not expected.", searchResult, containsInAnyOrder(expected.toArray()));
		assertThat("Search for 'obscure' && ('beaver' || 'hoof') returned wrong number of results.", searchResult, hasSize(expected.size()));
	}

	
	/**
	 * @param data The list of Pairs to be converted to a list of TestingPairs.
	 */
	private List<TestingPair<Integer, Integer>> makeTestingPair(List<Pair<Integer, Integer>> data) {
		List<TestingPair<Integer, Integer>> result = new ArrayList<>(); 
		for (Pair<Integer, Integer> pair: data) {
			result.add(new TestingPair<Integer, Integer>(pair));
		}
		return result;
	}

	/**
	 * @param data The list of Triples to be converted to a list of TestingTriples.
	 */
	private List<TestingTriple<Integer, Integer, String>> makeTestingTriple(List<Triple<Integer, Integer, String>> data) {
		List<TestingTriple<Integer, Integer, String>> result = new ArrayList<>(); 
		for (Triple<Integer, Integer, String> triple: data) {
			result.add(new TestingTriple<Integer, Integer, String>(triple));
		}
		return result;
	}

}
