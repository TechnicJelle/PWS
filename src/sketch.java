import processing.core.PApplet;
import processing.core.PVector;

public class sketch extends PApplet {

    Particle particle;

    public void settings() {
        size(800,600);
    }

    public void setup() {
        particle = new Particle(this, width/3f, height/2f);
        particle.applyForce(new PVector(8,0));
    }

    public void draw() {
        background(0);
        particle.update();
        particle.render();
    }

    public static void main(String[] args) {
        PApplet.main("sketch");
    }
}
