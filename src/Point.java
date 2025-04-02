import processing.core.PApplet;

public class Point{
    public float x; // X position
    public float y; // Y position
    public float r; // Radius
    public float mass; // Mass
    public float angle; // Angle
    private final PApplet p;
    public float acc;
    public float vel;

    public Point(PApplet p, float x1, float y1, float r1, float m1, float a1){
        this.x = x1;
        this.y = y1;
        this.r = r1;
        this.mass = m1;
        this.angle = a1;
        this. p = p;
        this.acc = 0;
        this.vel = 0;
    }

    public void draw(int r, int g, int b){
        p.fill(r, g, b);
        p.ellipse(x, y, mass, mass);
    }

}
