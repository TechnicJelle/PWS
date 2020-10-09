import processing.core.PApplet;
import processing.core.PVector;

public class sketch extends PApplet {

    Particle particle;

    RectCollider rc;

    public void settings() {
        size(800,600);
    }

    public void setup() {
        particle = new Particle(this, width/3f, height/2f);
        particle.applyForce(new PVector(8,0));

        rc = new RectCollider(this, width*2f/3f, height/3f, width*2f/3f+32, height*2f/3f);
    }

    public void draw() {
        background(0);

        rc.render();

        particle.update();
        particle.render();
    }

    public static void main(String[] args) {
        PApplet.main("sketch");
    }
}
