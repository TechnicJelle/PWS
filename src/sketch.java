import processing.core.PApplet;
import processing.core.PVector;

public class sketch extends PApplet {

    Particle particle;

    RectCollider rc;
    RectCollider mouse;

    public void settings() {
        size(800,600);
    }

    public void setup() {
        frameRate(5);

        particle = new Particle(this, width/3f, height/2f);
        particle.applyForce(new PVector(100000000,0));

        rc = new RectCollider(this, width*2f/3f+30, height/3f, width*2f/3f+32, height*2f/3f);

        mouse = new RectCollider(this, mouseX, mouseY, mouseX+20f, mouseY+20f);
    }

    public void draw() {
        background(0);
        rc.render();

        fill(TestRectOverlap(rc, mouse) ? 255 : 100);
        mouse.pos1.x = mouseX;
        mouse.pos1.y = mouseY;
        mouse.pos2.x = mouseX+20f;
        mouse.pos2.y = mouseY+20f;
        mouse.render();

        //rcball&rc
        fill(TestRectOverlap(particle.rcball, rc) ? 255 : 100);

        particle.update();
        particle.render();
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

        return true;
    }

    public static void main(String[] args) {
        PApplet.main("sketch");
    }
}
