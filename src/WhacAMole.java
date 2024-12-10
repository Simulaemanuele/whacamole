import java.awt.*;
import java.awt.event.*;
import java.util.Random;

import javax.swing.*;

public class WhacAMole {
    int boardWidth = 600;
    int boardHeight = 650; // 50px for the top text panel

    JFrame frame = new JFrame("Mario: Whac A Mole");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    JButton[] board = new JButton[9];

    ImageIcon moleIcon;
    ImageIcon plantIcon;

    // base variable for tile animation

    JButton currentMoleTile;
    JButton currentPlantTile;

    JButton restartButton = new JButton();

    Random random = new Random();

    Timer setMoleTimer;
    Timer setPlantTimer;

    int score = 0;

    boolean inGame;

    WhacAMole() {
        // frame settings
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        // text label settings
        textLabel.setFont(new Font("Arial", Font.PLAIN, 50));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Score: 0");
        textLabel.setOpaque(true);

        // label added to panel
        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(3, 3));
        // boardPanel.setBackground(Color.black);
        frame.add(boardPanel);

        restartButton.setText("Restart");
        restartButton.setFont(new Font("Arial", Font.PLAIN, 13));
        restartButton.setBorderPainted(true);
        restartButton.setSize(75, 35);
        restartButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                inGame = true;
                setMoleTimer.start();
                setPlantTimer.start();
                for (int i = 0; i < 9; i++) {
                    board[i].setEnabled(true);
                }
                score = 0;
                textLabel.setText("Score: " + Integer.toString(score));
            }
        });

        textLabel.add(restartButton);

        // plantIcon = new ImageIcon(getClass().getResource("../assets/piranha.png"));
        Image plantImg = new ImageIcon(getClass().getResource("./assets/piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./assets/monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        score = 0;

        // loop that create buttons
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            // tile.setIcon(moleIcon);

            // logic for scoring and game over
            tile.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    JButton tile = (JButton) e.getSource();

                    if (tile == currentMoleTile) {
                        score += 10;
                        textLabel.setText("Score: " + Integer.toString(score));
                    } else if (tile == currentPlantTile) {
                        textLabel.setText("Game Over: " + Integer.toString(score));
                        setMoleTimer.stop();
                        setPlantTimer.stop();
                        for (int i = 0; i < 9; i++) {
                            board[i].setEnabled(false);
                        }
                        inGame = false;
                    }
                }
            });
        }

        setMoleTimer = timerActionLoop(1000, new JButton[] { currentMoleTile }, board, moleIcon, true);
        setPlantTimer = timerActionLoop(1500, new JButton[] { currentPlantTile }, board, plantIcon, false);

        // flag for checking if a game session is running
        inGame = true;

        // start the timers
        if (inGame == true) {
            setMoleTimer.start();
            setPlantTimer.start();
        }

        // set visible later because we have to wait the complete components loading
        frame.setVisible(true);
    }

    public Timer timerActionLoop(int delay, JButton[] currentTileWrapper, JButton[] board, ImageIcon icon,
            boolean isMole) {
        return new Timer(delay, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentTileWrapper[0] != null) {
                    currentTileWrapper[0].setIcon(null);
                }

                // random select other tile
                JButton tile;
                do {
                    int num = random.nextInt(9); // random number between 0 - 9
                    tile = board[num];
                } while (tile == currentMoleTile || tile == currentPlantTile);

                // Update ref and icon of the tile
                currentTileWrapper[0] = tile;
                currentTileWrapper[0].setIcon(icon);

                // update the global ref
                if (isMole) {
                    currentMoleTile = currentTileWrapper[0];
                } else {
                    currentPlantTile = currentTileWrapper[0];
                }
            }
        });
    }
}
