package com.mand;

import java.util.concurrent.atomic.AtomicBoolean;

public class ScreenUpdater {

    private static ScreenUpdater updater;
    private MandelbrotThreadController controller;
    private static Thread worker = new Thread();

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

        if (worker != null) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        worker = new Thread(() -> {
            try {
                controller.update();
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
        });
        worker.start();

    }

}
