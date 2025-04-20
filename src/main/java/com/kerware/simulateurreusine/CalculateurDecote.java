package com.kerware.simulateurreusine;

/**
 * Calculateur de la décote fiscale
 */
public class CalculateurDecote {
    private static final double SEUIL_DECLARANT_SEUL = 1929;
    private static final double SEUIL_DECLARANT_COUPLE = 3191;
    private static final double MAXIMUM_DECLARANT_SEUL = 873;
    private static final double MAXIMUM_DECLARANT_COUPLE = 1444;
    private static final double TAUX_DECOTE = 0.4525;

    /**
     * Calcule la décote selon la situation familiale
     */
    public double calculerDecote(double impotBrut, boolean estCouple) {
        double seuil = estCouple ? SEUIL_DECLARANT_COUPLE : SEUIL_DECLARANT_SEUL;
        double maximum = estCouple ? MAXIMUM_DECLARANT_COUPLE : MAXIMUM_DECLARANT_SEUL;

        if (impotBrut < seuil) {
            double decote = maximum - (impotBrut * TAUX_DECOTE);
            return Math.min(Math.round(decote), impotBrut);
        }

        return 0;
    }
}