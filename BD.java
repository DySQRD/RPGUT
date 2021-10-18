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
	
	private void downloadDatabase(int joueurId) throws SQLException {
		downloadConsommable(joueurId);
		//setEffet();
		//setMob();
		//setNiveau();
	}
	
	/**
	 * 
	 * @param table
	 * @return L'integralite de la table demandee.
	 * @throws SQLException
	 */
	private ResultSet getTable(String table) throws SQLException {
		return statement.executeQuery(""
				+ "SELECT *"
				+ "FROM `" + table + "`"
			+ "");
	}
	/**
	 * 
	 * @param table
	 * @param joueurId
	 * @return L'integralite de la table demandée uniquement avec les tuples du joueur demandé.
	 * @throws SQLException
	 */
	private ResultSet getTable(String table, int joueurId) throws SQLException {
		return statement.executeQuery(""
				+ "SELECT *"
				+ "FROM `" + table + "`"
				+ "WHERE joueur_id = " + joueurId
			+ "");
	}
	
	
	
	/*
	 * Copie des tables STATIQUES de la BD en ArrayLists.
	 */
	
	private void downloadConsommable() throws SQLException {
		ResultSet consommable = getTable("consommable");
		while(consommable.next()) {
			int consommableId = consommable.getInt("id");
			Consommable.getNoms().add(consommableId, consommable.getString("nom"));
			Consommable.getDurabilites().add(consommableId, consommable.getInt("durabilite"));
			//
			//Consommable.getEffets().add(consommableId, EFFET.valueOf(consommable.getString("durabilite")));
		}
	}
	//TODO 
	private void downloadJoueurConsommable(int joueurId) throws SQLException {
		ResultSet joueurConsommable = getTable("joueur_consommable");
		while(joueurConsommable.next()) {
			int consommableId = joueurConsommable.getInt("id");
		}
	}
	
	
	
	/*
	 * Getters et setters classiques.
	 */
	
	public Connection getConnexion() {
		return connexion;
	}

	public void setConnexion(Connection connexion) {
		this.connexion = connexion;
	}

	public Statement getStatement() {
		return statement;
	}

	public void setStatement(Statement statement) {
		this.statement = statement;
	}
	
	
}
