import javax.swing.*;
import java.awt.*;
import java.util.Scanner;

public class GraphingCalculator extends JComponent {
    static int units = 25;
    static int plotPointSize = 5;
    static int min;
    static int max;

    public static void main(String[] args) {
        JFrame frame = new JFrame("Graphing Calulator");
        GraphingCalculator shapes = new GraphingCalculator();
        Scanner kb = new Scanner(System.in);

        System.out.print("Min Range (X): ");
        min = Integer.parseInt(kb.nextLine());

        System.out.print("Min Range (X): ");
        max = Integer.parseInt(kb.nextLine());

        frame.setSize(800, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLocationRelativeTo(null);
        frame.add(shapes);
        frame.setVisible(true);

        kb.close();
    }

    public void paint(Graphics g) {
        int xOffset = getWidth()/2;
        int yOffset = getHeight()/2;

        // Draw Grid
        g.setColor(new Color(0, 0, 0, 100));
        g.drawLine(xOffset, 0, xOffset, getHeight());
        g.drawLine(0, yOffset, getWidth(), yOffset);

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

        // Plot Points
        g.setColor(new Color(0, 0, 255));
        for (int x = min; x <= max; x++) {
            g.fillRect(x * units + xOffset - plotPointSize/2, -f(x) * units + yOffset - plotPointSize/2, plotPointSize, plotPointSize);
        }
    }

    public static int f(int x) {
        return x;
    }
}
