import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import Exceptions.JoueurAlreadyExistsException;
import Exceptions.JoueurNotFoundException;
import Exceptions.WrongPasswordException;


/**
 * Classe d'interaction avec la BD.
 * @author dydyt
 *
 */
public class CRUD {
	//"connexion" en francais, ca permettra aussi de faire la distinction entre les methodes de JDBC et celles que je cree.
	private Connection connexion;
	private Statement statement;
	
	/**
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 */
	public CRUD() throws SQLException {
		setConnexion(DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "joueur", "joueur"));
		setStatement(connexion.createStatement());
	}
	
	/**
	 * 
	 * @param pseudo
	 * @param mdp
	 * @throws JoueurNotFoundException Si le pseudonyme n'existe pas dans la BD.
	 * @throws SQLException S'il est impossible de se connecter a la BD.
	 * @throws WrongPasswordException Si le mot de passe est incorrect.
	 */
	public void connexion(String pseudo, String mdp) throws JoueurNotFoundException, SQLException, WrongPasswordException {
		ResultSet res = statement.executeQuery(""
			+ "SELECT nom, mdp"
			+ "FROM `compte`, `mob`"
			+ "WHERE `nom` = '" + pseudo
		+ "");
		if(!(res.getString("mdp") == mdp)) {
			throw new WrongPasswordException();
		}
	}
	
	/**
	 * Ajoute un pseudo et un mdp a la BD a condition que le pseudonyme ne soit pas deja utilise.
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
		if(res.next()) {
			throw new JoueurAlreadyExistsException();
		} else {
			statement.executeUpdate(""
				+ "INSERT INTO joueur(id, nom, mdp, xp)"
				+ "VALUES(0,'" + pseudo + "','" + mdp + "', 0)");
		}
	}

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
