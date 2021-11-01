package Jeu;


import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Mob {
    private static int nbreMobs = 0;
    private String name;
    private int id;
    private double posX, posY;
    private ImageView imageMobV;
    private Rectangle hitbox;
    private int velocity;

    public Mob(){
        this.id = nbreMobs;
        nbreMobs++;
    }

    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }

    public int getNbreMinions(){
        return nbreMobs;
    }
    public void setNbreMobs(int nbreMinions){
        this.nbreMobs = nbreMinions;
    }
    public void setImageMobV(ImageView imageMobV){
        this.imageMobV = imageMobV;
    }

    public double getPosX() {
        return posX;
    }

    public void setPosX(double posX) {
        this.posX = posX;
    }

    public double getPosY() {
        return posY;
    }

    public void setPosY(double posY) {
        this.posY = posY;
    }

    public Rectangle getHitbox() {
        return hitbox;
    }

    public void setHitbox(Rectangle hitbox) {
        this.hitbox = hitbox;
    }
}
