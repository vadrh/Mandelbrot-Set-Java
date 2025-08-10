package com.mand;

import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class ScreenUpdater {

    private static ScreenUpdater updater;
    private MandelbrotThreadController controller;

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
        try {
            controller.update();
        } catch (InterruptedException ex) {

        }

    }

}
