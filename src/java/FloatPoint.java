
public class FloatPoint {

    private float x;
    private float y;

    public FloatPoint(float x, float y) {
        this.x = x;
        this.y = y;
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public String toString() {
        return "[" + x + ", " + y + "]";
    }

}
