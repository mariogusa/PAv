import java.util.*;
        import java.util.concurrent.locks.ReentrantLock;

class Tile {
    private char letter;
    private int points;

    public Tile(char letter, int points) {
        this.letter = letter;
        this.points = points;
    }

    public char getLetter() {
        return letter;
    }

    public int getPoints() {
        return points;
    }

    @Override
    public String toString() {
        return letter + "(" + points + ")";
    }
}

class Bag {
    private List<Tile> tiles;
    private ReentrantLock lock = new ReentrantLock();

    // Each letter's point is randomly determined.
    public Bag() {
        tiles = new ArrayList<>();
        Random rand = new Random();
        // Map to hold point value for each letter.
        Map<Character, Integer> letterPoints = new HashMap<>();
        for (char letter = 'A'; letter <= 'Z'; letter++) {
            int points = rand.nextInt(10) + 1;
            letterPoints.put(letter, points);
            for (int i = 0; i < 10; i++) {
                tiles.add(new Tile(letter, points));
            }
        }
        // Shuffle the tiles.
        Collections.shuffle(tiles);
    }

    // Extraction method.
    public List<Tile> extractTiles(int count) {
        lock.lock();
        try {
            List<Tile> extracted = new ArrayList<>();
            int n = Math.min(count, tiles.size());
            for (int i = 0; i < n; i++) {
                extracted.add(tiles.remove(0));
            }
            return extracted;
        } finally {
            lock.unlock();
        }
    }

    public boolean isEmpty() {
        lock.lock();
        try {
            return tiles.isEmpty();
        } finally {
            lock.unlock();
        }
    }

    public int remainingTiles() {
        lock.lock();
        try {
            return tiles.size();
        } finally {
            lock.unlock();
        }
    }
}

class Dictionary {
    private Set<String> words;

    public Dictionary() {
        words = new HashSet<>();
        // Sample dictionary.
        words.add("CAT");
        words.add("DOG");
        words.add("BIRD");
        words.add("FISH");
        words.add("TREE");
        words.add("HOME");
        words.add("JAVA");
        words.add("CODE");
        words.add("GAME");
        words.add("PLAY");
        words.add("TILE");
        words.add("BOARD");
    }

    public boolean isValidWord(String word) {
        return words.contains(word.toUpperCase());
    }

    public Set<String> getWords() {
        return words;
    }
}

class Board {
    // Maintain the scores.
    private Map<String, Integer> scores = new HashMap<>();
    private ReentrantLock lock = new ReentrantLock();

    public void submitWord(String playerName, String word, int points) {
        lock.lock();
        try {
            System.out.println("Player " + playerName + " submits the word: " + word + " for " + points + " points.");
            scores.put(playerName, scores.getOrDefault(playerName, 0) + points);
        } finally {
            lock.unlock();
        }
    }

    public Map<String, Integer> getScores() {
        return scores;
    }
}

class Player extends Thread {
    private String name;
    private Bag bag;
    private Board board;
    private Dictionary dictionary;
    private List<Tile> hand;
    private int score = 0;
    private Random rand = new Random();

    private List<String> formedWords = new ArrayList<>();

    public Player(String name, Bag bag, Board board, Dictionary dictionary) {
        this.name = name;
        this.bag = bag;
        this.board = board;
        this.dictionary = dictionary;
        hand = new ArrayList<>();
    }

    // Check whether the given word can be formed.
    private boolean canFormWord(String word, List<Tile> currentHand) {
        Map<Character, Integer> letterCount = new HashMap<>();
        for (Tile t : currentHand) {
            char letter = t.getLetter();
            letterCount.put(letter, letterCount.getOrDefault(letter, 0) + 1);
        }
        for (char c : word.toCharArray()) {
            if (letterCount.getOrDefault(c, 0) <= 0) {
                return false;
            } else {
                letterCount.put(c, letterCount.get(c) - 1);
            }
        }
        return true;
    }

    // Calculate total points.
    private int calculatePoints(String word, List<Tile> currentHand) {
        int points = 0;
        List<Tile> copy = new ArrayList<>(currentHand);
        for (char c : word.toCharArray()) {
            for (Iterator<Tile> it = copy.iterator(); it.hasNext(); ) {
                Tile tile = it.next();
                if (tile.getLetter() == c) {
                    points += tile.getPoints();
                    it.remove();
                    break;
                }
            }
        }
        return points;
    }

    // Remove the tiles used to form the word.
    private void removeTilesUsed(String word) {
        for (char c : word.toCharArray()) {
            for (Iterator<Tile> it = hand.iterator(); it.hasNext(); ) {
                Tile tile = it.next();
                if (tile.getLetter() == c) {
                    it.remove();
                    break;
                }
            }
        }
    }

    // Try to form any valid word from the dictionary.
    private String formWord() {
        for (String word : dictionary.getWords()) {
            if (canFormWord(word, hand)) {
                return word;
            }
        }
        return null; // No valid word can be formed.
    }

    @Override
    public void run() {
        // Start by drawing 7 tiles.
        hand.addAll(bag.extractTiles(7));

        while (!bag.isEmpty() || !hand.isEmpty()) {
            System.out.println("Player " + name + " hand: " + hand);
            String word = formWord();
            if (word != null) {
                // Record the word formed.
                formedWords.add(word);

                // Word found – calculate points, remove used tiles, and refill hand.
                int points = calculatePoints(word, hand);
                removeTilesUsed(word);
                board.submitWord(name, word, points);
                score += points;

                // Draw new tiles equal to the length of the word, if available.
                int needed = word.length();
                List<Tile> newTiles = bag.extractTiles(needed);
                if (!newTiles.isEmpty()) {
                    hand.addAll(newTiles);
                }
            } else {
                // No valid word can be formed.
                System.out.println("Player " + name + " cannot form a word. Discarding hand.");
                hand.clear();
                // Draw 7 new tiles.
                hand.addAll(bag.extractTiles(7));
            }
            // Pause for a moment to simulate time taken by the player.
            try {
                Thread.sleep(rand.nextInt(300) + 200);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            // Terminate if both bag and hand are empty.
            if (bag.isEmpty() && hand.isEmpty()) {
                break;
            }
        }
        System.out.println("Player " + name + " finished with score: " + score);
    }

    public int getScore() {
        return score;
    }

    public String getPlayerName() {
        return name;
    }

    public List<String> getFormedWords() {
        return formedWords;
    }
}

public class WordGame {
    public static void main(String[] args) {
        // Initialize.
        Bag bag = new Bag();
        Board board = new Board();
        Dictionary dictionary = new Dictionary();

        // 3 player game.
        int numPlayers = 3;
        List<Player> players = new ArrayList<>();
        for (int i = 1; i <= numPlayers; i++) {
            players.add(new Player("Player" + i, bag, board, dictionary));
        }

        // Start.
        for (Player p : players) {
            p.start();
        }

        // Wait for all player threads to complete.
        for (Player p : players) {
            try {
                p.join();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        // Display scores.
        System.out.println("\n--- Game Over ---");
        Map<String, Integer> scores = board.getScores();
        for (Map.Entry<String, Integer> entry : scores.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue() + " points");
        }

        // Determine the winner.
        String winner = "";
        int maxScore = -1;
        Player winningPlayer = null;
        for (Player p : players) {
            if (p.getScore() > maxScore) {
                maxScore = p.getScore();
                winner = p.getPlayerName();
                winningPlayer = p;
            }
        }
        System.out.println("Winner: " + winner + " with " + maxScore + " points!");

        // Print the words and corresponding letters formed by the winner.
        if (winningPlayer != null) {
            System.out.println("\nWords formed by " + winningPlayer.getPlayerName() + ":");
            for (String word : winningPlayer.getFormedWords()) {
                StringBuilder letters = new StringBuilder();
                for (char letter : word.toCharArray()) {
                    letters.append(letter).append(" ");
                }
                System.out.println("Word: " + word + " | Letters: " + letters.toString().trim());
            }
        }
    }
}