package com.mand;

import java.awt.Color;

public class ColorTool {

    private static final COLOR_SETTING COLOR_MODE = COLOR_SETTING.GRADIENT;
    private static final int HISTOGRAM_SIZE = 150;
    private static final FractalGradient GRADIENT = new FractalGradient(HISTOGRAM_SIZE);

    public static Color linearInterpolate(Color c1, Color c2, float f) {
        int r = interpolate(c1.getRed(), c2.getRed(), f);
        int g = interpolate(c1.getGreen(), c2.getGreen(), f);
        int b = interpolate(c1.getBlue(), c2.getBlue(), f);

        return new Color(r, g, b);
    }

    private static int interpolate(int v1, int v2, float f) {
        return (int) ((1 - f) * v1 + f * v2);
    }

    public static Color getHSBColor(double iterations, double maxIterations, double hueCycle) {
        float hue = (float) ((iterations / maxIterations * hueCycle) % 1f);
        return Color.getHSBColor((float)(iterations)%255f, 1f, 1f);
    }

    public static Color getColor(double iterations, double maxIterations) {
        if (iterations >= maxIterations) {
            return Color.black;
        }

        switch (COLOR_MODE) {
            case HSB:
                return getHSB(iterations,maxIterations);
            case GRADIENT:
                return getGradient((int) iterations);
        }
        return null;
    }

    private static Color getGradient(int iterations) {
        return GRADIENT.getColors(iterations%HISTOGRAM_SIZE);
    }

    private static Color getHSB(double iterations, double maxIterations) {
        return ColorTool.getHSBColor(iterations, maxIterations, 2);
    }

    enum COLOR_SETTING {
        GRADIENT,
        HSB;
    }

}
