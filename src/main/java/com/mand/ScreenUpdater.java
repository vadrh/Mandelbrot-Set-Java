package com.mand;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenUpdater {

    private static ScreenUpdater updater;
    private MandelbrotThreadController controller;
    private Thread worker = new Thread();
    private static AtomicBoolean isRunning = new AtomicBoolean(false);

    public static void createUpdater(DrawingCanvas canvas) {
        if (updater == null) {
            updater = new ScreenUpdater(canvas);
        }
    }

    public static ScreenUpdater getUpdater() {
        return updater;
    }

    private ScreenUpdater(DrawingCanvas canvas) {
        this.controller = new MandelbrotThreadController(canvas);
    }

    public void update() {
        System.out.println("test");
        if (isRunning.get()) {
            return;
        }
        isRunning.set(true);

        if (worker != null) {
            worker.interrupt();
            System.out.println("state: " + worker.getState());
            System.out.println("worker before reset " + worker.isAlive());
            controller.reset();
            System.out.println("worker after reset " + worker.isAlive());
        }
        System.out.println(Thread.activeCount());
        GlobalVariables.CURRENT_MAX_ITERATIONS = GlobalVariables.MAX_ITERATIONS;
        worker = new Thread(() -> {
            try {
                controller.update();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        worker.start();

        isRunning.set(false);

    }

}
