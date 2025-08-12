package com.mand;

import java.awt.Color;

public class ColorTool {
    private static final double LOG2 = Math.log(2);
    private static final int HISTOGRAM_SIZE = 1024;
    private static final int GRADIENT_SCALE = 256;
    private static final FractalGradient GRADIENT = new FractalGradient(HISTOGRAM_SIZE);

    private static int interpolate(int v1, int v2, float f) {
        return (int) ((1 - f) * v1 + f * v2);
    }

    public static Color getHSBColor(double iterations, double maxIterations) {
        float hue = (float) (Math.pow(iterations / maxIterations,1.5) % 1.0f);
        return Color.getHSBColor(hue, 1f, 1f);
    }

    public static Color getColor(double iterations, double maxIterations, MandelbrotResult result) {
        if (iterations >= maxIterations) {
            return Color.black;
        }

        switch (GlobalVariables.COLOR_MODE) {
            case HSB:
                return getHSBColor(iterations,maxIterations);
            case GRADIENT:
                return getGradient(result);
        }
        return null;
    }

    /**
     * Based on the answer from article at <a href="https://stackoverflow.com/questions/16500656/which-color-gradient-is-used-to-color-mandelbrot-in-wikipedia">...</a>
     * @param result
     * @return
     */
    private static Color getGradient(MandelbrotResult result) {
        double log = Math.log(result.getX()*result.getX()+result.getY()*result.getY())/2;
        double n = Math.log (log / LOG2) / LOG2;

        int colorI = (int)(Math.sqrt((int)result.getIterations() + 10 - n) * GRADIENT_SCALE) % HISTOGRAM_SIZE;

        return GRADIENT.getColors(colorI);
    }


}
