import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
    private final PApplet sketch;

    private final PVector pos;

    public Particle(PApplet sketch, float x, float y) {
        this.sketch = sketch;
        pos = new PVector(x, y);
    }

    public void update () {
        pos.add(PVector.random2D());
    }

    public void render() {
        sketch.noStroke();
        sketch.circle(pos.x, pos.y, 16);
    }
}
