
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.geom.Point2D;

import javax.swing.JFrame;

public class Frame extends JFrame {

    public Frame() {

        Dimension size = new Dimension(500,500);

        setSize(size);
        setLayout(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        DrawingCanvas displayPanel = new DrawingCanvas(size);

        double gradientLength = GlobalVariables.MAX_ITERATIONS;
        FractalGradient gradient = new FractalGradient(gradientLength);
        ScreenUpdater.createUpdater(gradient, displayPanel);

        add(displayPanel);

        ScreenUpdater.getUpdater().update();

        setVisible(true);

        displayPanel.runZoom(new Point2D.Double(-1.74999841099374081749002483162428393452822172335808534616943930976364725846655540417646727085571962736578151132907961927190726789896685696750162524460775546580822744596887978637416593715319388030232414667046419863755743802804780843375,
                -0.00000000000000165712469295418692325810961981279189026504290127375760405334498110850956047368308707050735960323397389547038231194872482690340369921750514146922400928554011996123112902000856666847088788158433995358406779259404221904755));
    }

    public static void launch() {
        new Frame();
    }

    public static void main(String[] args) {
        launch();
    }

}
