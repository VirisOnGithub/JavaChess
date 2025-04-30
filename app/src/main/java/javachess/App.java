package javachess;

import javax.swing.*;
import java.awt.*;

public class App extends JFrame {
    public static void main(String[] args) {
        new App();
    }

    public App() {
        setTitle("Java Chess");
        setIconImage(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/white-king.png")));
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 600);

        JLabel principalLabel = new JLabel(new ImageIcon(Toolkit.getDefaultToolkit().getImage(Window.class.getResource("/background.jpeg"))));
        principalLabel.setFont(new Font("Arial", Font.BOLD, 24));
        principalLabel.setPreferredSize(new Dimension(800, 600));

        add(principalLabel);


        setLocationRelativeTo(null);
        setVisible(true);
    }
}
