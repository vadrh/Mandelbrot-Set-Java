
import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

public class DrawingCanvas extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {

    private double zoom = 1;
    private double minX, minY, maxX, maxY;

    public Point2D minPoint2D() {
        return new Point2D.Double(minX, minY);
    }

    public Point2D maxPoint2D() {
        return new Point2D.Double(maxX, maxY);
    }

    private Color[][] colors;

    public DrawingCanvas(Dimension size) {
        setSize(size);
        setLayout(null);

        minX = -2;
        minY = (float) -1.125;

        maxX = 2;
        maxY = (float) 1.125;

        colors = new Color[this.getHeight() + 1][this.getWidth() + 1];

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    public void setColors(Color[][] colors) {
        this.colors = colors;
    }

    public void setColor(int x, int y, Color color) {
        colors[y][x] = color;
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        super.paint(g2);
        BufferedImage image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        for (int y = 0; y < getHeight(); y++) {
            for (int x = 0; x < getWidth(); x++) {
                image.setRGB(x, y, colors[y][x].getRGB());
            }
        }

        g2.drawImage(image, 0, 0, null);
    }

    public void reset() {
        GlobalVariables.MAX_ITERATIONS = 100;
        zoom = 1;
        minX = -2;
        minY = (float) -1.125;

        maxX = 2;
        maxY = (float) 1.125;

        calculateValues();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int value = e.getWheelRotation();
        double zoomFactor = 2;
        if (value < 0) {
            double x = e.getX();
            double y = e.getY();

            zoom(x, y, zoomFactor);
        } else if (value > 0) {
            double x = e.getX();
            double y = e.getY();

            zoomFactor = 1 / zoomFactor;
            zoom(x, y, zoomFactor);
        }
        zoom *= zoomFactor;
        calculateValues();
        System.out.println(zoom);
        System.out.println(GlobalVariables.MAX_ITERATIONS);
    }

    private void calculateValues() {
        GlobalVariables.MAX_ITERATIONS = 300 * Math.pow((Math.log(zoom)), 1.5) + 100;
        ScreenUpdater.getUpdater().update();
        repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (e.getButton() == MouseEvent.BUTTON2) {
            System.exit(0);
        } else if (e.getButton() == MouseEvent.BUTTON3) {
            reset();
        }

    }

    private void zoom(double x, double y, double zoomFactor) {
        double centerX = minX + (x / ((double) getWidth())) * (maxX - minX);
        double centerY = minY + ((y) / ((double) getHeight())) * (maxY - minY);

        double width = (maxX - minX) / zoomFactor;
        double height = (maxY - minY) / zoomFactor;

        minX = centerX - width / 2;
        minY = centerY - height / 2;

        maxX = centerX + width / 2;
        maxY = centerY + height / 2;
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

}
