import processing.core.PApplet;
import processing.core.PVector;

import static processing.core.PConstants.CORNERS;

public class RectCollider {
	private final PApplet sketch;

	PVector pos1;
	PVector pos2;

	PVector hitlineStart;
	PVector hitlineEnd;

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

	void setHitLines(float ax, float ay, float bx, float by) {
		hitlineStart = new PVector(ax, ay);
		hitlineEnd = new PVector(bx, by);
	}

	void move(PVector shove) {
		pos1.add(shove);
		pos2.add(shove);
	}

	void render() {
		sketch.noStroke();
		sketch.rectMode(CORNERS);
		sketch.fill(hit ? hitColor : noHitColor);
		sketch.rect(pos1.x, pos1.y, pos2.x, pos2.y);
	}
}
