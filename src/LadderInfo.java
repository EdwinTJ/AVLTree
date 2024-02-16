import java.util.ArrayList;

/**
 * This class creates WordInfo objects which allows the program
 * to keep track of words, their ladder, and number of moves previous to the word.
 */
public class LadderInfo implements Comparable<LadderInfo> {
    public int priority;
    public String lastWord;   // last word of ladder
    public int moves;         // number of moves in ladder
    public String ladder;     // series of words in current word ladder

    public LadderInfo(String word, int moves, String ladder,int priority){
        this.lastWord = word;
        this.moves = moves;
        this.ladder = ladder;
        this.priority = priority;

    }
    public LadderInfo(String word, int moves, String ladder) {
        this.lastWord = word;
        this.moves = moves;
        this.ladder = ladder;
        this.priority = priority;
    }
    // Copy constructor for copying the move sequence
    public LadderInfo(LadderInfo other) {
        this.lastWord = other.lastWord;
        this.moves = other.moves;
        this.ladder = other.ladder;
    }




    public int compareTo(LadderInfo other) {
        // Compare based on the number of moves
        return Integer.compare(this.moves, other.moves);
    }
    public String toString2(){
       return "Word " + lastWord    + " Moves " +moves  + " Ladder ["+ ladder +"]";
    }
    public String toString(){
        return "  ["+ ladder +"]";
    }
}

