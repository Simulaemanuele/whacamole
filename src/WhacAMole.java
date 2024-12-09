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

        setMoleTimer = new Timer(1000, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentMoleTile != null) {
                    currentMoleTile.setIcon(null);
                    currentMoleTile = null;
                }

                // random select other tile
                int num = random.nextInt(9); // random number between 0 - 9
                JButton tile = board[num];

                // if tile is occupied by plant skip this tile
                if (currentPlantTile == tile) {
                    return;
                }

                currentMoleTile = tile;
                currentMoleTile.setIcon(moleIcon);
            }
        });

        setPlantTimer = new Timer(1500, new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (currentPlantTile != null) {
                    currentPlantTile.setIcon(null);
                    currentPlantTile = null;
                }

                int num = random.nextInt(9);
                JButton tile = board[num];

                if (currentMoleTile == tile) {
                    return;
                }

                currentPlantTile = tile;
                currentPlantTile.setIcon(plantIcon);
            }
        });

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
}
