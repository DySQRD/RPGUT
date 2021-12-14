package Jeu;

import BD.EntiteType;
import BD.Movepool;
import BD.Stats;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

/**
 * 
 * @author Marc SANCHEZ
 *
 */
public abstract class Entity {
	//Id d'instance d'entité dans la BD
	protected int entiteId;
	//Id de type d'entité dans la BD
	protected EntiteType entiteType;
	
    //Position à l'écran (0,0 = en haut à gauche)
    protected double posX, posY;

    //Image de l'entité
    protected ImageView imageV;
    
    //Liste des capacites
    protected Movepool capacites;

    //Vitesse de déplacement à l'écran
    protected int velocity;

    //Hitbox représentée par un rectangle
    protected Rectangle hitbox;
    protected int dxHitbox, dyHitbox;

    //Collision de l'hitbox avec une autre hitbox
    protected boolean collision;

    //Caractéristiques
    protected int PV;
    protected int lvl;

    public Stats statsBonus;
    
    protected final Stats statsPerLevel = new Stats(10, 2, 2);
    
	public Entity(int entiteId, EntiteType entiteType, double posX, double posY) {
		this.entiteId = entiteId;
    	this.entiteType = entiteType;
        this.posX = posX;
        this.posY = posY;
    }

    //Attaque une autre entité
    public void attaque(Entity entity) {
        entity.setPV(entity.getPV() - getStats().get("attaque"));
    }

    //Déplacements
    abstract void tp(double x, double y);
    abstract void moveUp();
    abstract void moveDown();
    abstract void moveLeft();
    abstract void moveRight();
    
    /*
     * Getters dépendant de la sous-classe d'entité.
     * Chaque joueur a ses propres caractéristiques
     * tandis que les mobs partagent celles de leur type d'Entité.
     */
    
    public abstract String getNom();
    public abstract Stats getStats();
    public abstract Movepool getMovepool();

    /**
     * Statistique après calcul des bonus.
     * TODO rajouter les bonus !!! Cette méthode sert de placeholder en attendant de les ajouter.
     * @param stat
     * Statistique à retourner
     * @return stat
     * la valeur de la statistique.
     */
    public int getActualStat(String stat) {
    	return getStats().get(stat); //+ statsBonus.get(stat)
    }
    
    public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public int getXpLoot() {
    	return entiteType.xpLoot;
    }

	public int getPV() {
		return PV;
	}

	public void setPV(int PV) {
		this.PV = PV;
	}

    public void fullPV(){
        this.setPV(getStats().get("pv_max"));
    }
    
	public int getDxHitbox() {
		return entiteType.xHitbox;
	}
	
	public int getDyHitbox() {
		return entiteType.yHitbox;
	}
}
