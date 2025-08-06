
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
        Color[] colors = new Color[]{new Color(10, 7, 100), new Color(50, 150, 200), new Color(200, 238, 30), new Color(237, 255, 255), new Color(255, 170, 0), Color.black};
        float[] fractions = new float[]{0.0f, 0.14f, 0.36f, 0.62f, 0.9425f, 0.98f};
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
