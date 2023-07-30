import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Map.Entry;


public class SearchSimilarWords {
    // Creates the HashMap for distance of given word in the PreSearch database.
    private static HashMap<String, Integer> sortByEditDistance(String keyword, PreSearch ps) {
        HashMap<String, Integer> indexDistance = new HashMap<>();
        for (Entry<String, HashSet<Integer>> me : ps.index.entrySet()) {
            String word = me.getKey();
            int distance = Sequences.editDistance(word, keyword);
            if (distance < 3 && distance != 0) {
                indexDistance.put(word, distance);
            }
        }

        return SortResultsByRank.sortValues(indexDistance);
    }

    // Searches for similar words in the database.
    public static void searchSimilar(String keyword, int numberOfResults, PreSearch ps) {
        HashMap<String, Integer> sortedDistance = sortByEditDistance(keyword, ps);
        for (Entry<String, Integer> me : sortedDistance.entrySet()) {
            String word = me.getKey();
            System.out.println("Instead showing results for : " + word);

            // Searching for the word with the lowest distance.
            Map<String, Integer> sortedMap;
            ArrayList<String> as = ps.find(word);
            String phrase = word.toLowerCase();

            sortedMap = SortResultsByRank.sortByRank(as, phrase);
            Search.printResult(sortedMap, numberOfResults);
            break;
        }

    }
}
