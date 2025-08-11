package com.mand;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenUpdater {

    private static ScreenUpdater updater;
    private MandelbrotThreadController controller;
    private static Thread worker = new Thread();
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
        if (isRunning.get()) {
            return;
        }
        isRunning.set(true);

        if (worker != null) {
            controller.reset();
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
