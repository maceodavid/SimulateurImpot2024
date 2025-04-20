package com.kerware.simulateurreusine;

/**
 * Calcule le nombre de parts fiscales selon la situation familiale
 */
public class CalculateurParts {
    private static final double PART_ENFANT_1_2 = 0.5;
    private static final double PART_ENFANT_SUPPLEMENTAIRE = 1.0;
    private static final double PART_PARENT_ISOLE = 0.5;
    private static final double PART_VEUF_AVEC_ENFANT = 1.0;
    private static final double PART_ENFANT_HANDICAP = 0.5;

    /**
     * Calcule le nombre total de parts du foyer fiscal
     */
    public double calculerNombreParts(FoyerFiscal foyer) {
        double parts = getPartsBase(foyer.getSituationFamiliale());
        parts += calculerPartsEnfants(foyer.getNbEnfantsACharge());
        parts += calculerPartsHandicap(foyer.getNbEnfantsSituationHandicap());

        if (foyer.isParentIsole() && foyer.getNbEnfantsACharge() > 0) {
            parts += PART_PARENT_ISOLE;
        }

        if (foyer.getSituationFamiliale() == SituationFamiliale.VEUF && foyer.getNbEnfantsACharge() > 0) {
            parts += PART_VEUF_AVEC_ENFANT;
        }

        return parts;
    }

    private double getPartsBase(SituationFamiliale situation) {
        return switch (situation) {
            case CELIBATAIRE, DIVORCE, VEUF -> 1.0;
            case MARIE, PACSE -> 2.0;
        };
    }

    /**
     * Calcule le nombre de parts pour les enfants à charge
     */
    private double calculerPartsEnfants(int nbEnfants) {
        if (nbEnfants <= 2) {
            return nbEnfants * PART_ENFANT_1_2;
        } else {
            return PART_ENFANT_1_2 * 2 + (nbEnfants - 2) * PART_ENFANT_SUPPLEMENTAIRE;
        }
    }

    /**
     * Calcule les parts supplémentaires pour les enfants en situation de handicap
     */
    private double calculerPartsHandicap(int nbEnfantsHandicap) {
        return nbEnfantsHandicap * PART_ENFANT_HANDICAP;
    }
}