import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.CORNERS;

public class RectCollider {
	private final PApplet sketch;

	PVector pos1;
	PVector pos2;

	boolean hit = false;
	int hitColor;
	int noHitColor;

	RectCollider(PApplet sketch, float x1, float y1, float x2, float y2) {
		this.sketch = sketch;
		pos1 = new PVector(x1, y1);
		pos2 = new PVector(x2, y2);
		hitColor = sketch.color(255);
		noHitColor = sketch.color(139, 85, 36);
	}

	void move(PVector shove) {
		pos1.add(shove);
		pos2.add(shove);
	}

	void render() {
		sketch.noStroke();
		sketch.rectMode(CORNERS);
		sketch.fill(hit ? hitColor : noHitColor);
		sketch.rect(pos1.x * sketch.width, pos1.y * sketch.height,
					pos2.x * sketch.width, pos2.y * sketch.height);
	}
}
