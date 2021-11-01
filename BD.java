import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

import Exceptions.ImprevuDBError;


/**
 * 	Classe d'interaction avec la BD. A utiliser comme suit :
 * 	<ul>
 * 		<li>Si l'utilisateur souhaite s'inscrire, utiliser BD.inscrire(String pseudo, String mdp).
 * 		<li>Sinon, s'il souhaite se connecter, utiliser BD.identifier(String pseudo, String mdp).
 * 		<li>Ces deux méthodes renvoient 1 s'il n'y a pas de problème.
 * 		<li>Toutes les données nécessaires sont téléchargées et l'accès au jeu peut être accordé.
 * 	</ul>
 * 
 * Plusieurs méthodes lancent SQLException.
 * Quand cela arrive, il faut indiquer que la connexion à la BD n'a pu être établie ou a été perdue.
 * @author Dylan Toledano
 *
 */
public class BD {
	private static Connection connexion; 
	//Eventuellement ajouter "?allowMultiQueries=true" s'il faut exécuter plusieurs commandes en un statement.
	//Sinon voir si le "batching" résout ce problème de façon plus sécurisée.
	private static PreparedStatement preparedStatement;
	private static ResultSet joueurTable;
	/**
	 * 	Le Joueur téléchargé suite à une connexion réussie à la BD.
	 */
	private static Joueur joueur;
	/**
	 * Dans telecharger(), détermine s'il faut télécharger les données statiques.<br>
	 * Cela ne devrait arriver qu'une fois par lancement du programme.
	 */
	private static boolean dejaTelecharge = false;
	
	/**
	 * Méthode à utiliser pour authentifier un joueur avant de le connecter.<br>
	 * Vérifie que le pseudo existe et que le mdp correspond.
	 * @param pseudo		
	 * @param mdp
	 * @return	<ul> 
	 * 				<li>0 si le pseudo n'est pas dans la BD.
	 * 				<li>-1 si le mdp ne correspond pas.
	 * 				<li>1 sinon.
	 * 			</ul>
	 * @throws SQLException
	 * @throws ImprevuDBError	Si le pseudo apparaît plusieurs fois dans la BD.
	 */
	public static int identifier(String pseudo, String mdp) throws SQLException, ImprevuDBError {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		if(!verifier(pseudo)) {	//S'il n'y a pas de joueur pour le pseudonyme donné...
			return 0;	//On indiquera que le joueur demandé n'existe pas.
		} else if(!joueurTable.isLast()) {
			//S'il y a plus d'un resultat... (si le premier résultat n'est pas le dernier)
			throw new ImprevuDBError();	//Une erreur qui n'est pas censée survenir.
			/*Ici, une erreur, pas une exception, car on requiert qu'un humain aille inspecter la BD pour corriger le probleme,
			prendre contact avec les joueurs concernes, et enfin modifier le code (ou plutôt la BD) pour que cette erreur ne se reproduise plus.
			Du cote de l'interface graphique, on indiquera au joueur qu'une erreur est survenue sans preciser laquelle,
			que le service technique a ete prevenu et qu'il faudra essayer de se connecter de nouveau plus tard.*/
		} else if(!(joueurTable.getString("mdp") == mdp)) {	
			//Sur l'interface, on demandera de saisir le mdp de nouveau.
			return -1;
		} else {
			return connecter();
		}
	}

	/**
	 * Ajoute un pseudo et un mdp a la BD à condition que le pseudonyme ne soit pas deja utilisé.
	 * 
	 * @param 	pseudo
	 * @param 	mdp
	 * @return	0 si le pseudo existe déjà dans la BD<br>
	 * 			-1 si le mdp ne correspond pas aux critères imposés<br>
	 * 			Sinon l'id du nouveau joueur
	 * @throws 	SQLException 	S'il est impossible de se connecter a la BD.
	 * @throws 	ImprevuDBError 	
	 */
	public static int inscrire(String pseudo, String mdp) throws SQLException {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		if(verifier(pseudo)) {// S'il existe déjà un joueur avec ce pseudo...
			return 0;
		} else if(false) {//si mot de passe non conforme aux restrictions imposées...
			return -1;	//pas encore rédigée
		} else {
			informer(""
				+ "INSERT INTO joueur(nom, mdp)"
				+ "VALUES(?, ?)"
			+ "", new String[]{pseudo, mdp});
			return connecter();
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
