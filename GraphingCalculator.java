import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class GraphingCalculator extends JComponent {
    static int height = 800;
    static int width = 800;
    static int units = 25;
    static int plotPointSize = units / 5;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphing Calculator");
        GraphingCalculator shapes = new GraphingCalculator();

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(shapes);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        width = getWidth();
        height = getHeight();

        int min = -width / units / 2 - 2;
        int max = width / units / 2 + 2;
        
        int xOffset = width / 2;
        int yOffset = height / 2;
        
        // Draw Grid
        g.setColor(new Color(0, 0, 0, 100));

        // Dark Center lines
        g.drawLine(xOffset, 0, xOffset, height); // Vertical x = 0
        g.drawLine(0, yOffset, width, yOffset); // Horizontal y = 0

        // Vertical
        g.setColor(new Color(0, 0, 0, 30));
        for (int x = 0; x * units + xOffset < width; x++) {
            g.drawLine(x * units + xOffset, 0, x * units + xOffset, height); // Pos
            g.drawLine(-x * units + xOffset, 0, -x * units + xOffset, height); // Neg
        }
        
        // Horizontal
        for (int y = 0; y * units + yOffset < height; y++) {
            g.drawLine(0, y * units + yOffset, width, y * units + yOffset);
            g.drawLine(0, -y * units + yOffset, width, -y * units + yOffset);
        }

        // Draw coord labels
        g.setColor(Color.black);
        for (int y = 0; y <= max * units; y += units) { // Vertical
            g.drawString(String.valueOf(-y / units), xOffset, y + yOffset + units/2); // Pos
            g.drawString(String.valueOf(y / units), xOffset, -y + yOffset + units/2); // Neg
        }

        for (int x = 0; x <= max * units; x += units) { // Horizontal
            g.drawString(String.valueOf(-x / units), -x + xOffset, yOffset + units/2); // Pos
            g.drawString(String.valueOf(x / units), x + xOffset, yOffset + units/2); // Neg
        }

        g.setColor(Color.blue);
        
        // Plot Points
        for (int x = min; x <= max; x++) {
            g.fillRect(x * units + xOffset - plotPointSize/2, (int)Math.round(-f(x) * units) + yOffset - plotPointSize/2, plotPointSize, plotPointSize);
        }

        // Draw graph line
        for (int x = min; x <= max * units; x++) {
            g.drawLine(x * units + xOffset, (int)Math.round(-f(x) * units) + yOffset, units * (x + 1) + xOffset, (int)Math.round(-f(x+1) * units) + yOffset);
        }
    }

    public static double f(double x) {
        return (x * x) * (1.0/2.0 * x) - 5;
    }
}
