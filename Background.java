import javax.swing.JComponent;
import java.awt.*;

/**
 * The `Background` class represents the background of the game and includes clouds and water.
 */
public class Background extends JComponent {
 
    private Cloud c1;
    private Cloud c2;
    private Cloud c3;
    private Cloud c4;
    private Water water;

    /**
     * Constructs a new Background object with the specified width and height.
     *
     * @param width  The width of the background.
     * @param height The height of the background.
     */
    public Background(int width, int height) {
        
        this.c1 = new Cloud(width / 10, 60, 70, new Color(174, 214, 241));
        this.c2 = new Cloud(width / 3, 50, 60, new Color(169, 204, 227));
        this.c3 = new Cloud(width / 2 , 50, 50, new Color(170, 206, 230));
        this.c4 = new Cloud(width / 2 + 275, 100, 75, new Color(172, 208, 225)); 
        this.water = new Water(width, height, (int) (height * 0.6), 15, 0.03, new Color (0, 240, 240, 70));
        this.setBounds(0, 75, width , height - 75);
        
    }

    /**
     * Paints the background component, including clouds and water, on the specified Graphics object.
     *
     * @param g The Graphics object on which to paint the background.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        Graphics2D g2d = (Graphics2D) g;

        // Makes the images look smoother:
        RenderingHints rh = new RenderingHints(
            RenderingHints.KEY_ANTIALIASING,
            RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHints(rh);

        /**
         * Call the drawing function of clouds:
         */
        c1.draw(g2d);
        c2.draw(g2d);
        c3.draw(g2d);
        c4.draw(g2d);

         /**
         * Call the drawing function of water:
         */
        water.draw(g2d);

    }


}

