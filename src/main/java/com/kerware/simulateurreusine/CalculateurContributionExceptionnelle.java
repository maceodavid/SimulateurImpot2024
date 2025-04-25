package com.kerware.simulateurreusine;

/**
 * Calculateur de la contribution exceptionnelle sur les hauts revenus
 */
public class CalculateurContributionExceptionnelle {
    private static final ContributionTranche[] TRANCHES_CELIBATAIRE = {
            new ContributionTranche(0, 250000, 0.0),
            new ContributionTranche(250000, 500000, 0.03),
            new ContributionTranche(500000, 1000000, 0.04),
            new ContributionTranche(1000000, Integer.MAX_VALUE, 0.04)
    };

    private static final ContributionTranche[] TRANCHES_COUPLE = {
            new ContributionTranche(0, 500000, 0.0),
            new ContributionTranche(500000, 1000000, 0.03),
            new ContributionTranche(1000000, Integer.MAX_VALUE, 0.04)
    };

    /**
     * Calcule la contribution exceptionnelle sur les hauts revenus en fonction
     * du revenu fiscal de référence et du statut marital.
     *
     * @param revenuFiscalReference le montant du revenu fiscal de référence
     * @param estCouple indique si le contribuable est un couple (true)
     *                  ou une personne seule (false)
     * @return le montant de la contribution exceptionnelle arrondi
     */
    public double calculerContribution(double revenuFiscalReference, boolean estCouple) {
        ContributionTranche[] tranches = estCouple ? TRANCHES_COUPLE : TRANCHES_CELIBATAIRE;
        double contribution = 0;

        for (ContributionTranche tranche : tranches) {
            if (revenuFiscalReference > tranche.min) {
                double revenuDansLaTranche = Math.min(revenuFiscalReference, tranche.max)
                                             - tranche.min;
                contribution += revenuDansLaTranche * tranche.taux;
            }
        }

        return Math.round(contribution);
    }

    private record ContributionTranche(int min, int max, double taux) {}
}