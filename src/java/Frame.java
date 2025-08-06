
import java.awt.Dimension;
import java.awt.Toolkit;

import javax.swing.JFrame;

public class Frame extends JFrame {

    private DrawingCanvas displayPanel;

    public Frame() {

        Dimension size = Toolkit.getDefaultToolkit().getScreenSize();

        setSize(size);
        setExtendedState(MAXIMIZED_BOTH);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setUndecorated(true);

        displayPanel = new DrawingCanvas(size);

        double gradientLength = GlobalVariables.MAX_ITERATIONS;
        FractalGradient gradient = new FractalGradient(gradientLength);
        ScreenUpdater.createUpdater(gradient, displayPanel);

        add(displayPanel);

        ScreenUpdater.getUpdater().update();

        setVisible(true);
    }

    public static void launch() {
        new Frame();
    }

    public static void main(String[] args) {
        launch();
    }

}
