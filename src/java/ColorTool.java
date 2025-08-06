
import java.awt.Color;

public class ColorTool {

    public static Color linearInterpolate(Color c1, Color c2, float f) {
        int r = interpolate(c1.getRed(), c2.getRed(), f);
        int g = interpolate(c1.getGreen(), c2.getGreen(), f);
        int b = interpolate(c1.getBlue(), c2.getBlue(), f);

        return new Color(r, g, b);
    }

    private static int interpolate(int v1, int v2, float f) {
        return (int) ((1 - f) * v1 + f * v2);
    }

    public static Color getHSBColor(double  iterations, double maxIterations, double hueCycle) {
        float hue = (float) ((iterations / maxIterations * hueCycle) % 1.0f);
        return Color.getHSBColor(hue, 1.0f, iterations < maxIterations ? 1.0f : 0.0f);
    }
}
