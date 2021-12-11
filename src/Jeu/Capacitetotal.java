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
	/*Cette classe permet de cr�er des listes de capacit�s, que ce soit la liste totale des capacit�s, la liste ennemi, ou la liste du personnage*/
	
	private static final AtomicInteger count = new AtomicInteger(0);
	
	/**
	 * Cr�e la liste de toutes les capacit�s du jeu.
	 */
	Capacitetotal(){
		this.put(count.incrementAndGet(), new Capacite("DES","D�compose l'adversaire en plusieurs �l�ments, divise sa d�fense sur 2.",8,Categorie.DownDefense,0,1,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("IPP","Int�gre l'adversaire pour n'en faire qu'une bouch�e.",6,95,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Changement de variable","Contourne les forces de l'adversaire, et touche ses points faibles",80,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Permutation","Augmente l'attaque de moiti�.",75,Categorie.UpAttaque,2,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("H�ritage","L'ex�cuteur s'�tend et prend de la place. Augmente l'attaque d'un quart",95,Categorie.UpAttaque,4,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("Abstraction","L'ex�cuteur fait abstraction de ce qu'il l'entoure. Augmente sa defense",100,Categorie.UpDefense,2,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("GC","Invoque le Garbage Collector et inflige des d�g�ts � l'adversaire",10,95,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Eveil Forc�","Se motive et se force pour affronter les �preuves d'un �tudiant, double l'attaque",50,Categorie.UpAttaque,1,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("Ciscocted","Cr�e un r�seau virtuel et inflige d'importants d�g�ts",12,50,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Coup d'crayon","Capacit� tr�s simple, basique, mais fait l'affaire",6,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("GANTT","S'organise et r�pertorit les t�ches selon une dur�e. Augmente d'un quart la d�fense",100,Categorie.UpDefense,1,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("RACI","Plus ou moins la m�me que GANTT, mais augmente les points de vie d'un quart",100,Categorie.UpHealth,4,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("ArrayList","Inflige des d�g�ts successifs",50,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Switch","Centralise plusieurs attaques en une attaque fulgurante. Met KO en un coup.",1,30,true,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Nuit de codage","Sacrifie sa nuit de sommeil pour coder un maximum. Met KO en un coup.",1,30,true,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Factorisation","Factorise un maximum l'adversaire. R�duit la d�fense d'un quart",100,Categorie.UpDefense,0,4,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("HUB","R�gen�re enti�rement les points de vie du lanceur",80,Categorie.UpHealth,1,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("PERT","Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		this.put(count.incrementAndGet(), new Capacite("Recherche Google","Capacit� tr�s basique, peut �tre consid�r� comme de la triche. Inflige des d�g�ts",6,100,false,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("CELENE","Acc�de aux cours de diff�rents modules pour consolider ses connaissances. Augmente l'attaque de moiti�",70, Categorie.UpAttaque,2,0,"adversaire"));
		this.put(count.incrementAndGet(), new Capacite("Implements","R�duit la d�fense de l'adversaire d'un quart.",80,Categorie.DownDefense,0,4,"adversaire"));
	}
	
	/**
	 * V�rifie si la capacit� existe dans la liste. Si l'on compare uniquement ces 3 attributs, c'est parce qu'ils repr�sentent suffisamment la capacit�,
	 * pour que l'on puisse se baser dessus.
	 * @param e 
	 * capacit�
	 * @return bool�en
	 */
	public boolean exist(Capacite e) {
			if(containsValue(e)) return true;
			else return false;
	}
		
	/**
	 * Retourne la capacit� choisi si elle existe.
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