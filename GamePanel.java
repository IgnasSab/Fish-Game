import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.util.Vector;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.LineBorder;

/**
 * The GamePanel class represents the main panel for the fishing game.
 **/
public class GamePanel extends JPanel implements KeyListener, MouseListener, ActionListener {

    // Variables of the frame:
    int windowWidth;
    int windowHeight;

    // Variables of the player:
    String playerName;
    int playerScore;
    int timeLeft;
    int rodsLeft;
    int fishCount;
    ScorePanel scorePanel;

    // Variables of the boat:
    BoatLabel boatLabel;
    char lastKeyPressed = 'd';
    
    // Variables for the bait:
    boolean paintBait;
    int posRod;
    int repaintCount = 0;
    Vector<Integer> rodVector = new Vector<Integer>();
    MouseEvent mouseEvent;

    // Variables for the animation: 
    FishLabel fish1;
    FishLabel fish2;
    FishLabel fish3;

    Timer timer;
    int totalTime;
    int timeUpdates;

    
    //Variables for the sound:
    Clip rodSwingClip;
    Clip fishCaughtClip;

    /**
     * Constructs a new GamePanel.
     * 
     * @param windowWidth The width of the game window.
     * @param windowHeight The height of the game window.
     * @param playerName The name of the player.
     */

    public GamePanel(int windowWidth, int windowHeight, String playerName) {

        // Initializing all of the variables:
        playerScore = 0;
        timeUpdates = 0;
        fishCount = 0;
        totalTime = 60000;
        timeLeft = totalTime;
        rodsLeft = 5;

        this.windowWidth = windowWidth;
        this.windowHeight = windowHeight;
        
        this.setLayout(null); 
    
        this.setBounds(0, 0, windowWidth, windowHeight);
        this.setBackground(new Color(100, 160, 250));

        this.addKeyListener(this);
        this.addMouseListener(this);

        boatLabel = new BoatLabel();

        this.playerName = playerName;

        this.fish1 = new FishLabel("Images/fish11.png", 1, 0, 4, 0.00);
        this.fish2 = new FishLabel("Images/fish21.png", 2, 0,3, 0.00);
        this.fish3 = new FishLabel("Images/fish11.png", 3,0, 2, 0.00);

        scorePanel = new ScorePanel(windowWidth, windowHeight, null);

        // Creating the background:
        Background bg = new Background(windowWidth, windowHeight);

        // Setting up the rod vector
        rodVector.setSize(4);
        rodVector.set(0, boatLabel.getX()+boatLabel.getWidth()-10);
        rodVector.set(1,boatLabel.getY()+30);
        rodVector.set(3,boatLabel.getY()+30);


        //Adding all of the components to the main panel.
        this.add(bg);
        this.add(boatLabel);
        this.add(fish1);
        this.add(fish2);
        this.add(fish3);
        this.add(scorePanel);

        // Creating our timer that will be used to animate everything (and starting it):
        timer = new Timer(25, this);
        timer.start();

        // Making so that the fishes appear on top:
        this.setComponentZOrder(boatLabel, 0);
        
        // Initializing the score:
        scorePanel.updateTime();
        scorePanel.updateRods();
        scorePanel.updateScore();
        
        fishCaughtClip = loadClip("Sounds/fishcaught.wav");
        
    }

    
    /**
     * Load an audio clip from a specified source.
     *
     * This method is used to load an audio clip (sound) from a designated source.
     * It prepares the audio clip for playback, making it accessible for use in
     * various parts of the application.
     *
     * @param source The source or location of the audio clip to be loaded.
     * @return An audio clip loaded from the specified source.
     * @throws IOException If an error occurs while loading the audio clip.
     */
    private Clip loadClip(String soundFilePath) {
        try {
            InputStream soundStream = getClass().getResourceAsStream(soundFilePath);
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new BufferedInputStream(soundStream));
            Clip clip = AudioSystem.getClip();
            clip.open(audioInputStream);
            return clip;
        } catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    

    /**
     * Play the sound effect associated with catching a fish.
     *
     * This method is used to trigger the playback of a sound effect that represents
     * the act of catching a fish. It provides audio feedback to the player when a
     * successful catch occurs.
     */
    public void playFishCaughtSound() {
        if (fishCaughtClip.isRunning()) {
            fishCaughtClip.stop();
        }
        fishCaughtClip.setFramePosition(0);
        fishCaughtClip.start();
    }

    
    /**
     * Repaint the specified panel.
     *
     * This method is used to trigger the repainting of a specific panel, which is
     * typically a graphical component. Repainting updates the visual representation
     * of the panel, ensuring that any changes or modifications to its content are
     * displayed on the screen.
     *
     * @param panel The panel to be repainted.
     */
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if (paintBait) {

            Graphics2D g2d = (Graphics2D) g;
            drawBait(g2d);

        }
        
    }

    /**
     * Handles the mouse being pressed.
     * 
     * @param e The MouseEvent object representing the mouse event.
     */
    @Override
    public void mousePressed(MouseEvent e) {

        mouseEvent = e;
        
        if (paintBait) {

            // Resetting the position of the rod
            rodVector.set(3, rodVector.get(1));

            // Subtract the amount of rods that are left, since button was pressed then the rod was moving:
            rodsLeft--;
            scorePanel.updateRods();
            if (rodsLeft == 0) {
                    gameEnded();
                }
            
        }
        
        // Assigns the boolean variable to true, so the rod is animated afterwards:
        paintBait = true;   
        
        this.repaint();
    }

    /*
     * 
     * Method that check is the fish is hit.
     * 
     */
    void hit(FishLabel fish) {
        
        if (fish.getX() <=  rodVector.get(0) && fish.getX() + 100 >= rodVector.get(0) && fish.getY() <= rodVector.get(3) && fish.getY() + 80 >= rodVector.get(3)) {

            // Update required variables if the end coordinate of the fishing is contained in the label of the fish:
            rodVector.set(3, rodVector.get(1));
            fish.setLocation(fish.initialX, fish.initialY);
            fishCount++;
            playerScore++;
            paintBait = false;
            scorePanel.updateScore();

            playFishCaughtSound();

            
        }

    }
    /*
     * 
     */
    @Override
    public void keyReleased(KeyEvent e) {

        if ((e.getKeyChar() == 'a' || e.getKeyChar() == 'A') && boatLabel.getX() >= 50) {
            if (paintBait) {
                rodsLeft--;
                scorePanel.updateRods();
                if (rodsLeft == 0) {
                    gameEnded();
                }
            }
            boatLabel.setLocation(boatLabel.getX() - 50, boatLabel.getY());
            lastKeyPressed = 'a';
            rodVector.set(0, boatLabel.getX()+10);
            rodVector.set(3, boatLabel.getY()+30);

        } else if ((e.getKeyChar() == 'd' || e.getKeyChar() == 'D') && boatLabel.getX() + boatLabel.getWidth() + 50 <= this.windowWidth) {
            if (paintBait) {
                rodsLeft--;
                scorePanel.updateRods();
                if (rodsLeft == 0) {
                    gameEnded();
                }
            }
            boatLabel.setLocation(boatLabel.getX() + 50, boatLabel.getY());
            lastKeyPressed = 'd';
            rodVector.set(0, boatLabel.getX()+boatLabel.getWidth()-10);
            rodVector.set(3, boatLabel.getY()+30);
        }

        paintBait = false;

    }
    
    /**
     * The BoatLabel class represents a label for the boat image.
     */
    class BoatLabel extends JLabel {

        BoatLabel() {
            this.setBounds(550, 325, 200, 150);  
        }

    /**
     * Painting or repaintint the boat
     **/ 

    @Override 
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        if(lastKeyPressed == 'd') {

            ImageIcon imageIcon = new ImageIcon("Images/boatfisher1.png");
            Image image = imageIcon.getImage();
            g.drawImage(image,0, 0, 200, 250, null);

        } else if (lastKeyPressed == 'a') {

            ImageIcon imageIcon = new ImageIcon("Images/boatfisher2.png");
            Image image = imageIcon.getImage();
            g.drawImage(image,0, 0, 200, 250, null);

        }
       
        }
        

    }

    /**
     * The FishLabel class represents a label for the 3 distinct fishes (contains all information about them).
     */
    class FishLabel extends JLabel {
     
        final int levelHeight = 150;
        int level;
        double xVelocity;
        String filePath1;   
        String filePath2;
        String mainPath;
        double xAcceleration; 
        double initialVelocity;
        double fishX;
        int initialX;
        int initialY;
        Image image;
        
        FishLabel(String filePath, int level, double fishX, double initialVelocity, double xAcceleration) {

            this.level = level;
            this.mainPath = filePath1;
            this.xAcceleration = xAcceleration;
            this.initialVelocity = initialVelocity;
            this.xVelocity = initialVelocity;
            this.fishX = fishX;
            this.initialX = - 150;

            ImageIcon imageIcon = new ImageIcon(filePath);
            image = imageIcon.getImage();

            if (level == 1) {
                this.setBounds(0, 520, 100, 80);
                this.initialY = 520; 
            }
            if (level == 2) {
                this.setBounds(0, 640, 100, 80);
                this.initialY = 640;
            }
            if (level == 3) {
                this.setBounds(0, 760, 100, 80);
                this.initialY = 760;
            }
            

        }

        @Override 
        protected void paintComponent(Graphics g) {

            g.drawImage(image,0, 0, 100, 80, null);

        }

    }

    /**
     * Repaints the panel and handles the animation timer.
     * 
     * @param e The ActionEvent object representing the timer event.
     */
    @Override
    public void actionPerformed(ActionEvent e) {

        timeUpdates++;

        timeLeft = totalTime - timeUpdates * 25;

        if (timeLeft % 1000 == 0) {
            scorePanel.updateTime();
            // Checks if there is still time left, if not, then end the game.
            if (timeLeft <= 0) {
                gameEnded();        
            }
        }

        paintFishes(fish1, fish2, fish3); 
        if (paintBait) {
            

            if (rodVector.get(3) <= 600) {
                hit(fish1);
            } else if (rodVector.get(3) <= 720) {
                hit(fish2);   
            } else if (rodVector.get(3) <= 840) {
                hit(fish3);
            } else if (rodVector.get(3) >= 940) {
                paintBait = false;
                rodVector.set(3, rodVector.get(1));
                rodsLeft--;
                scorePanel.updateRods();
                if (rodsLeft == 0) {
                    gameEnded();
                }
            }
            rodVector.set(3, rodVector.get(3) + 15);
        }
        repaint();
    }

    /*
     * 
     * This method changes the position of the fished.
     * 
     */
    void paintFishes(FishLabel fish1, FishLabel fish2, FishLabel fish3) {

        if (fish1.getX() >= windowWidth + 100) {
            fish1.setLocation(fish1.initialX, fish1.initialY);
            fishCount++;
            scorePanel.updateScore();
        } else if (fish2.getX() >= windowWidth + 100) {
            fish2.setLocation(fish2.initialX, fish2.initialY);
            fishCount++;
            scorePanel.updateScore();
        } else if (fish3.getX() >= windowWidth + 100) {
            fish3.setLocation(fish3.initialX, fish3.initialY);
            fishCount++;
            scorePanel.updateScore();
        }

        if (timeUpdates%400 == 0) {
           fish1.xVelocity += 1;
            fish2.xVelocity += 1;
            fish3.xVelocity += 1;  
        }
        

        fish1.setLocation((int) (fish1.getX() + fish1.xVelocity), fish1.getY());
        fish2.setLocation((int) (fish2.getX() + fish2.xVelocity), fish2.getY());
        fish3.setLocation((int) (fish3.getX() + fish3.xVelocity), fish3.getY());
    }

    /**
     * Draws the bait on the panel.
     * 
     * @param g2d The Graphics2D object used for drawing.
     */
    void drawBait(Graphics2D g2d) {
            
        g2d.setStroke(new BasicStroke(3.0f));
        g2d.setColor(new Color(110, 65, 29));    
        g2d.drawLine(rodVector.get(0), rodVector.get(1), rodVector.get(0), rodVector.get(3));

    }

    /**
     * Writes the player's name and score to a file.
     * 
     * @param playerName The name of the player.
     * @param playerScore The player's score.
     */

    public static void writeToFile(String playerName, int playerScore)
    {
        /**
         * This try block is used to check for exceptions and handle them appropriately.
         * It is a part of error-handling and exception management in the code.
         * 
         * @see Exception
         * @see RuntimeException
         * @see Error
         */

        try {

            /**
             * Reads the existing contents of a file and returns the data as a string.
             *
             * This method is used to open and read the contents of a file, converting the
             * data into a string for further processing or manipulation.
             *
             * @param filePath The path to the file to be read.
             * @return A string containing the existing contents of the file.
             * @throws IOException If an error occurs while reading the file.
             */

            BufferedReader in = new BufferedReader(new FileReader("score.txt"));
            StringBuilder fileContents = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                fileContents.append(line).append("\n");
            }
            in.close();

            /**
             * Opens the specified file in write mode, clearing its existing content.
             *
             * This method is used to open a file in write mode, which means it will
             * truncate and clear any existing content in the file. This operation is
             * typically performed when you want to overwrite the contents of the file
             * with new data.
             *
             * @param filePath The path to the file to be opened in write mode.
             * @throws IOException If an error occurs while opening the file in write mode.
             */
            BufferedWriter out = new BufferedWriter(new FileWriter("score.txt"));

            /**
             * Writes new data at the beginning of the specified file.
             *
             * This method is used to prepend new data to the beginning of a file while
             * preserving its existing content. The new data is inserted at the top of
             * the file, pushing the original content downwards.
             *
             * @param filePath The path to the file where the new data will be written.
             * @param newData The data to be written at the beginning of the file.
             * @throws IOException If an error occurs while writing the new data to the file.
             */

            out.write(String.format("%s: %d\n", playerName, playerScore));

            /**
             * Writes back the original contents to the specified file.
             *
             * This method is used to restore the original contents of a file after making
             * modifications. It overwrites any changes made to the file with the original
             * data.
             *
             * @param filePath The path to the file where the original contents will be written.
             * @param originalData The original data to be written back to the file.
             * @throws IOException If an error occurs while writing the original contents to the file.
             */

            out.write(fileContents.toString());

            out.close();
            
        }
 
        /**
         * This catch block is used to handle exceptions that may occur in the code.
         *
         * When an exception is thrown in the code preceding this catch block, this block
         * is responsible for catching and handling the exception. It contains the logic
         * to handle the exception, which may include logging, error messages, or other
         * appropriate actions to address the exceptional situation.
         *
         * @param exception The exception that is caught and handled by this block.
         */        
        
    catch (IOException e) {
 
    /**
     * Display a user-friendly message when an exception occurs in the code.
     *
     * This code snippet is responsible for providing feedback to the user or
     * developer when an exception is encountered. It typically displays an
     * error message or log information that helps identify and understand the
     * exceptional situation that has occurred.
     *
     * @param errorMessage The message to be displayed when an exception occurs.
     */
            System.out.println("exception occurred" + e);
        }
        
    }


    @Override
    public void mouseClicked(MouseEvent e) {}
    public void mouseReleased(MouseEvent e) {}
    public void mouseEntered(MouseEvent e) {}
    public void mouseExited(MouseEvent e) {}
    public void keyTyped(KeyEvent e) {}
    public void keyPressed(KeyEvent e){}



    /**
     * The ScorePanel class represents a panel for displaying game scores and controls.
     */
    public class ScorePanel extends JPanel {
        JLabel score;
        JPanel rodsPanel;
        JLabel timer;
        JLabel rods;
    
        JButton resetButton;
    
        ImageIcon baitIcon;
        ImageIcon baitCrossIcon;
    
        ScorePanel(int windowWidth, int windowHeight, GamePanel gamePanel) {

            baitIcon = new ImageIcon("Images/bait.png");
            baitCrossIcon = new ImageIcon("Images/baitcross.png");
            baitIcon = new ImageIcon(baitIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
            baitCrossIcon = new ImageIcon(baitCrossIcon.getImage().getScaledInstance(40, 40, Image.SCALE_SMOOTH));
    
            this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
            this.setBounds(0, 0, windowWidth, 75);
            this.setBackground(new Color(100, 160, 250));
    
            rodsPanel = new JPanel();
            rodsPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
            rodsPanel.setOpaque(false);
    
            JPanel verticalCenteringPanel = new JPanel(new GridBagLayout());
            verticalCenteringPanel.add(rodsPanel);
            verticalCenteringPanel.setOpaque(false);
            verticalCenteringPanel.setMaximumSize(new Dimension(300, 75));

            score = new JLabel();
            rods = new JLabel();
            timer = new JLabel();
    
            score.setFont(new Font("Arial", Font.PLAIN, 18));
            rods.setFont(new Font("Arial", Font.PLAIN, 18));
            timer.setFont(new Font("Arial", Font.PLAIN, 18));
    
            score.setForeground(Color.GRAY);
            rods.setForeground(Color.GRAY);
            timer.setForeground(Color.GRAY);
    
            resetButton = new JButton("Restart");            
            resetButton.setFont(new Font("Sans-Serif", Font.PLAIN, 16));        
            resetButton.setPreferredSize(new Dimension(80, 60));              
            resetButton.setBackground(new Color(230, 150, 150));
            resetButton.setForeground(Color.WHITE);     
            resetButton.setFocusable(true);     
            /**
             * Set the thickness and rounding of a shape's border.
             *
             * This code snippet is used to configure the thickness and rounding of a shape's
             * border. The parameters control the appearance of the shape's outline, where
             * '2' specifies the thickness of the border, and 'true' makes it rounded.
             *
             * @param thickness The thickness of the shape's border.
             * @param rounded True to make the border rounded, false for a sharp border.
             */
            LineBorder roundedBorder = new LineBorder(Color.RED, 3, true); 
            resetButton.setBorder(roundedBorder);


            resetButton.addActionListener(new ActionListener() {        
                @Override
                public void actionPerformed(ActionEvent e) {        
                    GamePanel.this.timer.stop();
                    
                    /**
                    * Retrieve the parent frame associated with the ScorePanel.
                    *
                    * This method is used to obtain the parent frame (usually a JFrame) that contains
                    * the ScorePanel component. It allows access to the enclosing frame for further
                    * manipulation or interaction with the graphical user interface.
                    *
                    * @return The parent frame (typically a JFrame) of the ScorePanel.
                    */
                    JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(ScorePanel.this);     
            
                    /**
                     * Remove the current GamePanel from the frame.
                     *
                     * This method is used to remove the existing GamePanel component from the frame,
                     * effectively detaching it from the graphical user interface. It allows for
                     * replacing the current GamePanel with a new one or altering the user interface
                     * dynamically.
                     *
                     * @param frame The frame from which the GamePanel will be removed.
                     */
                    Component[] components = frame.getContentPane().getComponents();       
                    for (Component component : components) {        
                        if (component instanceof GamePanel) {       
                            GamePanel oldGamePanel = (GamePanel) component;     
                            oldGamePanel.removeKeyListener(oldGamePanel); 
                            frame.getContentPane().remove(component);       
                            break;
                        }
                    }
                    
                    /**
                     * Create a new GamePanel and add it to the specified frame.
                     *
                     * This method is used to create a new instance of a GamePanel component and
                     * add it to a frame. It allows for refreshing or replacing the current GamePanel
                     * in the graphical user interface with a new one.
                     *
                     * @param frame The frame to which the new GamePanel will be added.
                     * @return The newly created GamePanel instance that is added to the frame.
                     */

                    GamePanel newGamePanel = new GamePanel(windowWidth, windowHeight, playerName);      
                    frame.add(newGamePanel);        
                    newGamePanel.addKeyListener(newGamePanel);
                    /**
                     * Add key listeners to the specified GamePanel for user input.
                     *
                     * This method is used to attach key listeners to a GamePanel component, enabling
                     * it to respond to user keyboard input. Key listeners are typically used to handle
                     * user interactions like moving objects or triggering actions within the game.
                     *
                     * @param gamePanel The GamePanel to which key listeners will be added.
                     */

                    newGamePanel.setFocusable(true);        
                    newGamePanel.requestFocusInWindow();        

            
                    /**
                     * Revalidate and repaint the frame to update its components.
                     *
                     * This method is used to trigger a revalidation and repainting of the frame's
                     * components. It ensures that any changes made to the graphical user interface
                     * are properly reflected and updated on the screen, providing a visual update
                     * to the user.
                     *
                     * @param frame The frame that needs revalidation and repainting.
                     */
                    frame.revalidate();     
                    frame.repaint();        
                }
            });
    
            rodsPanel.add(rods);
    
            // Required for the horizontal layout of all the components:
            this.add(Box.createHorizontalGlue());
            this.add(score);
            this.add(Box.createHorizontalGlue());
            this.add(verticalCenteringPanel);
            this.add(Box.createHorizontalGlue());
            this.add(timer);
            this.add(Box.createHorizontalGlue());
            this.add(resetButton);
            this.add(Box.createHorizontalGlue());

        }
    
        public void updateTime() {
            if (Math.round(timeLeft/1000) <= 10) {
                timer.setForeground(Color.RED);
            }
            timer.setText(String.format("Time left: %d seconds", Math.round(timeLeft/1000)));
        }
    
        public void updateScore() {
            score.setText(String.format("Fish caught: %d / %d", playerScore, fishCount));
        }
    
        public void updateRods() {
            rodsPanel.removeAll();
            rods.setText("Baits: ");
            rodsPanel.add(rods);
    
            for (int i = 1; i <= 5; i++) {
                if (i <= rodsLeft) {
                    JLabel rodImage = new JLabel(baitIcon);
                    rodsPanel.add(rodImage);
                } else {
                    JLabel rodImage = new JLabel(baitCrossIcon);
                    rodsPanel.add(rodImage);
                }
            }
        }
    }

    /**
     * Handles the game ending, displays a message, and returns to the starting screen.
     */
    void gameEnded() {

        timer.stop();
        writeToFile(playerName,  playerScore);

        String message;
        if (timeLeft == 0) {
            message = "Time is up.\n Fish caught: %d, Baits left: %d.\n Return to the starting screen...".formatted(playerScore, rodsLeft);
        } else {
            message = "You ran out of baits.\n Fish caught: %d, Baits left: %d.\n Return to the starting screen...".formatted(playerScore, rodsLeft);
        }
    
        JOptionPane.showMessageDialog(null, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);

        JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(this);
        
        this.removeAll();
        frame.remove(this);
        frame.dispose();

        FishGame.run();
        
    }


}
