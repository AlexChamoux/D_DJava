package Equipement;

import Personnage.Personnage;

public class Bouclier extends EquipementDefensif {
    public Bouclier(String type, int nivDefense, String nom) {
        this.setType(type);
        this.setNivDefense(nivDefense);
        this.setNom(nom);
    }

    public void interagir(Personnage personnage) {
        System.out.println("Vous trouvez une " + getNom());
        if (this.getType().equals(personnage.getEquipementDefensif().getType())) {
            personnage.setEquipementDefensif(this);
            System.out.println("Vous équipez " + personnage.getNom() + " avec " + getNom());
        } else {
            System.out.println("Ce personnage ne peut pas équiper ce bouclier.");
        }
    }
}
