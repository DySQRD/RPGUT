package Jeu;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 
 * @author AZROU-ISGHI Nasser
 *
 */
public class Listecapacite extends HashMap<Integer,Capacite> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*Cette classe permet de créer des listes de capacités, que ce soit la liste totale des capacités, la liste ennemi, ou la liste du personnage*/
	
	private static final AtomicInteger count = new AtomicInteger(0);
	/**
	 * Crée la liste de capacité nécessaire.
	 */
	Listecapacite(){
		put(count.incrementAndGet(), new Capacite("D.E.S","Décompose l'adversaire en plusieurs éléments. ", 90, Categorie.DownDefense, 0, 2, "adversaire"));
		put(count.incrementAndGet(), new Capacite("IPP","Intégre l'adversaire pour n'en faire qu'une bouchée. Inflige des dégâts.",10,80,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Changement de variable","S'adapte à l'adversaire et utilise les variables nécessaires pour lui infliger des dégâts.",8,100,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Garbage Collector","Invoque le GC pour attaquer l'adversaire.",15,60,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Ciscocted","Crée un réseau virtuel pour infliger une grosse quantité de dégâts à l'adversaire. Tue l'adversaire en un coup.",999,30,true,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Eveil Forcé","S'efforce à se réveiller et se préparer pour les partiels. Augmente l'attaque de moitié.",85,Categorie.UpAttaque,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Coup d'crayon","capacité simple, basique, mais fait l'affaire. Inflige des capacités.",5,100,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("SWITCH","Centralise toute l'attaque via un commutateur. Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("HUB","Centralise toute la défense via un concentrateur. Augmente la défense d'un quart.", 100, Categorie.UpDefense,4,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Celene","Accède aux modules et leur cours pour acquérir des capacités et double l'attaque.", 30, Categorie.UpAttaque, 1, 0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Recherche Google","Obtient les connaissances nécessaires sur Google, peut être considérer comme de la triche. Augmente l'attaque d'un cinquième.",40,Categorie.UpAttaque,5,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Factorisation","Simplifie au maximum l'adversaire, retire complètement la défense.",20,Categorie.DownDefense,1,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Abstraction","Fait abstraction de ce qu'il pourra lui porter atteinte. Augmente la défense de moitié.",70, Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Heritage","Augmente son attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("ArrayList","Retourne tous les éléments d'une arraylist et inflige des dégâts.",15,60,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("HashMap","Retourne tous les éléments et leur clé d'une hashmap et inflige des dégâts.",20,50,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("PERT","Si vous êtes mal organisé, cette capacité peut porter préjudice ... et c'est le cas. un quart de défense de moins.",80,Categorie.DownDefense,0,4,"soi"));
		put(count.incrementAndGet(), new Capacite("GANTT","S'organise sur le temps en fonction des tâches. Augmente les points de vie d'un quart.",100,Categorie.UpHealth,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("RACI","Fait appel à ses coéquipiers et s'organisent tous ensemble pour réduire les points de vie de l'adversaire de moitié.",90,Categorie.DownHealth,0,2,"soi"));
		put(count.incrementAndGet(), new Capacite("Implement","Restaure entierement la vie.",20,Categorie.UpHealth,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Extend","Retire toute la vie de l'adversaire.",20,Categorie.DownHealth,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Diagramme de classe","Submerge l'adversaire d'une tonne d'information qu'il ne pourra jamais traiter. Met ko en un coup.",999,30,true,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Diagramme de cas d'utilisation","Vérifie toute utilisation possible et réduit l'attaque de l'adversaire.",90,Categorie.DownAttaque,4,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Diagramme de séquence","Trace tous les processus avec des if et else. Réduit la défense de moitié.",80, Categorie.DownDefense,2,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Diagramme d'activité","Réduit la vie de l'adversaire de moitié.",60,Categorie.DownHealth,0,2,"adversaire"));
		put(count.incrementAndGet(), new Capacite("null pointer exception","Cauchemar de toutes entités. Met KO en un coup.",999,30,true,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Scrum MASTER","Faites face au courroux de la fac de Blois. Peut mettre son lanceur KO.",999,10,true,"soi"));
		put(count.incrementAndGet(), new Capacite("Escroquerie","Au vu du thème du jeu, il y a probablement des étudiants escrots. Augmente sa défense de moitié pour se protéger.",70,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Intégrale de Reimann","Façon d'intégrer l'adversaire. Inflige des dégats.",15,80,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("SQL","Double la défense du lanceur de moitié.",70,Categorie.UpDefense,2,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Jointure","Inflige plusieurs dégâts à l'adversaire.",7,100,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Objet Café","Revigore la vie du personnage principale d'un cinquième.",100,Categorie.UpHealth,5,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Batterie Externe","Augmente la défense d'un quart.",100,Categorie.UpDefense,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Calculatrice","Augmente l'attaque du personnage d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Règle","Augmente l'attaque d'un tier.",100,Categorie.UpAttaque,3,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Equerre","Augmente l'attaque d'un tiers.",100,Categorie.UpAttaque,3,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Compas","Augmente l'attaque de moitié.",100,Categorie.UpAttaque,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Sac à dos","Augmente la défense de moitié.",100,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Pochette d'ordi","Augmente la défense de moitié",100,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Stylo 4 couleurs","Augmente les points de vie d'un tier.",100,Categorie.UpHealth,3,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Effaceur","Augmente la défense de moitié.",100,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Tippex","Augmente la défense d'un tier.",100,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Gomme","Augmente la défense d'un quart.",100,Categorie.UpDefense,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Maximator","Réduit les points de vie d'un quart.",100,Categorie.DownHealth,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Alcool","Augmente les points de vie d'un quart.",100,Categorie.UpHealth,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Thé","Augmente les points de vie d'un tier.",100,Categorie.UpHealth,3,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Smartphone","Augmente l'attaque d'un tier.",100,Categorie.UpAttaque,3,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Sandwich","Augmente les points de vie de moitié.",100,Categorie.UpHealth,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Tacos","Réduit les points de vie d'un tier.",100,Categorie.UpHealth,3,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Pizza","Augmente les points de vie d'un quart.",100,Categorie.UpHealth,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Launchpack","Augmente les points de vie de moitié.",100,Categorie.UpHealth,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Feuille","Augmente l'attaque d'un cinquième.",100,Categorie.UpAttaque,5,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Cahier","Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Montre","Augmente la défense d'un tier.",100,Categorie.UpDefense,3,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Réveil","Augmente la défense de moitié.",100,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Craie","Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Feutre","Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Crayon","Augmente l'attaque d'un cinquième",100,Categorie.UpAttaque,5,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Ordinateur","Double l'attaque.",100,Categorie.UpAttaque,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Souris","Augmente l'attaque de moitié.",100,Categorie.UpAttaque,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Clavier","Augmente la défense de moitié.",100,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Post-it","Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Nouilles Instantanées","Redonne la totalité des points de vie.",100,Categorie.UpAttaque,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Agrafeuse","Double la défense.",100,Categorie.UpDefense,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Bouteille d'eau","Augmente les points de vie de moitié.",100,Categorie.UpHealth,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Chocolat Chaud","Redonne la totalité des points de vie.",100,Categorie.UpHealth,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Café Noisette","Augmente les points de vie de moitié.",100,Categorie.DownHealth,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Objet Haltères","Double la défense",100,Categorie.UpDefense,1,0,"soi"));
	}
	
	/*Vérifie si la capacité existe dans la liste. Si l'on compare uniquement ces 3 attributs, c'est parce qu'ils représentent suffisamment la capacité,
	 * pour que l'on puisse se baser dessus.*/
	public boolean exist(Capacite e) {
			if(containsValue(e)) return true;
			else return false;
	}	

	public Capacite getCapacite(Capacite Capacite) {
		if(exist(Capacite))  
			for(Capacite c : values()) {
			if(c.equals(Capacite)) return c;
		}
		return null;
	}
}
