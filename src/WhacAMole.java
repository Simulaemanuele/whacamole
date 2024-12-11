import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class WhacAMole {

    // immutable data
    private static final int BOARD_WIDTH = 600;
    private static final int BOARD_HEIGHT = 650; // 50px for the top text panel
    private static final int MOLE_DELAY = 1000;
    private static final int PLANT_DELAY = 1500;

    // game variables
    private JFrame frame;

    private JLabel textLabel;
    private JPanel textPanel;
    private JPanel boardPanel;

    JButton[] board = new JButton[9];

    private ImageIcon moleIcon;
    private ImageIcon plantIcon;

    // base variable for tile animation

    private JButton currentMoleTile;
    private JButton currentPlantTile;

    private JButton restartButton;

    private Random random = new Random();

    private TileTimer moleTimer;
    private TileTimer plantTimer;

    private int score = 0;

    private boolean inGame;

    WhacAMole() {
        // setup restart button
        setupRestartButton();

        // frame settings
        setupFrame();

        // setup icons
        setupIcons();

        // setup text panel
        setupTextPanel();

        // setup board panel
        setupBoardPanel();

        // initialize board
        initializeBoard();

        // initialize timer
        initializeTimers();

        // start game
        startGame();

        // set visible later because we have to wait the complete components loading
        frame.setVisible(true);
    }

    private void setupFrame() {
        frame = new JFrame("Mario: Whac A Mole");
        frame.setSize(BOARD_WIDTH, BOARD_HEIGHT);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
    }

    private void setupIcons() {
        Image plantImg = new ImageIcon(getClass().getResource("./assets/piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./assets/monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));
    }

    private void setupTextPanel() {
        textLabel = new JLabel("Score: 0", SwingConstants.CENTER);
        textLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        textLabel.setOpaque(true);

        textPanel = new JPanel(new BorderLayout());
        textPanel.add(textLabel, BorderLayout.CENTER);
        textPanel.add(restartButton, BorderLayout.WEST);
        frame.add(textPanel, BorderLayout.NORTH);
    }

    private void setupBoardPanel() {
        boardPanel = new JPanel(new GridLayout(3, 3));
        frame.add(boardPanel, BorderLayout.CENTER);
    }

    private void setupRestartButton() {
        restartButton = new JButton("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 13));
        restartButton.addActionListener(e -> restartGame());
    }

    private void initializeBoard() {
        for (int i = 0; i < 9; i++) {
            initializeTile(i);
        }
    }

    private void initializeTile(int index) {
        JButton tile = new JButton();
        board[index] = tile;
        boardPanel.add(tile);
        tile.setFocusable(false);
        tile.addActionListener(this::handleTileClick);
    }

    private void initializeTimers() {
        moleTimer = new TileTimer(MOLE_DELAY, board, moleIcon, () -> currentMoleTile = moleTimer.getCurrentTile());
        plantTimer = new TileTimer(PLANT_DELAY, board, plantIcon, () -> currentPlantTile = plantTimer.getCurrentTile());
    }

    private void handleTileClick(ActionEvent e) {
        JButton tile = (JButton) e.getSource();

        if (tile == currentMoleTile) {
            score += 10;
            updateScore();
        } else if (tile == currentPlantTile) {
            gameOver();
        }
    }

    private void updateScore() {
        textLabel.setText("Score: " + score);
    }

    private void gameOver() {
        textLabel.setText("Game Over: " + score);
        moleTimer.stop();
        plantTimer.stop();
        for (JButton button : board) {
            button.setEnabled(false);
        }
        inGame = false;
    }

    private void restartGame() {
        inGame = true;
        moleTimer.start();
        plantTimer.start();
        for (JButton button : board) {
            button.setEnabled(true);
        }
        score = 0;
        updateScore();
    }

    private void startGame() {
        inGame = true;
        moleTimer.start();
        plantTimer.start();
    }

    private class TileTimer {
        private final Timer timer;
        private final JButton[] board;
        private final ImageIcon icon;
        private JButton currentTile;

        public TileTimer(int delay, JButton[] board, ImageIcon icon, Runnable updateCallback) {
            this.board = board;
            this.icon = icon;
            this.timer = new Timer(delay, e -> {
                if (currentTile != null) {
                    currentTile.setIcon(null);
                }

                do {
                    int num = random.nextInt(board.length);
                    currentTile = board[num];
                } while (currentTile == currentMoleTile || currentTile == currentPlantTile);

                currentTile.setIcon(icon);
                updateCallback.run();
            });
        }

        public void start() {
            timer.start();
        }

        public void stop() {
            timer.stop();
        }

        public JButton getCurrentTile() {
            return currentTile;
        }
    }

}
