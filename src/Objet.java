public class Objet {
private String id;
private String name;
private int durabilite;
private int level;

int nombreobjet=0;
	public Objet(String id, int durabilite) {
		this.id=id;
		this.durabilite=durabilite;
		nombreobjet++;
		
	}
	public String getId() {
		return id;
	}
public void SetId(String id) {
	this.id=id;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name=name;
}
public int getDurabilite() {
	return durabilite;
}
public void setDurabilite(int durabilite) {
	this.durabilite=durabilite;
}
public int getlevel() {
	return level;
}
public void setlevel(int level) {
	this.level=level;
}
int max_level=10;
public void increseLevel() {
	if (level<max_level) {
		level++;
	}
	else {
		System.out.println("level maximum atteint pour l'arme");
	}
}
public void decreseLevl() {
	if(level>0) {
		level--;
	}
	else {
		System.out.println("on ne peut decrementer le level");
	} 
}
public String toString() {
	return "objet de nom="+this.name+"et de level"+this.level+"et de durabilite"+
this.durabilite;
}
}
