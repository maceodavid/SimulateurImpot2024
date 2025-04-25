package com.kerware.simulateurreusine;

public class AdaptateurSimulateur implements ICalculateurImpot {

    private final Simulateur simulateur = new Simulateur();

    private int revenusNetDecl1 = 0;
    private int revenusNetDecl2 = 0;
    private SituationFamiliale situationFamiliale;
    private int nbEnfantsACharge;
    private int nbEnfantsSituationHandicap;
    private boolean parentIsole;


    /**
     * Sets the net income for the first declarant.
     *
     * @param rn the net income to set for the first declarant
     */
    @Override
    public void setRevenusNetDeclarant1(int rn) {
        this.revenusNetDecl1 = rn;
    }

    /**
     * Sets the net income for the second declarant.
     *
     * @param rn the net income to set for the second declarant
     */
    @Override
    public void setRevenusNetDeclarant2(int rn) {
        this.revenusNetDecl2 = rn;
    }

    /**
     * Sets the familial situation for tax calculations.
     *
     * @param sf the familial situation, which can be one of the enumerated
     *           values in {@link SituationFamiliale} such as
     *           CELIBATAIRE, PACSE, MARIE, DIVORCE, or VEUF
     */
    @Override
    public void setSituationFamiliale(SituationFamiliale sf) {
        this.situationFamiliale = sf;
    }

    /**
     * Sets the number of dependent children for tax calculation purposes.
     *
     * @param nbe the number of children to declare as dependents
     */
    @Override
    public void setNbEnfantsACharge(int nbe) {
        this.nbEnfantsACharge = nbe;
    }

    /**
     * Sets the number of children in a situation of handicap for tax calculation purposes.
     *
     * @param nbesh the number of children in a situation of handicap to declare
     */
    @Override
    public void setNbEnfantsSituationHandicap(int nbesh) {
        this.nbEnfantsSituationHandicap = nbesh;
    }

    /**
     * Sets whether the parent is considered isolated for tax calculation purposes.
     *
     * @param pi a boolean indicating if the parent is isolated (true) or not (false)
     */
    @Override
    public void setParentIsole(boolean pi) {
        this.parentIsole = pi;
    }

    /**
     * Calculates the net income tax for the current fiscal situation using the simulator.
     * This method uses the provided attributes of the class,
     * including both declarants' net incomes,
     * familial situation, number of dependents, number of disabled dependents,
     * and whether the parent is isolated, to determine the applicable tax.
     * The computation is delegated to the underlying `simulateur` instance, which processes all
     * necessary validations, fiscal calculations, and adjustments such as abatements, decotes,
     * and contribution exceptions.
     * The calculated net taxes are based on information such as
     * - The net income for each declarant.
     * - The marital or familial situation (e.g., single, married, divorced).
     * - The number of dependent children and disabled dependents.
     * - Whether the individual is considered as an isolated parent.
     * This method is an override of a superclass or interface method and assumes
     * all input member variables have been initialized with valid values.
     */
    @Override
    public void calculImpotSurRevenuNet() {
         simulateur.calculImpot(revenusNetDecl1, revenusNetDecl2,
                 situationFamiliale, nbEnfantsACharge, nbEnfantsSituationHandicap, parentIsole);
    }

    /**
     * Retrieves the net income of the first declarant.
     *
     * @return the net income of the first declarant as an integer
     */
    @Override
    public int getRevenuNetDeclatant1() {
        return revenusNetDecl1;
    }

    /**
     * Retrieves the net income of the second declarant.
     *
     * @return the net income of the second declarant as an integer
     */
    @Override
    public int getRevenuNetDeclatant2() {
        return revenusNetDecl2;
    }

    /**
     * Retrieves the exceptional contribution amount calculated for the taxpayer.
     * The calculation is delegated to the underlying simulator, which evaluates
     * the contribution based on taxable income and familial situation
     * (e.g., whether the taxpayer is part of a couple).
     *
     * @return the exceptional contribution as a double value
     */
    @Override
    public double getContribExceptionnelle() {
        return simulateur.getContribExceptionnelle();
    }

    /**
     * Retrieves the fiscal reference income calculated using the underlying simulator.
     *
     * @return the fiscal reference income as an integer
     */
    @Override
    public int getRevenuFiscalReference() {
        return (int)simulateur.getRevenuReference();
    }

    /**
     * Retrieves the abatement value calculated by the simulator.
     * This value represents a deduction applied during tax calculations
     * based on specific fiscal rules and the taxpayer's situation.
     *
     * @return the abatement value as an integer
     */
    @Override
    public int getAbattement() {
        return (int)simulateur.getAbattement();
    }

    /**
     * Retrieves the number of parts attributed to the fiscal household
     * for tax calculation purposes.
     * This value is determined based on factors such as marital status,
     * number of dependent children,
     * and other applicable considerations within the fiscal regulations.
     *
     * @return the number of parts for the fiscal household as a double
     */
    @Override
    public double getNbPartsFoyerFiscal() {
        return simulateur.getNbParts();
    }

    /**
     * Retrieves the gross income tax amount before applying any reductions (decote).
     * The computation is delegated to the underlying simulateur instance.
     *
     * @return the gross income tax amount as an integer
     */
    @Override
    public int getImpotAvantDecote() {
        return (int)simulateur.getImpotAvantDecote();
    }

    /**
     * Retrieves the amount of the "decote" (a reduction applied to the income tax amount
     * under certain conditions) as an integer.
     * This value is calculated by the underlying simulator (`simulateur`) based on
     * fiscal rules and the taxpayer's situation.
     *
     * @return the "decote" amount as an integer
     */
    @Override
    public int getDecote() {
        return (int)simulateur.getDecote();
    }

    /**
     * Retrieves the net income tax amount as an integer.
     * This value is computed using the underlying simulator,
     * which takes into account all applicable fiscal regulations
     * and adjustments based on the taxpayer's situation (e.g.,
     * familial situation, dependents, abatements, etc.).
     *
     * @return the calculated net income tax amount
     */
    @Override
    public int getImpotSurRevenuNet() {
        return (int)simulateur.getImpotNet();
    }
}
