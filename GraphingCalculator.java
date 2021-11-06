/*

--Features--
Graphing lines
Coordinate labels
Coordinate plane
Zooming in and out
Moving the graph
Mouse hovering label

--Keys--
Zoom out: -
Zoom in: +

Move Up: w
Move Down: s
Move Left: a
Move Right: d

*/

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionListener;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class GraphingCalculator extends JComponent implements KeyListener, MouseMotionListener {
    static int height = 800;
    static int width = 800;
    static int units = Math.round(width / 20.5f);
    static int yCenterOffsetMultipler = 0;
    static int xCenterOffsetMultipler = 0;
    static int xOffset;
    static int yOffset;
    static int plotPointSize;
    static int xCenterOffset;
    static int yCenterOffset;
    static double graphLineFrequency;
    static double mousePosX;
    static double mousePosY;
    static char key;

    public static void main(String[] args) {
        final JFrame frame = new JFrame("Graphing Calculator");
        final GraphingCalculator shapes = new GraphingCalculator();

        frame.setSize(width, height);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(shapes);
        frame.addKeyListener(shapes);
        frame.addMouseMotionListener(shapes);
        frame.setVisible(true);
    }

    public void paint(Graphics g) {
        width = getWidth();
        height = getHeight();

        plotPointSize = units / 6;
        xOffset = Math.round(width / 2f);
        yOffset = Math.round(height / 2f);
        graphLineFrequency = (double) 0.4 / units; // More accurate if zoomed in, less accurate when zoomed out
        xCenterOffset = units * xCenterOffsetMultipler;
        yCenterOffset = units * yCenterOffsetMultipler;

        // Draw Grid
        g.setColor(new Color(0, 0, 0, 120));

        // Dark Center lines
        g.drawLine(0, yOffset + yCenterOffset, width, yOffset + yCenterOffset); // Horizontal black center line
        g.drawLine(xOffset + xCenterOffset, 0, xOffset + xCenterOffset, height); // Vertical black center line

        // Rest of grid
        g.setColor(new Color(0, 0, 0, 30));

        // Vertical gray lines
        for (int x = 0; x <= width / 2; x += units) {
            g.drawLine(x + xOffset + units, 0, x + xOffset + units, height);
            g.drawLine(-x + xOffset, 0, -x + xOffset, height);
        }

        // Horizontal gray lines
        for (int y = 0; y < height / 2; y += units) {
            g.drawLine(0, y + units + yOffset, width, y + units + yOffset);
            g.drawLine(0, -y + yOffset, width, -y + yOffset);
        }

        // Draw coord labels
        g.setColor(Color.black);
        g.setFont(g.getFont().deriveFont(units / 2.2f));

        int answer;
        // Horizontal coord labels
        for (int x = -xOffset - xCenterOffset - units; x <= xOffset - xCenterOffset + units; x += units) {
            answer = (int) Math.round((double) (x + xCenterOffset) / units);

            // Don't plot 0th coord. Vertical line will have the 0th value to stop overlap.
            if (answer + xCenterOffset / units == 0)
                continue;

            g.drawString("" + -(answer + xCenterOffset / units), xOffset + -answer * units, yOffset + yCenterOffset);
        }

        // Vertical coord labels
        for (int y = -yOffset - yCenterOffset - units; y < height / 2 - yCenterOffset + units; y += units) {
            answer = -(int) Math.round((double) (y + yCenterOffset) / units);
            g.drawString("" + (answer + yCenterOffset / units), xOffset + xCenterOffset, yOffset + -answer * units);
        }

        // Plot points
        for (int x = -xOffset - units; x <= xOffset + units; x++) {
            g.setColor(Color.blue);
            plotPoint(g, x, f(x));

            g.setColor(Color.red);
            plotPoint(g, x, g(x));

            g.setColor(Color.green);
            plotPoint(g, x, h(x));
        }

        // Draw graph lines
        for (double x = -xOffset; x <= xOffset; x += graphLineFrequency) {
            g.setColor(Color.blue);
            plotLines(g, x, f(x), f(x + graphLineFrequency));

            g.setColor(Color.red);
            plotLines(g, x, g(x), g(x + graphLineFrequency));

            g.setColor(Color.green);
            plotLines(g, x, h(x), h(x + graphLineFrequency));
        }

        // Mouse coord label
        g.setFont(g.getFont().deriveFont(25f));
        g.setColor(Color.black);
        drawCurrentMouseCoordLabel(g);
    }

    public static int convertToGraphUnitsX(double xCoord) {
        return (int) (Math.round(xCoord * units + width / 2.0));
    }

    public static int convertToGraphUnitsY(double yCoord) {
        return (int) (Math.round(-yCoord * units + height / 2.0));
    }

    public static void plotPoint(Graphics g, double x, double fx) {
        if (convertToGraphUnitsY(fx) + yCenterOffset > height || convertToGraphUnitsY(fx) + yCenterOffset < 0)
            return; // Not in render range

        g.fillRect(convertToGraphUnitsX(x) + xCenterOffset - plotPointSize / 2,
                convertToGraphUnitsY(fx) + yCenterOffset - plotPointSize / 2, plotPointSize, plotPointSize);
    }

    public static void plotLines(Graphics g, double x, double fx, double fXGraphlinefreq) {
        if (convertToGraphUnitsY(fx) + yCenterOffset > height || convertToGraphUnitsY(fx) + yCenterOffset < 0)
            return; // Not in render range

        g.drawLine(convertToGraphUnitsX(x) + xCenterOffset, convertToGraphUnitsY(fx) + yCenterOffset,
                convertToGraphUnitsX(x + graphLineFrequency) + xCenterOffset,
                convertToGraphUnitsY(fXGraphlinefreq) + yCenterOffset);
    }

    public static void drawCurrentMouseCoordLabel(Graphics g) {
        final double x = mousePosX - xCenterOffset - xOffset;
        final double y = mousePosY - yCenterOffset - yOffset;

        g.drawString("(X: " + Math.round(x / units) + " Y: " + Math.round(-y / units) + ")", 5, 25);
    }

    public static double f(double x) {
        return x; // y = x
    }

    public static double g(double x) {
        return x / 3 - 7; // Linear Function
    }

    public static double h(double x) {
        return (x * x) - (3 * x) - 5; // Quadratic Function
    }

    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {
        key = e.getKeyChar();
        if (key == '+') {
            units += 1;
        }
        if (key == '-' && units > 5) { // Gets very laggy if you go below 5 pixels/unit
            units -= 1;
        }
        if (key == 'd') {
            xCenterOffsetMultipler--;
        }
        if (key == 'a') {
            xCenterOffsetMultipler++;
        }
        if (key == 'w') {
            yCenterOffsetMultipler++;
        }
        if (key == 's') {
            yCenterOffsetMultipler--;
        }
        repaint();
    }

    public void mouseDragged(MouseEvent e) {}
    public void mouseMoved(MouseEvent e) {
        e = SwingUtilities.convertMouseEvent(e.getComponent(), e, this);
        Point relativePoint = new Point(e.getPoint());

        mousePosX = relativePoint.getX();
        mousePosY = relativePoint.getY();

        repaint();
    }
}
