package Jeu;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

import BD.ObjetType;

/**
 * 
 * @author AZROU-ISGHI Nasser
 *
 */
public class Listeobjet extends HashMap<Integer,ObjetType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final AtomicInteger count = new AtomicInteger(0);
	private static final AtomicInteger countID = new AtomicInteger(99);
	
	/**
	 * Crée la liste d'objet nécessaire.
	 */
	Listeobjet(){
		Listecapacite liste = new Listecapacite();
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Café",5,liste.get(32)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(), "Batterie Externe",2,liste.get(33)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(), "Calculatrice",2,liste.get(34)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Règle",2,liste.get(35)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Equerre",2,liste.get(36)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Compas",1,liste.get(37)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Sac à dos",1,liste.get(38)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Pochette d'ordi",1,liste.get(39)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Stylo 4 couleurs",2,liste.get(40)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Effaceur",1,liste.get(41)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Tippex",2,liste.get(42)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Gomme",2,liste.get(43)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Maximator",2,liste.get(44)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Alcool",2,liste.get(45)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Thé",2,liste.get(46)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Smartphone",2,liste.get(47)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Sandwich",1,liste.get(48)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Tacos",1,liste.get(49)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Pizza",1,liste.get(50)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Launchpack",1,liste.get(51)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Feuille",2,liste.get(52)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(), "Crayon",3,liste.get(53)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Ordinateur",1,liste.get(54)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Souris",1,liste.get(55)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Clavier",3,liste.get(56)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Post-it",2,liste.get(57)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Nouilles Instantanées",1,liste.get(58)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Agrafeuse",1,liste.get(59)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Bouteille d'eau",1,liste.get(60)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Chocolat Chaud",1,liste.get(61)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Café Noisette",1,liste.get(62)));
		put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Haltères",1,liste.get(63)));
		
	}
}
