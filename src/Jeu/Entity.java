package Jeu;

import java.sql.ResultSet;
import java.sql.SQLException;

import BD.BD;
import BD.EntiteType;
import BD.Stats;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public abstract class Entity {
	//Id d'instance d'entité dans la BD
	protected int entiteId;
	//Id de type d'entité dans la BD
	protected int entiteTypeId;
	
    //Position à l'écran (0,0 = en haut à gauche)
    protected double posX, posY;

    //Image de l'entité
    protected ImageView imageV;

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

    protected final Stats statsPerLevel = new Stats(10, 2, 2);
    
	public Entity(int entiteId, int entiteTypeId, double posX, double posY) {
		this.entiteId = entiteId;
    	this.entiteTypeId = entiteTypeId;
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
    
    public static void telecharger() throws SQLException {
    	//Sélectionne toutes les entites que le joueur n'a pas encore vaincues.
    	//Pour rappel, la table victoire enregistre les entités vaincues par les joueurs.
		ResultSet entiteTable = BD.querir(
			"SELECT * "
			+ "FROM joueur j, entite e, victoire v "
			//L'entité ne doit pas déjà avoir été vaincue.
			+ "WHERE e.entite_id != v.entite_id "
			+ "AND v.joueur_id = " + BD.getJoueurTable().getInt("joueur_id")
			//Ces entités ne peuvent être celles d'autres joueurs.
			//(les entités mobs et joueurs sont enregistrées dans la même table !)
			+ " AND v.entite_id NOT IN ("
				+ "SELECT entite_id "
				+ "FROM joueur j "
			+ ")"
		);
		while(entiteTable.next()) {
			BD.getEntiteTypes().put(entiteTable.getInt("entite_id"), new EntiteType(entiteTable));
		}
    }
    
    /*
     * Getters dépendant de la sous-classe d'entité.
     * Chaque joueur a ses propres caractéristiques
     * tandis que les mobs partagent celles de leur type d'Entité.
     */
    
    public abstract String getNom();
    public abstract Stats getStats();

    /**
     * Statistique après calcul des bonus.
     * TODO rajouter les bonus !!! Cette méthode sert de placeholder en attendant de les ajouter.
     * @param stat
     * @return
     */
    public int getActualStat(String stat) {
    	return getStats().get(stat); //+ statsBonus.get(stat)
    }
    
    public EntiteType getEntiteType() {
    	return BD.getEntiteTypes().get(entiteTypeId);
    }
    
    public double getPosX() {
		return posX;
	}

	public double getPosY() {
		return posY;
	}

	public int getXpLoot() {
    	return BD.getEntiteTypes().get(entiteTypeId).xpLoot;
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
    
	
}
