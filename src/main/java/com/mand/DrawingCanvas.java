package com.mand;

import java.awt.AWTException;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.HashSet;
import java.util.Set;

import javax.swing.JPanel;

public class DrawingCanvas extends JPanel implements MouseWheelListener, MouseMotionListener, MouseListener {

    private BufferedImage image;
    private double zoom = 1;
    private double minX, minY, maxX, maxY;
    private double zoomAnimationValue = 1;
    private boolean animationRunning = false;

    public Point2D minPoint2D() {
        return new Point2D.Double(minX, minY);
    }

    public Point2D maxPoint2D() {
        return new Point2D.Double(maxX, maxY);
    }

    public DrawingCanvas(Dimension size) {
        setSize(size);
        setLayout(null);

        minX = -2;
        minY = (minX / (getWidth() / 2)) * (getHeight() / 2);

        maxX = 2;
        maxY = (maxX / (getWidth() / 2)) * (getHeight() / 2);

        image = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);

        addMouseWheelListener(this);
        addMouseListener(this);
        addMouseMotionListener(this);

    }

    public void setIterations(int x, int y, double iteration, double maxIterations, MandelbrotResult result) {
        Color color = ColorTool.getColor(iteration, maxIterations,result);
        image.setRGB(x, y, color.getRGB());
    }

    @Override
    public void paint(Graphics g) {

        Graphics2D g2 = (Graphics2D) g;

        super.paint(g2);

        g2.drawImage(image, 0, 0, null);
    }

    public synchronized void syncRepaint() {
        repaint();
    }

    public void reset() {

        zoom = 1;

        minX = -2;
        minY = (minX / (getWidth() / 2)) * (getHeight() / 2);

        maxX = 2;
        maxY = (maxX / (getWidth() / 2)) * (getHeight() / 2);

        zoomAnimationValue = 1;

        animationRunning = false;

        calculateValues();
    }

    @Override
    public void mouseWheelMoved(MouseWheelEvent e) {
        int value = e.getWheelRotation();
        double zoomFactor = 1.1;
        double x = e.getX();
        double y = e.getY();
        if (value > 0) {
            zoomFactor = 1 / zoomFactor;
        }
        zoom(x, y, zoomFactor);
        zoom *= zoomFactor;
    }

    private void calculateValues() {
        ScreenUpdater.getUpdater().update();
    }

    public void runZoom(Point2D zoomPoint) {
        zoomAnimationValue = 1;
        animationRunning = true;
        moveOnPlane(zoomPoint.getX(), zoomPoint.getY());
        while (animationRunning) {
            zoom(getWidth() / 2, getHeight() / 2, zoomAnimationValue);
            this.zoom *= zoomAnimationValue += 0.01d;
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if (animationRunning) {
            return;
        }

        switch (e.getButton()) {
            case MouseEvent.BUTTON1:
                moveTo(e.getX(), e.getY());
                break;
            case MouseEvent.BUTTON2:
                System.exit(0);
            case MouseEvent.BUTTON3:
                reset();
                break;
            default:
                break;
        }

    }

    private void zoomOnPlane(double x, double y, double zoomFactor) {
        double currentWidth = maxX - minX;
        double width = currentWidth / zoomFactor;

        double currentHeight = maxY - minY;
        double height = currentHeight / zoomFactor;

        double dx = (currentWidth - width) / 2;
        double dy = (currentHeight - height) / 2;

        minX += dx;
        minY += dy;
        maxX -= dx;
        maxY -= dy;

        calculateValues();
    }

    private Point2D getClickCoordinates(double x, double y) {
        double realX = minX + x / getWidth() * (maxX - minX);
        double realY = minY + y / getHeight() * (maxY - minY);

        return new Point2D.Double(realX, realY);
    }

    private void zoom(double x, double y, double zoomFactor) {
        Point2D clickCoord = getClickCoordinates(x, y);

        zoomOnPlane(clickCoord.getX(), clickCoord.getY(), zoomFactor);
    }

    private void moveOnPlane(double x, double y) {
        double width = (maxX - minX) / 2;
        double height = (maxY - minY) / 2;

        minX = x - width;
        minY = y - height;

        maxX = x + width;
        maxY = y + height;

        calculateValues();
    }

    private void moveTo(double x, double y) {
        Point2D clickCoord = getClickCoordinates(x, y);

        moveOnPlane(clickCoord.getX(), clickCoord.getY());

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
