package Jeu;


import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Mob extends Entity{
    protected static int nbreMobs = 0;
    protected String name;
    protected int id;

    public Mob(){
        this.id = nbreMobs;
        nbreMobs++;
    }

    public void tp(double x, double y){
        this.posX = x;
        this.posY = y;
        this.hitbox.setX(x);
        this.hitbox.setY(y);
        this.imageV.setX(x);
        this.imageV.setY(y);
    }
}
