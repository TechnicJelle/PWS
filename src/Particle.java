import processing.core.PApplet;
import processing.core.PVector;

public class Particle {
	private final PApplet sketch;

	PVector pos;
	PVector vel;
	PVector acc;

	RectCollider rcBall;

	float dm;
	float r;

	Particle(PApplet sketch, float x, float y, float d) {
		this.sketch = sketch;
		pos = new PVector(x, y);
		vel = new PVector(0f, 0f);
		acc = new PVector(0f, 0f);
		dm = d;
		r = dm/2f;
		rcBall = new RectCollider(sketch, x-r, y-r, x+r,y+r);
	}

	void applyForce(PVector f) {
		acc.add(f);
	}

	void update() {
		vel.add(acc);
		if (vel.mag() < 0.01f) {
			vel.x = 0f;
			vel.y = 0f;
		}
		pos.add(vel);
		acc.mult(0f);
		rcBall.move(vel);
	}

	void render() {
		//sketch.fill(rcBall.hit ? rcBall.hitColor : sketch.color(238, 40, 53));
		sketch.fill(238, 40, 53);
		sketch.noStroke();
		sketch.circle(pos.x, pos.y, dm);
	}
}
