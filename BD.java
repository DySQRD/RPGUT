import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
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
	 * Dans telecharger(), détermine s'il faut télécharger les données statiques.<br>
	 * Cela ne devrait arriver qu'une fois par lancement du programme.
	 */
	private static boolean dejaTelecharge = false;
	
	/**
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 */
	public BD() throws SQLException {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "joueur", "joueur");
		//Eventuellement ajouter "?allowMultiQueries=true" s'il faut exécuter plusieurs commandes en un statement.
		//Sinon voir si le "batching" résout ce problème de façon plus sécurisée.
	}
	
	/**
	 * 
	 * @param 	pseudo
	 * @param 	mdp
	 * @return	l'id du Joueur correspondant
	 * @throws 	JoueurIntrouvableException Si le pseudonyme n'existe pas dans la BD.
	 * @throws 	SQLException S'il est impossible de se connecter a la BD.
	 * @throws 	ImprevuDBError S'il y a plus d'un resultat pour le pseudonyme demande.
	 * @throws 	MauvaisMDPException Si le mot de passe est incorrect.
	 */
	public int connecter(String pseudo, String mdp) throws JoueurIntrouvableException, ImprevuDBError, SQLException, MauvaisMDPException {
		ResultSet rs = querir(""
				+ "SELECT id, nom, mdp"
				+ "FROM `joueur`"
				+ "WHERE `joueur`.`nom` = ? AND mdp = ?"
		+ "", new String[]{pseudo, mdp});
		
		if(!rs.next()) {	//S'il n'y a pas de joueur pour le pseudonyme donné...
			throw new JoueurIntrouvableException();	//On demandera de saisir le pseudo de nouveau
			
		} else if(!rs.isLast()) {	//S'il y a plus d'un resultat... (si le premier résultat n'est pas le dernier)
			throw new ImprevuDBError();	//Une erreur qui n'est pas censée survenir.
		/*Ici, une erreur, pas une exception, car on requiert qu'un humain aille inspecter la BD pour corriger le probleme,
		prendre contact avec les joueurs concernes, et enfin modifier le code pour que cette erreur ne se reproduise plus.
		Du cote de l'interface graphique, on indiquera au joueur qu'une erreur est survenue sans preciser laquelle,
		que le service technique a ete prevenu et qu'il faudra essayer de se connecter de nouveau plus tard.*/
		
		//S'il y a un joueur mais que le mot de passe ne correspond pas...
		} else if(!(rs.getString("mdp") == mdp)) {	
			//Sur l'interface, on demandera de saisir le mdp de nouveau.
			throw new MauvaisMDPException();
		} else {
			return rs.getInt("id");
		}
	}
	
	/**
	 * Ajoute un pseudo et un mdp a la BD à condition que le pseudonyme ne soit pas deja utilisé.
	 * 
	 * @param pseudo
	 * @param mdp
	 * @throws JoueurExisteException Si le pseudonyme est deja present dans la BD.
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 */
	public void inscrire(String pseudo, String mdp) throws JoueurExisteException, SQLException {
		ResultSet res = querir(""
			+ "SELECT nom "
			+ "FROM joueur "
			+ "WHERE nom = ?"
		+ "", new String[]{pseudo});
		
		if(res.next()) {					// S'il existe déjà un joueur avec ce pseudo...
			throw new JoueurExisteException();	// On empêche d'aller plus loin.
		} else {
			informer(""
				+ "INSERT INTO joueur(nom, mdp)"
				+ "VALUES(?, ?)"
			+ "", new String[]{pseudo, mdp});
		}
	}
	
	public void desinscrire(int joueurId) throws SQLException {
		//TODO retirer toutes les données relatives au joueur de la BD
		
		//Ca c'est ce qui est fait en dernier normalement
		informer("DELETE FROM joueur WHERE id = ?",
			new String[] {Integer.toString(joueurId)});
	}
	public void desinscrire(Joueur joueur) throws SQLException {
		desinscrire(joueur.getId());
	}
	
	/**
	 * Télécharge toutes les données du joueur.
	 * @param joueurId
	 * @throws SQLException
	 */
	public Joueur telecharge(int joueurId) throws SQLException {
		ResultSet joueurTable = telecharge("joueur", joueurId);
		ResultSet joueurConsommableTable = telecharge("joueur_consommable", joueurId);
		
		ArrayList<Consommable> inventaire = new ArrayList<Consommable>();
		while(joueurConsommableTable.next()) {
			inventaire.add(new Consommable(
				joueurConsommableTable.getInt("id"),
				joueurConsommableTable.getInt("durabilite")
			));
		}
		
		return new Joueur(
			joueurTable.getString("nom"),
			joueurTable.getInt("id"),
			joueurTable.getInt("xp"),
			joueurTable.getInt("pv"),
			joueurTable.getInt("attaque"),
			joueurTable.getInt("vitesse"));
	}
	
	/**
	 * Télécharge toutes les données statiques de la BD pour les insérer dans des ArrayLists statiques,<br>
	 * sauf si cette méthode a déjà été appelée depuis le lancement du programme.<br>
	 * Pas besoin de télécharger plusieurs fois les données qui ne changent pas.
	 * @throws SQLException
	 */
	public void telecharge() throws SQLException {
		if(!dejaTelecharge) {
			dejaTelecharge = !dejaTelecharge;
			
			ResultSet consommableTable = telecharge("consommable");
			
			while(consommableTable.next()) {
				int consommableId = consommableTable.getInt("id");
				
				Consommable.getNoms().add(consommableId, consommableTable.getString("nom"));
				Consommable.getDurabilites().add(consommableId, consommableTable.getInt("durabilite"));
				Consommable.getActions().add(consommableId, Consommable.getActions().get(consommableId));
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
	private ResultSet telecharge(String table) throws SQLException {
		return querir("SELECT * FROM ?", new String[] {table});
	}
	
	/**
	 * Télécharge les données d'une table correspondants à l'id du joueur donné.
	 * @param 	table		Le nom de la table à télécharger.
	 * @param 	joueurId	L'id du joueur dans la BD dont il faut télécharger les données.
	 * @return 	L'integralite de la table demandée uniquement avec les tuples du joueur demandé.
	 * @throws 	SQLException
	 */
	private ResultSet telecharge(String table, int joueurId) throws SQLException {
		return querir("SELECT * FROM ? WHERE joueur_id = ?", new String[] {table, Integer.toString(joueurId)});
	}
	
	/**
	 * Permet d'exécuter une requête lisant la BD.<br>
	 * Plus sécurisée car utilise PreparedStatement.
	 * @param 	sql				La requête à exécuter.
	 * @param 	valeurs			Les paramètres de la requête (ce qui remplace les ? du PreparedStatement).
	 * @return	La table correspondant à la requête demandée.
	 * @throws 	SQLException
	 */
	private ResultSet querir(String sql, String[] valeurs) throws SQLException {
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
	private int informer(String sql, String[] valeurs) throws SQLException {
		preparer(sql, valeurs);
		return preparedStatement.executeUpdate();
	}
	
	/**
	 * Prépare et remplit preparedStatement.
	 * @param sql
	 * @param valeurs
	 * @throws SQLException
	 */
	private void preparer(String sql, String[] valeurs) throws SQLException {
		System.out.println(sql);
		preparedStatement = connexion.prepareStatement(sql);
		for(int i = 0; i < valeurs.length; i++) {
			preparedStatement.setString(i + 1, valeurs[i]);
		}
	}
	
	
}
