package com.mand;

import java.awt.*;

public class GlobalVariables {
    public static final double ZOOM_FACTOR = 2;
    public static final double THREAD_COUNT = 1;
    //Mostly affects transitioning, the higher it is, the less transitioning there will be, however initial load will be longer
    public static double MAX_ITERATIONS = 1;
    public static final COLOR_SETTING COLOR_MODE = COLOR_SETTING.GRADIENT;
    public static final Dimension RESOLUTION = Toolkit.getDefaultToolkit().getScreenSize();

    public enum COLOR_SETTING {
        GRADIENT,
        HSB;
    }
}
