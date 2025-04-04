import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;

public class DotGame extends JFrame {

    private ConfigPanel configPanel;
    private GameCanvas gameCanvas;
    private ControlPanel controlPanel;

    public DotGame() {
        setTitle("Dot Game");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        configPanel = new ConfigPanel();
        add(configPanel, BorderLayout.NORTH);

        gameCanvas = new GameCanvas();
        add(gameCanvas, BorderLayout.CENTER);

        controlPanel = new ControlPanel();
        add(controlPanel, BorderLayout.SOUTH);

        setSize(600, 600);
        setLocationRelativeTo(null);
        setVisible(true);
    }

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

    class GameCanvas extends JPanel {
        private ArrayList<Point> dots;
        private ArrayList<Line> lines;

        public GameCanvas() {
            dots = new ArrayList<>();
            lines = new ArrayList<>();
            setBackground(Color.WHITE);

            addMouseListener(new MouseAdapter() {
                Point selectedDot = null;
                public void mousePressed(MouseEvent e) {
                    for (Point p : dots) {
                        if (p.distance(e.getPoint()) < 10) {
                            if (selectedDot == null) {
                                selectedDot = p;
                            } else {
                                Color lineColor = (lines.size() % 2 == 0) ? Color.BLUE : Color.RED;
                                lines.add(new Line(selectedDot, p, lineColor));
                                selectedDot = null;
                                repaint();
                                break;
                            }
                        }
                    }
                }
            });
        }

        public void initializeDots(int numDots) {
            dots.clear();
            lines.clear();
            Random rand = new Random();
            int margin = 20;
            for (int i = 0; i < numDots; i++) {
                int x = margin + rand.nextInt(getWidth() - 2 * margin);
                int y = margin + rand.nextInt(getHeight() - 2 * margin);
                dots.add(new Point(x, y));
            }
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            g.setColor(Color.BLACK);
            for (Point p : dots) {
                g.fillOval(p.x - 5, p.y - 5, 10, 10);
            }
            for (Line line : lines) {
                g.setColor(line.color);
                g.drawLine(line.start.x, line.start.y, line.end.x, line.end.y);
            }
        }
    }

    class ControlPanel extends JPanel {
        public ControlPanel() {
            setLayout(new FlowLayout(FlowLayout.RIGHT));
            JButton loadButton = new JButton("Load");
            JButton saveButton = new JButton("Save");
            JButton exitButton = new JButton("Exit");

            add(loadButton);
            add(saveButton);
            add(exitButton);

            loadButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Load functionality not implemented yet.");
            });
            saveButton.addActionListener(e -> {
                JOptionPane.showMessageDialog(this, "Save functionality not implemented yet.");
            });
            exitButton.addActionListener(e -> System.exit(0));
        }
    }

    class Line {
        Point start, end;
        Color color;

        public Line(Point start, Point end, Color color) {
            this.start = start;
            this.end = end;
            this.color = color;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new DotGame());
    }
}
