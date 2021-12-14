package Jeu;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import BD.BD;
import BD.EntiteType;
import BD.Movepool;
import BD.Stats;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;
/**
 * Les entités monstres seront gérés ici. <br>
 * Cette classe attribue à chaque mob (monstre) un identifiant, un type, et une position.
 * 
 * @author Marc SANCHEZ
 *
 */
public class Mob extends Entity{
	/**
	 * Image pour pouvoir visualiser le monstre.
	 */
    protected Image imageMob;
    
    /**
     * Identifiant du monstre
     */
    protected static int nbreMobs = 0;
    
    /**
     * Constructeur permettant de créer un monstre (instance d'un type).
     * @param entiteId
     * Identifiant de l'entité.
     * @param entiteType
     * Type de l'entité.
     * @param posX
     * Position x (abscisse) du monstre.
     * @param posY
     * Position y (ordonnée) du monstre.
     * @throws IOException
     * Erreur In/Out.
     */
    public Mob(int entiteId, EntiteType entiteType, double posX, double posY) throws IOException {
    	super(entiteId, entiteType, posX, posY);
        nbreMobs++;
    	this.imageMob = new Image("file:res/Images/" + this.getNom() + ".png");
        imageV = new ImageView();
        imageV.setImage(imageMob);
        imageV.setFitWidth(25);
        imageV.setFitHeight(25);
        imageV.setX(posX);
        imageV.setY(posY);
        this.hitbox = new Rectangle(posX+dxHitbox, posY+dyHitbox, 20, 20);
        this.setPV(entiteType.stats.get("pv_max"));
        this.velocity = 7;
        this.lvl = 1;
    }
    
    /**
     * Constructeur pour la création d'un mob à partir de la base de données.
     * @param table
     * La table où les données seront récupérés.
     * @throws IOException
     * Erreur In/Out.
     * @throws SQLException
     * Erreur BDD/SQL.
     */
    public Mob(ResultSet table) throws IOException, SQLException {
    	this(
	    	table.getInt("entite_id"),
	    	BD.getEntiteTypes().get(table.getInt("entite_type_id")),
			table.getDouble("x"),
			table.getDouble("y")
		);
    }
    
    /**
     * Redéfinit la position du monstre. (En fonction du x et du y).
     */
    public void tp(double x, double y){
        this.posX = x;
        this.posY = y;
        this.hitbox.setX(x);
        this.hitbox.setY(y);
        this.imageV.setX(x);
        this.imageV.setY(y);
    }
    
    /**
     * Méthode pour le déplacement vers le haut (y-1).
     */
    public void moveUp(){
        this.posY -= this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY);
    }
    
    /**
     * Méthode pour le déplacement vers le bas (y+1).
     */
    public void moveDown(){
        this.posY += this.velocity;
        this.imageV.setY(this.posY);
        this.hitbox.setY(posY);
    }
    
    /**
     * Méthode pour le déplacement vers la gauche (x-1).
     */
    public void moveLeft(){
        this.posX -= this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX);
    }
    
    /**
     * Méthode pour le déplacement vers la droite (x+1).
     */
    public void moveRight(){
        this.posX += this.velocity;
        this.imageV.setX(this.posX);
        this.hitbox.setX(posX);
    }
    
    /**
     * Obtient le nom de l'entité
     * @return entiteType.nom
     * Le nom du type d'entité.
     */
    public String getNom() {
    	return entiteType.nom;
    }
    
    /**
     * Obtient les statistiques du type d'entité.
     * @return entiteType.stats
     * Les statistiques du entiteType.
     */
    public Stats getStats() {
    	return entiteType.stats;
    }
    
    /**
     * Obtient les capacités du type d'entité.
     * @return entiteType.movepool
     * Les capacités du entiteType
     */
    public Movepool getMovepool() {
    	return entiteType.movepool;
    }
}
