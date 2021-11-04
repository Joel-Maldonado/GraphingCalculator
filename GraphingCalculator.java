import javax.swing.*;
import java.awt.*;

public class GraphingCalculator extends JComponent {
    static int height = 800;
    static int width = 800;
    static int units = 25;
    static int plotPointSize = units / 5;
    static double graphLineFrequency = 0.001; // 1 is a normal plot point

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

        int min = -width / units / 2 - 1;
        int max = width / units / 2 + 1;
        
        int xOffset = width / 2;
        int yOffset = height / 2;
        
        // Draw Grid
        g.setColor(Color.black);

        // Dark Center lines
        g.drawLine(xOffset, 0, xOffset, height); // Vertical x = 0
        g.drawLine(0, yOffset, width, yOffset); // Horizontal y = 0
        
        // Rest of grid
        g.setColor(new Color(0, 0, 0, 30));

        // Vertical
        for (int x = 0; x * units + xOffset < width; x++) {
            g.drawLine(convertToGraphUnitsX(x), 0, convertToGraphUnitsX(x), height); // Pos
            g.drawLine(convertToGraphUnitsX(-x), 0, convertToGraphUnitsX(-x), height); // Neg
        }
        
        // Horizontal
        for (int y = 0; convertToGraphUnitsY(-y) < height; y++) {
            g.drawLine(0, convertToGraphUnitsY(y), width, convertToGraphUnitsY(y));
            g.drawLine(0, convertToGraphUnitsY(-y), width, convertToGraphUnitsY(-y));
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
        
        // Plot points
        g.setColor(Color.blue);
        for (int x = min; x <= max; x++) {
            g.fillRect(convertToGraphUnitsX(x) - plotPointSize/2, convertToGraphUnitsY(f(x)) - plotPointSize/2, plotPointSize, plotPointSize);
        }

        // Draw graph line
        for (double x = min; x <= max; x += graphLineFrequency) {
            g.drawLine(convertToGraphUnitsX(x), convertToGraphUnitsY(f(x)), convertToGraphUnitsX(x + graphLineFrequency), convertToGraphUnitsY(f(x+graphLineFrequency)));
        }
    }

    public static int convertToGraphUnitsX(double xCoord) {
        return (int)(Math.round(xCoord * units + width / 2.0));
    }

    public static int convertToGraphUnitsY(double yCoord) {
        return (int)(Math.round(-yCoord * units + height / 2.0));
    }

    public static double f(double x) {
        return Math.sin(x);
    }
}
