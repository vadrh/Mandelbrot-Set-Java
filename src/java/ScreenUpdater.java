
public class ScreenUpdater {

    private static ScreenUpdater updater;
    private MandelbrotThreadController controller;

    public static void createUpdater(FractalGradient gradient, DrawingCanvas canvas) {
        if (updater == null) {
            updater = new ScreenUpdater(gradient, canvas);
        }
    }

    public static ScreenUpdater getUpdater() {
        return updater;
    }

    private ScreenUpdater(FractalGradient gradient, DrawingCanvas canvas) {
        this.controller = new MandelbrotThreadController(gradient, canvas);
    }

    public void update() {

        try {
            controller.update();
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
    }

}
