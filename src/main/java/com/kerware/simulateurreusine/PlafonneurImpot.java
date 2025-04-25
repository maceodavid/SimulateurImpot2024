package com.kerware.simulateurreusine;

/**
 * Calcule le plafonnement de la réduction d'impôt liée au quotient familial
 */
public class PlafonneurImpot {
    private static final double NOMBRE_PARTS = 0.5;
    private static final double PLAFOND_DEMI_PART = 1759; // Valeur du plafond pour une demi-part

    /**
     * Calcule l'impôt plafonné selon les règles du quotient familial
     * @param impotBrutDeclarants Impôt brut calculé avec les parts des déclarants uniquement
     * @param impotBrutTotal Impôt brut calculé avec toutes les parts
     * @param nombrePartsTotal Nombre total de parts du foyer
     * @param nombrePartsDeclarants Nombre de parts des déclarants
     * @return Impôt après plafonnement
     */
    public double calculerImpotPlafonne(double impotBrutDeclarants, double impotBrutTotal,
                                        double nombrePartsTotal, double nombrePartsDeclarants) {
        double baisseImpot = impotBrutDeclarants - impotBrutTotal;
        double ecartParts = nombrePartsTotal - nombrePartsDeclarants;
        double plafondBaisseAutorisee = (ecartParts / NOMBRE_PARTS) * PLAFOND_DEMI_PART;

        if (baisseImpot >= plafondBaisseAutorisee) {
            return impotBrutDeclarants - plafondBaisseAutorisee;
        }
        return impotBrutTotal;
    }
}