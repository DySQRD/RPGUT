package Jeu;

import java.sql.SQLException;

import BD.BD;
import BD.Inventaire;
import BD.Stats;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;


public class Personnage extends Entity {
	//Id du joueur
	protected final int joueurId;
	protected final String nom;
    
    //Inventaire et Stats de l'entité
    protected Inventaire inventaire;
    protected Movepool movepool;
    protected Stats stats;
    protected Stats statsBonus;
	
    public Image imageCharacter = new Image("file:res/Images/lucas.png");
    protected int u,d,l,r,xp = 0;
    protected Mob mobVS;
    protected static final int ATK_PER_LVL = 2;
    protected static final int DEFENSE_PER_LVL = 2;
    protected static final int HEALTH_PER_LVL = 10;

    public Personnage() throws SQLException {
    	this(
			BD.getJoueurTable().getInt("joueur_id"),
			BD.getJoueurTable().getInt("entite_id"),
			BD.getJoueurTable().getString("nom"),
			BD.getJoueurTable().getInt("xp"),
			BD.getJoueurTable().getInt("pv"),
			new Stats(BD.getJoueurTable()),
			new Inventaire(),
			BD.getJoueurTable().getInt("x"),
			BD.getJoueurTable().getInt("y")
    	);
    }

    public Personnage(int joueurId, int entiteId, String nom, int xp, int PV, Stats stats, Inventaire inventaire, double posX, double posY) {
    	super(entiteId, BD.getEntiteTypes().get(1), posX, posY);	//1 correspond à l'id du type de mob "Joueur" dans la BD
    	this.joueurId = joueurId;
    	this.nom = nom;
    	this.xp = xp;
    	this.PV = PV;
    	this.stats = stats;
    	this.inventaire = inventaire;
        
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
        this.setPV(stats.get("pv_max"));
        this.lvl = 1;
    }

    public void giveXp(){
        float ratio = (float)mobVS.lvl/(float)lvl;
        int lvlAfter;
        int xpReceived = (int) (mobVS.getXpLoot() * ratio + 1);
        this.xp+= xpReceived;
        System.out.println("Vous avez gagné " + xpReceived + " xp.");
        lvlAfter = (int)(Math.log(xp)/Math.log(2) - 1);
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
        fullPV();
        //Augmente toutes les stats suite au passage de niveau
        for(String stat : Stats.statsOrdre) stats.put(stat, stats.get(stat) + statsPerLevel.get(stat));
        System.out.println("Attaque : " + (stats.get("attaque")));
        System.out.println("Défense : " + (stats.get("defense")));
        System.out.println("PV :" + (stats.get("pv_max")));
    }
    
    /*
     * Getters et setters
     */

	public int getJoueurId() {
		return joueurId;
	}
    
    public String getNom() {
    	return nom;
    }

	public int getXp() {
		return xp;
	}
	
    public Inventaire getInventaire() {
    	return inventaire;
    }

	public Stats getStats() {
		return stats;
	}

	@Override
	public Movepool getMovepool() {
		// TODO Auto-generated method stub
		return null;
	}
    
    
    
    
}
