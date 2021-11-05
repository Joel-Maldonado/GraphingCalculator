import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

public class GraphingCalculator extends JComponent implements KeyListener {
    static int height = 800;
    static int width = 800;
    static int units = 25;
    static int plotPointSize;
    static char key;
    static int xCenterOffset;
    static int yCenterOffset;
    static double graphLineFrequency = 0.01; // 0.01 = 100 lines per point

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
        
        int xOffset = width / 2;
        int yOffset = height / 2;
        
        // Draw Grid
        g.setColor(Color.black);

        // Dark Center lines
        g.drawLine(0, yOffset + yCenterOffset, width, yOffset + yCenterOffset); // Horizontal black center line
        g.drawLine(xOffset + xCenterOffset, 0 , xOffset + xCenterOffset, height); // Vertical black center line
        
        // Rest of grid
        g.setColor(new Color(0, 0, 0, 30));

        // Vertical gray lines
        for (int x = -xOffset; convertToGraphUnitsX(x) < width; x++) {
            g.drawLine(x * units + xOffset, 0, x * units + xOffset, height);
        }
        
        // Horizontal gray lines
        for (int y = -yOffset; convertToGraphUnitsY(-y) < height; y++) {
            g.drawLine(0, convertToGraphUnitsY(y), width, convertToGraphUnitsY(y));
        }

        // Draw coord labels
        g.setColor(Color.black);
        g.setFont(g.getFont().deriveFont(units/2.2f));

        // Horizontal coord labels
        for (int x = -xOffset; x <= xOffset; x += units) {
            // Vertical line will have 0th value to stop overlap
            if (Math.round((double) (x - xCenterOffset)/units) == 0) continue;
            System.out.println(units);
            g.drawString("" + Math.round((double)(x - xCenterOffset)/ units), x + xOffset, yOffset + yCenterOffset); 
        }

        // Vertical coord labels
        for (int y = -yOffset; y < height; y += units) {
            g.drawString("" + Math.round(-(double)(y - yCenterOffset)/ units), xOffset + xCenterOffset, y + yOffset - units/3);
        }
        
        // Plot points
        for (int x = -xOffset; x <= xOffset; x++) {
            g.setColor(Color.blue);
            plotPoint(g, x, f(x));

            g.setColor(Color.red);
            plotPoint(g, x, h(x));

            g.setColor(Color.green);
            plotPoint(g, x, g(x));
        }

        // Draw graph lines
        for (double x = -xOffset; x <= xOffset; x += graphLineFrequency) {
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
        g.fillRect(convertToGraphUnitsX(x) + xCenterOffset - plotPointSize/2, convertToGraphUnitsY(fx) + yCenterOffset - plotPointSize/2, plotPointSize, plotPointSize);
    }

    public static void plotLines(Graphics g, double x, double fx, double fXGraphlinefreq) {
        g.drawLine(convertToGraphUnitsX(x) + xCenterOffset, convertToGraphUnitsY(fx) + yCenterOffset, convertToGraphUnitsX(x + graphLineFrequency) + xCenterOffset, convertToGraphUnitsY(fXGraphlinefreq) + yCenterOffset);
    }

    public static double f(double x) {
        return 5;
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
            units += 4;
            xCenterOffset = 0;
            yCenterOffset = 0;
            System.out.println("Zoom in");
        }
        if (key == '-' && units >= 6) {
            units -= 4;
            xCenterOffset = 0;
            yCenterOffset = 0;
            System.out.println("Zoom out");
        }
        if (key == 'd') {
            System.out.println("right");
            xCenterOffset -= units;
        }
        if (key == 'a') {
            System.out.println("left");
            xCenterOffset += units;
        }
        if (key == 'w') {
            yCenterOffset += units;
        }
        if (key == 's') {
            yCenterOffset -= units;
        }
        repaint();
    }
}
