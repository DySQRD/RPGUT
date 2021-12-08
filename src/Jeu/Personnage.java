package Jeu;

import BD.Inventaire;
import BD.Stats;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Personnage extends Entity{
	//Id du joueur
	protected int joueur_id;
	
    public Image imageCharacter = (new Image("file:res/Images/lucas.png"));
    protected int u,d,l,r = 0;
    protected Mob mobVS;
    protected int totalXp = 0;
    protected final int ATK_PER_LVL = 2;
    protected final int DEFENSE_PER_LVL = 2;
    protected final int HEALTH_PER_LVL = 10;


    public Personnage(int joueur_id, Stats stats, Inventaire inventaire, double posX, double posY) {
    	this.id = 1; //id du type de mob "Joueur" dans la BD
        
        this.dxHitbox = 15;
        this.dyHitbox = 35;
        this.imageV = new ImageView();
        this.imageV.setImage(this.imageCharacter);
        this.imageV.setImage(new WritableImage((PixelReader) this.imageCharacter.getPixelReader(),0,0,64,64));
        this.imageV.setX(posX);
        this.imageV.setY(posY);
        this.hitbox = new Rectangle(posX+dxHitbox, posY+dyHitbox, 20, 20);
        this.hitbox.setFill(Color.RED);
        this.velocity = 5;
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
        this.hitbox.setY(posY+dyHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*u,64*3,64,64));
        u++;
        if(u>3) u=0;
    }
    public void moveDown(){
        this.posY += this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY+dyHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*d,64*0,64,64));
        d++;
        if(d>3) d=0;
    }
    public void moveLeft(){
        this.posX -= this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX+dxHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*l,64*1,64,64));
        l++;
        if(l>3) l=0;
    }
    public void moveRight(){
        this.posX += this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX+dxHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*r,64*2,64,64));
        r++;
        if(r>3) r=0;
    }
    public void tp(double x, double y){
        this.posX = x;
        this.imageV.setX(x);
        this.hitbox.setX(x+dxHitbox);
        this.posY = y;
        this.imageV.setY(y);
        this.hitbox.setY(y+dyHitbox);
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
