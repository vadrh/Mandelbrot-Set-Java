package com.mand;

import java.awt.geom.Point2D;
import java.awt.Color;

public class MandelbrotWorker implements Runnable {

    private static final double LN2 = Math.log(2);
    private final COLOR_SETTING COLOR_MODE = COLOR_SETTING.HSB;
    private DrawingCanvas canvas;
    private FractalGradient gradient;
    private Point2D minPoint2D, maxPoint2D, minScreenPoint2D, maxScreenPoint2D;
    private double dx;

    public MandelbrotWorker(DrawingCanvas canvas, FractalGradient gradient, Point2D minPoint2D,
            Point2D maxPoint2D, Point2D minScreenPoint2D, Point2D maxScreenPoint2D) {
        this.canvas = canvas;
        this.gradient = gradient;
        this.minPoint2D = minPoint2D;
        this.maxPoint2D = maxPoint2D;
        this.minScreenPoint2D = minScreenPoint2D;
        this.maxScreenPoint2D = maxScreenPoint2D;
        this.dx = (maxPoint2D.getY() - (minPoint2D.getY())) / (canvas.getHeight());
    }

    @Override
    public void run() {
        double[] coordX = precompute_coordinates(minPoint2D.getX(), maxPoint2D.getX());
        double[] coordY = precompute_coordinates(minPoint2D.getY(), maxPoint2D.getY());

        int startY = (int) minScreenPoint2D.getY();
        int startX = (int) minScreenPoint2D.getX();

        if (maxScreenPoint2D.getX() - startX > coordX.length) {
            canvas.reset();
        }

        for (int y = startY; y < maxScreenPoint2D.getY(); y++) {
            for (int x = startX; x < maxScreenPoint2D.getX(); x++) {
                checkDivergence(coordX[x - startX], coordY[y - startY], x, y);
            }
        }
    }

    private double[] precompute_coordinates(double min, double max) {
        double[] coords = new double[(int) Math.ceil((max - min) / dx)];
        double val = min;
        for (int i = 0; i < coords.length; i++) {
            coords[i] = val;
            val += dx;
        }
        return coords;
    }

    private void checkDivergence(double x0, double y0, int Px, int Py) {

        double x = 0, y = 0, x2 = 0, y2 = 0;
        double iterations = 0;

        while (x2 + y2 <= 4 && iterations < GlobalVariables.MAX_ITERATIONS) {
            y = (x + x) * y + y0;
            x = x2 - y2 + x0;
            x2 = x * x;
            y2 = y * y;
            iterations++;

        }

        Color color = null;
        if (iterations < GlobalVariables.MAX_ITERATIONS) {
            double log_zn = Math.log(x2 + y2) / 2;
            double nu = Math.log(log_zn / Math.log(2)) / LN2;
            iterations += 1 - nu;

            color = getColor(iterations);
        } else {
            color = Color.BLACK;
        }
        canvas.setColor(Px, Py, color);
    }

    private Color getColor(double iterations) {
        switch (COLOR_MODE) {
            case HSB:
                return getHSB(iterations);
            case GRADIENT:
                return getGradient(iterations);
        }
        return null;
    }

    private Color getGradient(double iterations) {
        Color c1 = gradient.getColors((int) iterations);
        Color c2 = gradient.getColors(((int) (iterations)) + 1);
        return ColorTool.linearInterpolate(c1, c2, (float) (iterations % 1));
    }

    private Color getHSB(double iterations) {
        return ColorTool.getHSBColor(iterations, GlobalVariables.MAX_ITERATIONS, 2);
    }

    enum COLOR_SETTING {
        GRADIENT,
        HSB;
    }
}
