package Jeu;

import java.sql.SQLException;

import BD.BD;
import BD.Inventaire;
import BD.Movepool;
import BD.Stats;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelReader;
import javafx.scene.image.WritableImage;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;

/**
 * 
 * L'objet Personnage est créé via cette classe.<br>
 * Il possède un identifiant, un nom, un inventaire (d'objet), une liste de capacité (movepool), et des statistiques.<br>
 * Il peut se déplacer en haut, en bas, à droite, ou à gauche. Et également combattre des mobs.
 * @author Marc SANCHEZ
 *
 *
 */

public class Personnage extends Entity {
	/**
	 * Identifiant du joueur.
	 */
	protected final int joueurId;
	/**
	 * Le nom du joueur.
	 */
	protected final String nom;
    
    /**
     * Inventaire (liste d'objets) du joueur.
     */
    protected Inventaire inventaire;
    
    /**
     * Liste de capacités attribués au joueur.
     */
    protected Movepool movepool;
    
    /**
     * Les statistiques de bases du joueur.
     */
    protected Stats stats;
    
    /**
     * Les statistiques bonus du joueur, or les stastiques qui seront additionnées aux statistiques de bases en cas d'augmentation ou de réduction <br>
     * de statistiques en utilisant une capacité ou un objet.
     */
    protected Stats statsBonus;
	
    /**
     * Image par laquelle le joueur sera visible.
     */
    public Image imageCharacter = new Image("file:res/Images/lucas.png");
    
    /**
     * Les integers pour la position et l'expérience (obtenu une fois un monstre vaincu).
     */
    protected int u,d,l,r,xp = 0;
    
    /**
     * Le monstre contre qui le personnage se combattra.
     */
    protected Mob mobVS;
    
    /**
     * Augmentation de la statistique 'attaque' par niveau.
     */
    protected static final int ATK_PER_LVL = 2;
    
    /**
     * Augmentation de la statistique 'defense' par niveau.
     */
    protected static final int DEFENSE_PER_LVL = 2;
    
    /**
     * Augmentation de la statistique 'Points de vie' par niveau.
     */
    protected static final int HEALTH_PER_LVL = 10;

    /**
     * Crée le personnage du jeur vidéo, il possède un identifiant, un identifiant entité, un nom, de l'xp, des points de vie, un inventaire, et une position (x,y)
     * @throws SQLException
     * Erreur SQL/BDD
     */
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
    
    /**
     * 
     * @param joueurId
     * Identifiant de l'utilisateur
     * @param entiteId
     * Identifiant de l'entité
     * @param nom
     * Nom du personnage
     * @param xp
     * Expérience du jeu
     * @param PV
     * Points de vie actuels
     * @param stats
     * Statistique comportant (les pdv max, attaque, défense)
     * @param inventaire
     * Inventaire du personnage (Objets)
     * @param posX
     * Position abscisse
     * @param posY
     * Position ordonnéee
     */
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
    
    /**
     * Calcule l'xp obtenu après avoir gagné un combat.
     */
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

    /**
     * Méthode pour se déplacer vers le haut.
     */
    public void moveUp(){
        this.posY -= this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY+dyHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*u,64*3,64,64));
        u++;
        if(u>3) u=0;
    }
    
    /**
     * Méthode pour se déplacer vers le bas.
     */
    public void moveDown(){
        this.posY += this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY+dyHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*d,64*0,64,64));
        d++;
        if(d>3) d=0;
    }
    
    /**
     * Méthode pour se déplacer vers la gauche.
     */
    public void moveLeft(){
        this.posX -= this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX+dxHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*l,64*1,64,64));
        l++;
        if(l>3) l=0;
    }
    
    /**
     * Méthode pour se déplacer vers la droite.
     */
    public void moveRight(){
        this.posX += this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX+dxHitbox);
        this.imageV.setImage(new WritableImage((PixelReader) imageCharacter.getPixelReader(), 64*r,64*2,64,64));
        r++;
        if(r>3) r=0;
    }
    
    /**
     * Redéfinit la position du personnage.
     */
    public void tp(double x, double y){
        this.posX = x;
        this.imageV.setX(x);
        this.hitbox.setX(x+dxHitbox);
        this.posY = y;
        this.imageV.setY(y);
        this.hitbox.setY(y+dyHitbox);
    }
    
    /**
     * Permet d'atteindre le niveau suivant. (ex : niveau actuel 1, niveau suivant 2)
     * @param lvlAfter
     * Le niveau suivant à atteindre (en fonction de l'xp)
     */
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
    
    /**
     * Retourne l'identifiant du joueur.
     * @return jouerId
     * Identifiant du joueur
     */
	public int getJoueurId() {
		return joueurId;
	}
    
	/***
	 * Retourne le nom du joueur.
	 * @return nom
	 * Le nom du joueur
	 */
    public String getNom() {
    	return nom;
    }

    /**
     * Retourne l'expérience du personnage.
     * @return xp
     * l'xp du personnage
     */
	public int getXp() {
		return xp;
	}
	
	/**
	 * Retourne l'inventaire du personnage.
	 * @return inventaire
	 * L'inventaire du joueur
	 */
    public Inventaire getInventaire() {
    	return inventaire;
    }
    
    /**
     * Retourne les statistiques (PV_max, attaque, défense) du personnage.
     * @return stats
     * Les statistiques du joueur
     */
	public Stats getStats() {
		return stats;
	}
	
	@Override
	public Movepool getMovepool() {
		return movepool;
	}

	public Mob getMobVS() {
		return mobVS;
	}
	
	

}
