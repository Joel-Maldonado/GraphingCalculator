import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphingCalculator extends JComponent implements KeyListener {
    static int height = 800;
    static int width = 800;
    static int units = 30;
    static int plotPointSize;
    static int min;
    static int max;
    static char key;
    static double graphLineFrequency = 0.01; // 0-1, 1 is 1 line per plot point

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphing Calculator");
        GraphingCalculator shapes = new GraphingCalculator();

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(shapes);
        frame.addKeyListener(shapes);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        width = getWidth();
        height = getHeight();

        plotPointSize = units / 6;

        min = -Math.max(width, height) / units;
        max = Math.max(width, height) / units;
        
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
        for (int x = -xOffset; convertToGraphUnitsX(x) < width; x++) {
            g.drawLine(convertToGraphUnitsX(x), 0, convertToGraphUnitsX(x), height);
        }
        
        // Horizontal
        for (int y = 0; convertToGraphUnitsY(-y) < height; y++) {
            g.drawLine(0, convertToGraphUnitsY(y), width, convertToGraphUnitsY(y));
            g.drawLine(0, convertToGraphUnitsY(-y), width, convertToGraphUnitsY(-y));
        }

        for (int y = -yOffset; convertToGraphUnitsY(-y) < height; y++) {
            g.drawLine(0, convertToGraphUnitsY(y), width, convertToGraphUnitsY(y));
        }

        // Draw coord labels
        g.setColor(Color.black);
        g.setFont(g.getFont().deriveFont(units/2.2f));

        // Horizontal
        for (int x = units; x < width/2 + units; x += units) {
            g.drawString("" + x/units, x + width/2, yOffset); // Pos
            g.drawString("" + -x/units, -x + width/2, yOffset); // Neg
        }

        // Vertical
        for (int y = 0; y < height/2; y += units) {
            g.drawString("" + -y/units, xOffset, y + height/2); // Pos
            g.drawString("" + y/units, xOffset, -y + height/2); // Neg
        }

        g.setFont(g.getFont().deriveFont(12f));
        
        // Plot points
        for (int x = -xOffset; x <= xOffset; x++) {
            g.setColor(Color.blue);
            plotPoint(g, x, f(x));

            g.setColor(Color.red);
            plotPoint(g, x, h(x));

            g.setColor(Color.green);
            plotPoint(g, x, g(x));
        }

        // Draw graph line
        for (double x = min; x <= max; x += graphLineFrequency) {
            g.setColor(Color.blue);
            plotLines(g, x, f(x), f(x+graphLineFrequency));

            g.setColor(Color.red);
            plotLines(g, x, h(x), h(x+graphLineFrequency));

            g.setColor(Color.green);
            plotLines(g, x, g(x), g(x+graphLineFrequency));
        }
    }

    public static int convertToGraphUnitsX(double xCoord) {
        return (int)(Math.round(xCoord * units + width / 2.0));
    }

    public static int convertToGraphUnitsY(double yCoord) {
        return (int)(Math.round(-yCoord * units + height / 2.0));
    }

    public static void plotPoint(Graphics g, double x, double fx) {
        g.fillRect(convertToGraphUnitsX(x) - plotPointSize/2, convertToGraphUnitsY(fx) - plotPointSize/2, plotPointSize, plotPointSize);
    }

    public static void plotLines(Graphics g, double x, double fx, double fXGraphlinefreq) { // f(x+graphLineFrequency)
        g.drawLine(convertToGraphUnitsX(x), convertToGraphUnitsY(fx), convertToGraphUnitsX(x + graphLineFrequency), convertToGraphUnitsY(fXGraphlinefreq));
    }

    public static double f(double x) {
        return x;
    }

    public static double h(double x) {
        return x * 0.5 - 5;
    }

    public static double g(double x) {
        return (x * x) + (2 * x) - 3;
    }

    public void keyPressed(KeyEvent e) { }
    public void keyReleased(KeyEvent e) { }
    public void keyTyped(KeyEvent e) {
        key = e.getKeyChar();
        if (key == '+') {
            units += 1;
            System.out.println("Zoom in");
        }
        if (key == '-' && units - 1 > 5) {
            units -= 1;
            System.out.println("Zoom out");
        }
        repaint();
    }
}
