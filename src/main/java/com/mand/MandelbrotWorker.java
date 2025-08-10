package com.mand;

import java.awt.geom.Point2D;
import java.lang.reflect.InvocationTargetException;
import java.util.HashSet;
import java.util.Set;
import java.awt.Color;

import javax.swing.SwingUtilities;

public class MandelbrotWorker implements Runnable {

    private static final double LN2 = Math.log(2);
    private DrawingCanvas canvas;
    private Point2D minPoint2D, maxPoint2D, minScreenPoint2D, maxScreenPoint2D;
    private double dx;

    public MandelbrotWorker(DrawingCanvas canvas, Point2D minPoint2D,
            Point2D maxPoint2D, Point2D minScreenPoint2D, Point2D maxScreenPoint2D) {
        this.canvas = canvas;
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
            return;
        }

        for (int y = startY; y < maxScreenPoint2D.getY(); y++) {
            for (int x = startX; x < maxScreenPoint2D.getX(); x++) {
                checkDivergence(coordX[x-startX], coordY[y-startY], x, y);
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

    private boolean checkDivergence(double x0, double y0, int Px, int Py) {

        double x = 0, y = 0, x2 = 0, y2 = 0;
        double iterations = 0;
        double maxIterations = GlobalVariables.MAX_ITERATIONS;

        while (x2 + y2 <= 4 && iterations < maxIterations) {
            y = (x + x) * y + y0;
            x = x2 - y2 + x0;
            x2 = x * x;
            y2 = y * y;
            iterations++;

        }

        if (iterations < maxIterations) {
            double log_zn = Math.log(x2 + y2) / 2;
            double nu = Math.log(log_zn / Math.log(2)) / LN2;
            iterations += 1 - nu;

        }
        canvas.setIterations(Px, Py, iterations,maxIterations);

        return iterations < maxIterations;
    }
}
