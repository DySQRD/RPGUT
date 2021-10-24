import java.sql.SQLException;
import java.util.ArrayList;

import Exceptions.ImprevuDBError;

public class Test {

	public static void main(String[] args) throws SQLException, ImprevuDBError {
		BD bd = new BD();
		ArrayList<String> test = new ArrayList<String>();
		test.add("nice"); test.add("not nice");
		System.out.println(test);
		bd.inscrire("NICEST","NOTNICEST");
		Joueur joueur = bd.getJoueur();
		ArrayList<Consommable> nice = joueur.getInventaire();
		nice.removeAll(nice);
		joueur.getInventaire().add(new Consommable(1,4));
		joueur.getInventaire().add(new Consommable(2,9));
		joueur.getInventaire().add(new Consommable(3,11));
		
		joueur.getStats().put("pv", 4);
		joueur.getStats().put("attaque", 5);
		joueur.getStats().put("vitesse", 999);
		
		bd.sauvegarder();
		System.out.println(nice.size());
		System.out.println(joueur.toString());
	}
	
}
