package Jeu;


import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Mob extends Entity{
    protected Image imageMob;
    protected static int nbreMobs = 0;
    protected String name;
    protected int id;
    protected int xpGiven;

    public Mob(String matiere, String mobType) throws IOException{
        this.id = nbreMobs;
        nbreMobs++;
        switch(matiere){
            case "Maths" : switch (mobType){
                case "Minion" : {
                    this.name = "Méchante inconnue";
                    this.imageMob = new Image("file:res/Images/minions.png");
                    ImageView imageView = new ImageView();
                    imageView.setImage(imageMob);
                    imageView.setFitWidth(25);
                    imageView.setFitHeight(25);
                    this.imageV = imageView;
                    this.health_base_max = 25;
                    this.actual_health_max = health_base_max;
                    this.actual_health = actual_health_max;
                    this.atk_base = 2;
                    this.actual_atk = atk_base;
                    this.defense_base = 2;
                    this.actual_defense = defense_base;
                    this.velocity = 7;
                    this.xpGiven = 4;
                    this.lvl = 1;break;
                }
                case "boss" : {

                }
            }

            case "Logique" :





        }
    }

    public void tp(double x, double y){
        this.posX = x;
        this.posY = y;
        this.hitbox.setX(x);
        this.hitbox.setY(y);
        this.imageV.setX(x);
        this.imageV.setY(y);
    }

    public void moveUp(){
        this.posY -= this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY);
    }
    public void moveDown(){
        this.posY += this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY);
    }
    public void moveLeft(){
        this.posX -= this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX);
    }
    public void moveRight(){
        this.posX += this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX);
    }
}
