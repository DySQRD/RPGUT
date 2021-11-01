package Jeu;

import com.google.gson.annotations.Expose;
import javafx.scene.shape.Rectangle;

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
    //https://stackoverflow.com/questions/56803554/exception-in-thread-thread-0-java-lang-reflect-inaccessibleobjectexception

    public Obstacle(){}
    public void setHitbox(){
        this.hitbox = new Rectangle(x, y, width, height);
        this.hitbox.setOpacity(0);
    }
}
