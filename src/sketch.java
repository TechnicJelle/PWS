import processing.core.PApplet;
import processing.core.PVector;

public class sketch extends PApplet {
    int scaleFac = 6; // real world scale to pixel scale --> 6 pixels : 1 cm
    int wallThickness = 10 * scaleFac; // wall should be 10 cm thick

    float k = 0.044f; // air resistance coefficient
    Particle particle;

    RectCollider[] staticColliders = new RectCollider[4];

    public void settings() {
        size(254*scaleFac + 2*wallThickness, 138*scaleFac + 2*wallThickness);
    }

    public void setup() {
        frameRate(60);

        particle = new Particle(this, width/3f, height/2f);
        particle.applyForce(new PVector(2,1)); // Keep this small! This is kind of the time step (dt). If you want the simulation to run faster, change the frameRate up there

        staticColliders[0] = new RectCollider(this, 0, 0, width, wallThickness);
        staticColliders[1] = new RectCollider(this, width-wallThickness, 0, width, height);
        staticColliders[2] = new RectCollider(this, 0, height-wallThickness, width, height);
        staticColliders[3] = new RectCollider(this, 0, 0, wallThickness, height);
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

        // Force_resistance,air
        PVector Fra = new PVector(
                k * particle.vel.x * abs(particle.vel.x),
                k * particle.vel.y * abs(particle.vel.y));

        particle.applyForce(Fra.mult(-1));
        //renderForce(Fra, 10000f);

        particle.update();


        // ==== RENDERING ====
        for (RectCollider sc : staticColliders) {
            if (sc != null) sc.render();
        }
        particle.render();

        //saveFrame("/frames/take0004/frame-####.png");
    }

    void renderForce(PVector f, float scl) {
        stroke(255);
        strokeWeight(3);
        line(particle.pos.x, particle.pos.y,particle.pos.x + scl*f.x, particle.pos.y + scl*f.y);
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

    public static void main(String[] args) {
        PApplet.main("sketch");
    }
}
