package Jeu;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

import java.awt.*;


public class Personnage {
    public double posX;
    public double posY;
    public Image imageCharacter;
    public ImageView imageV = null;
    public int velocity = 5;
    public Rectangle hitbox;
    public boolean collision;


    public Personnage(double posX, double posY, String fileName){
        this.posX = posX;
        this.posY = posY;
        this.imageCharacter = (new Image(fileName));
        this.imageV = new ImageView();
        this.imageV.setImage(this.imageCharacter);
        this.imageV.setFitHeight(40);
        this.imageV.setFitWidth(40);
        this.imageV.setX(posX);
        this.imageV.setY(posY);
        this.hitbox = new Rectangle(posX+10, posY+20, 20, 18);
        this.hitbox.setFill(Color.RED);
    }

    public void moveUp(){
        this.posY -= this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY+20);
    }
    public void moveDown(){
        this.posY += this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY+20);
    }
    public void moveLeft(){
        this.posX -= this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX+10);
    }
    public void moveRight(){
        this.posX += this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX+10);
    }
    public void tp(double x, double y){
        this.posX = x;
        this.imageV.setX(x);
        this.hitbox.setX(x);
        this.posY = y;
        this.imageV.setY(y);
        this.hitbox.setY(y);
    }
}
