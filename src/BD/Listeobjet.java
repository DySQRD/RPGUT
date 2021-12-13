package BD;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

//import Jeu.Listecapacite;

public class Listeobjet extends HashMap<Integer,ObjetType> {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private static final AtomicInteger count = new AtomicInteger(0);
	private static final AtomicInteger countID = new AtomicInteger(99);
	
	Listeobjet(){
		//put(count.incrementAndGet(),new ObjetType(countID.incrementAndGet(),"Café",5,Listecapacite.get(1)));
	}
}
