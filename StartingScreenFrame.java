import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;

/**
 * The `StartingScreenFrame` class represents the starting screen of the Fisherman Game.
 * It allows the player to enter their name and start the game.
 */
public class StartingScreenFrame extends JFrame {
    private JButton startButton;
    private JTextField nameField;
    private JLabel titleLabel;
    private JLabel nameLabel;
    private JPanel topScoresPanel; // Panel to display top scores
    private Clip backgroundMusic;

    /**
     * Creates a new `StartingScreenFrame` for the Fisherman Game.
     *
     * @param fishGame The `FishGame` instance that manages the game flow.
     */
    public StartingScreenFrame(FishGame fishGame) {

        this.setTitle("Fisherman Game - Starting Screen");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setSize(1280, 920); // Make the frame wider
        this.setResizable(false);
        this.setLocationRelativeTo(null);
        this.setVisible(true);

        /**
         * Create a custom JPanel for setting a background image.
         *
         * This method is used to create a customized JPanel that allows setting a
         * background image for the panel. The custom JPanel can be used to enhance the
         * visual appearance of the user interface by displaying an image as the panel's
         * background.
         *
         * @return A custom JPanel with support for a background image.
         */
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                try {
                    BufferedImage backgroundImage = ImageIO.read(new File("Images/background.jpg"));
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        };

        panel.setLayout(new BorderLayout());

        /**
         * Load and play background music.
         *
         * This method is used to load and play background music in the application. It
         * facilitates the addition of background audio to enhance the user experience
         * during program execution.
         *
         * @param musicSource The source or location of the background music file.
         * @throws IOException If an error occurs while loading the background music.
         */
        playBackgroundMusic();

        /**
         * Position the title at the top of the content or interface.
         *
         * This code snippet is used to specify the layout or arrangement of a title
         * within the content or graphical interface, ensuring that it is displayed
         * at the topmost part of the visual display.
         */
        titleLabel = new JLabel("WELCOME TO THE FISHERMAN GAME");
        titleLabel.setFont(new Font("Serif", Font.PLAIN, 35));
        titleLabel.setHorizontalAlignment(SwingConstants.CENTER);
        titleLabel.setPreferredSize(new Dimension(600, 200)); // Make the title less wide in the x-axis

        /**
         * Create a panel to center the label and text input components.
         *
         * This method is used to create a panel that serves as a container for the label
         * and text input components, ensuring they are visually centered within the panel.
         * It helps in achieving an organized and visually appealing layout for user
         * interface elements.
         *
         * @return A panel designed to center the label and text input components.
         */
        JPanel centerPanel = new JPanel();
        centerPanel.setLayout(new BoxLayout(centerPanel, BoxLayout.Y_AXIS));
        centerPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        centerPanel.setAlignmentY(Component.CENTER_ALIGNMENT);

        /**
         * Create a label and input field for entering a name at the center.
         *
         * This method is used to design and create a label and an input field specifically
         * for the purpose of entering a name. These components are positioned at the center
         * of the graphical interface, ensuring a visually balanced and user-friendly layout.
         *
         * @return An organized set of components for entering a name, centered in the layout.
         */
        nameLabel = new JLabel("Enter Your Name:");
        nameLabel.setFont(new Font("Arial", Font.PLAIN, 16)); // Smaller font size
        nameLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        nameLabel.setAlignmentY(Component.CENTER_ALIGNMENT);

        nameField = new JTextField(20);
        nameField.setFont(new Font("Arial", Font.PLAIN, 18));
        nameField.setMaximumSize(new Dimension(300, 30));

        centerPanel.add(Box.createRigidArea(new Dimension(0, 20))); // Add spacing above the label
        centerPanel.add(nameLabel);
        centerPanel.add(Box.createRigidArea(new Dimension(0, 10))); // Add spacing between label and input field
        centerPanel.add(nameField);

        /**
        * Create a "Start Game" button with a yellow background at the bottom.
        *
        * This method is used to design and create a button with the label "Start Game" and
        * positions it at the bottom of the graphical interface. The button is styled with a
        * yellow background to enhance its visual appearance.
        *
        * @return A "Start Game" button with a yellow background, placed at the bottom.
        */
        startButton = new JButton("Start Game");
        startButton.setFont(new Font("Sans-Serif", Font.PLAIN, 24));
        startButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        startButton.setPreferredSize(new Dimension(600, 80)); // Make the button less wide in the x-axis
        startButton.setBackground(Color.YELLOW); // Set the button's background color to yellow

        /**
        * Add multiple components to the main panel of the user interface.
        *
        * This method is used to add a collection of graphical components, such as labels,
        * input fields, and buttons, to the main panel of the user interface. It is an
        * essential step in building and organizing the visual layout of the application.
        *
        * @param mainPanel The main panel to which components will be added.
        */
        panel.add(titleLabel, BorderLayout.NORTH);
        panel.add(centerPanel, BorderLayout.CENTER);
        panel.add(startButton, BorderLayout.SOUTH);

        /**
         * Create a panel for displaying top scores.
         *
         * This method is used to design and create a panel dedicated to displaying and
         * presenting the top scores achieved by players. It facilitates the organization
         * of score-related information in the graphical user interface.
         *
         * @return A panel for presenting top scores.
         */
        topScoresPanel = new JPanel();
        topScoresPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        topScoresPanel.setLayout(new BoxLayout(topScoresPanel, BoxLayout.Y_AXIS));
        topScoresPanel.setPreferredSize(new Dimension(300, 600)); // Adjust the size in the x-axis
        topScoresPanel.setBackground(Color.PINK); // Set the background color to pink

        /**
         * Set a title for the top scores section.
         *
         * This method is used to define and set a title for the top scores section within
         * the graphical user interface. It helps provide context and clarity about the
         * purpose of the displayed top scores.
         *
         * @param title The title text to be displayed for the top scores section.
         */
        JLabel topScoresTitle = new JLabel("TOP SCORES");
        topScoresTitle.setFont(new Font("Sans-Serif", Font.PLAIN, 24));
        topScoresTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        topScoresPanel.add(topScoresTitle);

        readFromFile();
        
        /**
         * Add the top scores panel to the right side of the user interface.
         *
         * This method is used to incorporate the panel displaying top scores into the
         * right side of the graphical user interface. It positions and integrates the top
         * scores information within the overall layout of the application.
         *
         * @param topScoresPanel The panel containing top scores to be added to the right.
         */
        panel.add(topScoresPanel, BorderLayout.EAST);

        getContentPane().add(panel);

        startButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                /**
                 * Stop the background music.
                 *
                 * This method is used to halt the playback of background music in the application.
                 * It terminates the audio stream, effectively silencing the background music.
                 */
                stopBackgroundMusic();

                String playerName = nameField.getText();
                if (!playerName.isEmpty()) {
                    /**
                     * Incorporate the 'playerName' variable into the game's logic.
                     *
                     * This comment suggests that the 'playerName' variable is intended to be utilized
                     * as part of the game's logic. It is likely used to store or represent the name of
                     * the player, which can be relevant for various in-game interactions and features.
                     *
                     * @param playerName The variable representing the name of the player.
                     */
                    fishGame.openMainFrame(playerName);
                } else {
                    JOptionPane.showMessageDialog(null, "Please enter your name.");
                    playBackgroundMusic();
                }
            }
        });
    }

    /**
     * Plays the background music for the starting screen.
     */
    private void playBackgroundMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Sounds/songscreenframe.wav"));
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY); // Loop the background music
            backgroundMusic.start(); // Start playing the music ..........................................................

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Stops the background music when no longer needed.
     */
    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
        }
    }

    /**
     * Reads top scores data from a file and populates the top scores panel.
     */
    // Reading the name, score from the file and adding it to the scoreboard.
    private void readFromFile(){
        File scoreFile = new File("score.txt");
        Scanner scanner;
        try {
            scanner = new Scanner(scoreFile);
            
            while(scanner.hasNextLine()){
                String nameScoreData = scanner.nextLine();
                JLabel scoreLabel = new JLabel(nameScoreData);
                scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT); // Center scores within the panel
                topScoresPanel.add(scoreLabel);
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

    }

}
