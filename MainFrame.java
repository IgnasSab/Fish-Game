
import javax.swing.*;


/**
 * The MainFrame class represents the main application window for the fishing game.
 */
public class MainFrame extends JFrame {

    int windowWidth;
    int windowHeight;
    JLabel label;
    /**
     * Constructs a new MainFrame for the fishing game.
     *
     * @param windowWith  The width of the game window.
     * @param windowHeight The height of the game window.
     * @param playerName  The name of the player.
     */
    MainFrame(int windowWith, int windowHeight, String playerName){

        this.windowWidth = windowWith;
        this.windowHeight = windowHeight;

        this.setSize(windowWidth, windowHeight);
        this.setTitle("Adventure of a fisherman");
        this.setLayout(null);
        this.setResizable(false);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setVisible(true);
        this.setLocationRelativeTo(null);
        
        GamePanel gamePanel = new GamePanel(windowWidth, windowHeight, playerName);

        gamePanel.setFocusable(true);

        this.add(gamePanel);
        
        
    }

}