
import java.awt.geom.Point2D;
import java.util.ArrayList;
import java.util.List;

public class MandelbrotThreadController {

    private static final int THREAD_COUNT = 10;
    private FractalGradient gradient;
    private DrawingCanvas canvas;

    public MandelbrotThreadController(FractalGradient gradient, DrawingCanvas canvas) {
        this.gradient = gradient;
        this.canvas = canvas;
    }

    public void update() throws InterruptedException {
        // gradient.updateGradientSize(GlobalVariables.MAX_ITERATIONS);
        Point2D minPoint2D = canvas.minPoint2D();
        Point2D maxPoint2D = canvas.maxPoint2D();
        System.out.println(minPoint2D);
        System.out.println(maxPoint2D);
        runThreads(minPoint2D, maxPoint2D);
    }

    private void runThreads(Point2D minPoint2D, Point2D maxPoint2D) throws InterruptedException {
        List<Thread> threadList = new ArrayList<>();
        double screenDX = (double) (canvas.getWidth()) / THREAD_COUNT;
        double dX = (double) (maxPoint2D.getX() - minPoint2D.getX()) / THREAD_COUNT;
        Point2D localMin2D = minPoint2D, localMax2D = null, localMinScreen2D = new Point2D.Double(0, 0), localMaxScreen2D = null;

        for (int i = 0; i < THREAD_COUNT - 1; i++) {
            localMax2D = new Point2D.Double(localMin2D.getX() + dX, maxPoint2D.getY());
            localMaxScreen2D = new Point2D.Double(localMinScreen2D.getX() + screenDX, canvas.getHeight());

            Thread thread = new Thread(new MandelbrotWorker(canvas, localMin2D, localMax2D, localMinScreen2D, localMaxScreen2D, THREAD_COUNT));
            threadList.add(thread);

            thread.start();

            localMin2D = new Point2D.Double(localMax2D.getX(), minPoint2D.getY());
            localMinScreen2D = new Point2D.Double(localMaxScreen2D.getX(), 0);

        }

        Thread thread = new Thread(new MandelbrotWorker(canvas, localMin2D, maxPoint2D, localMinScreen2D, new Point2D.Double(canvas.getWidth(), canvas.getHeight()), THREAD_COUNT));
        threadList.add(thread);

        thread.start();

        for (Thread thread1 : threadList) {
            thread1.join();
        }

    }

}
