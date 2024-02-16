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
                    wordLists[word.length()].add(word);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        solutionQueue = new MyLinkedList<>();
        priorityQueue = new AVLTree<>();

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
        if (a.length() != b.length() || !wordLists[a.length()].contains(a) || !wordLists[b.length()].contains(b)) {
            System.out.println("No solution: Given words are not the same length");
            System.out.println();
            return;
        }
//        findLadder(a, b);
        System.out.println("Brute Force:");
        findLadder(a, b);
        System.out.println("A* Search:");
        findLadderAStar(a, b);

    }

    private boolean isOneCharApart(String word1, String word2) {
        // Check if the lengths of the words are the same
        if (word1.length() != word2.length()) {
            return false;
        }
        // Logic to check if two words differ by one character
        int differences = 0;
        for (int i = 0; i < word1.length(); i++) {
            if (word1.charAt(i) != word2.charAt(i)) {
                differences++;
            }
        }
        return differences == 1;
    }


    /**
     * Display the word ladder from a to b
     * Copy the wordList of the appropriate length so that you can delete a word when its
     * used.
     * @param a starting word
     * @param b ending word
     */
    public void findLadder(String a, String b) {
        ArrayList<String> list = wordLists[a.length()];
        ArrayList<String> cloneList = (ArrayList<String>) list.clone();

        System.out.println("Looking for a solution from " + a + " -> " + b + " Size of List " + cloneList.size());

        // Verify that provided words are valid
        if (a.length() >= MaxWordSize || b.length() >= MaxWordSize) {
            System.out.println("No solution: Words exceed maximum length");
            return;
        }

        if (a.length() != b.length() || !wordLists[a.length()].contains(a) || !wordLists[b.length()].contains(b)) {
            System.out.println("No solution: Invalid input words");
            return;
        }

        int count = 0;
        cloneList.remove(a);
        // Initialize the queue with the initial ladder
        solutionQueue.addAtEnd(new LadderInfo(a, 0,  a ));
        while (!solutionQueue.isEmpty() && !done) {
            LadderInfo currLadder = solutionQueue.removeFromFront();
            String lastWord = currLadder.lastWord;
//            System.out.println("Remove current Ladder" + currLadder.toString());
            ArrayList<String> removeList = new ArrayList<>();

            for (String newWord : cloneList) {

                if (isOneCharApart(lastWord, newWord)){
                    int moves = currLadder.moves + 1;

                    // Extend the current ladder
                    LadderInfo newLadder = new LadderInfo(newWord, moves, currLadder.ladder + " " + newWord);
                    if (newWord.equals(b)) {
                        done = true;
                        System.out.println("Shortest ladder found: " + newLadder.ladder +
                                " " + moves + " total enqueues " + count);
                    }
                    // Append this ladder to the end of the queue
                    removeList.add(newWord);
                    solutionQueue.addAtEnd(newLadder);
//                    System.out.println("Adding to the queue" + newLadder.toString());
                    // Count here
                    count++;
                }
            }
            for(String useWord : removeList){
                cloneList.remove(useWord);
            }
        }
        if (!done) {
            System.out.println("No ladder found from " + a + " to " + b + ": " +
                    (wordLists[a.length()].contains(a) ? "" : "Original words not found in dictionary") +
                    (a.length() == b.length() ? "" : " Words not the same length"));
        }
    }

    public void findLadderAStar(String a, String b) {
        ArrayList<String> listAVL = wordLists[a.length()];

        System.out.println("Seeking an A* solution from " + a + " to " + b);

        // Verify that provided words are valid
        if (a.length() >= MaxWordSize || b.length() >= MaxWordSize) {
            System.out.println("No solution: Words exceed maximum length");
            return;
        }

        if (a.length() != b.length() || !wordLists[a.length()].contains(a) || !wordLists[b.length()].contains(b)) {
            System.out.println("No solution: Invalid input words");
            return;
        }

        done = false;
        int count = 0;

        // Initialize the priority queue with the initial ladder
        priorityQueue.insert(new LadderInfo(a, 0, a, heuristicCost(a, b)));

        while (!priorityQueue.isEmpty()) {
            LadderInfo currLadder = priorityQueue.findMin();
            String lastWord = currLadder.lastWord;
            ArrayList<String> removeList = new ArrayList<>();

            for (String newWord : listAVL) {
                if (isOneCharApart(lastWord, newWord)) {
                    int moves = currLadder.moves + 1;

                    // Extend the current ladder
                    LadderInfo newLadder = new LadderInfo(newWord, moves, currLadder.ladder + " " + newWord, totalCost(newWord, b, moves));

                    if (newWord.equals(b)) {
                        done = true;
                        System.out.println("[ " + newLadder.ladder + "] total enqueues " + count);
                        return;
                    }

                    // Append this ladder to the priority queue
                    removeList.add(newWord);
                    priorityQueue.insert(newLadder);
                    count++;
                }
            }
            // Remove processed words from the clone list
            listAVL.removeAll(removeList);
            priorityQueue.deleteMin();

        }

        if (!done) {
            System.out.println("No ladder found from " + a + " to " + b);
        }
    }

    /**
     * Heuristic function for A* search.
     * @param a current word
     * @param b target word
     * @return heuristic cost
     */
    private int heuristicCost(String a, String b) {
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
        return heuristicCost(word, target) + moves;
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
        //        for (int i = 0; i < source.length; i++) {
//            g.play(source[i], dest[i]);
//        }

//        int RANDOMCT = 8;
//        for (int i = 4; i <= RANDOMCT; i++)
//            g.play(i);

    }
}
