import java.io.File;
import java.util.*;


public class SortResultsByRank {
    // Sorts values of given HashMap in descending order
    private static HashMap<String, Integer> sortByValue(HashMap<String, Integer> map, Comparator<Map.Entry<String, Integer>> comparator) {
        List<Map.Entry<String, Integer>> list = new LinkedList<>(map.entrySet());
        list.sort(comparator);

        // copying the sorted list into a new HashMap to preserve the iteration order
        HashMap<String, Integer> sortedHashMap = new LinkedHashMap<>();
        for (Map.Entry<String, Integer> entry : list) {
            sortedHashMap.put(entry.getKey(), entry.getValue());
        }
        return sortedHashMap;
    }

    public static HashMap<String, Integer> sortValues(HashMap<String, Integer> map) {
        Comparator<Map.Entry<String, Integer>> ascendingComparator = Map.Entry.comparingByValue();
        return sortByValue(map, ascendingComparator);
    }

    public static HashMap<String, Integer> sortValuesInverse(HashMap<String, Integer> map) {
        Comparator<Map.Entry<String, Integer>> descendingComparator = (o1, o2) -> o2.getValue().compareTo(o1.getValue());
        return sortByValue(map, descendingComparator);
    }


    // Sorts search output in Descending Rank. ArrayList contains a list of files that have the given phrase.
    public static Map<String, Integer> sortByRank(ArrayList<String> as, String phrase) {
        HashMap<String, Integer> wordCount = new HashMap<>();

        for (String fileName : as) {
            String[] words = PreSearch.getWordsFromFile(new File("WebPages/" + fileName));
            for (String word : words) {
                if (word.toLowerCase().equals(phrase.split("\\W+")[0])) {
                    if (wordCount.containsKey(fileName)) {
                        wordCount.put(fileName, wordCount.get(fileName) + 1);
                    } else {
                        wordCount.put(fileName, 1);
                    }
                }
            }
        }
        return sortValuesInverse(wordCount);
    }
}
