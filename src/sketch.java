import processing.core.PApplet;
import processing.core.PVector;
//import processing.data.Table;
//import processing.data.TableRow;

public class sketch extends PApplet {
	int scaleFac = 6; // real world scale to pixel scale --> 6 pixels : 1 cm
	int wallThickness = 10 * scaleFac; // wall should be 10 cm thick
	boolean applyResistances = false ;
	boolean useOwnMethod = true ;

	// == Constants ==
	float k = 0.044f; // air resistance coefficient
	float e = 1.0f;   // COR (coefficient of restitution)
	float c = 0.34f;  // roll resistance magnitude

	Particle particle;

	RectCollider[] staticColliders = new RectCollider[4];

//	Table table;

	public void settings() {
		size(254*scaleFac + 2*wallThickness, 138*scaleFac + 2*wallThickness);
	}

	public void setup() {
		frameRate(99999999);

		particle = new Particle(this, width/3f, height/2f, 6 * scaleFac);
		particle.applyForce(new PVector(2,1)); // Keep this small! This is kind of the time step (dt). If you want the simulation to run faster, change the frameRate up there

		float r = particle.r;
		staticColliders[0] = new RectCollider(this, 0, 0, width, wallThickness);
		staticColliders[0].setHitLines(0, wallThickness+r, width, wallThickness+r);

		staticColliders[1] = new RectCollider(this, width-wallThickness, 0, width, height);
		staticColliders[1].setHitLines(width-wallThickness-r, 0, width-wallThickness-r, height);

		staticColliders[2] = new RectCollider(this, 0, height-wallThickness, width, height);
		staticColliders[2].setHitLines(width, height-wallThickness-r, 0, height-wallThickness-r);

		staticColliders[3] = new RectCollider(this, 0, 0, wallThickness, height);
		staticColliders[3].setHitLines(wallThickness+r, height, wallThickness+r, 0);

		// == Table Logging Setup ==
//		table = new Table();
//		table.addColumn("millis");
//		table.addColumn("frameCount");
//		table.addColumn("velMag");
//		table.addColumn("fps");
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

		if(useOwnMethod) {// Own method
			for (int i = 0; i < staticColliders.length; i++) {
				RectCollider sc = staticColliders[i];
				PVector hitPoint = Intersect3(sc.hitlineStart, sc.hitlineEnd, particle.pos, PVector.add(particle.pos, particle.vel));
				if (hitPoint != null) {
					executeParticleBounce(i);
					//particle.pos = hitPoint;
				}
			}
		} else {//AABB
			for (int i = 0; i < staticColliders.length; i++) {
				if (TestRectOverlap(staticColliders[i], particle.rcBall)) {
					executeParticleBounce(i);
				}
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
				PVector Frr = particle.vel.copy().setMag(c);

				particle.applyForce(Frr.mult(-1f / scaleFac));
				//renderForce(Frr, 10000f);
			}
		}

		particle.update();


		// ==== RENDERING ====
		for (RectCollider sc : staticColliders) {
			sc.render();
		}

//		for (RectCollider sc : staticColliders) {
//			stroke(255);
//			strokeWeight(4);
//			arrow(sc.hitlineStart.x, sc.hitlineStart.y, sc.hitlineEnd.x, sc.hitlineEnd.y, 10f);
//		}

		particle.render();
//		stroke(0, 255, 0);
//		//renderForce(new PVector(particle.r, particle.r), 1f);
//		stroke(0, 0, 255);
//		//renderForce(particle.vel, 10f);
//		{
//			stroke(255,255,0);
//			PVector temp = particle.vel.copy();
//			temp.setMag(particle.r).add(particle.pos);
//			arrow(temp, PVector.add(temp, particle.vel), 0);
//		}

		// == Frame Saving ==
		//saveFrame("/out/frames/take0006/frame-####.png");

		// == Table Logging ==
//		TableRow newRow = table.addRow();
//		newRow.setInt("millis", millis());
//		newRow.setInt("frameCount", frameCount);
//		newRow.setFloat("velMag", particle.vel.mag());
//		newRow.setFloat("fps", frameRate);
//
//		if(frameCount >= 5000) {
//			saveTable(table, "out/table_aabb.csv");
//			exit();
//		}
	}

	//public void mousePressed() { saveTable(table, "out/table_aabb.csv"); }

	private void executeParticleBounce(int i) {
		// i is the index of the wall array.
		// It lets the code know which wall has just gotten hit

		if (i == 0 || i == 2) particle.vel.y *= -e; //top/bottom wall
		if (i == 1 || i == 3) particle.vel.x *= -e; //left/right wall
	}

	void renderForce(PVector f, float sclFac) {
		arrow(particle.pos, PVector.add(particle.pos, PVector.mult(f, sclFac)), 5);
	}

	void arrow(PVector a, PVector b, float s) {
		arrow(a.x, a.y, b.x, b.y, s);
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
		float h = (line_r2e.x - line_r2s.x) * (line_r1s.y - line_r1e.y)
				- (line_r1s.x - line_r1e.x) * (line_r2e.y - line_r2s.y);
		float t1 = ((line_r2s.y - line_r2e.y) * (line_r1s.x - line_r2s.x)
				 + (line_r2e.x - line_r2s.x) * (line_r1s.y - line_r2s.y)) / h;
		float t2 = ((line_r1s.y - line_r1e.y) * (line_r1s.x - line_r2s.x)
				 + (line_r1e.x - line_r1s.x) * (line_r1s.y - line_r2s.y)) / h;

		return t1 >= 0.0f && t1 < 1.0f && t2 >= 0.0f && t2 < 1.0f;
	}

	PVector Intersect3(PVector line_r1s, PVector line_r1e, PVector line_r2s, PVector line_r2e) {
		//Adapted from https://stackoverflow.com/a/1968345/8109619
		float p0_x = line_r1s.x; float p0_y = line_r1s.y;
		float p1_x = line_r1e.x; float p1_y = line_r1e.y;

		float p2_x = line_r2s.x; float p2_y = line_r2s.y;
		float p3_x = line_r2e.x; float p3_y = line_r2e.y;

		float s1_x, s1_y, s2_x, s2_y;
		s1_x = p1_x - p0_x;     s1_y = p1_y - p0_y;
		s2_x = p3_x - p2_x;     s2_y = p3_y - p2_y;

		float s, t;
		float h = -s2_x * s1_y + s1_x * s2_y;
		s = (-s1_y * (p0_x - p2_x) + s1_x * (p0_y - p2_y)) / h;
		t = ( s2_x * (p0_y - p2_y) - s2_y * (p0_x - p2_x)) / h;

		if (s >= 0 && s <= 1 && t >= 0 && t <= 1)
		{
			// Collision detected
            float i_x = p0_x + (t * s1_x);
            float i_y = p0_y + (t * s1_y);
			return new PVector(i_x, i_y);
		}

		return null; // No collision
	}

	void point(PVector p) {
		point(p.x, p.y);
	}

	public static void main(String[] args) {
		PApplet.main("sketch");
	}
}
