import processing.core.PApplet;

public class sketch extends PApplet {

    Particle[] particles = new Particle[10];

    public void settings() {
        size(512,512);
        //fullScreen();
    }

    public void setup() {
        background(0);
        for (int i = 0; i < particles.length; i++) {
            particles[i] = new Particle(this, random(width), random(height));
        }
    }

    public void draw() {
        for (Particle p : particles) {
            p.update();
            p.render();
        }
    }

    public static void main(String[] args) {
        PApplet.main("sketch");
    }
}
