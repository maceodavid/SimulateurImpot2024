package com.kerware.simulateurreusine;

/**
 * Calcule l'abattement fiscal selon les règles en vigueur
 */
public class CalculateurAbattement {
    // Constantes pour les abattements
    private static final int MONTANT_MINIMAL = 495;
    private static final int MONTANT_MAXIMAL = 14171;
    private static final double TAUX = 0.1;

    /**
     * Calcule l'abattement total pour le foyer fiscal
     */
    public double calculerAbattement(FoyerFiscal foyer) {
        double abattement1 = calculerAbattementIndividuel(foyer.getRevenuNetDeclarant1());
        double abattement2 = foyer.getSituationFamiliale().estCouple() ?
                calculerAbattementIndividuel(foyer.getRevenuNetDeclarant2()) : 0;

        return abattement1 + abattement2;
    }

    private double calculerAbattementIndividuel(int revenu) {
        double abattement = revenu * TAUX;
        return Math.max(MONTANT_MINIMAL,
                Math.min(abattement, MONTANT_MAXIMAL));
    }
}