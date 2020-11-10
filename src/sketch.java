import processing.core.PApplet;
import processing.core.PVector;

public class sketch extends PApplet {

    Particle particle;

    RectCollider[] staticColliders = new RectCollider[4];

    public void settings() {
        size(800,600);
    }

    public void setup() {
        frameRate(60);

        particle = new Particle(this, width/3f, height/2f);
        particle.applyForce(new PVector(2,1)); // Keep this small! This is kind of the time step (dt). If you want the simulation to run faster, change the frameRate up there

        float wallThickness = 16;
        staticColliders[0] = new RectCollider(this, 0, 0, width, wallThickness);
        staticColliders[1] = new RectCollider(this, width-wallThickness, 0, width, height);
        staticColliders[2] = new RectCollider(this, 0, height-wallThickness, width, height);
        staticColliders[3] = new RectCollider(this, 0, 0, wallThickness, height);
    }

    public void draw() {
        background(0);

        // ==== PHYSICS ====

        // == Hits ==
        // Hit resets
        particle.rcBall.hit = false;
        for(RectCollider sc : staticColliders) {
            sc.hit = false;
        }

        // Hit checking
        for (int i = 0; i < staticColliders.length; i++) {
            if(TestRectOverlap(staticColliders[i], particle.rcBall)) {
                if (i == 0 || i == 2) particle.vel.y *= -1f;
                if (i == 1 || i == 3) particle.vel.x *= -1f;
            }
        }

        particle.update();


        // ==== RENDERING ====
        for (RectCollider sc : staticColliders) {
            if (sc != null) sc.render();
        }
        particle.render();

        //saveFrame("/frames/take0004/frame-####.png");
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
