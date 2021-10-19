import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Exceptions.JoueurExisteException;
import Exceptions.JoueurIntrouvableException;
import Exceptions.ImprevuDBError;
import Exceptions.MauvaisMDPException;


/**
 * Classe d'interaction avec la BD.
 * @author dydyt
 *
 */
public class BD {
	//"connexion" en francais, ca permettra aussi de faire la distinction entre les methodes de JDBC et celles que je cree.
	private Connection connexion;
	private PreparedStatement preparedStatement;
	
	/**
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 */
	public BD() throws SQLException {
		setConnexion(DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "joueur", "joueur"));
	}
	
	/**
	 * 
	 * @param pseudo
	 * @param mdp
	 * @throws JoueurIntrouvableException Si le pseudonyme n'existe pas dans la BD.
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 * @throws ImprevuDBError S'il y a plus d'un resultat pour le pseudonyme demande.
	 * @throws MauvaisMDPException Si le mot de passe est incorrect.
	 */
	public Joueur connexion(String pseudo, String mdp) throws JoueurIntrouvableException, ImprevuDBError, SQLException, MauvaisMDPException {
		ResultSet rs = query(""
				+ "SELECT * FROM `joueur`"
				+ "LEFT JOIN `joueur_role`"
				+ "ON joueur.id = joueur_role.joueur_id"
				+ "WHERE `joueur`.`nom` = ? AND mdp = ?"
		+ "", new String[]{pseudo, mdp});
		
		//S'il n'y a pas de joueur pour le pseudonyme donné...
		if(!rs.next()) {
			//On demandera de saisir le pseudo de nouveau
			throw new JoueurIntrouvableException();
			
		//S'il y a plus d'un resultat...
		} else if(!rs.isLast()) {
			//Une erreur qui n'est pas censée survenir.
			throw new ImprevuDBError();
		/*Ici, une erreur, pas une exception, car on requiert qu'un humain aille inspecter la BD pour corriger le probleme,
		prendre contact avec les joueurs concernes, et enfin modifier le code pour que cette erreur ne se reproduise plus.
		Du cote de l'interface graphique, on indiquera au joueur qu'une erreur est survenue sans preciser laquelle,
		que le service technique a ete prevenu et qu'il faudra essayer de se connecter de nouveau plus tard.*/
		
		//S'il y a un joueur mais que le mot de passe ne correspond pas...
		} else if(!(rs.getString("mdp") == mdp)) {	
			//Sur l'interface, on demandera de saisir le mdp de nouveau.
			throw new MauvaisMDPException();
		} else {
			int joueurId = rs.getInt("id");
			
			//Si le joueur est admin...
			if(rs.getInt("role_id") == 1) {
				//On se reconnecte avec les droits roots.
				setConnexion(DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "root", "root"));
				setPreparedStatement(connexion.createStatement());
			}
			telecharge(joueurId);
		}
	}
	
	/**
	 * Ajoute un pseudo et un mdp a la BD a condition que le pseudonyme ne soit pas deja utilisé.
	 * 
	 * @param pseudo
	 * @param mdp
	 * @throws JoueurExisteException Si le pseudonyme est deja present dans la BD.
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 */
	public Joueur inscription(String pseudo, String mdp) throws JoueurExisteException, SQLException {
		ResultSet res = statement.executeQuery(""
			+ "SELECT nom "
			+ "FROM joueur "
			+ "WHERE nom = '" + pseudo + "'");
		
		if(res.next()) {									// S'il existe déjà un joueur avec ce pseudo...
			throw new JoueurExisteException();		// On empêche d'aller plus loin.
		} else {
			statement.executeUpdate(""
				+ "INSERT INTO joueur(id, nom, mdp, xp)"
				+ "VALUES(0,'" + pseudo + "','" + mdp + "', 0)");
		}
		return connexion(pseudo, mdp);
	}
	
	/**
	 * Enclenche toutes les méthodes "telecharge".<br>
	 * Càd, télécharge toutes les tables statiques ainsi que les données du joueur dont l'id est donnée.
	 * @param joueurId
	 * @throws SQLException
	 */
	private void telecharge(int joueurId) throws SQLException {
		//Téléchargement des données du joueur
		telechargeConsommable(joueurId);
		telechargeJoueur(joueurId);
		telechargeJoueurConsommable(joueurId);
		
		//Téléchargement des données statiques
		telechargeMob();
	}
	
	/**
	 * Télécharge l'intégralité de la table demandée.<br>
	 * A n'utiliser que pour les tables statiques,<br>
	 * c'est-à-dire ne contenant que des données qui ne sont pas susceptibles de changer.
	 * @param table
	 * @return L'integralite de la table demandée.
	 * @throws SQLException
	 */
	private ResultSet telechargeTable(String table) throws SQLException {
		return statement.executeQuery(""
				+ "SELECT *"
				+ "FROM `" + table + "`"
			+ "");
	}
	
	/**
	 * Télécharge les données d'une table correspondants à l'id du joueur donné.
	 * @param 	table		Le nom de la table à télécharger.
	 * @param 	joueurId	L'id du joueur dans la BD dont il faut télécharger les données.
	 * @return 	L'integralite de la table demandée uniquement avec les tuples du joueur demandé.
	 * @throws 	SQLException
	 */
	private ResultSet telechargeTable(String table, int joueurId) throws SQLException {
		return statement.executeQuery(""
				+ "SELECT *"
				+ "FROM `" + table + "`"
				+ "WHERE joueur_id = " + joueurId
			+ "");
	}
	
	
	
	/*
	 * Téléchargement des tables STATIQUES de la BD dans des ArrayLists.
	 */
	
	/**
	 * Télécharge l'intégralité de la table Consommable<br>
	 * et range ses données dans les ArrayLists de la classe Consommable.
	 * @throws SQLException
	 */
	private void telechargeConsommable() throws SQLException {
		ResultSet consommableTable = telechargeTable("consommable");
		
		//Tant qu'il reste une ligne à télécharger
		while(consommableTable.next()) {
			int consommableId = consommableTable.getInt("id");
			
			//Ajoute les attributs des objets dans les ArrayLists,
			//dont les clès sont les ids des objets
			Consommable.getNoms().add(consommableId, consommableTable.getString("nom"));
			Consommable.getDurabilites().add(consommableId, consommableTable.getInt("durabilite"));
			Consommable.getEffets().add(consommableId, Consommable.getEffets().get(consommableId));
		}
	}
	
	/*
	 * Téléchargement des tables DYNAMIQUES de la BD.
	 */

	public Joueur telechargeJoueur(int joueurId) throws SQLException {
		ResultSet joueur = telechargeTable("joueur", joueurId);
		return new Joueur(joueur.getString("nom"), joueur.getInt("id"), joueur.getInt("xp"), joueur.getInt("pv"), joueur.getInt("attaque"), joueur.getInt("vitesse"));
	}
	public void telechargeJoueurConsommable(int joueurId) throws SQLException {
		ResultSet joueurConsommable = telechargeTable("joueur_consommable");
		while(joueurConsommable.next()) {
			
		}
	}
	
	
	private ResultSet query(String sql, String[] valeurs) throws SQLException {
		for(int i = 1; i < valeurs.length + 1; i++) {
			preparedStatement.setString(i, valeurs[i]);
		}
		return preparedStatement.executeQuery();
	}
	
	
}
