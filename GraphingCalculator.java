import javax.swing.*;
import java.awt.*;

public class GraphingCalculator extends JComponent {
    
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
        
    }
}
