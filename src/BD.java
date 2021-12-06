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
	 * Tous les objets existants dans la BD.
	 */
	private static HashMap<Integer, Objet> OBJETS = new HashMap<Integer, Objet>();
	private static HashMap<Integer, Entite> ENTITES = new HashMap<Integer, Entite>();
	private static HashMap<Integer, Stats> STATS = new HashMap<Integer, Stats>();
	/**
	 * Le premier Integer correspond à l'id de l'entite qui drop un objet.<br>
	 * Le second est l'id de l'objet à CLONER depuis l'ArrayList OBJETS.
	 */
	private static HashMap<Integer, Integer> DROPS = new HashMap<Integer, Integer>();
	private static ArrayList<Integer> vaincus = new ArrayList<Integer>();
	
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
		} else if(!(joueurTable.getString("mdp").equals(mdp))) {	
			//Sur l'interface, on demandera de saisir le mdp de nouveau.
			BDebug("Mot de passe incorrect ! Saisi: \"", mdp, "\", BD: \"", joueurTable.getString("mdp"), "\"");
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
	 */
	public static int inscrire(String pseudo, String mdp) throws SQLException, ImprevuDBError {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		Pattern p = Pattern.compile("[a-zA-Z0-9]{1,30}");
		if(verifier(pseudo)) {// S'il existe déjà un joueur avec ce pseudo...
			BDebug("Le pseudo demandé est déjà pris ! Pseudo: ", pseudo);
			return 0;
		} else if(!(p.matcher(mdp).matches() && p.matcher(pseudo).matches())) {//si mot de passe non conforme aux restrictions imposées...
			BDebug("Pseudonyme ou mot de passe non conforme ! Pseudo: ", pseudo, " MDP: ", mdp,"\n"
					+ "        Merci de n'utiliser que des chiffres et des lettres !");
			return -1;
		} else {
			//Création d'un tuple stats pour le nouveau joueur.
			//Mettre les valeurs à NULL dans la requête indique de créer un tuple avec les valeurs de bases
			//enregistrées dans la BD.
			informer("INSERT INTO stats(id) VALUES(NULL)");
			ResultSet statsIdTable = querir("SELECT MAX(id) id FROM stats");
			//On récupère l'id du tuple inséré pour la mettre dans le tuple
			//de la table spawn du joueur.
			statsIdTable.next();
			int statsId = statsIdTable.getInt("id");
			
			//Création d'un tuple spawn pour le nouveau joueur.
			informer("INSERT INTO spawn(id, stats_id) VALUES(NULL, ?);", statsId);
			ResultSet spawnIdTable = querir("SELECT MAX(id) id FROM spawn");
			spawnIdTable.next();
			int spawnId = spawnIdTable.getInt("id");
			
			
			informer("INSERT INTO joueur(nom, mdp, spawn_id) VALUES(?, ?, ?)", pseudo, mdp, spawnId);
				
			BDebug("Inscription réussie ! Pseudo: ", pseudo,
					"MDP: ", mdp,
					"StatsID: ", Integer.toString(spawnId),
					"SpawnId: ", Integer.toString(spawnId));
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
	public static void desinscrire(int id) throws SQLException {
		BDebug("Début de la désinscription...");
		//TODO retirer toutes les données relatives au joueur de la BD

		ResultSet spawnIdTable = querir("SELECT spawn_id FROM joueur WHERE id = ?", id);
		spawnIdTable.next();
		int spawnId = spawnIdTable.getInt("spawn_id");
		informer("DELETE FROM stats WHERE id = (SELECT stats_id FROM spawn WHERE id = ?)", spawnId);
		informer("DELETE FROM spawn WHERE id = (SELECT spawn_id FROM joueur WHERE id = ?)", id);
		informer("DELETE FROM connexion WHERE joueur_id = ?", id);
		informer("DELETE FROM completion WHERE joueur_id = ?", id);
		informer("DELETE FROM victoire WHERE joueur_id = ?", id);
		informer("DELETE FROM inventaire WHERE joueur_id = ?", id);
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
				+ "NATURAL JOIN "
					+ "(SELECT id spawn_id, map_id, entite_id, x, y, stats_id "
					+ "FROM spawn) sp "
				+ "NATURAL JOIN "
					+ "(SELECT id stats_id, xp, pv, attaque, defense "
					+ "FROM stats) st "
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
	 * @throws 	ImprevuDBError S'il y a plus d'un resultat pour le pseudonyme demande.
	 */
	private static int connecter() throws SQLException {
		BDebug("Connexion en cours...");
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "joueur", "joueur");
		//Télécharge toutes les données pertinentes de la BD.
		telecharger();
		telecharger(joueurTable.getInt("id"));
		BDebug("Connexion terminée !");
		informer("INSERT INTO connexion(id, joueur_id) VALUES(?,?)", 0, joueur.id);
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

			ResultSet objetTable = telecharger("objet");
			ResultSet statsTable = telecharger("stats");
			ResultSet entiteTable = telecharger("entite");
			ResultSet dropTable = telecharger("drop");
			
			while(objetTable.next()) {
				int id = objetTable.getInt("id");
				
				OBJETS.put(
					id,
					new Objet(
						id,
						objetTable.getString("nom"),
						objetTable.getInt("durabilite")
					)
				);
			}
			
			//Télécharge les stats du joueur.
			while(statsTable.next()) {
				STATS.put(
					statsTable.getInt("id"),
					new Stats(
						statsTable.getInt("xp"),
						statsTable.getInt("pv"),
						statsTable.getInt("attaque"),
						statsTable.getInt("defense")
					)
				);
			}
			
			/*while(entiteTable.next()) {
				int id = entiteTable.getInt("id");
				String nom = entiteTable.getString("nom");
				HashMap<String, Integer> stats = STATS.get(id);
				ArrayList<Objet> inventaire = new ArrayList<Objet>();
				entiteTable.getInt("stats_id");
				
				BD.ENTITES.add(
					id,
					new Entite(
						id,
						nom,
						stats,
						inventaire
					)
				);
			}*/
			
			//Tous les drops possibles des monstres.
			while(dropTable.next()) {
				DROPS.put(dropTable.getInt("mob_id"), dropTable.getInt("objet_id"));
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
	 * Télécharge toutes les données du joueur.
	 * @param joueurId
	 * @throws SQLException
	 */
	private static void telecharger(int joueurId) throws SQLException {
		BDebug("Téléchargement des données du joueur à l'id ", Integer.toString(joueurId));

		/*ResultSet statsTable = querir(
			"SELECT * "
			+ "FROM stats "
			+ "WHERE id != joueur.id"
		);*/
		
		//Sélectionne tous les spawns que le joueur n'a pas encore vaincu.
		//Pour rappel, la table victoire enregistre les spawns vaincus par les joueurs.
		ResultSet spawnTable = querir(
			"SELECT * "
			+ "FROM spawn "
			+ "INNER JOIN victoire "
			+ "ON id != spawn_id "
			+ "WHERE joueur_id = " + joueurId
		);
		
		while(spawnTable.next()) {
			
		}
		
		ResultSet inventaireTable = telecharger("inventaire", joueurId);
		Inventaire joueurInventaire = new Inventaire();
		while(inventaireTable.next()) {
			joueurInventaire.put(
				inventaireTable.getInt("ordre"),
				new Objet(
					inventaireTable.getInt("objet_id"),
					inventaireTable.getString("nom"),
					inventaireTable.getInt("durabilite")
			));
		}
		
		
		joueur = new Joueur(
			joueurTable.getInt("id"),
			new Stats(0, 0, 0, 0),
			joueurInventaire);
	}
	
	/** Télécharge les données d'une table correspondants à l'id du joueur donné.
	 * @param 	table		Le nom de la table à télécharger.
	 * @param 	joueurId	L'id du joueur dans la BD dont il faut télécharger les données.
	 * @return 	L'intégralité de la table demandée uniquement avec les tuples du joueur demandé.
	 * @throws 	SQLException
	 */
	private static ResultSet telecharger(String table, int joueurId) throws SQLException {
		//setString ne doit pas être utilisé pour les noms de table et de colonne, car la méthode écrit du texte entre guillemets dans la requête
		//Si utilisé sur un nom de table -> erreur, un nom de table ne peut être un string.
		//Sur colonne (dans WHERE par exemple) -> ça va du coup vérifier si un string existe est égal à un autre, pas ce qu'on veut.
		
		
		return querir(
			"SELECT * "
			+ "FROM " + table
			+ " WHERE ? = ?",
			(table == "joueur") ? "id" : "joueur_id",
			joueurId);
	}
	
	/**
	 * Sauvegarde toutes les données du Joueur dans la BD.
	 * @throws SQLException
	 */
	public static void sauvegarder() throws SQLException {
		Stats stats = joueur.getStats();
		informer("UPDATE stats SET xp = ?, pv = ?, attaque = ?, defense = ? WHERE id = ?",
			stats.get("xp"),
			stats.get("pv"),
			stats.get("attaque"),
			stats.get("defense"),
			joueurTable.getInt("stats_id"));
		
		//Supprimer tous les objets du joueur dans la BD...
		informer("DELETE FROM inventaire WHERE joueur_id = ?",
			joueur.getId());
		//J'imagine qu'il y a bel et bien un risque de perte de données si la connexion est perdue entre temps.
		//Correctif à venir.

		connexion.setAutoCommit(false);
		//...puis insérer les objets de l'Inventaire dans la BD.
		preparer("INSERT INTO inventaire VALUES(?,?,?,?)");
		Inventaire inventaire = joueur.getInventaire();
		for(int i = 0; i < inventaire.size(); i++) {
			Objet objet = inventaire.get(i);
			preparer(joueur.getId(), objet.getId(), objet.getDurabilite(), i);
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		
		//Ajoute à la BD les ids des mobs vaincus depuis le dernier chargement de sauvegarde.
		preparer("INSERT INTO victoire VALUES(?,?)");
		for(int i = 0; i < vaincus.size(); i++) {
			preparer(joueur.getId(), vaincus.get(i));
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		vaincus.clear();	//On vide la liste pour que les mêmes mobs ne soient pas envoyés de nouveau.
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
	private static ResultSet querir(String sql, Object... valeurs) throws SQLException {
		preparer(sql, valeurs);
		BDebugRequete();
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
	 * Ajoute l'id d'une instance de mob vaincue par le joueur
	 * à la liste des mobs vaincus depuis le dernier chargement de sauvegarde.
	 * @param mob_id
	 */
	public static void victoire(int mob_id) {
		vaincus.add(mob_id);
	}
	
	/*
	 * Getters et setters
	 */
	
	public static Joueur getJoueur() {
		return joueur;
	}

	
	/*
	 * Getters pour les données STATIQUES de la BD.
	 */
	public static HashMap<Integer, Objet> getObjets() {
		return OBJETS;
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
}
