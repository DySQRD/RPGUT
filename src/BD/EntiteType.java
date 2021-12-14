package BD;

import java.sql.ResultSet;
import java.sql.SQLException;
import BD.Movepool;

/**
 * 
 * @author Dylan TOLEDANO
 *
 */


public class EntiteType {
	public final int entiteTypeId;
	public final String nom;
	public final Stats stats;
	public final int xpLoot;
	public final int xHitbox;
	public final int yHitbox;
	public final Movepool movepool;
	
	/**
	 * Crée une EntiteType, or cela représente les mobs/monstres qui pourraient être créé, et non les instances de monstres.
	 * @param entiteTypeId
	 * Identifiant du type d'entité.
	 * @param nom
	 * Nom dy type d'entité.
	 * @param stats
	 * Les statistiques du type d'entité.
	 * @param xpLoot
	 * La quantité d'xp relâché par le type d'entité après sa défaite.
	 * @param xHitbox
	 * Coordonnée x (abscisse) de la hitbox (cadre de collision) du type d'entité.
	 * @param yHitbox
	 * Coordonnée y (ordonnée) de la hitbox (cadre de collision) du type d'entité.
	 * @param movepool
	 * Les capacités qui seront attribuées à ce type de mob.
	 */
	EntiteType(int entiteTypeId, String nom, Stats stats, int xpLoot, int xHitbox, int yHitbox, Movepool movepool) {
		super();
		this.entiteTypeId = entiteTypeId;
		this.nom = nom;
		this.stats = stats;
		this.xpLoot = xpLoot;
		this.xHitbox = xHitbox;
		this.yHitbox = yHitbox;
		this.movepool = movepool;
	}
	
	/**
	 * Lie les types d'entité à la Base de données.
	 * @param table
	 * la table de BDD où ces données seront envoyés.
	 * @throws SQLException
	 * Erreur BDD/SQL
	 */
	EntiteType(ResultSet table) throws SQLException {
		this(
			table.getInt("entite_type_id"),
			table.getString("nom"),
			new Stats(table),
			table.getInt("xp_loot"),
			table.getInt("xHitbox"),
			table.getInt("yHitbox"),
			BD.getMovepools().get(table.getInt("movepool_id"))
		);
	}
	
	/**
	 * Permet d'obtenir les données de type d'entité de la base de données.
	 * @throws SQLException
	 * Erreur BDD/SQL
	 */
	static void telecharger() throws SQLException {
		ResultSet entiteTypeTable = BD.querir("SELECT * FROM entite_type NATURAL JOIN stats");
		while(entiteTypeTable.next()) {
			int entiteType = entiteTypeTable.getInt("entite_type_id");
			
			BD.getEntiteTypes().put(
				entiteType,
				new EntiteType(
					entiteType,
					entiteTypeTable.getString("nom"),
					new Stats(
						entiteTypeTable.getInt("pv_max"),
						entiteTypeTable.getInt("attaque"),
						entiteTypeTable.getInt("defense")
					),
					entiteTypeTable.getInt("xp_loot"),
					entiteTypeTable.getInt("xHitbox"),
					entiteTypeTable.getInt("yHitbox"),
					BD.getMovepools().get(entiteTypeTable.getInt("movepool_id"))
				)
			);
		}
	}
	
}
