package Jeu;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Personnage extends Entity{
    public Image imageCharacter;
    protected Mob mobVS;
    protected int totalXp = 0;
    protected final int ATK_PER_LVL = 2;
    protected final int DEFENSE_PER_LVL = 2;
    protected final int HEALTH_PER_LVL = 10;


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
        super.velocity = velocity;
        this.health_base_max = 100;
        this.actual_health_max = health_base_max;
        this.actual_health = health_base_max;
        this.atk_base = 5;
        this.actual_atk = atk_base;
        this.defense_base = 5;
        this.actual_defense = defense_base;
        this.lvl = 1;
    }

    public void giveXp(){
        float ratio = (float)mobVS.lvl/(float)lvl;
        int lvlAfter;
        int xpReceived = (int) (mobVS.xpGiven * ratio + 1);
        this.totalXp+= xpReceived;
        System.out.println("Vous avez gagné " + xpReceived + " xp.");
        lvlAfter = (int)(Math.log(totalXp)/Math.log(2) - 1);
        if(lvlAfter > lvl){
            lvlUp(lvlAfter);
        }
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
    public void lvlUp(int lvlAfter){
        System.out.println("Vous êtes monté lvl " + lvlAfter + ".");
        this.lvl = lvlAfter;
        updateStats();
        fullHealth();
        System.out.println("Attaque : " + (actual_atk));
        System.out.println("Défense : " + (actual_defense));
        System.out.println("Health :" + (actual_health_max));
    }

    public void fullHealth(){
        this.actual_health = (actual_health_max);
    }

    public void updateStats(){
        this.actual_atk = atk_base + ((lvl -1) * ATK_PER_LVL) + atk_bonus;
        this.actual_defense = defense_base + ((lvl-1) * DEFENSE_PER_LVL) + defense_bonus;
        this.actual_health_max = health_base_max + ((lvl -1) * HEALTH_PER_LVL) + health_bonus_max;
    }
}
