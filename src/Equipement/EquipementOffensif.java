package Equipement;

public abstract class EquipementOffensif {
    private String type;
    private int nivDAttaque;
    private String nom;

//    public Equipement.EquipementOffensif(){
//        this("Equipement.Sort");
//    }
//
//    public Equipement.EquipementOffensif(String type){
//        this(type, 2);
//    }
//
//    public Equipement.EquipementOffensif(String type, int nivDAttaque){
//        this(type, nivDAttaque, "Éclair");
//    }
//
//    public Equipement.EquipementOffensif(String type, int nivDAttaque, String nom){
//        this.type = type;
//        this.nivDAttaque = nivDAttaque;
//        this.nom = nom;
//    }

    public String getType() {
        return type;
    }
    public void setType(String type) {
        this.type = type;
    }
    public int getNivDAttaque() {
        return nivDAttaque;
    }
    public void setNivDAttaque(int nivDAttaque) {
        this.nivDAttaque = nivDAttaque;
    }
    public String getNom() {
        return nom;
    }
    public void setNom(String nom) {
        this.nom = nom;
    }

    public String toString(){
        return "Attaque de type : " + type + "\n" +
                "Niveau : " + "+ " + nivDAttaque + "\n" +
                "Nom : " + nom;
    }
}