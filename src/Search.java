import java.io.File;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

public class Search {
    // Searches for a phrase in the files using InvertedIndex created by PreSearch.java.
    public static void searchPhrase(String keyword, int numberOfResults) {
        PreSearch ps = new PreSearch();
        String phrase = keyword;
        ArrayList<String> as = ps.find(phrase);
        phrase = phrase.toLowerCase();
        if (as == null) {
            SearchSimilarWords.searchSimilar(phrase, numberOfResults, ps);
        } else {
            Map<String, Integer> sortedMap = SortResultsByRank.sortByRank(as, phrase);
            printResult(sortedMap, numberOfResults);
        }
    }

    // Prints given number of results from the provided Map
    public static void printResult(Map<String, Integer> result, int numberOfResults) {
        Iterator<Entry<String, Integer>> iterator = result.entrySet().iterator();
        int i = 0;
        while (iterator.hasNext() && numberOfResults > i++) {
            Map.Entry<String, Integer> me = iterator.next();
            String fileName = me.getKey();
            File f = new File("WebPages/" + fileName);
            In in = new In(f);
            String url = in.readLine();
            System.out.println("-----------------------------------------");
            System.out.println(fileName.substring(0, fileName.length() - 4) + "\t\tOccurrences : " + me.getValue());
            System.out.println(url);
        }
    }

    public static void main(String[] args) {
        Search.searchPhrase("voice", 5);
    }
}
