package com.mand;

import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;

public class MandelbrotThreadController {

    private List<Thread> threadList = new ArrayList<>(GlobalVariables.THREAD_COUNT);
    private List<MandelbrotWorker> workerList = new ArrayList<>();
    private DrawingCanvas canvas;
    protected static AtomicBoolean isRunning = new AtomicBoolean(false);

    public MandelbrotThreadController(DrawingCanvas canvas) {
        this.canvas = canvas;
    }

    public void update() throws InterruptedException {
        reset();

        Point2D minPoint2D = canvas.minPoint2D();
        Point2D maxPoint2D = canvas.maxPoint2D();
        runThreads(minPoint2D, maxPoint2D);
    }

    public synchronized void reset() {
        if(!isRunning.get())
            return;

        isRunning.set(false);

        for(Thread thread :threadList) {
            try {
                thread.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        threadList.clear();
        workerList.clear();
    }

    private void runThreads(Point2D minPoint2D, Point2D maxPoint2D) throws InterruptedException {
        isRunning.set(true);
        double screenDX = (double) (canvas.getWidth()) / GlobalVariables.THREAD_COUNT;
        double dX = (maxPoint2D.getX() - minPoint2D.getX()) / GlobalVariables.THREAD_COUNT;

        Point2D localMin2D = minPoint2D, localMax2D = null, localMinScreen2D = new Point2D.Double(0, 0), localMaxScreen2D = null;

        for (int i = 0; i < GlobalVariables.THREAD_COUNT; i++) {
            localMax2D = new Point2D.Double(localMin2D.getX() + dX, maxPoint2D.getY());
            localMaxScreen2D = new Point2D.Double(localMinScreen2D.getX() + screenDX, canvas.getHeight());

            MandelbrotWorker worker = new MandelbrotWorker(canvas, localMin2D, localMax2D, localMinScreen2D, localMaxScreen2D);

            Thread thread = new Thread(worker, String.valueOf(i + 1));
            threadList.add(thread);
            workerList.add(worker);

            thread.start();

            localMin2D = new Point2D.Double(localMax2D.getX(), minPoint2D.getY());
            localMinScreen2D = new Point2D.Double(localMaxScreen2D.getX(), 0);

        }

    }

}
