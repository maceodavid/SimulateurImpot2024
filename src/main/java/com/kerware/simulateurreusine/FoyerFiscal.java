package com.kerware.simulateurreusine;

/**
 * Représente les données d'un foyer fiscal
 */
public class FoyerFiscal {
    private int revenuNetDeclarant1;
    private int revenuNetDeclarant2;
    private SituationFamiliale situationFamiliale;
    private int nbEnfantsACharge;
    private int nbEnfantsSituationHandicap;
    private boolean parentIsole;
    private double nombreParts;
    private double revenuImposable;
    private double abattement;
    private double impotBrut;
    private double decote;
    private double impotNet;

    public int getRevenuNetDeclarant1() {
        return revenuNetDeclarant1;
    }

    public void setRevenuNetDeclarant1(int revenu) {
        if (revenu < 0) {
            throw new IllegalArgumentException("Le revenu ne peut pas être négatif");
        }
        this.revenuNetDeclarant1 = revenu;
    }

    public int getRevenuNetDeclarant2() {
        return revenuNetDeclarant2;
    }

    public void setRevenuNetDeclarant2(int revenu) {
        if (revenu < 0) {
            throw new IllegalArgumentException("Le revenu ne peut pas être négatif");
        }
        this.revenuNetDeclarant2 = revenu;
    }

    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    public void setSituationFamiliale(SituationFamiliale situation) {
        if (situation == null) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }
        this.situationFamiliale = situation;
    }

    public int getNbEnfantsACharge() {
        return nbEnfantsACharge;
    }

    public void setNbEnfantsACharge(int nbEnfants) {
        if (nbEnfants < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être négatif");
        }
        if (nbEnfants > 7) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas être supérieur à 7");
        }
        this.nbEnfantsACharge = nbEnfants;
    }

    public int getNbEnfantsSituationHandicap() {
        return nbEnfantsSituationHandicap;
    }

    public void setNbEnfantsSituationHandicap(int nbEnfants) {
        if (nbEnfants < 0) {
            throw new IllegalArgumentException("Le nombre d'enfants en situation de handicap ne peut pas être négatif");
        }
        if (nbEnfants > nbEnfantsACharge) {
            throw new IllegalArgumentException("Le nombre d'enfants en situation de handicap ne peut pas être supérieur au nombre d'enfants à charge");
        }
        this.nbEnfantsSituationHandicap = nbEnfants;
    }

    public boolean isParentIsole() {
        return parentIsole;
    }

    public void setParentIsole(boolean parentIsole) {
        this.parentIsole = parentIsole;
    }

    // Getters et setters pour les résultats de calcul
    public void setNombreParts(double nombreParts) {
        this.nombreParts = nombreParts;
    }

    public double getNombreParts() {
        return nombreParts;
    }

    public void setRevenuImposable(double revenuImposable) {
        this.revenuImposable = revenuImposable;
    }

    public double getRevenuImposable() {
        return revenuImposable;
    }

    public void setAbattement(double abattement) {
        this.abattement = abattement;
    }

    public double getAbattement() {
        return abattement;
    }

    public void setImpotBrut(double impotBrut) {
        this.impotBrut = impotBrut;
    }

    public double getImpotBrut() {
        return impotBrut;
    }

    public void setDecote(double decote) {
        this.decote = decote;
    }

    public double getDecote() {
        return decote;
    }

    public void setImpotNet(double impotNet) {
        this.impotNet = impotNet;
    }

    public double getImpotNet() {
        return impotNet;
    }
}