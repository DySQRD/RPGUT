package BD;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Exceptions.ImprevuDBError;
import Jeu.Entity;
import Jeu.Mob;
import Jeu.Personnage;


/**
 * 	Classe d'interaction avec la BD. A utiliser comme suit :
 * 	<ul>
 * 		<li>Si l'utilisateur souhaite s'inscrire, utiliser BD.inscrire(String pseudo, String mdp).
 * 		<li>Sinon, s'il souhaite se connecter, utiliser BD.identifier(String pseudo, String mdp).
 * 		<li>Ces deux méthodes renvoient 1 s'il n'y a pas de problème.
 * 		<li>Toutes les données nécessaires sont téléchargées et l'accès au jeu peut être accordé.
 * 	</ul>
 * 
 * De nombreuses méthodes lancent SQLException.
 * Quand cela arrive, il faut indiquer que la connexion à la BD n'a pu être établie ou a été perdue.
 * @author Dylan Toledano
 *
 */
public class BD {
	private static Connection connexion;
	/**
	 * L'objet contenant les requêtes SQL à envoyer.<br>
	 * N'utilisant jamais plus de deux PreparedStatement en même temps, il est plus simple d'en avoir un
	 * seul. Ainsi pas besoin d'instancier un nouveau à chaque fois.<br>
	 * Le PreparedStatement aurait comme avantage, sur le Statement, d'être pré-compilé et de
	 * nettoyer les chaînes de caractères passées en paramètres de sa méthode associée setString().
	 */
	private static PreparedStatement preparedStatement;
	/**
	 * Une fois un joueur connecté, ce ResultSet contient la ligne correspondante des tables joueur,
	 * entite et stats de la BD.
	 */
	private static ResultSet joueurTable;
	/**
	 * 	Le Joueur téléchargé suite à une connexion réussie à la BD.
	 */
	private static Personnage joueur;
	/**
	 * Dans telecharger(), détermine s'il faut télécharger les données statiques.<br>
	 * Cela ne devrait arriver qu'une fois par lancement du programme.
	 */
	private static boolean dejaTelecharge = false;
	/**
	 * Tous les objets existants dans la BD.
	 */
	private static HashMap<Integer, Objet> OBJETS = new HashMap<Integer, Objet>();
	private static HashMap<Integer, HashMap<Integer, ArrayList<Entity>>> ENTITES = new HashMap<Integer, HashMap<Integer, ArrayList<Entity>>>();
	private static ArrayList<Integer> vaincus = new ArrayList<Integer>();
	/**
	 * Regex utilisé par la entreeSafe() pour vérifier la conformité des chaînes insérées.
	 */
	private static final Pattern safePattern = Pattern.compile("^[a-zA-Z0-9]{1,30}$");
	
	public static void main(String[] args) throws SQLException, ImprevuDBError, IOException {
		identifier("Marc", "Sanchez");
	}
	
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
	 * @throws IOException 
	 */
	public static int identifier(String pseudo, String mdp) throws SQLException, ImprevuDBError, IOException {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		if(!verifier(pseudo) || !entreeSafe(pseudo)) {	//S'il n'y a pas de joueur pour le pseudonyme donné...
			BDebug("Le joueur n'existe pas dans la BD !");
			return 0;	//On indiquera que le joueur demandé n'existe pas.
		} else if(!joueurTable.isLast()) {
			BDebug("Le joueur apparaît deux fois dans la BD ! A corriger vite !");
			//S'il y a plus d'un resultat... (si le premier résultat n'est pas le dernier)
			throw new ImprevuDBError();	//Une erreur qui n'est pas censée survenir.
			/*Ici, une erreur, pas une exception, car on requiert qu'un humain aille inspecter la BD pour corriger le probleme,
			prendre contact avec les joueurs concernes, et enfin modifier le code (ou plutôt la BD) pour que cette erreur ne se reproduise plus.
			Du cote de l'interface graphique, on indiquera au joueur qu'une erreur est survenue sans preciser laquelle,
			que le service technique a ete prevenu et qu'il faudra essayer de se connecter de nouveau plus tard.*/
		} else if(!(joueurTable.getString("mdp").equals(mdp) && entreeSafe(mdp))) {	
			//Sur l'interface, on demandera de saisir le mdp de nouveau.
			BDebug("Mot de passe incorrect ! Saisi: ", mdp, ", BD: ", joueurTable.getString("mdp"));
			return -1;
		} else {
			BDebug("Identification réussie !");
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
	 * @throws IOException 
	 */
	public static int inscrire(String pseudo, String mdp) throws SQLException, ImprevuDBError, IOException {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		if(verifier(pseudo)) {// S'il existe déjà un joueur avec ce pseudo...
			BDebug("Le pseudo demandé est déjà pris ! Pseudo: ", pseudo);
			return 0;
		} else if(!(entreeSafe(mdp) && entreeSafe(pseudo))) {//si mot de passe non conforme aux restrictions imposées...
			BDebug("Pseudonyme ou mot de passe non conforme ! Pseudo: ", pseudo, " MDP: ", mdp,"\n"
					+ "        Merci de n'utiliser que des chiffres et des lettres !");
			return -1;
		} else {
			//Création d'un tuple stats pour le nouveau joueur.
			//Mettre les valeurs à NULL dans la requête indique de créer un tuple avec les valeurs de bases
			//enregistrées dans la BD.
			informer("INSERT INTO stats(stats_id) VALUES(NULL)");
			//On récupère l'id du tuple inséré pour la mettre dans le tuple
			//de la table entite du joueur.
			int statsId = derniereId("stats");
			
			//Création d'un tuple entite pour le nouveau joueur.
			informer("INSERT INTO entite(entite_id, stats_id) VALUES(NULL, ?);", statsId);
			int entiteId = derniereId("entite");
			
			
			informer("INSERT INTO joueur(nom, mdp, entite_id) VALUES(?, ?, ?)", pseudo, mdp, entiteId);
				
			BDebug("Inscription réussie ! Pseudo: ", pseudo,
					"MDP: ", mdp,
					"StatsID: ", Integer.toString(statsId),
					"entiteId: ", Integer.toString(entiteId));
			return identifier(pseudo, mdp);
		}
	}
	
	/**
	 * Retire toutes les données de la BD du Joueur dont l'ID est passée en argument.
	 * @throws SQLException
	 */
	public static void desinscrire() throws SQLException {
		desinscrire(joueur.getId());
	}
	
	/**
	 * Retire toutes les données de la BD du Joueur dont l'ID est passée en argument.
	 * @throws SQLException
	 */
	private static void desinscrire(int id) throws SQLException {
		BDebug("Début de la désinscription...");

		informer("DELETE FROM stats WHERE stats_id = ?", joueurTable.getInt("stats_id"));
		informer("DELETE FROM entite WHERE entite_id = ?", joueurTable.getInt("entite_id"));
		informer("DELETE FROM connexion WHERE joueur_id = ?", id);
		informer("DELETE FROM completion WHERE joueur_id = ?", id);
		informer("DELETE FROM victoire WHERE joueur_id = ?", id);
		informer("DELETE FROM objet WHERE joueur_id = ?", id);
		//Le joueur est supprimé en dernier sinon SQL va se plaindre
		//que des tuples d'autres tables pointent vers une ID joueur non existante.
		informer("DELETE FROM joueur WHERE id = ?", id);
		
		BDebug("Désinscription complète !");
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
		BDebug("Vérification de la présence du pseudo ", pseudo, " dans la BD.");
		joueurTable = querir(
				"SELECT * "
				+ "FROM joueur "
				+ "NATURAL JOIN entite "
				+ "NATURAL JOIN stats "
				+ "WHERE nom = ? "
				, pseudo);
		//next() renvoie true s'il y a au moins un résultat.
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
	 * @throws IOException 
	 * @throws 	ImprevuDBError S'il y a plus d'un resultat pour le pseudonyme demande.
	 */
	private static int connecter() throws SQLException, IOException {
		BDebug("Connexion en cours...");
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut?allowMultiQueries=true", "joueur", "joueur");
		//Télécharge toutes les données pertinentes de la BD.
		telecharger();
		BDebug("Connexion terminée !");
		informer("INSERT INTO connexion(joueur_id) VALUES(?)", joueur.getId());
		return 1;
	}
	
	/**
	 * Télécharge l'intégralité de la table demandée.<br>
	 * A n'utiliser que pour les tables statiques,<br>
	 * c'est-à-dire ne contenant que des données qui ne sont pas susceptibles de changer.
	 * @param table
	 * @return L'integralite de la table demandée.
	 * @throws SQLException
	 */
	public static ResultSet telecharger(String table) throws SQLException {
		BDebug("Téléchargement de la table \"", table, "\"");
		return querir(
			"SELECT * "
			+ "FROM `" + table + "`"
		);
		//Je sais que je referai encore cette erreur alors je la note un peu partout :
		//Il ne faut PAS mettre les noms de colonne en paramètres d'un PreparedStatement
		//Puisque ça les entoure avec '', qui ne fonctionnent qu'avec les chaînes de caractères !
	}
	
	/**
	 * Télécharge toutes les données de la BD nécessaires au fonctionnement du jeu,<br>
	 * sauf si cette méthode a déjà été appelée depuis le lancement du programme.<br>
	 * Pas besoin de télécharger plusieurs fois les données statiques.
	 * @param joueurId
	 * @throws SQLException
	 * @throws IOException 
	 */
	private static void telecharger() throws SQLException, IOException {
		int joueurId = joueurTable.getInt("joueur_id");
		if(!dejaTelecharge) {
			dejaTelecharge = !dejaTelecharge;

			ResultSet objetTypeTable = telecharger("objet_type");
			
			while(objetTypeTable.next()) {
				int id = objetTypeTable.getInt("id");
				
				OBJETS.put(
					id,
					new Objet(
						id,
						objetTypeTable.getString("nom"),
						objetTypeTable.getInt("durabilite")
					)
				);
			}
		}
		BDebug("Téléchargement des données du joueur à l'id ", Integer.toString(joueurId));
		
		//Sélectionne tous les entites que le joueur n'a pas encore vaincu.
		//Pour rappel, la table victoire enregistre les entites vaincus par les joueurs.
		ResultSet entiteTable = querir(
			"SELECT * "
			+ "FROM joueur j, entite sp, stats st, victoire v "
			//L'entité ne doit pas déjà avoir été vaincue.
			+ "WHERE sp.entite_id != v.entite_id "
			//On récupère évidemment les stats de l'entité
			+ "AND sp.stats_id = st.stats_id "
			+ "AND v.joueur_id = " + joueurId
			//Ces entités ne peuvent être celles d'autres joueurs.
			//(les entités mobs et joueurs sont enregistrées dans la même table !)
			+ " AND j.entite_id != v.entite_id "
		);
		while(entiteTable.next()) {
			ENTITES.get(entiteTable.getInt("niveau_id")).get(entiteTable.getInt("map_id")).add(
				new Mob(entiteTable)
			);
		}
		
		joueur = new Personnage(joueurTable);
	}
	
	/** Télécharge les données d'une table correspondants à l'id du joueur donné.
	 * @param 	table		Le nom de la table à télécharger.
	 * @param 	joueurId	L'id du joueur dans la BD dont il faut télécharger les données.
	 * @return 	L'intégralité de la table demandée uniquement avec les tuples du joueur demandé.
	 * @throws 	SQLException
	 */
	public static ResultSet telecharger(String table, int joueurId) throws SQLException {
		return querir("SELECT * FROM " + table + " WHERE joueur_id = ?", joueurId);
	}
	
	/**
	 * Sauvegarde toutes les données du Joueur dans la BD.
	 * @throws SQLException
	 */
	public static void sauvegarder() throws SQLException {
		
		int stats_id = joueurTable.getInt("stats_id");
		
		informer("UPDATE entite SET xp = ?, x = ?, y = ?", joueur.getTotalXp(), joueur.getPosX(), joueur.getPosY());
		
		Stats stats = joueur.getStats();
		informer("UPDATE stats pv = ?, attaque = ?, defense = ? WHERE id = ?",
			stats.get("pv"),
			stats.get("attaque"),
			stats.get("defense"),
			stats_id
		);
		
		//Supprimer tous les objets du joueur dans la BD...
		informer("DELETE FROM objet WHERE joueur_id = ?", joueur.getId());

		connexion.setAutoCommit(false);
		//...puis insérer les objets de l'Inventaire dans la BD.
		preparer("INSERT INTO objet VALUES(?,?,?,?)");
		Inventaire inventaire = joueur.getInventaire();
		Set<Integer> cles = inventaire.keySet();
		for(Integer cle : cles) {
			Objet objet = inventaire.get(cle);
			preparer(joueur.getId(), objet.getId(), objet.getDurabilite(), cle);
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		
		//Ajoute à la BD les ids des mobs vaincus depuis le dernier chargement de sauvegarde.
		preparer("INSERT INTO victoire VALUES(?,?)");
		for(int i = 0; i < vaincus.size(); i++) {
			preparer(joueur.getInventaire(), vaincus.get(i));
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		vaincus.clear();	//On vide la liste pour que les mêmes mobs ne soient pas envoyés de nouveau.
		connexion.commit();	//Dit que toutes les requêtes du batch sont définitives, pas de rollback possible.
		connexion.setAutoCommit(true);
	}

	/**
	 * Permet d'exécuter une requête lisant la BD.
	 * @param 	sql				La requête à exécuter.
	 * @param 	valeurs			Les paramètres de la requête (ce qui remplace les ? du PreparedStatement).
	 * @return	La table correspondant à la requête demandée.
	 * @throws 	SQLException
	 */
	private static ResultSet querir(String sql, Object... valeurs) throws SQLException {
		preparer(sql, valeurs);
		BDebugRequete();
		return preparedStatement.executeQuery();
	}
	
	/**
	 * Permet d'exécuter une requête mettant à jour la BD.
	 * @param 	sql				La requête à exécuter.
	 * @param 	valeurs			Les paramètres de la requête (ce qui remplace les ? du PreparedStatement).
	 * @return	Le nombre de lignes affectées par la requête.
	 * @throws 	SQLException
	 */
	private static int informer(String sql, Object... valeurs) throws SQLException {
		preparer(sql, valeurs);
		BDebugRequete();
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
	 * Ajoute les valeurs en paramètres de preparedStatement.
	 * @param valeurs
	 * @throws SQLException
	 */
	private static void preparer(Object... valeurs) throws SQLException {
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
	private static void preparer(String sql, Object... valeurs) throws SQLException {
		preparer(sql);
		preparer(valeurs);
	}
	
	/**
	 * Récupère l'attribut id le plus élevé de la table dont le nom est passé en argument.<br>
	 * Ne fonctionne pas avec les tables n'ayant pas de colonne dont le nom a la forme <nom de la table>_id.<br>
	 * Elle n'est cependant jamais utilisée de cette manière.
	 * @param table
	 * @return
	 * @throws SQLException 
	 */
	private static int derniereId(String tableName) throws SQLException {
		ResultSet table = querir("SELECT MAX(" + tableName + "_id) " + tableName + "_id FROM " + tableName);
		table.next();
		return table.getInt(tableName + "_id");
	}
	
	/**
	 * Ajoute l'id d'une instance de mob vaincue par le joueur
	 * à la liste des mobs vaincus depuis le dernier chargement de sauvegarde.
	 * @param mob_id
	 */
	public static void victoire(int entite_id) {
		vaincus.add(entite_id);
	}

	/**
	 * Une fonction me permettant de personnaliser syso au cas où j'en aurais besoin.
	 * @param options	Les chaînes à afficher.
	 */
	private static void BDebug(String... options) {
		String message = "BDebug: ";
		for(int i = 0; i < options.length; i++) {
			message = message + options[i];
		}
		System.out.println(message);
	}
	
	/**
	 * Ecrit la requête contenue dans le PreparedStatement dans la console.
	 */
	private static void BDebugRequete() {
		String prepStr = preparedStatement.toString();	//PreparedStatement en String contient la requête qui sera envoyée.
		BDebug("Requête: ", prepStr.substring(43, prepStr.length()));//Retire la partie de prepStr qui n'est pas la requête
	}

	/**
	 * Vérifie qu'une chaîne entrée par l'utilisateur respecte les critères d'insertion.
	 * @param entree
	 * @return true si la chaîne est conforme.
	 */
	private static boolean entreeSafe(String entree) {
		BDebug("Vérification de la conformité de l'entrée: ", entree);
		return safePattern.matcher(entree).matches();
	}

	/*
	 * Getters et setters
	 */
	
	public static Personnage getPersonnage() {
		return joueur;
	}

	public static HashMap<Integer, Objet> getObjets() {
		return OBJETS;
	}
}
