import java.awt.*;
import java.awt.geom.*;

/**
 * The `Cloud` class represents a cloud object that can be drawn on a Graphics2D object.
 */
public class Cloud implements drawObject<Graphics2D> {

    private double x;
    private double y;
    private double size;
    private Color color;

    /**
     * Constructs a new Cloud object with the specified position, size, and color.
     *
     * @param x     The x-coordinate of the cloud's position.
     * @param y     The y-coordinate of the cloud's position.
     * @param size  The size of the cloud.
     * @param color The color of the cloud.
     */
    public Cloud(int x, int y, int size, Color color) {

        this.x = Double.valueOf(x);
        this.y = Double.valueOf(y);
        this.size = Double.valueOf(size);
        this.color = color;

    }

    /**
     * Draws the cloud on the specified Graphics2D object.
     *
     * @param g2d The Graphics2D object on which to draw the cloud.
     */
    @Override
    public void draw(Graphics2D g2d) {

        Ellipse2D.Double e1 = new Ellipse2D.Double(x, y, size * 0.90, size * 0.90);
        Ellipse2D.Double e2 = new Ellipse2D.Double(x + size*0.35, y - size * 0.20, size * 1.75, size * 1.75);
        Ellipse2D.Double e3 = new Ellipse2D.Double(x + size*1.50, y + size * 0.15, size * 0.90, size * 0.90);
        Ellipse2D.Double e4 = new Ellipse2D.Double(x + size*1.80, y + size * 0.05, size * 0.30, size * 0.30);

        g2d.setColor(color);

        g2d.fill(e1);
        g2d.fill(e2);
        g2d.fill(e3);
        g2d.fill(e4);

    }
    

}
