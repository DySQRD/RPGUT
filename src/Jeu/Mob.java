package Jeu;


import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;

import BD.BD;
import BD.EntiteType;
import BD.Stats;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Rectangle;

public class Mob extends Entity{
    protected Image imageMob;
    protected static int nbreMobs = 0;
    
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
    
    public Mob(ResultSet table) throws IOException, SQLException {
    	this(
	    	table.getInt("entite_id"),
	    	BD.getEntiteTypes().get(table.getInt("entite_type_id")),
			table.getDouble("x"),
			table.getDouble("y")
		);
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
    
    public String getNom() {
    	return entiteType.nom;
    }
    
    public Stats getStats() {
    	return entiteType.stats;
    }
    
    public Movepool getMovepool() {
    	return entiteType.movepool;
    }
}
