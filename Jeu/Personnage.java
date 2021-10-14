package Jeu;

import javafx.geometry.BoundingBox;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;


public class Personnage {
    public double posX;
    public double posY;
    public Image imageCharacter;
    public ImageView imageV = null;
    public int velocity = 5;


    public Personnage(double posX, double posY, String fileName){
        this.posX = posX;
        this.posY = posY;
        this.imageCharacter = (new Image(fileName));
        this.imageV = new ImageView();
        this.imageV.setImage(this.imageCharacter);
        this.imageV.setFitHeight(60);
        this.imageV.setFitWidth(60);
        this.imageV.setX(posX);
        this.imageV.setY(posY);
    }

    public void moveUp(){
        this.posY -= this.velocity;
        this.imageV.setY(this.posY);
    }
    public void moveDown(){
        this.posY += this.velocity;
        this.imageV.setY(this.posY);
    }
    public void moveLeft(){
        this.posX -= this.velocity;
        this.imageV.setX(this.posX);
    }
    public void moveRight(){
        this.posX += this.velocity;
        this.imageV.setX(this.posX);
    }

}
