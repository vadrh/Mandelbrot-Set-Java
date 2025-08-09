package com.mand;

import java.awt.Color;

public class FractalGradient {

    private Color[] gradientColors;

    public FractalGradient(double size) {
        updateGradientSize(size);
    }

    public void updateGradientSize(double size) {
        gradientColors = new Color[(int) (size + 3)];
        init();
    }

    private void init() {
        Color[] colors = new Color[]{
            new Color(0, 1, 20),
            new Color(205, 200, 80),
            new Color(248, 251, 95),
            new Color(255, 170, 0),
            new Color(204, 128, 0),
            new Color(153, 87, 0),
            new Color(106, 52, 3)
        };
        float[] fractions = {
            0.0f,
            0.16f,
            0.42f,
            0.6425f,
            0.8575f,
            0.95f,
            1.0f
        };
        int size = gradientColors.length;

        for (int i = 0; i < colors.length; i++) {
            Color from = colors[i];
            Color to = i != colors.length - 1 ? colors[i + 1] : colors[0];

            int start = (int) (fractions[i] * (size - 1));
            int end = i != colors.length - 1 ? (int) (fractions[i + 1] * (size - 1)) : size - 1;

            for (int v = start; v <= end; v++) {
                float scalar = ((float) (v - start)) / (end - start);

                int red = (int) ((to.getRed() - from.getRed()) * scalar + from.getRed());
                int green = (int) ((to.getGreen() - from.getGreen()) * scalar + from.getGreen());
                int blue = (int) ((to.getBlue() - from.getBlue()) * scalar + from.getBlue());

                gradientColors[v] = new Color(red, green, blue);

            }
        }
    }

    public Color getColors(int index) {
        return gradientColors[index];
    }

}
