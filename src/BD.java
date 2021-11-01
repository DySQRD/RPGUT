import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

import Exceptions.JoueurAlreadyExistsException;
import Exceptions.JoueurNotFoundException;
import Exceptions.UnexpectedDBError;
import Exceptions.WrongPasswordException;


/**
 * Classe d'interaction avec la BD.
 * @author dydyt
 *
 */
public class BD {
	//"connexion" en francais, ca permettra aussi de faire la distinction entre les methodes de JDBC et celles que je cree.
	private Connection connexion;
	private Statement statement;
	
	/**
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 */
	public BD() throws SQLException {
		setConnexion(DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "joueur", "joueur"));
		setStatement(connexion.createStatement());
	}
	
	/**
	 * 
	 * @param pseudo
	 * @param mdp
	 * @throws JoueurNotFoundException Si le pseudonyme n'existe pas dans la BD.
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 * @throws UnexpectedDBError S'il y a plus d'un resultat pour le pseudonyme demande.
	 * @throws WrongPasswordException Si le mot de passe est incorrect.
	 */
	public void connexion(String pseudo, String mdp) throws JoueurNotFoundException, UnexpectedDBError, SQLException, WrongPasswordException {
		ResultSet rs = statement.executeQuery(""
				+ "SELECT * FROM `joueur`"
				+ "LEFT JOIN `joueur_role`"
				+ "ON joueur.id = joueur_role.joueur_id"
				+ "WHERE `joueur`.`nom` = '" + pseudo + "' AND mdp = '" + mdp + "'"
		+ "");
		
		//S'il n'y a pas de joueur pour le pseudonyme donné...
		if(!rs.next()) {
			//On demandera de saisir le pseudo de nouveau
			throw new JoueurNotFoundException();
			
		//S'il y a plus d'un resultat...
		} else if(!rs.isLast()) {
			//Une erreur qui n'est pas censée survenir.
			throw new UnexpectedDBError();
		/*Ici, une erreur, pas une exception, car on requiert qu'un humain aille inspecter la BD pour corriger le probleme,
		prendre contact avec les joueurs concernes, et enfin modifier le code pour que cette erreur ne se reproduise plus.
		Du cote de l'interface graphique, on indiquera au joueur qu'une erreur est survenue sans preciser laquelle,
		que le service technique a ete prevenu et qu'il faudra essayer de se connecter de nouveau plus tard.*/
		
		//S'il y a un joueur mais que le mot de passe ne correspond pas...
		} else if(!(rs.getString("mdp") == mdp)) {	
			//Sur l'interface, on demandera de saisir le mdp de nouveau.
			throw new WrongPasswordException();
		} else {
			int joueurId = rs.getInt("id");
			
			//Si le joueur est admin...
			if(rs.getInt("role_id") == 1) {
				//On se reconnecte avec les droits roots.
				setConnexion(DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "root", "root"));
				setStatement(connexion.createStatement());
			}
			downloadDatabase(joueurId);
		}
	}
	
	/**
	 * Ajoute un pseudo et un mdp a la BD a condition que le pseudonyme ne soit pas deja utilisé.
	 * 
	 * @param pseudo
	 * @param mdp
	 * @throws JoueurAlreadyExistsException Si le pseudonyme est deja present dans la BD.
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 */
	public void inscription(String pseudo, String mdp) throws JoueurAlreadyExistsException, SQLException {
		ResultSet res = statement.executeQuery(""
			+ "SELECT nom "
			+ "FROM joueur "
			+ "WHERE nom = '" + pseudo + "'");
		
		if(res.next()) {									// S'il existe déjà un joueur avec ce pseudo...
			throw new JoueurAlreadyExistsException();		// On empêche d'aller plus loin.
		} else {
			statement.executeUpdate(""
				+ "INSERT INTO joueur(id, nom, mdp, xp)"
				+ "VALUES(0,'" + pseudo + "','" + mdp + "', 0)");
		}
	}
	
	/**
	 * Retire toutes les données du Joueur de la BD.
	 * @throws SQLException
	 */
	public static void desinscrire() throws SQLException {
		//TODO retirer toutes les données relatives au joueur de la BD
		
		//Ca c'est ce qui est fait en dernier normalement
		informer("DELETE FROM joueur WHERE id = ?",
			new String[] {Integer.toString(joueur.getId())});
	}
	
	/**
	 * Tente de télécharger la table du joueur demandé.<br>
	 * Renvoie s'il y a au moins une correspondance.
	 * @param pseudo
	 * @param mdp
	 * @return
	 * @throws SQLException
	 */
	private static boolean verifier(String pseudo) throws SQLException {
		joueurTable = querir(""
				+ "SELECT id, nom, mdp"
				+ "FROM `joueur`"
				+ "WHERE `joueur`.`nom` = ?"
		+ "", new String[]{pseudo});
		return joueurTable.next();
	}
	
	/**
	 * Télécharge toutes les données du jeu et celles du joueur demandé.
	 * @param 	pseudo
	 * @param 	mdp
	 * @return	0 si le joueur est introuvable<br>
	 * 			-1 si le mot de passe est incorrect<br>
	 * 			sinon l'id du joueur correspondant
	 * @throws 	SQLException S'il est impossible de se connecter a la BD.
	 * @throws 	ImprevuDBError S'il y a plus d'un resultat pour le pseudonyme demande.
	 */
	private static int connecter() throws SQLException {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "joueur", "joueur");
		//Télécharge toutes les données pertinentes de la BD.
		telecharger();
		telecharger(joueurTable.getInt("id"));
		return 1;
	}
	
	/**
	 * Télécharge toutes les données statiques de la BD pour les insérer dans des ArrayLists statiques,<br>
	 * sauf si cette méthode a déjà été appelée depuis le lancement du programme.<br>
	 * Pas besoin de télécharger plusieurs fois les données qui ne changent pas.
	 * @throws SQLException
	 */
	private static void telecharger() throws SQLException {
		if(!dejaTelecharge) {
			dejaTelecharge = !dejaTelecharge;
			
			ResultSet consommableTable = telecharger("consommable");
			
			while(consommableTable.next()) {
				int consommableId = consommableTable.getInt("id");
				
				Consommable.getNoms().add(consommableId, consommableTable.getString("nom"));
				Consommable.getDurabilites().add(consommableId, consommableTable.getInt("durabilite"));
				//Consommable.getActions().add(consommableId, Consommable.getActions().get(consommableId));
			}
		}
	}
	
	/**
	 * Télécharge l'intégralité de la table demandée.<br>
	 * A n'utiliser que pour les tables statiques,<br>
	 * c'est-à-dire ne contenant que des données qui ne sont pas susceptibles de changer.
	 * @param table
	 * @return L'integralite de la table demandée.
	 * @throws SQLException
	 */
	private static ResultSet telecharger(String table) throws SQLException {
		return querir("SELECT * FROM ?", new String[] {table});
	}
	
	/**
	 * Télécharge toutes les données du joueur.
	 * @param joueurId
	 * @throws SQLException
	 */
	private static void telecharger(int joueurId) throws SQLException {
		joueurTable = telecharger("joueur", joueurId);
		joueurTable.next();	//Peut pas utiliser les gets dans le return Joueur si on n'avance pas au premier tuple !
		
		ResultSet joueurConsommableTable = telecharger("joueur_consommable", joueurId);
		ArrayList<Consommable> joueurInventaire = new ArrayList<Consommable>();
		while(joueurConsommableTable.next()) {
			joueurInventaire.add(new Consommable(
				joueurConsommableTable.getInt("consommable_id"),
				joueurConsommableTable.getInt("durabilite")
			));
		}
		
		joueur = new Joueur(
				joueurTable.getInt("id"),
				joueurTable.getInt("xp"),
				new HashMap<String, Integer>(),
				joueurInventaire,
				joueurTable.getString("nom"));
	}
	
	/** Télécharge les données d'une table correspondants à l'id du joueur donné.
	 * @param 	table		Le nom de la table à télécharger.
	 * @param 	joueurId	L'id du joueur dans la BD dont il faut télécharger les données.
	 * @return 	L'intégralité de la table demandée uniquement avec les tuples du joueur demandé.
	 * @throws 	SQLException
	 */
	private static ResultSet telecharger(String table, int joueurId) throws SQLException {
		//setString ne doit pas être utilisé pour les noms de table et de colonne, car la méthode envoie en fait des Strings
		//Si utilisé sur un nom de table -> erreur, un nom de table ne peut être un string.
		//Sur colonne (dans WHERE par exemple) -> ça va du coup vérifier si un string existe est égal à un autre, pas ce qu'on veut.
		String colonne = (table == "joueur") ? colonne = "id" : "joueur_id";
		return querir("SELECT * FROM " + table + " WHERE " + colonne + " = ?", new String[] {Integer.toString(joueurId)});
	}
	
	/**
	 * Sauvegarde toutes les données du Joueur dans la BD.
	 * @throws SQLException
	 */
	public static void sauvegarder() throws SQLException {
		HashMap<String, Integer> stats = joueur.getStats();
		informer("UPDATE joueur SET xp = ?, pv = ?, attaque = ?, vitesse = ? WHERE id = ?",
			new Object[] {joueur.getXp(), stats.get("pv"), stats.get("attaque"), stats.get("vitesse"), joueur.getId()});
		
		//Supprimer tous les objets du joueur dans la BD...
		informer("DELETE FROM joueur_consommable WHERE joueur_id = ?",
			new Object[] {joueur.getId()});
		//J'imagine qu'il y a bel et bien un risque de perte de données si la connexion est perdue entre temps.
		//Correctif à venir.

		connexion.setAutoCommit(false);
		//...puis insérer les objets de l'ArrayList<Consommable> dans la BD.
		preparer("INSERT INTO joueur_consommable VALUES(?,?,?,?)");
		ArrayList<Consommable> inventaire = joueur.getInventaire();
		for(int i = 0; i < inventaire.size(); i++) {
			preparer(new Object[] {joueur.getId(), inventaire.get(i).getId(), inventaire.get(i).getDurabilite(), i});
			preparedStatement.addBatch();
			//System.out.println(preparedStatement);
		}
		preparedStatement.executeBatch();	//Exécute la requête
		connexion.commit();	//Dit que toutes les requêtes du batch sont définitives, pas de rollback possible.
		connexion.setAutoCommit(true);
	}
	
	/**
	 * Permet d'exécuter une requête lisant la BD.<br>
	 * Plus sécurisée car utilise PreparedStatement.
	 * @param 	sql				La requête à exécuter.
	 * @param 	valeurs			Les paramètres de la requête (ce qui remplace les ? du PreparedStatement).
	 * @return	La table correspondant à la requête demandée.
	 * @throws 	SQLException
	 */
	private static ResultSet querir(String sql, String[] valeurs) throws SQLException {
		preparer(sql, valeurs);
		return preparedStatement.executeQuery();
	}
	
	/**
	 * Permet d'exécuter une requête mettant à jour la BD.<br>
	 * Plus sécurisée car utilise PreparedStatement.
	 * @param 	sql				La requête à exécuter.
	 * @param 	valeurs			Les paramètres de la requête (ce qui remplace les ? du PreparedStatement).
	 * @return	Le nombre de lignes affectées par la requête.
	 * @throws 	SQLException
	 */
	private static int informer(String sql, Object[] valeurs) throws SQLException {
		preparer(sql, valeurs);
		return preparedStatement.executeUpdate();
	}
	
	/**
	 * Raccourci d'écriture pour :<br>
	 * {@code preparedStatement = connexion.prepareStatement(sql);}
	 * @param sql
	 * @throws SQLException
	 */
	private static void preparer(String sql) throws SQLException {
		preparedStatement = connexion.prepareStatement(sql);
	}
	
	/**
	 * Ajoute les éléments du tableau en paramètres de preparedStatement.
	 * @param valeurs
	 * @throws SQLException
	 */
	private static void preparer(Object[] valeurs) throws SQLException {
		for(int i = 0; i < valeurs.length; i++) {
			preparedStatement.setObject(i + 1, valeurs[i]);
		}
	}
	
	/**
	 * Prépare et remplit preparedStatement.
	 * @param sql
	 * @param valeurs
	 * @throws SQLException
	 */
	private static void preparer(String sql, Object[] valeurs) throws SQLException {
		preparer(sql);
		preparer(valeurs);
	}
	
	/*
	 * Getters et setters
	 */
	
	public static Joueur getJoueur() {
		return joueur;
	}
	
}
