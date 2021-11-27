import java.util.HashMap;

public class Monstre extends Entite {
	Objet drop; //Objet que le mob peut drop

	public Monstre(int id, Stats stats, HashMap<Integer, Objet> inventaire, Objet drop) {
		super(id, stats, inventaire);
		this.drop = drop;
		// TODO Auto-generated constructor stub
	}
	
	public boolean dropObjet(int chance) { //Retourne si l'objet est lach� ou non 
		return (Math.random()*100<100);
	}
}
