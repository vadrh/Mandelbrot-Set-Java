
import java.awt.geom.Point2D;
import java.awt.Color;

public class MandelbrotWorker implements Runnable {

    private static final double LN2 = Math.log(2);
    private final COLOR_SETTING COLOR_MODE = COLOR_SETTING.HSB;
    private DrawingCanvas canvas;
    private FractalGradient gradient;
    private Point2D minPoint2D, maxPoint2D, minScreenPoint2D, maxScreenPoint2D;
    private float divisonFactor;

    public MandelbrotWorker(DrawingCanvas canvas, FractalGradient gradient, Point2D minPoint2D,
            Point2D maxPoint2D, Point2D minScreenPoint2D, Point2D maxScreenPoint2D, int divisonFactor) {
        this.canvas = canvas;
        this.gradient = gradient;
        this.minPoint2D = minPoint2D;
        this.maxPoint2D = maxPoint2D;
        this.minScreenPoint2D = minScreenPoint2D;
        this.maxScreenPoint2D = maxScreenPoint2D;
        this.divisonFactor = divisonFactor;
    }

    @Override
    public void run() {
        double dx = (maxPoint2D.getX() - (minPoint2D.getX())) / (canvas.getWidth() / divisonFactor);
        double dy = (maxPoint2D.getY() - (minPoint2D.getY())) / (canvas.getHeight());

        for (double y = minPoint2D.getY(), screenY = minScreenPoint2D.getY(); y < maxPoint2D.getY()
                && screenY < maxScreenPoint2D.getY(); y += dy, screenY++) {
            for (double x = minPoint2D.getX(), screenX = minScreenPoint2D.getX(); x < maxPoint2D.getX()
                    && screenX < maxScreenPoint2D.getX(); x += dx, screenX++) {
                checkDivergence(x, y, (int) screenX, (int) screenY);
            }
        }
    }

    private void checkDivergence(double x0, double y0, int Px, int Py) {

        double x = 0, y = 0, x2 = 0, y2 = 0;
        double iterations = 0;

        while (x2 + y2 <= 4 && iterations < GlobalVariables.MAX_ITERATIONS) {
            y = (x + x) * y + y0;
            x = x2 - y2 + x0;
            x2 = x * x;
            y2 = y * y;
            iterations++;

        }

        Color color = null;
        if (iterations < GlobalVariables.MAX_ITERATIONS) {
            double log_zn = Math.log(x2 + y2) / 2;
            double nu = Math.log(log_zn / Math.log(2)) / LN2;
            iterations += 1 - nu;

            color = getColor(iterations);
        } else {
            color = Color.BLACK;
        }
        canvas.setColor(Px, Py, color);
    }

    private Color getColor(double iterations) {
        switch (COLOR_MODE) {
            case HSB:
                return getHSB(iterations);
            case GRADIENT:
                return getGradient(iterations);
        }
        return null;
    }

    private Color getGradient(double iterations) {
        Color c1 = gradient.getColors((int) iterations);
        Color c2 = gradient.getColors(((int) (iterations)) + 1);
        return ColorTool.linearInterpolate(c1, c2, (float) (iterations % 1));
    }

    private Color getHSB(double iterations) {
        return ColorTool.getHSBColor(iterations, GlobalVariables.MAX_ITERATIONS, 2);
    }

    enum COLOR_SETTING {
        GRADIENT,
        HSB;
    }
}
