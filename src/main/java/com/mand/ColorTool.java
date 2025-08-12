package com.mand;

import java.awt.Color;

public class ColorTool {
    private static final double LOG2 = Math.log(2);
    private static final COLOR_SETTING COLOR_MODE = COLOR_SETTING.GRADIENT;
    private static final int HISTOGRAM_SIZE = 256;
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

    public static Color getHSBColor(double iterations) {
        return Color.getHSBColor((float)(iterations)%255f, 1f, 1f);
    }

    public static Color getColor(double iterations, double maxIterations, MandelbrotResult result) {
        if (iterations >= maxIterations) {
            return Color.black;
        }

        switch (COLOR_MODE) {
            case HSB:
                return getHSB(iterations);
            case GRADIENT:
                return getGradient(result);
        }
        return null;
    }

    private static Color getGradient(MandelbrotResult result) {
        double log_zn = Math.log(result.getX()*result.getX()+result.getY()*result.getY());
        double nu = Math.log(log_zn / LOG2) / LOG2;

        Color c1 = GRADIENT.getColors((int) (result.getIterations()%(HISTOGRAM_SIZE)));
        Color c2 = GRADIENT.getColors((int) ((result.getIterations()+1)%(HISTOGRAM_SIZE)));
        float f = (float) ((result.getIterations())+1-nu);
        return linearInterpolate(c1,c2,f%1);
    }

    private static Color getHSB(double iterations) {
        return ColorTool.getHSBColor(iterations);
    }

    enum COLOR_SETTING {
        GRADIENT,
        HSB;
    }

}
