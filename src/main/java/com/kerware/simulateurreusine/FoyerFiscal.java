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

    /**
     * Retrieves the net income of declarant 1.
     *
     * @return the net income of the first declarant as an integer
     */
    public int getRevenuNetDeclarant1() {
        return revenuNetDeclarant1;
    }

    /**
     * Sets the net income of declarant 1.
     *
     * @param revenu the net income of the first declarant as an integer
     */
    public void setRevenuNetDeclarant1(int revenu) {
        if (revenu < 0) {
            throw new IllegalArgumentException("Le revenu ne peut pas être négatif");
        }
        this.revenuNetDeclarant1 = revenu;
    }

    /**
     * Retrieves the net income of declarant 2.
     *
     * @return the net income of the second declarant as an integer
     */
    public int getRevenuNetDeclarant2() {
        return revenuNetDeclarant2;
    }

    /**
     * Sets the net income of declarant 2.
     *
     * @param revenu the net income of the second declarant as an integer
     */
    public void setRevenuNetDeclarant2(int revenu) {
        if (revenu < 0) {
            throw new IllegalArgumentException("Le revenu ne peut pas être négatif");
        }
        this.revenuNetDeclarant2 = revenu;
    }

    /**
     * Retrieves the familial situation of the tax household.
     *
     * @return the familial situation as an instance of the {@code SituationFamiliale} enumeration
     */
    public SituationFamiliale getSituationFamiliale() {
        return situationFamiliale;
    }

    /**
     * Sets the familial situation of the tax household.
     *
     * @param situation the familial situation as
     *                  an instance of the {@code SituationFamiliale} enumeration
     */
    public void setSituationFamiliale(SituationFamiliale situation) {
        if (situation == null) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }
        this.situationFamiliale = situation;
    }

    /**
     * Retrieves the number of dependent children in the tax household.
     *
     * @return the number of dependent children as an integer
     */
    public int getNbEnfantsACharge() {
        return nbEnfantsACharge;
    }

    /**
     * Sets the number of dependent children in the tax household.
     *
     * @param nbEnfants the number of dependent children as an integer
     */
    public void setNbEnfantsACharge(int nbEnfants) {
        if (nbEnfants < 0) {
            throw new IllegalArgumentException(
                    "Le nombre d'enfants ne peut pas être négatif");
        }
        if (nbEnfants > Simulateur.NB_ENFANTS_MAX) {
            throw new IllegalArgumentException("Le nombre d'enfants ne peut pas" +
                    "être supérieur à " + Simulateur.NB_ENFANTS_MAX);
        }
        this.nbEnfantsACharge = nbEnfants;
    }

    /**
     * Retrieves the number of children in a situation of handicap in the tax household.
     *
     * @return the number of children in a situation of handicap as an integer
     */
    public int getNbEnfantsSituationHandicap() {
        return nbEnfantsSituationHandicap;
    }

    /**
     * Sets the number of children in a situation of handicap in the tax household.
     *
     * @param nbEnfants the number of children in a situation of handicap as an integer
     */
    public void setNbEnfantsSituationHandicap(int nbEnfants) {
        if (nbEnfants < 0) {
            throw new IllegalArgumentException(
                    "Le nombre d'enfants en situation de handicap ne peut pas être négatif");
        }
        if (nbEnfants > nbEnfantsACharge) {
            throw new IllegalArgumentException(
                    "Le nombre d'enfants en situation de handicap ne" +
                            "peut pas être supérieur au nombre d'enfants à charge");
        }
        this.nbEnfantsSituationHandicap = nbEnfants;
    }

    /**
     * Checks if the parent in the tax household is designated as isolated.
     *
     * @return {@code true} if the parent is classified as isolated, {@code false} otherwise
     */
    public boolean isParentIsole() {
        return parentIsole;
    }

    /**
     * Sets whether the parent in the tax household is designated as isolated.
     *
     * @param estParentIsole {@code true} if the parent is classified as isolated,
     * {@code false} otherwise
     */
    public void setParentIsole(boolean estParentIsole) {
        this.parentIsole = estParentIsole;
    }

    // Getters et setters pour les résultats de calcul

    /**
     * Sets the number of tax parts applicable to the tax household.
     *
     * @param nombreDeParts the number of parts as a double
     */
    public void setNombreParts(double nombreDeParts) {
        this.nombreParts = nombreDeParts;
    }

    /**
     * Retrieves the number of tax parts applicable to the tax household.
     *
     * @return the number of parts as a double
     */
    public double getNombreParts() {
        return nombreParts;
    }

    /**
     * Sets the taxable income for the tax household.
     *
     * @param revenuImposableDuDeclarant the taxable income as a double
     */
    public void setRevenuImposable(double revenuImposableDuDeclarant) {
        this.revenuImposable = revenuImposableDuDeclarant;
    }

    /**
     * Retrieves the taxable income for the tax household.
     *
     * @return the taxable income as a double
     */
    public double getRevenuImposable() {
        return revenuImposable;
    }

    /**
     * Sets the abatement value for the tax household.
     *
     * @param abattementDuDeclarant the abatement value as a double
     */
    public void setAbattement(double abattementDuDeclarant) {
        this.abattement = abattementDuDeclarant;
    }

    /**
     * Retrieves the abatement value for the tax household.
     *
     * @return the abatement value as a double
     */
    public double getAbattement() {
        return abattement;
    }

    /**
     * Sets the gross tax amount for the tax household.
     *
     * @param impotBrutDuDeclarant the gross tax amount as a double
     */
    public void setImpotBrut(double impotBrutDuDeclarant) {
        this.impotBrut = impotBrutDuDeclarant;
    }

    /**
     * Retrieves the gross tax amount for the tax household.
     *
     * @return the gross tax amount as a double
     */
    public double getImpotBrut() {
        return impotBrut;
    }

    /**
     * Sets the decote value for the tax household.
     *
     * @param decoteDuDeclarant the decote value as a double
     */
    public void setDecote(double decoteDuDeclarant) {
        this.decote = decoteDuDeclarant;
    }

    /**
     * Retrieves the decote value for the tax household.
     *
     * @return the decote value as a double
     */
    public double getDecote() {
        return decote;
    }

    /**
     * Sets the net income tax amount for the tax household.
     *
     * @param impotNetDuDeclarant the net income tax amount as a double
     */
    public void setImpotNet(double impotNetDuDeclarant) {
        this.impotNet = impotNetDuDeclarant;
    }

    /**
     * Retrieves the net income tax amount for the tax household.
     *
     * @return the net income tax amount as a double
     */
    public double getImpotNet() {
        return impotNet;
    }
}