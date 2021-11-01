package Jeu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
    protected double posX, posY;
    protected ImageView imageV;
    protected int velocity;
    protected Rectangle hitbox;
    protected boolean collision;
    protected Level currentLevel;

    public Entity(){}

    abstract void tp(double x, double y);
}
