
import java.awt.*;
import java.awt.geom.*;

/**
 * The `Water` class is used to create a water effect for drawing on a graphics context.
 */
public class Water implements drawObject<Graphics2D> {

    private int height;
    private int width;
    private int waveHeight;
    private int waveAmplitude;
    private double waveFrequency;
    private Color color;

    /**
     * Constructs a new Water object with the specified parameters.
     *
     * @param width         The width of the water.
     * @param height        The height of the water.
     * @param waveHeight    The height of the waves in the water.
     * @param waveAmplitude The amplitude of the waves.
     * @param waveFrequency The frequency of the waves.
     * @param color         The color of the water.
     */
    public Water(int width, int height, int waveHeight, int waveAmplitude,
                             double waveFrequency, Color color) {

        this.height = height;
        this.width = width;
        this.waveHeight = waveHeight;
        this.waveAmplitude = waveAmplitude;
        this.waveFrequency = waveFrequency;
        this.color = color;

    }

    /**
     * Draws the water effect on the specified graphics context (Graphics2D).
     *
     * @param g2d The Graphics2D object on which to draw the water effect.
     */
    @Override
    public void draw(Graphics2D g2d) {

        for (int x = 0; x < width; x += 1) {

            double y = waveAmplitude * Math.sin(waveFrequency * x);

            Rectangle2D.Double r1 = new Rectangle.Double(x, height - waveHeight - y, 1, waveHeight + y);

            g2d.setColor(color);
            g2d.fill(r1);

        }

    }

}
