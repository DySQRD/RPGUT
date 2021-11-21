package Jeu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
    //Position à l'écran (0,0 = en haut à gauche)
    protected double posX, posY;

    //Image de l'entité
    protected ImageView imageV;

    //Vitesse de déplacement à l'écran
    protected int velocity;

    //Hitbox représentée par un rectangle
    protected Rectangle hitbox;

    //Collision de l'hitbox avec une autre hitbox
    protected boolean collision;

    //Caractéristiques
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

    //Attaque une autre entité
    public void attaque(Entity entity) {
        entity.actual_health -= (actual_atk);
    }

    //Déplacements
    abstract void tp(double x, double y);
    abstract void moveUp();
    abstract void moveDown();
    abstract void moveLeft();
    abstract void moveRight();
}
