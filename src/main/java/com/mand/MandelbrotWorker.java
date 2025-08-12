package com.mand;

import java.awt.geom.Point2D;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

import javax.swing.SwingUtilities;

public class MandelbrotWorker implements Runnable {

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

        double[] coordX = precomputeCoordinates(minPoint2D.getX(), maxPoint2D.getX());
        double[] coordY = precomputeCoordinates(minPoint2D.getY(), maxPoint2D.getY());

        int startY = (int) minScreenPoint2D.getY();
        int startX = (int) minScreenPoint2D.getX();
        int screenDX = (int) (maxScreenPoint2D.getX() - startX);

        if (screenDX > coordX.length) {
            canvas.reset();
            return;
        }

        Set<MandelbrotResult> results = new HashSet<>();

        for (int y = startY; y < (int)maxScreenPoint2D.getY(); y++) {
            for (int x = startX; x < (int) maxScreenPoint2D.getX(); x++) {
                MandelbrotResult result = new MandelbrotResult(x, y, coordX[x - startX], coordY[y - startY], 0, 0, 0);
                results.add(result);
            }
        }

        while (!results.isEmpty() && MandelbrotThreadController.isRunning.get()) {
            Set<MandelbrotResult> escaped = new HashSet<>();
            for (MandelbrotResult result : results) {
                boolean wasEscaped = checkDivergence(result);
                if (wasEscaped) {
                    escaped.add(result);
                }
            }
            canvas.repaint();
            results.removeAll(escaped);
        }
    }

    private double[] precomputeCoordinates(double min, double max) {
        double[] coords = new double[(int) Math.ceil((max - min) / dx)];
        double val = min;
        for (int i = 0; i < coords.length; i++) {
            coords[i] = val;
            val += dx;
        }
        return coords;
    }

    private boolean checkDivergence(MandelbrotResult result) {

        double x = result.getX(), y = result.getY(), x2 = x * x, y2 = y * y;
        double iterations = result.getIterations();
        double maxIterations = GlobalVariables.MAX_ITERATIONS + iterations;

        while (x2 + y2 <= 4 && iterations < maxIterations) {
            y = (x + x) * y + result.getY0();
            x = x2 - y2 + result.getX0();
            x2 = x * x;
            y2 = y * y;
            iterations++;

        }
        result.setX(x);
        result.setY(y);
        result.setIterations(iterations);
        canvas.setIterations(result.getPx(), result.getPy(), iterations, maxIterations,result);

        return iterations < maxIterations;
    }

}
