/**
 * @author Nasser AZROU-ISGHI
 */

package Jeu;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Capacitetotal extends HashMap<Integer,Capacite> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*Cette classe permet de créer des listes de capacités, que ce soit la liste totale des capacités, la liste ennemi, ou la liste du personnage*/
	
	private static final AtomicInteger count = new AtomicInteger(0);
	
	/**
	 * Crée la liste de toutes les capacités du jeu.
	 */
	Capacitetotal(){
		this.put(count.incrementAndGet(), new Capacite("DES","Décompose l'adversaire en plusieurs éléments, divise sa défense sur 2.",8,Categorie.DownDefense,0,1,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("IPP","Intègre l'adversaire pour n'en faire qu'une bouchée.",6,95,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Changement de variable","Contourne les forces de l'adversaire, et touche ses points faibles",80,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Permutation","Augmente l'attaque de moitié.",75,Categorie.UpAttaque,2,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("Héritage","L'exécuteur s'étend et prend de la place. Augmente l'attaque d'un quart",95,Categorie.UpAttaque,4,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("Abstraction","L'exécuteur fait abstraction de ce qu'il l'entoure. Augmente sa defense",100,Categorie.UpDefense,2,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("GC","Invoque le Garbage Collector et inflige des dégâts à l'adversaire",10,95,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Eveil Forcé","Se motive et se force pour affronter les épreuves d'un étudiant, double l'attaque",50,Categorie.UpAttaque,1,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("Ciscocted","Crée un réseau virtuel et inflige d'importants dégâts",12,50,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Coup d'crayon","Capacité très simple, basique, mais fait l'affaire",6,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("GANTT","S'organise et répertorit les tâches selon une durée. Augmente d'un quart la défense",100,Categorie.UpDefense,1,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("RACI","Plus ou moins la même que GANTT, mais augmente les points de vie d'un quart",100,Categorie.UpHealth,4,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("ArrayList","Inflige des dégâts successifs",50,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Switch","Centralise plusieurs attaques en une attaque fulgurante. Met KO en un coup.",1,30,true,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Nuit de codage","Sacrifie sa nuit de sommeil pour coder un maximum. Met KO en un coup.",1,30,true,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Factorisation","Factorise un maximum l'adversaire. Réduit la défense d'un quart",100,Categorie.UpDefense,0,4,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("HUB","Régenère entièrement les points de vie du lanceur",80,Categorie.UpHealth,1,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("PERT","Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("Recherche Google","Capacité très basique, peut être considéré comme de la triche. Inflige des dégâts",6,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("CELENE","Accède aux cours de différents modules pour consolider ses connaissances. Augmente l'attaque de moitié",70, Categorie.UpAttaque,2,0,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Implements","Réduit la défense de l'adversaire d'un quart.",80,Categorie.DownDefense,0,4,"adversaire"));
	}
	
	/**
	 * Vérifie si la capacité existe dans la liste. Si l'on compare uniquement ces 3 attributs, c'est parce qu'ils représentent suffisamment la capacité,
	 * pour que l'on puisse se baser dessus.
	 * @param e 
	 * capacité
	 * @return booléen
	 */
	public boolean exist(Capacite e) {
			if(containsValue(e)) return true;
			else return false;
	}
		
	/**
	 * Retourne la capacité choisi si elle existe.
	 * @param Capacite
	 * @return Capacite
	 */
	public Capacite getCapacite(Capacite Capacite) {
		if(exist(Capacite))  
			for(Capacite c : values()) {
			if(c.equals(Capacite)) return c;
		}
		return null;
	}
}