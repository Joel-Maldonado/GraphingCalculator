import javax.swing.*;
import java.awt.*;

public class GraphingCalculator extends JComponent {
    static int units = 30;
    static int plotPointSize = units / 5;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphing Calulator");
        GraphingCalculator shapes = new GraphingCalculator();

        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(shapes);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        int min = -getWidth() / units / 2;
        int max = getWidth() / units / 2;
        
        int xOffset = getWidth() / 2;
        int yOffset = getHeight() / 2;
        
        // Draw Grid
        g.setColor(new Color(0, 0, 0, 100));

        // Dark Center lines
        g.drawLine(xOffset, 0, xOffset, getHeight()); // Vertical x = 0
        g.drawLine(0, yOffset, getWidth(), yOffset); // Horizontal y = 0

        // Vertical
        g.setColor(new Color(0, 0, 0, 30));
        for (int x = 0; x * units + xOffset < getWidth(); x++) {
            g.drawLine(x * units + xOffset, 0, x * units + xOffset, getHeight()); // Pos
            g.drawLine(-x * units + xOffset, 0, -x * units + xOffset, getHeight()); // Neg
        }
        
        // Horizontal
        for (int y = 0; y * units + yOffset < getHeight(); y++) {
            g.drawLine(0, y * units + yOffset, getWidth(), y * units + yOffset);
            g.drawLine(0, -y * units + yOffset, getWidth(), -y * units + yOffset);
        }

        // Draw coord labels
        g.setColor(Color.black);
        for (int y = 0; y <= getHeight() / units / 2; y++) { // Vertical
            g.drawString(String.valueOf(-y), xOffset, y * units + yOffset + units/2); // Pos
            g.drawString(String.valueOf(y), xOffset, -y * units + yOffset + units/2); // Neg
        }

        for (int x = 0; x <= getWidth() / units / 2; x++) { // Horizontal
            g.drawString(String.valueOf(-x), -x * units + xOffset, yOffset + units/2); // Pos
            g.drawString(String.valueOf(x), x * units + xOffset, yOffset + units/2); // Neg
        }

        // Plot Points
        g.setColor(Color.blue);
        for (int x = min; x <= max; x++) {
            g.fillRect(x * units + xOffset - plotPointSize/2, (int)Math.round(-f(x) * units) + yOffset - plotPointSize/2, plotPointSize, plotPointSize);
        }
    }

    public static double f(double x) {
        return x / 2;
    }
}
