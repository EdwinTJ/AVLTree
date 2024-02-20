import java.util.Scanner;
import java.util.Random;
import java.io.File;
import java.util.ArrayList;


/**
 * This class holds all of the functions needed for the ladder game to run successfully.
 */
public class LadderGameSolution {
    static int MaxWordSize = 15;
    ArrayList<String>[] wordLists;  // Array of ArrayLists of words of each length.
    Random random;
    private boolean done = false;
    MyLinkedList<LadderInfo> solutionQueue;

    AVLTree<LadderInfo> priorityQueue;

    /**
     * This function reads the dictionary and makes an array list of arraylists for words of each length.
     */

    /**
     * Divide the dictionary into wordLists of different length words
     * @param filename Name of file containing dictionary of legal words
     */
    public LadderGameSolution(String filename) {
        random = new Random();
        populateWordList(filename);
        solutionQueue = new MyLinkedList<>();
        priorityQueue = new AVLTree<>();

    }

    private void populateWordList(String filename) {
        wordLists = new ArrayList[MaxWordSize +1];
        for (int i = 0; i < MaxWordSize; i++) {
            wordLists[i] = new ArrayList<>();
        }
        File file = new File(filename);
        try {
            Scanner reader = new Scanner(file);
            while (reader.hasNext()) {
                String word = reader.next();
                if (word.length() < MaxWordSize) {
                    wordLists[word.length()-1].add(word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * make sure a and b are legal words before calling FindLadder
     * @param a starting word of word ladder
     * @param b endsing word of word ladder
     */
    public void play(String a, String b) {
        // Reset the done
        done = false;
        if (a.length() >= MaxWordSize) {
            return;
        }

        // Verify that provided words are valid
        if (a.length() != b.length() || !wordLists[a.length()-1].contains(a) || !wordLists[b.length()-1].contains(b)) {
            System.out.println("No solution: Given words are not the same length");
            System.out.println();
            return;
        }
//        findLadder(a, b);
        System.out.println("Brute Force:");
        findLadder(a, b, new MyLinkedList<>(), false);
        System.out.println("A* Search:");
        findLadder(a,b, new AVLTree<>(), true);

    }

    public void findLadder(String a, String b, Queue<LadderInfo> queue, boolean isAStar) {
        ArrayList<String> listAVL = wordLists[a.length()-1];
        ArrayList<String> cloneList = (ArrayList<String>) listAVL.clone();

        System.out.println("Seeking an A* solution from " + a + " to " + b);

        // Verify that provided words are valid
        if (a.length() >= MaxWordSize || b.length() >= MaxWordSize) {
            System.out.println("No solution: Words exceed maximum length");
            return;
        }

        if (a.length() != b.length() || !wordLists[a.length()-1].contains(a) || !wordLists[b.length()-1].contains(b)) {
            System.out.println("No solution: Invalid input words");
            return;
        }

        done = false;
        int count = 0;

        // Initialize the priority queue with the initial ladder
        queue.add(new LadderInfo(a, 0, a, getCost(a, b)));
        cloneList.remove(a);
        while (!queue.isEmpty()) {
            LadderInfo currLadder = queue.remove();
            // Remove processed words from the clone list
            String lastWord = currLadder.lastWord;

            ArrayList<String> removeList = new ArrayList<>();
            for (String newWord : cloneList) {
                if (getCost(lastWord, newWord) == 1) {
                    int moves = currLadder.moves + 1;

                    // Extend the current ladder
                    LadderInfo newLadder;
                    if (isAStar) {
                        newLadder = new LadderInfo(newWord, moves, currLadder.ladder + " " + newWord, totalCost(newWord, b, moves));
                    } else {
                        newLadder = new LadderInfo(newWord, moves, currLadder.ladder + " " + newWord);
                    }
                    if (newWord.equals(b)) {
                        done = true;
                        System.out.println("[ " + newLadder.ladder + "] total enqueues " + count);
                        return;
                    }

                    // Append this ladder to the priority queue
                    removeList.add(newWord);
                    queue.add(newLadder);
                    count++;
                }
            }
            cloneList.removeAll(removeList);
        }

        if (!done) {
            System.out.println("No ladder found from " + a + " to " + b);
        }
    }

    /**
     * Calculate num of different Char between 2 words.
     * @param a current word
     * @param b target word
     * @return heuristic cost
     */
    private int getCost(String a, String b) {
        if (a.length() != b.length()){
            return -1;
        }
        int cost = 0;
        for (int i = 0; i < a.length(); i++) {
            if (a.charAt(i) != b.charAt(i)) {
                cost++;
            }
        }
        return cost;
    }
    private int totalCost(String word, String target, int moves) {
        // Calculate the total cost (f(n)) for A* search
        return getCost(word, target) + moves;
    }
    /**
     * Generate two random words of length len for a word ladder problem.
     * @param len Length of the source and target words
     */

    public void play(int len) {
        done = false;
        if (len >= MaxWordSize || wordLists[len].isEmpty()) {
            return;
        }
        ArrayList<String> list = wordLists[len];
        String a = list.get(random.nextInt(list.size()));
        String b = list.get(random.nextInt(list.size()));
        play(a, b);
    }

    /**
     *
     * Notice that the main program just sets up the problem.  All the code to solve the problem is outside of main.
     */
    public static void main(String[] args) {
        String[] source = {"irk", "hit", "toes", "oops", "toes",  "ride", "happily", "slow", "stone", "biff", "unabated", "basket"};
        String[] dest = {"yuk", "hog", "tied", "tots", "tied", "ands", "angrily", "fast", "money", "axal", "notified", "doughy"};

        // This organization allows the dictionary to be read in only once.
        LadderGameSolution g = new LadderGameSolution("src/dictionary.txt");
        g.play("kiss", "woof");
        g.play("cock", "numb");
        g.play("jura", "such");
        g.play("stet", "whey");
        g.play("rums", "numb");
        g.play("irk", "yuk");

    }
}
