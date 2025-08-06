
import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;

public class MandelbrotWorkerCopy implements Runnable {

    private FractalGradient gradient;
    private DrawingCanvas canvas;
    private FloatPoint minPoint2D, maxPoint2D, minScreenPoint2D, maxScreenPoint2D;
    private float divisonFactor;

    public MandelbrotWorkerCopy(FractalGradient gradient, DrawingCanvas canvas, FloatPoint minPoint2D,
            FloatPoint maxPoint2D, FloatPoint minScreenPoint2D, FloatPoint maxScreenPoint2D, int divisonFactor) {
        this.gradient = gradient;
        this.canvas = canvas;
        this.minPoint2D = minPoint2D;
        this.maxPoint2D = maxPoint2D;
        this.minScreenPoint2D = minScreenPoint2D;
        this.maxScreenPoint2D = maxScreenPoint2D;
        this.divisonFactor = divisonFactor;
    }

    @Override
    public void run() {
        float dx = (maxPoint2D.getX() - (minPoint2D.getX())) / (canvas.getWidth() / divisonFactor);
        float dy = (maxPoint2D.getY() - (minPoint2D.getY())) / (canvas.getHeight());

        for (float y = minScreenPoint2D.getY(); y < maxScreenPoint2D.getY(); y++) {
            float realY = minPoint2D.getY() + (y - minScreenPoint2D.getY()) * dy;
            for (float x = minScreenPoint2D.getX(); x < maxScreenPoint2D.getX(); x++) {
                float realX = minPoint2D.getX() + (x - minScreenPoint2D.getX()) * dx;
                checkDivergence(realX, realY, (int) x, (int) y);
            }
        }
    }

    private void checkDivergence(float x0, float y0, int Px, int Py) {

        float x = 0, y = 0, x2 = 0, y2 = 0;
        int iterations = 0;

        while (x2 + y2 <= 4 && iterations < GlobalVariables.MAX_ITERATIONS) {
            y = (x + x) * y + y0;
            x = x2 - y2 + x0;
            x2 = x * x;
            y2 = y * y;
            iterations++;

            canvas.setColor(Px, Py, gradient.getColors(iterations));
        }
    }
}
