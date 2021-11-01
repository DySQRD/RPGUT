import java.util.ArrayList;
import java.util.HashMap;

public class Monstre extends Entite {
	Consommable drop; //Objet que le mob peut drop

	public Monstre(int id, int xp, HashMap<String, Integer> stats, ArrayList<Consommable> inventaire, Consommable drop) {
		super(id, xp, stats, inventaire);
		this.drop = drop;
		// TODO Auto-generated constructor stub
	}
	
	public boolean dropObjet(int chance) { //Retourne si l'objet est lach� ou non 
		return (Math.random()*100<100);
	}
}
