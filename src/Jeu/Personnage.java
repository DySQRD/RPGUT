package Jeu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Personnage extends Entity{
    public Image imageCharacter;
    protected Mob mobVS;


    public Personnage(double posX, double posY, int velocity, String fileName, Level currentLevel){
        super.posX = posX;
        super.posY = posY;
        this.imageCharacter = (new Image(fileName));
        super.imageV = new ImageView();
        super.imageV.setImage(this.imageCharacter);
        super.imageV.setFitHeight(40);
        super.imageV.setFitWidth(40);
        super.imageV.setX(posX);
        super.imageV.setY(posY);
        super.hitbox = new Rectangle(posX+10, posY+20, 20, 18);
        super.hitbox.setFill(Color.RED);
        super.currentLevel = currentLevel;
        super.velocity = velocity;
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
        this.hitbox.setX(x+10);
        this.posY = y;
        this.imageV.setY(y);
        this.hitbox.setY(y+20);
    }

}
