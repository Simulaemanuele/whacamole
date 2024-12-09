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

    Random random = new Random();

    Timer setMoleTimer;
    Timer setPlantTimer;

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

        // plantIcon = new ImageIcon(getClass().getResource("../assets/piranha.png"));
        Image plantImg = new ImageIcon(getClass().getResource("./assets/piranha.png")).getImage();
        plantIcon = new ImageIcon(plantImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        Image moleImg = new ImageIcon(getClass().getResource("./assets/monty.png")).getImage();
        moleIcon = new ImageIcon(moleImg.getScaledInstance(150, 150, java.awt.Image.SCALE_SMOOTH));

        // loop that create buttons
        for (int i = 0; i < 9; i++) {
            JButton tile = new JButton();
            board[i] = tile;
            boardPanel.add(tile);
            tile.setFocusable(false);
            // tile.setIcon(moleIcon);
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

                currentMoleTile = tile;
                currentMoleTile.setIcon(moleIcon);
            }
        });

        // start the timer
        setMoleTimer.start();
        // set visible later because we have to wait the complete components loading
        frame.setVisible(true);
    }
}
