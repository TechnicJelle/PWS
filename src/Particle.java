import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
    private final PApplet sketch;

    private final PVector pos;
    private final PVector vel;
    private final PVector acc;

    public Particle(PApplet sketch, float x, float y) {
        this.sketch = sketch;
        pos = new PVector(x, y);
        vel = new PVector(0, 0);
        acc = new PVector(0, 0);
    }

    public void applyForce(PVector f) {
        acc.add(f);
    }

    public void update() {
        vel.add(acc);
        pos.add(vel);
        acc.mult(0);
    }

    public void render() {
        sketch.fill(255,0,0);
        sketch.noStroke();
        sketch.circle(pos.x, pos.y, 16);
    }
}
