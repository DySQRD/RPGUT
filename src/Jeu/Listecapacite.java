/*Auteur : Nasser AZROU-ISGHI*/

package Jeu;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class Listecapacite extends HashMap<Integer,Capacite> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	/*Cette classe permet de cr�er des listes de capacit�s, que ce soit la liste totale des capacit�s, la liste ennemi, ou la liste du personnage*/
	
	private static final AtomicInteger count = new AtomicInteger(0);
	
	Listecapacite(){
		put(count.incrementAndGet(), new Capacite("D.E.S","D�compose l'adversaire en plusieurs �l�ments. ", 90, Categorie.DownDefense, 0, 2, "adversaire"));
		put(count.incrementAndGet(), new Capacite("IPP","Int�gre l'adversaire pour n'en faire qu'une bouch�e. Inflige des d�g�ts.",10,80,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Changement de variable","S'adapte � l'adversaire et utilise les variables n�cessaires pour lui infliger des d�g�ts.",8,100,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Garbage Collector","Invoque le GC pour attaquer l'adversaire.",15,60,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Ciscocted","Cr�e un r�seau virtuel pour infliger une grosse quantit� de d�g�ts � l'adversaire. Tue l'adversaire en un coup.",999,30,true,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Eveil Forc�","S'efforce � se r�veiller et se pr�parer pour les partiels. Augmente l'attaque de moiti�.",85,Categorie.UpAttaque,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Coup d'crayon","Capacit� simple, basique, mais fait l'affaire. Inflige des capacit�s.",5,100,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("SWITCH","Centralise toute l'attaque via un commutateur. Augmente l'attaque d'un quart.",100,Categorie.UpAttaque,4,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("HUB","Centralise toute la d�fense via un concentrateur. Augmente la d�fense d'un quart.", 100, Categorie.UpDefense,4,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Celene","Acc�de aux modules et leur cours pour acqu�rir des capacit�s et double l'attaque.", 30, Categorie.UpAttaque, 1, 0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Recherche Google","Obtient les connaissances n�cessaires sur Google, peut �tre consid�rer comme de la triche. Augmente l'attaque d'un cinqui�me.",40,Categorie.UpAttaque,5,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Factorisation","Simplifie au maximum l'adversaire, retire compl�tement la d�fense.",20,Categorie.DownDefense,1,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Abstraction","Fait abstraction de ce qu'il pourra lui porter atteinte. Augmente la d�fense de moiti�.",70, Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Heritage","Augmente son attaque d'un quart.",100,Categorie.UpAttaque,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("ArrayList","Retourne tous les �l�ments d'une arraylist et inflige des d�g�ts.",15,60,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("HashMap","Retourne tous les �l�ments et leur cl� d'une hashmap et inflige des d�g�ts.",20,50,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("PERT","Si vous �tes mal organis�, cette capacit� peut porter pr�judice ... et c'est le cas. un quart de d�fense de moins.",80,Categorie.DownDefense,0,4,"soi"));
		put(count.incrementAndGet(), new Capacite("GANTT","S'organise sur le temps en fonction des t�ches. Augmente les points de vie d'un quart.",100,Categorie.UpHealth,4,0,"soi"));
		put(count.incrementAndGet(), new Capacite("RACI","Fait appel � ses co�quipiers et s'organisent tous ensemble pour r�duire les points de vie de l'adversaire de moiti�.",90,Categorie.DownHealth,0,2,"soi"));
		put(count.incrementAndGet(), new Capacite("Implement","Restaure entierement la vie.",20,Categorie.UpHealth,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Extend","Retire toute la vie de l'adversaire.",20,Categorie.DownHealth,1,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Diagramme de classe","Submerge l'adversaire d'une tonne d'information qu'il ne pourra jamais traiter. Met ko en un coup.",999,30,true,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Diagramme de cas d'utilisation","V�rifie toute utilisation possible et r�duit l'attaque de l'adversaire.",90,Categorie.DownAttaque,4,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Diagramme de s�quence","Trace tous les processus avec des if et else. R�duit la d�fense de moiti�.",80, Categorie.DownDefense,2,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Diagramme d'activit�","R�duit la vie de l'adversaire de moiti�.",60,Categorie.DownHealth,0,2,"adversaire"));
		put(count.incrementAndGet(), new Capacite("null pointer exception","Cauchemar de toutes entit�s. Met KO en un coup.",999,30,true,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Scrum MASTER","Faites face au courroux de la fac de Blois. Peut mettre son lanceur KO.",999,10,true,"soi"));
		put(count.incrementAndGet(), new Capacite("Escroquerie","Au vu du th�me du jeu, il y a probablement des �tudiants escrots. Augmente sa d�fense de moiti� pour se prot�ger.",70,Categorie.UpDefense,2,0,"soi"));
		put(count.incrementAndGet(), new Capacite("Int�grale de Reimann","Fa�on d'int�grer l'adversaire. Inflige des d�gats.",15,80,false,"adversaire"));
		put(count.incrementAndGet(), new Capacite("SQL","Double la d�fense du lanceur de moiti�.",70,Categorie.UpDefense,2,0,"adversaire"));
		put(count.incrementAndGet(), new Capacite("Jointure","Inflige plusieurs d�g�ts � l'adversaire.",7,100,false,"adversaire"));
	}
	
	/*V�rifie si la capacit� existe dans la liste. Si l'on compare uniquement ces 3 attributs, c'est parce qu'ils repr�sentent suffisamment la capacit�,
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
