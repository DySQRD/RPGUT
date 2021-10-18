import java.util.ArrayList;

public enum EFFET {
	BRULURE, GEL, PARALYSIE, POISON, SOMMEIL;
	
	ArrayList<EFFET> effets;
	

	void activer() {
		if (this == BRULURE) {
			//do stuff
		}
	}
}
