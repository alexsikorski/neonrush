package game.neonrush.Utilities;

public class Vector2D {

    private float x;
    private float y;

    public Vector2D(float X, float Y) {
        x = X;
        y = Y;
    }

    public float mag() {
        return (float) Math.sqrt(x * x + y * y); // magnifies
    }

    public Vector2D mult(float n) { // multiplies
        x *= n;
        y *= n;
        return this;
    }

    public Vector2D div(float n) { // divides
        x /= n;
        y /= n;
        return this;
    }

    public Vector2D normalize() { // sets it back to normal for further calculations
        float m = mag();
        if (m != 0 && m != 1) {
            div(m);
        }
        return this;
    }

    public float magSq() { //magnitude square root
        return (x * x + y * y);
    }

    public Vector2D limit(float max) { // limits the speed
        if (magSq() > max * max) {
            normalize();
            mult(max);
        }
        return this;
    }


    public Vector2D setMag(float len) { // sets magnitude
        normalize();
        mult(len);
        return this;
    }


    public Vector2D add(Vector2D v) { // add vectors
        x = x + v.x;
        y = y + v.y;
        return this;
    }

    public Vector2D sub(Vector2D v) { // remove vectors
        x = x - v.x;
        y = y - v.y;
        return this;
    }

    public int getX() { // gets x value
        return (int) x;
    }

    public int getY() { // gets y value
        return (int) y;
    }
}
