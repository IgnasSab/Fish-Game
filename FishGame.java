import javax.swing.SwingUtilities;

/**
 * The FishGame class represents a simple class in which the starting screen and a main frame. 
 * This is the class that should be run to start the game.
 */
public class FishGame {

    private StartingScreenFrame startingScreenFrame;
    /**
     * Creates the StartingScreenFrame.
     */
    public FishGame() {
        startingScreenFrame = new StartingScreenFrame(this);
        startingScreenFrame.setVisible(true);
    }

    /**
     * Opens the MainFrame and disposes of the StartingScreenFrame.
     *
     * @param playerName The name of the player.
     */
    public void openMainFrame(String playerName) {

        startingScreenFrame.dispose(); // Close the StartingScreenFrame
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                new MainFrame(1280, 920, playerName);
            }
        });
    }

    /**
     * The main entry point for the FishGame application. Creates a new instance of FishGame and runs the game.
     */
    public static void run() {
        new FishGame();
    }

    /**
     * The main method that starts the FishGame application.
     *
     * @param args Command-line arguments (not used in this application).
     */
    public static void main(String[] args) {
        run();
    }
}
