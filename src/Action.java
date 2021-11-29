
public class Action {
	String nom;
	int puissance;
	/**
	 * Probabilité que l'action réussisse entre 0 et 100.<br>
	 * Si NULL, l'action n'échoue jamais.<br>
	 * Eviter tout calcul pouvant impliquer le NULL.
	 */
	int precision;
	/**
	 * Sur qui appliquer l'action. Valeurs possibles :<br>
	 * - NULL: cette action n'affecte aucun combattant (par exemple: effet de terrain)<br>
	 * - "soi": seulement l'utilisateur<br>
	 * - "ennemi": seulement l'ennemi<br>
	 * - "deux": utilisateur ET ennemi<br>
	 * - "aleatoire": une des options ci-dessus<br>
	 * - "aleatoireXOR": soit l'utilisateur, soit l'ennemi
	 */
	String cible;
}
