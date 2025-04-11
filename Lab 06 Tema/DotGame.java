import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.*;
import javax.imageio.ImageIO;

public class DotGame extends JFrame implements Serializable {

    private transient ConfigPanel configPanel;
    private transient GameCanvas gameCanvas;
    private transient ControlPanel controlPanel;

    public DotGame() {
        setTitle("Dot Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Top: Configuration Panel & New Game
        configPanel = new ConfigPanel();
        add(configPanel, BorderLayout.NORTH);

        // Center
        gameCanvas = new GameCanvas();
        add(gameCanvas, BorderLayout.CENTER);

        // Bottom
        controlPanel = new ControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    // Configuration Panel
    class ConfigPanel extends JPanel {
        private JTextField dotCountField;
        private JButton newGameButton;

        public ConfigPanel() {
            setLayout(new FlowLayout(FlowLayout.LEFT));
            add(new JLabel("Number of Dots:"));
            dotCountField = new JTextField("10", 5);
            add(dotCountField);

            newGameButton = new JButton("New Game");
            add(newGameButton);

            newGameButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    int numDots;
                    try {
                        numDots = Integer.parseInt(dotCountField.getText());
                    } catch (NumberFormatException ex) {
                        numDots = 10;
                    }
                    gameCanvas.initializeDots(numDots);
                }
            });
        }
    }
    // GameCanvas: The board where dots and lines are drawn.
    // Implements a retained mode using BufferedImage.
    // Includes mouse listeners for both click-selection and dragging.
    class GameCanvas extends JPanel implements Serializable {
        private ArrayList<Point> dots;
        private ArrayList<Line> lines;
        // Transient fields for drawing and mouse actions:
        private transient BufferedImage offscreen;
        private transient Point dragStart;
        private transient Point dragCurrent;

        public GameCanvas() {
            dots = new ArrayList<>();
            lines = new ArrayList<>();
            setBackground(Color.WHITE);

            MouseAdapter ma = new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                    // Check if a dot was pressed.
                    for (Point p : dots) {
                        if (p.distance(e.getPoint()) < 10) {
                            dragStart = p;
                            break;
                        }
                    }
                }

                @Override
                public void mouseDragged(MouseEvent e) {
                    if (dragStart != null) {
                        dragCurrent = e.getPoint();
                        repaint();
                    }
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    if (dragStart != null) {
                        // Look for a second dot near the release point
                        for (Point p : dots) {
                            if (p.distance(e.getPoint()) < 10 && !p.equals(dragStart)) {
                                Color lineColor = (lines.size() % 2 == 0) ? Color.BLUE : Color.RED;
                                lines.add(new Line(dragStart, p, lineColor));
                                break;
                            }
                        }
                        // Clear temporary drag information
                        dragStart = null;
                        dragCurrent = null;
                        // Recreate the offscreen image since state changed.
                        offscreen = null;
                        repaint();
                    }
                }

                @Override
                public void mouseClicked(MouseEvent e) {
                    // For quick click-selection (if not dragging)
                    if (dragStart == null) {
                        // If a dot is clicked, store it as a candidate selection.
                        // We also check if a previous selection exists.
                    }
                }
            };
            addMouseListener(ma);
            addMouseMotionListener(ma);
        }

        // Create new game state with the specified number of dots.
        public void initializeDots(int numDots) {
            dots.clear();
            lines.clear();
            offscreen = null;  // forces re-creation of offscreen buffer
            Random rand = new Random();
            int margin = 20;
            // Ensure the panel has proper size before generating dots.
            int w = Math.max(getWidth(), 100);
            int h = Math.max(getHeight(), 100);
            for (int i = 0; i < numDots; i++) {
                int x = margin + rand.nextInt(w - 2 * margin);
                int y = margin + rand.nextInt(h - 2 * margin);
                dots.add(new Point(x, y));
            }
            repaint();
        }

        // Checks if all dots are connected using union-find (for game completion).
        public boolean isConnected() {
            if (dots.isEmpty()) return true;
            int n = dots.size();
            int[] parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;
            // Union-find:
            class UF {
                int find(int i) {
                    if (parent[i] != i) parent[i] = find(parent[i]);
                    return parent[i];
                }
                void union(int i, int j) {
                    int ri = find(i);
                    int rj = find(j);
                    parent[ri] = rj;
                }
            }
            UF uf = new UF();
            // For every line, union the two connected dots.
            for (Line line : lines) {
                int i = dots.indexOf(line.start);
                int j = dots.indexOf(line.end);
                if (i != -1 && j != -1) {
                    uf.union(i, j);
                }
            }
            // Check if all dots have the same representative.
            int rep = uf.find(0);
            for (int i = 1; i < n; i++) {
                if (uf.find(i) != rep) return false;
            }
            return true;
        }

        // Computes MST for the dots using Kruskal's algorithm.
        public double computeMSTScore() {
            if (dots.isEmpty()) return 0;
            ArrayList<Edge> allEdges = new ArrayList<>();
            // Create complete graph edges between dots.
            for (int i = 0; i < dots.size(); i++) {
                for (int j = i + 1; j < dots.size(); j++) {
                    double d = dots.get(i).distance(dots.get(j));
                    allEdges.add(new Edge(i, j, d));
                }
            }
            // Sort edges by distance.
            allEdges.sort(Comparator.comparingDouble(e -> e.weight));

            // Use union-find to build the MST.
            int n = dots.size();
            int[] parent = new int[n];
            for (int i = 0; i < n; i++) parent[i] = i;

            // Helper methods.
            class UF {
                int find(int i) {
                    if (parent[i] != i) parent[i] = find(parent[i]);
                    return parent[i];
                }
                boolean union(int i, int j) {
                    int ri = find(i);
                    int rj = find(j);
                    if (ri == rj) return false;
                    parent[ri] = rj;
                    return true;
                }
            }
            UF uf = new UF();
            double mstTotal = 0.0;
            int edgesAdded = 0;
            for (Edge edge : allEdges) {
                if (uf.union(edge.i, edge.j)) {
                    mstTotal += edge.weight;
                    edgesAdded++;
                    if (edgesAdded == n - 1) break;
                }
            }
            return mstTotal;
        }

        // Computes the score (sum of line lengths) for a given player's color.
        public double computePlayerScore(Color playerColor) {
            double score = 0;
            for (Line line : lines) {
                if (line.color.equals(playerColor)) {
                    score += line.start.distance(line.end);
                }
            }
            return score;
        }

        // Overridden painting method that uses a retained offscreen image.
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            // Create the offscreen image if needed.
            if (offscreen == null || offscreen.getWidth() != getWidth() || offscreen.getHeight() != getHeight()) {
                offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
                redrawOffscreen();
            }
            g.drawImage(offscreen, 0, 0, null);
            // Draw any temporary (ghost) line if dragging.
            if (dragStart != null && dragCurrent != null) {
                g.setColor(Color.GRAY);
                g.drawLine(dragStart.x, dragStart.y, dragCurrent.x, dragCurrent.y);
            }
        }

        // Draw the game state into the offscreen image.
        private void redrawOffscreen() {
            Graphics2D g2 = offscreen.createGraphics();
            // Clear the image.
            g2.setColor(getBackground());
            g2.fillRect(0, 0, offscreen.getWidth(), offscreen.getHeight());
            // Draw dots.
            g2.setColor(Color.BLACK);
            for (Point p : dots) {
                g2.fillOval(p.x - 5, p.y - 5, 10, 10);
            }
            // Draw lines.
            for (Line line : lines) {
                g2.setColor(line.color);
                g2.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            }
            g2.dispose();
        }
    }

    // Control Panel: Load, Save, Export, Show Scores, and Exit.
    // Load/Save use object serialization; Export writes the board into a PNG file.
    class ControlPanel extends JPanel {
        public ControlPanel() {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            JButton loadButton = new JButton("Load");
            JButton saveButton = new JButton("Save");
            JButton exportButton = new JButton("Export PNG");
            JButton showScoreButton = new JButton("Show Scores");
            JButton exitButton = new JButton("Exit");

            add(loadButton);
            add(saveButton);
            add(exportButton);
            add(showScoreButton);
            add(exitButton);

            loadButton.addActionListener(e -> loadGame());
            saveButton.addActionListener(e -> saveGame());
            exportButton.addActionListener(e -> exportImage());
            showScoreButton.addActionListener(e -> showScores());
            exitButton.addActionListener(e -> System.exit(0));
        }

        // Object serialization to save the game state.
        private void saveGame() {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(file))) {
                    // Save the current game state from the GameCanvas.
                    oos.writeObject(gameCanvas.dots);
                    oos.writeObject(gameCanvas.lines);
                    JOptionPane.showMessageDialog(this, "Game saved successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error saving game: " + ex.getMessage());
                }
            }
        }

        // Loads the game state from a file using object serialization.
        @SuppressWarnings("unchecked")
        private void loadGame() {
            JFileChooser fc = new JFileChooser();
            if (fc.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
                    gameCanvas.dots = (ArrayList<Point>) ois.readObject();
                    gameCanvas.lines = (ArrayList<Line>) ois.readObject();
                    gameCanvas.offscreen = null;
                    gameCanvas.repaint();
                    JOptionPane.showMessageDialog(this, "Game loaded successfully.");
                } catch (IOException | ClassNotFoundException ex) {
                    JOptionPane.showMessageDialog(this, "Error loading game: " + ex.getMessage());
                }
            }
        }

        // Exports the current game board (canvas) as a PNG file.
        private void exportImage() {
            JFileChooser fc = new JFileChooser();
            if (fc.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
                File file = fc.getSelectedFile();
                BufferedImage image = new BufferedImage(gameCanvas.getWidth(), gameCanvas.getHeight(), BufferedImage.TYPE_INT_ARGB);
                Graphics2D g2 = image.createGraphics();
                gameCanvas.paint(g2);
                g2.dispose();
                try {
                    ImageIO.write(image, "png", file);
                    JOptionPane.showMessageDialog(this, "Image exported successfully.");
                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error exporting image: " + ex.getMessage());
                }
            }
        }

        // Compares each player's score with the optimal MST score and shows the result.
        private void showScores() {
            double blueScore = gameCanvas.computePlayerScore(Color.BLUE);
            double redScore = gameCanvas.computePlayerScore(Color.RED);
            double mstScore = gameCanvas.computeMSTScore();
            String message = String.format(
                    "Blue Score: %.2f\nRed Score: %.2f\nOptimal (MST) Score: %.2f",
                    blueScore, redScore, mstScore
            );
            // Notify if the structure is complete.
            if (!gameCanvas.isConnected()) {
                message += "\n\nThe board is not yet fully connected.";
            }
            JOptionPane.showMessageDialog(this, message);
        }
    }

    // Used for MST computation.
    static class Edge {
        int i, j;
        double weight;
        public Edge(int i, int j, double weight) {
            this.i = i;
            this.j = j;
            this.weight = weight;
        }
    }

    static class Line implements Serializable {
        Point start, end;
        Color color;

        public Line(Point start, Point end, Color color) {
            this.start = start;
            this.end = end;
            this.color = color;
        }
    }

    // Main method: Launch the game.
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DotGame());
    }
}