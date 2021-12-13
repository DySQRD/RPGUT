package Jeu;

import com.google.gson.annotations.Expose;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Marc SANCHEZ
 *
 */
public class Obstacle {
    @Expose
    public float height;
    @Expose
    public float width;
    @Expose
    public String type;
    @Expose
    public String name;
    @Expose
    public float x;
    @Expose
    public float y;
    public Rectangle hitbox = null;

    /**
     * Crée un objet Obstacle, permettant soit de définir les limites du niveau, soit les portes de sortie.
     */
    public Obstacle(){}
    public void setHitbox(){
        this.hitbox = new Rectangle(x, y, width, height);
        this.hitbox.setOpacity(.5);
    }
}
