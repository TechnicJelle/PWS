import processing.core.PApplet;
import processing.core.PVector;
//import processing.data.Table;
//import processing.data.TableRow;

public class sketch extends PApplet {
	int scaleFac = 6; // real world scale to pixel scale --> 6 pixels : 1 cm
	int wallThickness = 10 * scaleFac; // wall should be 10 cm thick
	boolean applyResistances = false;

	float k = 0.044f; // air resistance coefficient
	Particle particle;

	RectCollider[] staticColliders = new RectCollider[4];

	//Table table;

	public void settings() {
		size(254*scaleFac + 2*wallThickness, 138*scaleFac + 2*wallThickness);
	}

	public void setup() {
		frameRate(99999999);

		particle = new Particle(this, width/3f, height/2f, 6 * scaleFac);
		particle.applyForce(new PVector(2,1)); // Keep this small! This is kind of the time step (dt). If you want the simulation to run faster, change the frameRate up there

		staticColliders[0] = new RectCollider(this, 0, 0, width, wallThickness);
		staticColliders[0].setHitLines(0, wallThickness, width, wallThickness);

		staticColliders[1] = new RectCollider(this, width-wallThickness, 0, width, height);
		staticColliders[1].setHitLines(width-wallThickness, 0, width-wallThickness, height);

		staticColliders[2] = new RectCollider(this, 0, height-wallThickness, width, height);
		staticColliders[2].setHitLines(width, height-wallThickness, 0, height-wallThickness);

		staticColliders[3] = new RectCollider(this, 0, 0, wallThickness, height);
		staticColliders[3].setHitLines(wallThickness, height, wallThickness, 0);

		// == Table Logging Setup ==
		//table = new Table();
		//table.addColumn("millis");
		//table.addColumn("frameCount");
		//table.addColumn("velMag");
	}

	public void draw() {
		background(66, 141, 66);

		// ==== PHYSICS ====

		// == Hits ==
		// Hit resets for this frame (iteration)
		particle.rcBall.hit = false;
		for(RectCollider sc : staticColliders) {
			sc.hit = false;
		}

		// Hit checking and wall bounces
		for (int i = 0; i < staticColliders.length; i++) {
			if(TestRectOverlap(staticColliders[i], particle.rcBall)) {
				if (i == 0 || i == 2) particle.vel.y *= -1f;
				if (i == 1 || i == 3) particle.vel.x *= -1f;
			}
		}

		// == Resistances ==
		if(applyResistances) {
			{// Force_resistance,air
				PVector Fra = new PVector(
						k * particle.vel.x * abs(particle.vel.x),
						k * particle.vel.y * abs(particle.vel.y));

				particle.applyForce(Fra.mult(-1f / scaleFac));
				//renderForce(Fra, 10000f);
			}

			{// Force_resistance_roll
				PVector Frr = particle.vel.copy().setMag(0.34f);
				//PVector Frr = PVector.fromAngle(particle.vel.heading()).setMag(c);

				particle.applyForce(Frr.mult(-1f / scaleFac));
				//renderForce(Frr, 10000f);
			}
		}

		particle.update();


		// ==== RENDERING ====
		for (RectCollider sc : staticColliders) {
			sc.render();
		}

		for (RectCollider sc : staticColliders) {
			stroke(255);
			strokeWeight(4);
			arrow(sc.hitlineStart.x, sc.hitlineStart.y, sc.hitlineEnd.x, sc.hitlineEnd.y, 10);
		}

		particle.render();
		stroke(0);
		renderForce(particle.vel, 1);

		// == Frame Saving ==
		//saveFrame("/out/frames/take0006/frame-####.png");

		// == Table Logging ==
		//TableRow newRow = table.addRow();
		//newRow.setInt("millis", millis());
		//newRow.setInt("frameCount", frameCount);
		//newRow.setFloat("velMag", particle.vel.mag());
	}

	//public void mousePressed() { saveTable(table, "out/table_highFPS-unlinked.csv"); }

	void renderForce(PVector f, float sclFac) {
		stroke(255);
		strokeWeight(3);
		arrow(particle.pos.x, particle.pos.y,particle.pos.x + sclFac*f.x, particle.pos.y + sclFac*f.y, 5);
	}

	void arrow(float x1, float y1, float x2, float y2, float s) {
		//From: https://processing.org/discourse/beta/num_1219607845.html
		line(x1, y1, x2, y2);
		pushMatrix();
		translate(x2, y2);
		float a = atan2(x1-x2, y2-y1);
		rotate(a);
		line(0, 0, -s, -s);
		line(0, 0, s, -s);
		popMatrix();
	}

	boolean TestRectOverlap(RectCollider a, RectCollider b) {
		//TestAABBOverlap from https://www.toptal.com/game/video-game-physics-part-ii-collision-detection-for-solid-objects
		float d1x = b.pos1.x - a.pos2.x;
		float d1y = b.pos1.y - a.pos2.y;
		float d2x = a.pos1.x - b.pos2.x;
		float d2y = a.pos1.y - b.pos2.y;

		if (d1x > 0f || d1y > 0f)
			return false;

		if(d2x > 0f || d2y > 0f)
			return false;

		a.hit = true;
		b.hit = true;
		return true;
	}
	boolean Intersect2(PVector line_r1s, PVector line_r1e, PVector line_r2s, PVector line_r2e){
		//From https://youtu.be/7Ik2vowGcU0?t=1727
		float h = (line_r2e.x - line_r2s.x) * (line_r1s.y - line_r1e.y) - (line_r1s.x - line_r1e.x) * (line_r2e.y - line_r2s.y);
		float t1 = ((line_r2s.y - line_r2e.y) * (line_r1s.x - line_r2s.x) + (line_r2e.x - line_r2s.x) * (line_r1s.y - line_r2s.y)) / h;
		float t2 = ((line_r1s.y - line_r1e.y) * (line_r1s.x - line_r2s.x) + (line_r1e.x - line_r1s.x) * (line_r1s.y - line_r2s.y)) / h;
		return t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f;
	}

	public static void main(String[] args) {
		PApplet.main("sketch");
	}
}
