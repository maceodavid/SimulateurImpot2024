package com.kerware.simulateurreusine;

/**
 * Simulateur de calcul d'impôt sur le revenu pour l'année 2024 (revenus 2023)
 * Cette classe implémente les règles de calcul de l'impôt sur le revenu en France.
 */
public class Simulateur {
    public static final int NB_ENFANTS_MAX = 7;
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

    /**
     * Calculates the income tax for a household based on various parameters including incomes,
     * family situation, number of children, and special conditions.
     *
     * @param revenuNetDecl1 the net income of the first declarant
     * @param revenuNetDecl2 the net income of the second declarant, if applicable
     * @param situation the marital or family situation of the household
     * @param nbEnfants the total number of dependent children
     * @param nbEnfantsHandicap the number of dependent children with disabilities
     * @param parentIsole a boolean indicating if the household is led by a single parent
     * @return the calculated income tax for the household as an integer
     */
    public int calculImpot(int revenuNetDecl1, int revenuNetDecl2,
                           SituationFamiliale situation, int nbEnfants,
                           int nbEnfantsHandicap, boolean parentIsole) {

        validerEtInitialiserDonnees(revenuNetDecl1, revenuNetDecl2, situation, nbEnfants,
                nbEnfantsHandicap, parentIsole);

        double nombreParts = foyerFiscal.getNombreParts();
        double revenuImposable = foyerFiscal.getRevenuImposable();

        // Calcul de l'impôt et des ajustements
        double impotBrut = calculateurImpot.calculerImpotBrut(revenuImposable, nombreParts);
        foyerFiscal.setImpotBrut(impotBrut);

        // Calcul de l'impôt brut pour les déclarants seuls
        double partsDeclarants = situation.estCouple() ? 2.0 : 1.0;
        double impotBrutDeclarants =
                calculateurImpot.calculerImpotBrut(revenuImposable, partsDeclarants);

        // Calcul de l'impôt brut avec toutes les parts
        double nombrePartsTotal = calculateurParts.calculerNombreParts(foyerFiscal);
        double impotBrutTotal =
                calculateurImpot.calculerImpotBrut(revenuImposable, nombrePartsTotal);

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

        return (int) Math.round(impotNet);
    }

    private void validerEtInitialiserDonnees(int revenuNetDecl1, int revenuNetDecl2,
                                                SituationFamiliale situation, int nbEnfants,
                                                int nbEnfantsHandicap, boolean parentIsole) {

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
        if (nbEnfants < 0 || nbEnfants > NB_ENFANTS_MAX) {
            throw new IllegalArgumentException("Le nombre d'enfants doit être" +
                    "entre 0 et " + NB_ENFANTS_MAX);
        }
        if (nbEnfantsHandicap < 0 || nbEnfantsHandicap > nbEnfants) {
            throw new IllegalArgumentException(
                    "Le nombre d'enfants en situation de handicap invalide");
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

    /**
     * Retrieves the reference income for the tax household. This value corresponds
     * to the taxable income obtained from the tax household configuration.
     *
     * @return the reference income as a double
     */
    public double getRevenuReference() {
        return foyerFiscal.getRevenuImposable();
    }

    /**
     * Retrieves the abatement value associated with the tax household in the simulator.
     *
     * @return the abatement value as a double
     */
    public double getAbattement() {
        return foyerFiscal.getAbattement();
    }

    /**
     * Retrieves the number of tax parts for the household being simulated.
     *
     * @return the number of tax parts as a double
     */
    public double getNbParts() {
        return foyerFiscal.getNombreParts();
    }

    /**
     * Retrieves the gross tax amount before applying the decote (discount) for the tax household.
     *
     * @return the gross tax amount as a double
     */
    public double getImpotAvantDecote() {
        return foyerFiscal.getImpotBrut();
    }

    /**
     * Retrieves the decote value associated with the tax household in the simulator.
     *
     * @return the decote value as a double
     */
    public double getDecote() {
        return foyerFiscal.getDecote();
    }

    /**
     * Retrieves the net income tax amount for the tax household as calculated
     * by the simulator.
     *
     * @return the net income tax amount as a double
     */
    public double getImpotNet() {
        return foyerFiscal.getImpotNet();
    }

    /**
     * Retrieves the exceptional contribution amount for the tax household.
     * The calculation is based on the taxable income and familial situation.
     *
     * @return the exceptional contribution amount as a double
     */
    public double getContribExceptionnelle() {
        return calculateurContribution.calculerContribution(
                foyerFiscal.getRevenuImposable(),
                foyerFiscal.getSituationFamiliale().estCouple()
        );
    }
}