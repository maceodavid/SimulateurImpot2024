package com.kerware.simulateurreusine;

/**
 * Simulateur de calcul d'impôt sur le revenu pour l'année 2024 (revenus 2023)
 * Cette classe implémente les règles de calcul de l'impôt sur le revenu en France.
 */
public class Simulateur {
    private final FoyerFiscal foyerFiscal;
    private final CalculateurParts calculateurParts;
    private final CalculateurAbattement calculateurAbattement;
    private final CalculateurImpot calculateurImpot;
    private final CalculateurDecote calculateurDecote;
    private final CalculateurContributionExceptionnelle calculateurContribution;

    public Simulateur() {
        this.foyerFiscal = new FoyerFiscal();
        this.calculateurParts = new CalculateurParts();
        this.calculateurAbattement = new CalculateurAbattement();
        this.calculateurImpot = new CalculateurImpot();
        this.calculateurDecote = new CalculateurDecote();
        this.calculateurContribution = new CalculateurContributionExceptionnelle();
    }

    public int calculImpot(int revenuNetDecl1, int revenuNetDecl2,
                           SituationFamiliale situation, int nbEnfants,
                           int nbEnfantsHandicap, boolean parentIsole) {
        // Validation et initialisation du foyer fiscal
        validerDonnees(revenuNetDecl1, revenuNetDecl2, situation, nbEnfants,
                nbEnfantsHandicap, parentIsole);
        initialiserFoyerFiscal(revenuNetDecl1, revenuNetDecl2, situation,
                nbEnfants, nbEnfantsHandicap, parentIsole);

        // Calcul des différents éléments
        double nombreParts = calculateurParts.calculerNombreParts(foyerFiscal);
        foyerFiscal.setNombreParts(nombreParts);

        double abattement = calculateurAbattement.calculerAbattement(foyerFiscal);
        foyerFiscal.setAbattement(abattement);

        double revenuImposable = calculerRevenuImposable(abattement);
        foyerFiscal.setRevenuImposable(revenuImposable);

        // Calcul de l'impôt et des ajustements
        double impotBrut = calculateurImpot.calculerImpotBrut(revenuImposable, nombreParts);
        foyerFiscal.setImpotBrut(impotBrut);

        // Calcul de l'impôt brut pour les déclarants seuls
        double partsDeclarants = situation.estCouple() ? 2.0 : 1.0;
        double impotBrutDeclarants = calculateurImpot.calculerImpotBrut(revenuImposable, partsDeclarants);

        // Calcul de l'impôt brut avec toutes les parts
        double nombrePartsTotal = calculateurParts.calculerNombreParts(foyerFiscal);
        double impotBrutTotal = calculateurImpot.calculerImpotBrut(revenuImposable, nombrePartsTotal);

        // Application du plafonnement
        PlafonneurImpot plafonneur = new PlafonneurImpot();
        double impotBrutPlafonne = plafonneur.calculerImpotPlafonne(
                impotBrutDeclarants,
                impotBrutTotal,
                nombrePartsTotal,
                partsDeclarants
        );

        foyerFiscal.setNombreParts(nombrePartsTotal);
        foyerFiscal.setImpotBrut(impotBrutPlafonne);

        // Calcul de la décote et de la contribution exceptionnelle
        double decote = calculateurDecote.calculerDecote(impotBrutPlafonne, situation.estCouple());
        foyerFiscal.setDecote(decote);

        double contribution = calculateurContribution.calculerContribution(revenuImposable,
                situation.estCouple());

        double impotNet = impotBrutPlafonne - decote + contribution;
        foyerFiscal.setImpotNet(impotNet);

        System.out.println("Abattement : " + abattement);
        System.out.println("Revenu fiscal de référence : " + revenuImposable);
        System.out.println("Nombre de parts : " + nombreParts);
        System.out.println("Contribution exceptionnelle sur les hauts revenus : " + contribution);
        System.out.println("Impôt brut du foyer fiscal complet : " + impotBrut);
        System.out.println("Impôt brut après plafonnement avant decote : " + impotBrutPlafonne);
        System.out.println("Decote : " + decote);
        System.out.println("Impôt sur le revenu net final : " + impotNet);

        return (int) Math.round(impotNet);
    }

    private void validerDonnees(int revenuNetDecl1, int revenuNetDecl2,
                                SituationFamiliale situation, int nbEnfants,
                                int nbEnfantsHandicap, boolean parentIsole) {
        if (revenuNetDecl1 < 0 || revenuNetDecl2 < 0) {
            throw new IllegalArgumentException("Les revenus ne peuvent pas être négatifs");
        }
        if (situation == null) {
            throw new IllegalArgumentException("La situation familiale ne peut pas être null");
        }
        if (nbEnfants < 0 || nbEnfants > 7) {
            throw new IllegalArgumentException("Le nombre d'enfants doit être entre 0 et 7");
        }
        if (nbEnfantsHandicap < 0 || nbEnfantsHandicap > nbEnfants) {
            throw new IllegalArgumentException("Le nombre d'enfants en situation de handicap invalide");
        }
        if (parentIsole && situation.estCouple()) {
            throw new IllegalArgumentException("Un parent isolé ne peut pas être marié ou pacsé");
        }
        if (!situation.estCouple() && revenuNetDecl2 > 0) {
            throw new IllegalArgumentException("Un célibataire ne peut pas avoir de second revenu");
        }
    }

    private void initialiserFoyerFiscal(int revenuNetDecl1, int revenuNetDecl2,
                                        SituationFamiliale situation, int nbEnfants,
                                        int nbEnfantsHandicap, boolean parentIsole) {
        foyerFiscal.setRevenuNetDeclarant1(revenuNetDecl1);
        foyerFiscal.setRevenuNetDeclarant2(revenuNetDecl2);
        foyerFiscal.setSituationFamiliale(situation);
        foyerFiscal.setNbEnfantsACharge(nbEnfants);
        foyerFiscal.setNbEnfantsSituationHandicap(nbEnfantsHandicap);
        foyerFiscal.setParentIsole(parentIsole);
    }

    private double calculerRevenuImposable(double abattement) {
        return Math.max(0, foyerFiscal.getRevenuNetDeclarant1() +
                foyerFiscal.getRevenuNetDeclarant2() - abattement);
    }

    // Getters pour l'adaptateur
    public double getRevenuReference() {
        return foyerFiscal.getRevenuImposable();
    }

    public double getAbattement() {
        return foyerFiscal.getAbattement();
    }

    public double getNbParts() {
        return foyerFiscal.getNombreParts();
    }

    public double getImpotAvantDecote() {
        return foyerFiscal.getImpotBrut();
    }

    public double getDecote() {
        return foyerFiscal.getDecote();
    }

    public double getImpotNet() {
        return foyerFiscal.getImpotNet();
    }

    public double getContribExceptionnelle() {
        return calculateurContribution.calculerContribution(
                foyerFiscal.getRevenuImposable(),
                foyerFiscal.getSituationFamiliale().estCouple()
        );
    }
}