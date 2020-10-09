import processing.core.PApplet;
import processing.core.PVector;

import java.time.format.TextStyle;

public class Particle {
    private final PApplet sketch;

    private final PVector pos;
    private final PVector vel;
    private final PVector acc;

    RectCollider rcball;

    float dm;
    float r;

    public Particle(PApplet sketch, float x, float y) {
        this.sketch = sketch;
        pos = new PVector(x, y);
        vel = new PVector(0, 0);
        acc = new PVector(0, 0);
        dm = 16;
        r=dm/2;
        rcball = new RectCollider(sketch, x-r, y-r, x+r,y+r);
    }

    public void applyForce(PVector f) {
        acc.add(f);
    }

    public void update() {
        vel.add(acc);
        pos.add(vel);
        acc.mult(0);
        rcball.move(vel);
    }
    public void render() {

        //sketch.fill(255,0,0);
        sketch.noStroke();
        sketch.circle(pos.x, pos.y, dm);
    }
}
