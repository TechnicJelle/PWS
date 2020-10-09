import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.CORNERS;

public class RectCollider {
    private final PApplet sketch;

    public final PVector pos1;
    public final PVector pos2;

    public RectCollider(PApplet sketch, float x1, float y1, float x2, float y2) {
        this.sketch = sketch;
        pos1 = new PVector(x1, y1);
        pos2 = new PVector(x2, y2);
    }

    public void update() {}

    public void render() {
        sketch.noStroke();
        sketch.rectMode(CORNERS);
        sketch.rect(pos1.x, pos1.y, pos2.x, pos2.y);
    }
}
