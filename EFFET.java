//Juste un placeholder le temps qu'on se mette d'accord sur les effets qui seront utilisés
//Devront être implémentés d'une façon ou d'une autre les paramètres suivants :
//Intensité de l'effet (ex: augmente la stat de X crans)
//Cible de l'effet (seulement l'ennemi ? soi-même ? les deux ? aléatoire ?)
//Précision de l'effet (n'échoue jamais, ou un int)
import java.util.ArrayList;

public enum EFFET {
	BRULURE, GEL, PARALYSIE, POISON, SOMMEIL;
	
	private static ArrayList<EFFET> effets = null;
	
	void activer() {
		if (this == BRULURE) {
			//do stuff
		}
	}
	
	public static ArrayList<EFFET> getEffets() {
		return effets;
	}
}
