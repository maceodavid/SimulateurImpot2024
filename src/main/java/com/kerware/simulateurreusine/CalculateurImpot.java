package com.kerware.simulateurreusine;

/**
 * Calculateur de l'impôt brut selon les tranches d'imposition
 */
public class CalculateurImpot {
    private static final Tranche[] TRANCHES = {
            new Tranche(0, 11294, 0.0),
            new Tranche(11294, 28797, 0.11),
            new Tranche(28797, 82341, 0.30),
            new Tranche(82341, 177106, 0.41),
            new Tranche(177106, Integer.MAX_VALUE, 0.45)
    };

    /**
     * Calcule l'impôt brut selon les tranches d'imposition
     * @param revenuImposable Revenu imposable du foyer
     * @param nombreParts Nombre de parts du foyer
     * @return Montant de l'impôt brut
     */
    public double calculerImpotBrut(double revenuImposable, double nombreParts) {
        double revenuParPart = revenuImposable / nombreParts;
        double impotParPart = 0;

        for (Tranche tranche : TRANCHES) {
            if (revenuParPart > tranche.min) {
                double revenuDansLaTranche = Math.min(revenuParPart, tranche.max) - tranche.min;
                impotParPart += revenuDansLaTranche * tranche.taux;
            }
        }

        return Math.round(impotParPart * nombreParts);
    }

    private record Tranche(int min, int max, double taux) {}
}