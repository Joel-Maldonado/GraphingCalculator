/*
--Keys--

Zoom out: (-)
Zoom in: (+)

Move Up by 1: (w)
Move Down by 1: (s)
Move Left by 1: (a)
Move Right by 1: (d)

Move Up by 5: (shift + w)
Move Down by 5: (shift + s)
Move Left by 5: (shift + a)
Move Right by 5: (shift + d)

Dark theme: (t)
Reset Center Offset & Zoom: (r)
Hide Mouse Hover Coords: (h)

--Keys--
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
    static JFrame frame;
    static boolean darkTheme = false;
    static boolean showHoverCoords = true;
    static int height = 800;
    static int width = 800;
    static int units = (int) Math.round(width / 21);
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

    public static void main(String[] args) {
        frame = new JFrame("Graphing Calculator");
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
        if (darkTheme)
            g.setColor(new Color(255, 255, 255, 120));
        else
            g.setColor(new Color(0, 0, 0, 120));

        g.drawLine(0, yOffset + yCenterOffset, width, yOffset + yCenterOffset); // Horizontal black center line
        g.drawLine(xOffset + xCenterOffset, 0, xOffset + xCenterOffset, height); // Vertical black center line

        // Rest of grid
        if (darkTheme)
            g.setColor(new Color(255, 255, 255, 30));
        else
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

        // Draw coord labels
        if (darkTheme)
            g.setColor(Color.white);
        else
            g.setColor(Color.black);

        g.setFont(g.getFont().deriveFont(units / 2.2f));

        int answer;
        // Horizontal coord labels
        for (int x = -xOffset - xCenterOffset - units; x <= xOffset - xCenterOffset + units; x += units) {
            answer = (int) Math.round((double) (x + xCenterOffset) / units);

            // Don't plot 0th coord. Vertical line will have the 0th value to stop overlap.
            if (answer + xCenterOffset / units == 0)
                continue;

            g.drawString("" + -(answer + xCenterOffset / units), xOffset + -answer * units - Math.round(units / 6f),
                    yOffset + yCenterOffset);
        }

        // Vertical coord labels
        for (int y = -yOffset - yCenterOffset - units; y < height / 2 - yCenterOffset + units; y += units) {
            answer = -(int) Math.round((double) (y + yCenterOffset) / units);
            g.drawString("" + (answer + yCenterOffset / units), xOffset + xCenterOffset, yOffset + -answer * units);
        }

        // Mouse coord label
        if (showHoverCoords) {
            g.setFont(g.getFont().deriveFont(25f));
            if (darkTheme)
                g.setColor(Color.white);
            else
                g.setColor(Color.black);
            drawCurrentMouseCoordLabel(g);
        }
    }

    private static int convertToGraphUnitsX(double xCoord) {
        return (int) (Math.round(xCoord * units + width / 2.0));
    }

    private static int convertToGraphUnitsY(double yCoord) {
        return (int) (Math.round(-yCoord * units + height / 2.0));
    }

    private static void plotPoint(Graphics g, double x, double fx) {
        if (convertToGraphUnitsY(fx) + yCenterOffset > height || convertToGraphUnitsY(fx) + yCenterOffset < 0)
            return; // Not in render range

        g.fillRect(convertToGraphUnitsX(x) + xCenterOffset - plotPointSize / 2,
                convertToGraphUnitsY(fx) + yCenterOffset - plotPointSize / 2, plotPointSize, plotPointSize);
    }

    private static void plotLines(Graphics g, double x, double fx, double fXGraphlinefreq) {
        if (convertToGraphUnitsY(fx) + yCenterOffset > height || convertToGraphUnitsY(fx) + yCenterOffset < 0)
            return; // Not in render range

        g.drawLine(convertToGraphUnitsX(x) + xCenterOffset, convertToGraphUnitsY(fx) + yCenterOffset,
                convertToGraphUnitsX(x + graphLineFrequency) + xCenterOffset,
                convertToGraphUnitsY(fXGraphlinefreq) + yCenterOffset);
    }

    private static void drawCurrentMouseCoordLabel(Graphics g) {
        final double x = mousePosX - xCenterOffset - xOffset;
        final double y = mousePosY - yCenterOffset - yOffset;

        g.drawString("(X: " + Math.round(x / units) + " Y: " + Math.round(-y / units) + ")", 5, 25);
    }

    private static void switchTheme() {
        darkTheme = !darkTheme;
        if (darkTheme) {
            frame.getContentPane().setBackground(Color.black);
        } else {
            frame.getContentPane().setBackground(new Color(238, 238, 238)); // Default background color
        }
    }

    private static double f(double x) {
        return x; // y = x
    }

    private static double g(double x) {
        return x / 3 - 7; // Linear Function
    }

    private static double h(double x) {
        return (x * x) - (3 * x) - 5; // Quadratic Function
    }

    public void keyPressed(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {}
    public void keyTyped(KeyEvent e) {
        switch(e.getKeyChar()) {
            case '+': // Zooming
                units += 1;
                break;
            case '-':
                units -= 1;
                break;
            case 'w': // Movement
                yCenterOffsetMultipler++;
                break;
            case 'a':
                xCenterOffsetMultipler++;
                break;
            case 's':
                yCenterOffsetMultipler--;
                break;
            case 'd':
                xCenterOffsetMultipler--;
                break;
            case 'W': // Holding shift
                yCenterOffsetMultipler += 5;
                break;
            case 'A':
                xCenterOffsetMultipler += 5;
                break;
            case 'S':
                yCenterOffsetMultipler -= 5;
                break;
            case 'D':
                xCenterOffsetMultipler -= 5;
                break;
            case 't': // Dark Theme
                switchTheme();
                break;
            case 'r': // Reset zoom / center offset
                yCenterOffsetMultipler = 0;
                xCenterOffsetMultipler = 0;
                units = (int) Math.round(800 / 21f);
                break;
            case 'h': // Hide hover coords
                showHoverCoords = !showHoverCoords;
                break;
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
