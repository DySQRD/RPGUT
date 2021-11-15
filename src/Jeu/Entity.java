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
    protected int health_base_max;
    protected int health_bonus_max;
    protected int actual_health_max;
    protected int actual_health;
    protected int atk_base;
    protected int atk_bonus;
    protected int actual_atk;
    protected int defense_base;
    protected int defense_bonus;
    protected int actual_defense;
    protected int lvl;

    public Entity(){}

    public void attaque(Entity entity) {
        entity.actual_health -= (actual_atk);
    }

    abstract void tp(double x, double y);
    abstract void moveUp();
    abstract void moveDown();
    abstract void moveLeft();
    abstract void moveRight();
}
