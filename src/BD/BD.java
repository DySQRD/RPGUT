package BD;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Pattern;

import Exceptions.ImprevuDBError;
import Jeu.FirstApplication;
import Jeu.Mob;
import Jeu.Personnage;


/**
 * 	<p>
 * 		Classe d'interaction avec la base de données.<br>
 * 		Il ne peut y avoir qu'un seul joueur connecté à une seule BD par lancement de programme.<br>
 * 		Pour cette raison, il n'y a pas de BD à instancier, et tous les attributs et méthodes de cette classe sont statiques.<br>
 * 		Cela permet d'y accéder depuis n'importe où dans le programme.
 * 	</p>
 * 	Si aucun joueur n'est connecté, l'utiliser comme suit :
 * 	<ul>
 * 		<li>Si l'utilisateur souhaite s'inscrire, utiliser BD.inscrire(String pseudo, String mdp).</li>
 * 		<li>Sinon, s'il souhaite se connecter, utiliser BD.identifier(String pseudo, String mdp).</li>
 * 		<li>Ces deux méthodes renvoient 1 en cas de succès.<br>
 * 		Sinon se référer à leur doc.</li>
 * 		<li>Toutes les données nécessaires sont téléchargées et l'accès au jeu peut être accordé.</li>
 * 	</ul>
 * 
 * 	D'autres méthodes deviennent ensuite utilisables :
 * 	<ul>
 * 		<li>BD.sauvegarder() enregistre la progression du joueur dans la BD.</li>
 * 		<li>BD.deconnecter() est à lancer lorsque le joueur souhaite arrêter de jouer sans fermer le programme.<br>
 * 		Cette méthode ne sauvegarde pas automatiquement !</li>
 * 		<li>BD.desinscrire() supprime les données du joueur connecté de la BD puis le déconnecte.</li>
 * 		<li>BD.victoire() enregistre une victoire du joueur connecté face à un mob, de sorte à ce que ce mob
 * 		ne réapparaisse plus jamais pour ce joueur.</li>
 * 	</ul>
 * 
 * 	<p>
 * 		De nombreuses méthodes lancent SQLException.
 * 		Quand cela arrive, il faut indiquer que la connexion à la BD n'a pu être établie ou a été perdue.
 * 	</p>
 * 
 * 	@author Dylan Toledano
 *
 */
public class BD {
	static Connection connexion;
	/**
	 * L'objet contenant les requêtes SQL à envoyer.<br>
	 * N'utilisant jamais plus de deux PreparedStatement en même temps, il est plus simple d'en avoir un
	 * seul. Ainsi pas besoin d'instancier un nouveau à chaque fois.<br>
	 * Le PreparedStatement aurait comme avantage, sur le Statement, d'être pré-compilé et de
	 * nettoyer les chaînes de caractères passées en paramètres de sa méthode associée setString().
	 */
	static PreparedStatement preparedStatement;
	/**
	 * Une fois un joueur connecté, ce ResultSet contient la ligne correspondante des tables joueur,
	 * entite et stats de la BD.
	 */
	static ResultSet joueurTable;
	/**
	 * 	Le joueur téléchargé suite à une connexion réussie à la BD.
	 */
	static Personnage personnage;
	/**
	 * Dans telecharger(), détermine s'il faut télécharger les collections statiques.<br>
	 * Cela ne devrait arriver qu'une fois par lancement du programme.
	 */
	static boolean dejaTelecharge = false;
	/**
	 * Tous les types d'objets existants dans la BD.<br>
	 * Collection statique, çad dont les données sont communes à tous les joueurs
	 * et qui n'est pas téléchargée de nouveau à chaque connexion.
	 */
	static HashMap<Integer, ObjetType> objetTypes = new HashMap<Integer, ObjetType>();
	/**
	 * Tous les types d'entites existants dans la BD.<br>
	 * Collection statique, çad dont les données sont communes à tous les joueurs
	 * et qui n'est pas téléchargée de nouveau à chaque connexion.
	 */
	static HashMap<Integer, EntiteType> entiteTypes = new HashMap<Integer, EntiteType>();
	/**
	 * Toutes les entités non encore vaincues par le joueur.
	 */
	private static HashMap<Integer, HashMap<Integer, HashMap<Integer, Mob>>> entites = new HashMap<Integer, HashMap<Integer, HashMap<Integer, Mob>>>();
	/**
	 * Enregistre les entités vaincues depuis la dernière sauvegarder().<br>
	 * Le contenu de cette ArrayList est envoyé dans la BD puis effacé à chaque sauvegarder().
	 */
	static ArrayList<Integer> vaincus = new ArrayList<Integer>();
	static HashMap<Integer, Capacite> capacites = new HashMap<Integer, Capacite>();
	static HashMap<Integer, Movepool> movepools = new HashMap<Integer, Movepool>();
	/**
	 * Regex utilisé par la entreeSafe() pour vérifier la conformité des chaînes insérées.
	 */
	static final Pattern safePattern = Pattern.compile("^[a-zA-Z0-9]{1,30}$");
	
	public static void main(String[] args) throws SQLException, ImprevuDBError, IOException {
		identifier("nice", "notnice");
	}
	
	/**
	 * Méthode à utiliser pour authentifier un joueur avant de le connecter.<br>
	 * Vérifie que le pseudo existe et que le mdp correspond.
	 * @param pseudo	
	 * Le pseudo du joueur.	
	 * @param mdp
	 * son mot de passe.
	 * @return	
	 * 			<ul> 
	 * 				<li>0 si le pseudo n'est pas dans la BD.
	 * 				<li>-1 si le mdp ne correspond pas.
	 * 				<li>1 sinon.
	 * 			</ul>
	 * @throws SQLException
	 * Erreur BDD/SQL
	 * @throws ImprevuDBError	
	 * Si le pseudo apparaît plusieurs fois dans la BD.
	 * @throws IOException 
	 * Erreur In/Out
	 */
	public static int identifier(String pseudo, String mdp) throws SQLException, ImprevuDBError, IOException {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		if(!verifier(pseudo) || !entreeSafe(pseudo)) {	//S'il n'y a pas de joueur pour le pseudonyme donné...
			BDebug("Le personnage n'existe pas dans la BD !");
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
			return connecter();
		}
	}

	/**
	 * Ajoute un pseudo et un mdp a la BD à condition que le pseudonyme ne soit pas deja utilisé.
	 * 
	 * @param 	pseudo
	 * pseudonyme du joueur.
	 * @param 	mdp
	 * son mdp.
	 * @return	0 si le pseudo existe déjà dans la BD<br>
	 * 			-1 si le mdp ne correspond pas aux critères imposés<br>
	 * 			Sinon l'id du nouveau joueur
	 * @throws 	SQLException 	
	 * S'il est impossible de se connecter a la BD.
	 * @throws 	ImprevuDBError 	
	 * Si le pseudo apparaît plusieurs fois dans la BD.
	 * @throws IOException 
	 * Erreur In/Out.
	 */
	public static int inscrire(String pseudo, String mdp) throws SQLException, ImprevuDBError, IOException {
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		if(!(entreeSafe(mdp) && entreeSafe(pseudo))) {//si mot de passe non conforme aux restrictions imposées...
			BDebug("Pseudonyme ou mot de passe non conforme ! Pseudo: ", pseudo, " MDP: ", mdp,"\n"
					+ "        Merci de n'utiliser que des chiffres et des lettres !");
			return -1;
		} else if(verifier(pseudo)) {// S'il existe déjà un joueur avec ce pseudo...
			BDebug("Le pseudo demandé est déjà pris ! Pseudo: ", pseudo);
			return 0;
		} else {
			//Création d'un tuple stats pour le nouveau joueur.
			//Mettre les valeurs à NULL dans la requête indique de créer un tuple avec les valeurs de bases
			//enregistrées dans la BD.
			informer("INSERT INTO stats(pv_max, attaque, defense) VALUES(100, 5, 5)");
			//On récupère l'id du tuple inséré pour la mettre dans le tuple
			//de la table entite du joueur.
			int statsId = derniereId("stats");
			
			//Création d'un tuple entite pour le nouveau joueur.
			informer("INSERT INTO entite(entite_id) VALUES(NULL)");
			int entiteId = derniereId("entite");

			ResultSet movepoolIdTable = querir("SELECT MAX(movepool_id) movepool_id FROM (SELECT movepool_id FROM joueur UNION SELECT movepool_id FROM entite_type) m");
			movepoolIdTable.next();
			
			int movepoolId = movepoolIdTable.getInt("movepool_id") + 1;
			
			informer("INSERT INTO joueur(nom, mdp, entite_id, stats_id, movepool_id) VALUES(?, ?, ?, ?, ?)", pseudo, mdp, entiteId, statsId, movepoolId);
			
			//Correspond à la capacité Coup d'Crayon
			informer("INSERT INTO movepool VALUES(?, ?, ?)", movepoolId, 11, 1);
			//Correspond à la capacité Eveil Forcé
			informer("INSERT INTO movepool VALUES(?, ?, ?)", movepoolId, 10, 2);
				
			BDebug("Inscription réussie ! Pseudo: ", pseudo,
					"MDP: ", mdp,
					"StatsID: ", Integer.toString(statsId),
					"entiteId: ", Integer.toString(entiteId));
			return identifier(pseudo, mdp);
		}
	}
	
	/**
	 * Retire toutes les données de la BD du joueur connecté, puis le déconnecte.
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	public static void desinscrire() throws SQLException {
		BDebug("Début de la désinscription...");
		int joueurId = joueurTable.getInt("joueur_id");

		informer("DELETE FROM stats WHERE stats_id = ?", joueurTable.getInt("stats_id"));
		informer("DELETE FROM entite WHERE entite_id = ?", joueurTable.getInt("entite_id"));
		informer("DELETE FROM connexion WHERE joueur_id = ?", joueurId);
		informer("DELETE FROM completion WHERE joueur_id = ?", joueurId);
		informer("DELETE FROM victoire WHERE joueur_id = ?", joueurId);
		informer("DELETE FROM objet WHERE joueur_id = ?", joueurId);
		informer("DELETE FROM movepool WHERE movepool_id = ?", joueurTable.getInt("movepool_id"));
		//Le joueur est supprimé en dernier sinon SQL va se plaindre
		//que des tuples d'autres tables pointent vers une ID joueur non existante.
		informer("DELETE FROM joueur WHERE id = ?", joueurId);
		
		BDebug("Désinscription complète !");
		deconnecter();
	}
	
	/**
	 * Tente de télécharger la table du joueur demandé.<br>
	 * Renvoie s'il y a au moins une correspondance.
	 * @param pseudo
	 * Pseudonyme du joueur.
	 * @param mdp
	 * Mot de passe du joueur.
	 * @return boolean
	 * true si ce joueur existe, sinon false
	 * @throws SQLException
	 * Erreur BDD/SQL.
	 */
	private static boolean verifier(String pseudo) throws SQLException {
		BDebug("Vérification de la présence du pseudo ", pseudo, " dans la BD.");
		joueurTable = querir(
				"SELECT nom, mdp "
				+ "FROM joueur "
				+ "WHERE nom = ? "
				, pseudo);
		//next() renvoie true s'il y a au moins un résultat.
		return joueurTable.next();
	}
	
	/**
	 * Télécharge toutes les données du jeu et celles du joueur demandé.
	 * @param 	pseudo
	 * Pseudonyme du joueur.
	 * @param 	mdp
	 * Mot de passe du joueur.
	 * @return	
	 * 0 si le joueur est introuvable<br> -1 si le mot de passe est incorrect<br> sinon l'id du joueur correspondant
	 * @throws 	SQLException 
	 * S'il est impossible de se connecter a la BD.
	 * @throws IOException 
	 * Erreur In/Out.
	 * @throws 	ImprevuDBError 
	 * S'il y a plus d'un resultat pour le pseudonyme demande.
	 */
	public static int connecter() throws SQLException, IOException {
		BDebug("Connexion en cours...");
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut?allowMultiQueries=true", "joueur", "joueur");
		//Télécharge toutes les données pertinentes de la BD.
		BDebug("Identification réussie !");
		telecharger();
		BDebug("Connexion terminée !");
		informer("INSERT INTO connexion(joueur_id) VALUES(?)", personnage.getJoueurId());
		return 1;
	}
	
	/**
	 * Supprime du programme les données propres au joueur connecté,<br>
	 * puis diminue les privilèges (DELETE et UPDATE).<br>
	 * Cette méthode ne sauvegarde PAS automatiquement, il faut le faire avant !!!
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	public static void deconnecter() throws SQLException {
		BDebug("Déconnexion en cours...");
		connexion = DriverManager.getConnection("jdbc:mysql://localhost/rpgut", "invite", "invite");
		personnage = null;
		vaincus.clear();
		entites.clear();
		BDebug("Déconnexion terminée !");
	}
	
	/**
	 * Télécharge toutes les données de la BD nécessaires au fonctionnement du jeu,<br>
	 * sauf si cette méthode a déjà été appelée depuis le lancement du programme.<br>
	 * Pas besoin de télécharger plusieurs fois les données statiques.
	 * @param joueurId
	 * Identifiant du joueur
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 * @throws IOException 
	 * Erreur In/Out.
	 */
	private static void telecharger() throws SQLException, IOException {
		if(!dejaTelecharge) {
			dejaTelecharge = !dejaTelecharge;

			BDebug("Téléchargement des données statiques de la BD.");
			EntiteType.telecharger();
			ObjetType.telecharger();
			
		}
		
		telechargerJoueur();
		
		BDebug("Téléchargement des données du joueur à l'id ", Integer.toString(joueurTable.getInt("joueur_id")));
		
		telechargerEntite();
		Capacite.telecharger();
		
		personnage = new Personnage();
		
	}
	
	/**
	 * Télécharge l'intégralité de la table demandée.<br>
	 * A n'utiliser que pour les tables statiques,<br>
	 * c'est-à-dire ne contenant que des données qui ne sont pas susceptibles de changer.
	 * @param table
	 * table dont les données seront téléchargés.
	 * @return ResultSet
	 * L'integralite de la table demandée.
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	static ResultSet telecharger(String table) throws SQLException {
		BDebug("Téléchargement de la table ", table);
		return querir(
			"SELECT * "
			+ "FROM `" + table + "`"
		);
		//Je sais que je referai encore cette erreur alors je la note un peu partout :
		//Il ne faut PAS mettre les noms de colonne en paramètres d'un PreparedStatement
		//Puisque ça les entoure avec '', qui ne fonctionnent qu'avec les chaînes de caractères !
	}
	
	private static void telechargerJoueur() throws SQLException {
		joueurTable = querir(
				"SELECT * "
				+ "FROM joueur "
				+ "NATURAL JOIN entite "
				+ "NATURAL JOIN stats "
				+ "WHERE nom = ? "
				, joueurTable.getString("nom"));
		//next() renvoie true s'il y a au moins un résultat.
		joueurTable.next();
	}
	
	private static void telechargerEntite() throws SQLException, IOException {
    	//Sélectionne toutes les entites que le joueur n'a pas encore vaincues.
    	//Pour rappel, la table victoire enregistre les entités vaincues par les joueurs.
		ResultSet entiteTable = BD.querir(
			"SELECT * "
			+ "FROM entite e "
			//Ces entités ne peuvent être celles d'autres joueurs.
			+ "WHERE e.entite_type_id != 1 "
			//L'entité ne doit pas déjà avoir été vaincue par le joueur connecté.
			+ "AND e.entite_id NOT IN ("
				//Renvoie les ids des mobs vainus par le joueur connecté
				+ "SELECT v.entite_id "
				+ "FROM victoire v, joueur j "
				+ "WHERE j.joueur_id = ? "
				+ "AND j.joueur_id = v.joueur_id"
			+ ")",
			BD.getJoueurTable().getInt("joueur_id")
		);
		//Il y a trois niveaux...
		for(int niveau = 1; niveau <= 3; niveau++) {
			entites.put(niveau, new HashMap<Integer, HashMap<Integer, Mob>>());
			//...et neuf maps par niveau.
			for (int map = 1; map <= 9; map++) {
				entites.get(niveau).put(map, new HashMap<Integer, Mob>());
			}
		}
		while(entiteTable.next()) {
			Mob mob = new Mob(entiteTable);
			int niveauId = entiteTable.getInt("niveau_id");
			int mapId = entiteTable.getInt("map_id");
			int entiteId = entiteTable.getInt("entite_id");
			entites.get(niveauId).get(mapId).put(entiteId, mob);
		}
    }
	
	/**
	 * Sauvegarde toutes les données du Joueur dans la BD.
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	public static void sauvegarder() throws SQLException {

		sauvegarderJoueur();
		sauvegarderEntite();
		Stats.sauvegarder();
		Objet.sauvegarder();
		sauvegarderVictoire();
	}
	
	private static void sauvegarderJoueur() throws SQLException {
		informer("UPDATE joueur SET xp = ?, pv = ? WHERE joueur_id = ?",
			personnage.getXp(),
			personnage.getPV(),
			joueurTable.getInt("joueur_id")
		);
	}
	
	private static void sauvegarderEntite() throws SQLException {
		informer("UPDATE entite SET x = ?, y = ?, niveau_id = ?, map_id = ? WHERE entite_id = ?",
			personnage.getPosX(),
			personnage.getPosY(),
			FirstApplication.loopManager.getGameLoop().getCurrentLevelId(),
			FirstApplication.loopManager.getGameLoop().getCurrentMapId(),
			joueurTable.getInt("entite_id")
		);
	}

	private static void sauvegarderVictoire() throws SQLException {
		connexion.setAutoCommit(false);
		//Ajoute à la BD les ids des mobs vaincus depuis le dernier chargement de sauvegarde.
		preparer("INSERT INTO victoire VALUES(?,?)");
		for(int i = 0; i < vaincus.size(); i++) {
			preparer(personnage.getJoueurId(), vaincus.get(i));
			preparedStatement.addBatch();
		}
		preparedStatement.executeBatch();
		vaincus.clear();	//On vide la liste pour que les mêmes mobs ne soient pas envoyés de nouveau.
		connexion.commit();	//Dit que toutes les requêtes du batch sont définitives, pas de rollback possible.
		connexion.setAutoCommit(true);
	}

	/**
	 * Permet d'exécuter une requête lisant la BD.
	 * @param 	sql			
	 * La requête à exécuter.
	 * @param 	valeurs			
	 * Les paramètres de la requête (ce qui remplace les ? du PreparedStatement).
	 * @return	ResultSet
	 * La table correspondant à la requête demandée.
	 * @throws 	SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	static ResultSet querir(String sql, Object... valeurs) throws SQLException {
		preparer(sql, valeurs);
		BDebugRequete();
		return preparedStatement.executeQuery();
	}
	
	/**
	 * Permet d'exécuter une requête mettant à jour la BD.
	 * @param 	sql				
	 * La requête à exécuter.
	 * @param 	valeurs			
	 * Les paramètres de la requête (ce qui remplace les ? du PreparedStatement).
	 * @return	int
	 * Le nombre de lignes affectées par la requête.
	 * @throws 	SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	static int informer(String sql, Object... valeurs) throws SQLException {
		preparer(sql, valeurs);
		BDebugRequete();
		return preparedStatement.executeUpdate();
	}
	
	/**
	 * Raccourci d'écriture pour :<br>
	 * {@code preparedStatement = connexion.prepareStatement(sql);}
	 * @param sql 
	 * Requête à préparer.
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	static void preparer(String sql) throws SQLException {
		preparedStatement = connexion.prepareStatement(sql);
	}
	
	/**
	 * Ajoute les valeurs en paramètres de preparedStatement.
	 * @param valeurs
	 * Valeurs à ajouter.
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	static void preparer(Object... valeurs) throws SQLException {
		for(int i = 0; i < valeurs.length; i++) {
			preparedStatement.setObject(i + 1, valeurs[i]);
		}
	}
	
	/**
	 * Prépare et remplit preparedStatement.
	 * @param sql
	 * Requête à préparer.
	 * @param valeurs
	 * Valeurs à remplir.
	 * @throws SQLException
	 * S'il est impossible de se connecter a la BD.
	 */
	static void preparer(String sql, Object... valeurs) throws SQLException {
		preparer(sql);
		preparer(valeurs);
	}
	
	/**
	 * Récupère l'attribut id le plus élevé de la table dont le nom est passé en argument.<br>
	 * Ne fonctionne pas avec les tables n'ayant pas de colonne dont le nom a la forme <nom de la table>_id.<br>
	 * Elle n'est cependant jamais utilisée de cette manière.
	 * @param table
	 * La table dont l'id sera récupéré.
	 * @return int
	 * Id le plus élevé
	 * @throws SQLException 
	 * S'il est impossible de se connecter a la BD.
	 */
	static int derniereId(String tableName) throws SQLException {
		ResultSet table = querir("SELECT MAX(" + tableName + "_id) " + tableName + "_id FROM " + tableName);
		table.next();
		return table.getInt(tableName + "_id");
	}
	
	/**
	 * Ajoute l'id d'une instance de mob vaincue par le joueur
	 * à la liste des mobs vaincus depuis le dernier chargement de sauvegarde.<br>
	 * Supprime également le mob en question des entités téléchargées
	 * parce qu'on est jamais trop sûrs.
	 * @param mob_id
	 * Id du mob vaincu.
	 */
	public static void victoire(int entite_id) {
		int lvl = FirstApplication.loopManager.getGameLoop().getCurrentLevelId();
		int map = FirstApplication.loopManager.getGameLoop().getCurrentMapId();
		vaincus.add(entite_id);
		entites.get(lvl).get(map).remove(entite_id);
	}

	/**
	 * Une fonction me permettant de personnaliser syso au cas où j'en aurais besoin.
	 * @param options	
	 * Les chaînes à afficher.
	 */
	static void BDebug(String... options) {
		String message = "BDebug: ";
		for(int i = 0; i < options.length; i++) {
			message = message + options[i];
		}
		System.out.println(message);
	}
	
	/**
	 * Ecrit la requête contenue dans le PreparedStatement dans la console.
	 */
	static void BDebugRequete() {
		String prepStr = preparedStatement.toString();	//PreparedStatement en String contient la requête qui sera envoyée.
		BDebug("Requête: ", prepStr.substring(43, prepStr.length()));//Retire la partie de prepStr qui n'est pas la requête
	}

	/**
	 * Vérifie qu'une chaîne entrée par l'utilisateur respecte les critères d'insertion.
	 * @param entree
	 * la chaîne entrée.
	 * @return true 
	 * si la chaîne est conforme.
	 */
	static boolean entreeSafe(String entree) {
		BDebug("Vérification de la conformité de l'entrée: ", entree);
		return safePattern.matcher(entree).matches();
	}

	/*
	 * Getters et setters
	 */
	
	public static Personnage getPersonnage() {
		return personnage;
	}

	public static ResultSet getJoueurTable() {
		return joueurTable;
	}
	
	public static HashMap<Integer, EntiteType> getEntiteTypes() {
		return entiteTypes;
	}
	
	public static HashMap<Integer, ObjetType> getObjetTypes() {
		return objetTypes;
	}

	public static HashMap<Integer, HashMap<Integer, HashMap<Integer, Mob>>> getEntites(){
		return entites;
	}
	
	public static HashMap<Integer, Capacite> getCapacites() {
		return capacites;
	}
	
	public static HashMap<Integer, Movepool> getMovepools() {
		return movepools;
	}
}
