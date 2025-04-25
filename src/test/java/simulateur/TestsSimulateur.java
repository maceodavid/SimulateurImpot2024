package simulateur;

import com.kerware.simulateurreusine.AdaptateurSimulateur;
import com.kerware.simulateurreusine.ICalculateurImpot;
import com.kerware.simulateurreusine.SituationFamiliale;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TestsSimulateur {

    private static ICalculateurImpot simulateur;

    @BeforeAll
    public static void setUp() {
        simulateur = new AdaptateurSimulateur();
    }

    public static Stream<Arguments> donneesPartsFoyerFiscal() {
        return Stream.of(
                Arguments.of(24000, "CELIBATAIRE", 0, 0, false, 1),
                Arguments.of(24000, "CELIBATAIRE", 1, 0, false, 1.5),
                Arguments.of(24000, "CELIBATAIRE", 2, 0, false, 2),
                Arguments.of(24000, "CELIBATAIRE", 3, 0, false, 3),
                Arguments.of(24000, "MARIE", 0, 0, false, 2),
                Arguments.of(24000, "PACSE", 0, 0, false, 2),
                Arguments.of(24000, "MARIE", 3, 1, false, 4.5),
                Arguments.of(24000, "DIVORCE", 2, 0, true, 2.5),
                Arguments.of(24000, "VEUF", 3, 0, true, 4.5)
                );

    }

    // COUVERTURE EXIGENCE : EXG_IMPOT_03
    @DisplayName("Tests du calcul des parts pour différents foyers fiscaux")
    @ParameterizedTest
    @MethodSource( "donneesPartsFoyerFiscal" )
    public void testNombreDeParts( int revenuNetDeclarant1, String situationFamiliale, int nbEnfantsACharge,
                                   int nbEnfantsSituationHandicap, boolean parentIsole, double nbPartsAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   nbPartsAttendu, simulateur.getNbPartsFoyerFiscal());

    }


    public static Stream<Arguments> donneesAbattementFoyerFiscal() {
        return Stream.of(
                Arguments.of(4900, "CELIBATAIRE", 0, 0, false, 495), // < 495 => 495
                Arguments.of(12000, "CELIBATAIRE", 0, 0, false, 1200), // 10 %
                Arguments.of(200000, "CELIBATAIRE", 0, 0, false, 14171) // > 14171 => 14171
        );

    }

    // COUVERTURE EXIGENCE : EXG_IMPOT_03
    @DisplayName("Tests des abattements pour les foyers fiscaux")
    @ParameterizedTest
    @MethodSource( "donneesAbattementFoyerFiscal" )
    public void testAbattement( int revenuNetDeclarant1, String situationFamiliale, int nbEnfantsACharge,
                                   int nbEnfantsSituationHandicap, boolean parentIsole, int abattementAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   abattementAttendu, simulateur.getAbattement());
    }


    public static Stream<Arguments> donneesRevenusFoyerFiscal() {
        return Stream.of(
                Arguments.of(12000, "CELIBATAIRE", 0, 0, false, 0), // 0%
                Arguments.of(20000, "CELIBATAIRE", 0, 0, false, 199), // 11%
                Arguments.of(35000, "CELIBATAIRE", 0, 0, false, 2736 ), // 30%
                Arguments.of(95000, "CELIBATAIRE", 0, 0, false, 19284), // 41%
                Arguments.of(200000, "CELIBATAIRE", 0, 0, false, 60768) // 45%
        );

    }

    // COUVERTURE EXIGENCE : EXG_IMPOT_04
    @DisplayName("Tests des différents taux marginaux d'imposition")
    @ParameterizedTest
    @MethodSource( "donneesRevenusFoyerFiscal" )
    public void testTrancheImposition( int revenuNet, String situationFamiliale, int nbEnfantsACharge,
                                int nbEnfantsSituationHandicap, boolean parentIsole, int impotAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNet );
        simulateur.setRevenusNetDeclarant2( 0);
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf(situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   impotAttendu, simulateur.getImpotSurRevenuNet());
    }



    public static Stream<Arguments> donneesRobustesse() {
        return Stream.of(
                Arguments.of(-1, 0,"CELIBATAIRE", 0, 0, false), // 0%
                Arguments.of(20000,0, null , 0, 0, false), // 11%
                Arguments.of(35000,0, "CELIBATAIRE", -1, 0, false ), // 30%
                Arguments.of(95000,0, "CELIBATAIRE", 0, -1, false), // 41%
                Arguments.of(200000,0, "CELIBATAIRE", 3, 4, false, 60768),
                Arguments.of(200000,0, "MARIE", 3, 2, true),
                Arguments.of(200000,0, "PACSE", 3, 2, true),
                Arguments.of(200000,0, "MARIE", 8, 0, false),
                Arguments.of(200000,10000, "CELIBATAIRE", 8, 0, false),
                Arguments.of(200000,10000, "VEUF", 8, 0, false),
                Arguments.of(200000,10000, "DIVORCE", 8, 0, false)
        );
    }

    // COUVERTURE EXIGENCE : Robustesse
    @DisplayName("Tests de robustesse avec des valeurs interdites")

    @ParameterizedTest( name ="Test avec revenuNetDeclarant1={0}, revenuDeclarant2={1}, situationFamiliale={2}, nbEnfantsACharge={3}, nbEnfantsSituationHandicap={4}, parentIsole={5}")
    @MethodSource( "donneesRobustesse" )
    public void testRobustesse( int revenuNetDeclarant1, int revenuNetDeclarant2 , String situationFamiliale, int nbEnfantsACharge,
                                       int nbEnfantsSituationHandicap, boolean parentIsole) {

        // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( revenuNetDeclarant2 );
        if ( situationFamiliale == null )
                simulateur.setSituationFamiliale( null  );
        else
                simulateur.setSituationFamiliale( SituationFamiliale.valueOf( situationFamiliale ));
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act & Assert
        assertThrows( IllegalArgumentException.class,  () -> simulateur.calculImpotSurRevenuNet());


    }

    // AVEC D'AUTRES IDEES DE TESTS
    // AVEC @ParameterizedTest et @CsvFileSource
    @DisplayName("Tests supplémentaires de cas variés de foyers fiscaux - ")
    @ParameterizedTest( name = " avec revenuNetDeclarant1={0}, revenuNetDeclarant2={1}, situationFamiliale={2}, nbEnfantsACharge={3}, nbEnfantsSituationHandicap={4}, parentIsole={5} - IMPOT NET ATTENDU = {6}")
    @CsvFileSource( resources={"/datasImposition.csv"} , numLinesToSkip = 1 )
    public void testCasImposition( int revenuNetDeclarant1, int revenuNetDeclarant2,  String situationFamiliale, int nbEnfantsACharge,
                                       int nbEnfantsSituationHandicap, boolean parentIsole, int impotAttendu) {

       // Arrange
        simulateur.setRevenusNetDeclarant1( revenuNetDeclarant1 );
        simulateur.setRevenusNetDeclarant2( revenuNetDeclarant2 );
        simulateur.setSituationFamiliale( SituationFamiliale.valueOf( situationFamiliale) );
        simulateur.setNbEnfantsACharge( nbEnfantsACharge );
        simulateur.setNbEnfantsSituationHandicap( nbEnfantsSituationHandicap );
        simulateur.setParentIsole( parentIsole );

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(   Integer.valueOf(impotAttendu), simulateur.getImpotSurRevenuNet());
    }



// Ajout des nouveaux tests pour augmenter la couverture

    // Test de la contribution exceptionnelle pour les hauts revenus
    public static Stream<Arguments> donneesContributionExceptionnelle() {
        return Stream.of(
                // Cas sans contribution (revenus < seuil)
                Arguments.of(200000, "CELIBATAIRE", 0, 0, false, 0),
                // Cas avec contribution pour célibataire
                Arguments.of(300000, "CELIBATAIRE", 0, 0, false, 1075),
                Arguments.of(600000, "CELIBATAIRE", 0, 0, false, 10933),
                // Cas avec contribution pour couple
                Arguments.of(600000, "MARIE", 0, 0, false, 2575),
                Arguments.of(1200000, "MARIE", 0, 0, false, 22433)
        );
    }

    @DisplayName("Tests de la contribution exceptionnelle sur hauts revenus")
    @ParameterizedTest
    @MethodSource("donneesContributionExceptionnelle")
    public void testContributionExceptionnelle(int revenuNetDeclarant1, String situationFamiliale,
                                               int nbEnfantsACharge, int nbEnfantsSituationHandicap,
                                               boolean parentIsole, int contributionAttendue) {

        // Arrange
        simulateur.setRevenusNetDeclarant1(revenuNetDeclarant1);
        simulateur.setRevenusNetDeclarant2(0);
        simulateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        simulateur.setNbEnfantsACharge(nbEnfantsACharge);
        simulateur.setNbEnfantsSituationHandicap(nbEnfantsSituationHandicap);
        simulateur.setParentIsole(parentIsole);

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(contributionAttendue, simulateur.getContribExceptionnelle());
    }

    // Test des cas limites d'abattement
    public static Stream<Arguments> donneesAbattementLimites() {
        return Stream.of(
                // Cas limites minimum d'abattement
                Arguments.of(4000, 0, "CELIBATAIRE", 495),
                // Cas limites maximum d'abattement
                Arguments.of(150000, 0, "CELIBATAIRE", 14171),
                // Cas particuliers couples
                Arguments.of(200000, 0, "MARIE", 14171),
                Arguments.of(150000, 150000, "MARIE", 28342) // 2 * max
        );
    }

    @DisplayName("Tests des cas limites d'abattement")
    @ParameterizedTest
    @MethodSource("donneesAbattementLimites")
    public void testAbattementCasLimites(int revenuNetDeclarant1, int revenuNetDeclarant2,
                                         String situationFamiliale, int abattementAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1(revenuNetDeclarant1);
        simulateur.setRevenusNetDeclarant2(revenuNetDeclarant2);
        simulateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        simulateur.setNbEnfantsACharge(0);
        simulateur.setNbEnfantsSituationHandicap(0);
        simulateur.setParentIsole(false);

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(abattementAttendu, simulateur.getAbattement());
    }

    // Test du plafonnement du quotient familial
    @DisplayName("Tests du plafonnement du quotient familial")
    @ParameterizedTest
    @CsvSource({
            "100000, CELIBATAIRE, 3, 0, false, 14093",  // Avec plafonnement
            "50000, MARIE, 4, 1, false, 0",             // Avec plafonnement et handicap
            "30000, DIVORCE, 2, 0, true, 0"             // Avec parent isolé
    })
    public void testPlafonnementQuotientFamilial(int revenuNetDeclarant1,
                                                 String situationFamiliale, int nbEnfantsACharge,
                                                 int nbEnfantsSituationHandicap, boolean parentIsole,
                                                 int impotAttendu) {

        // Arrange
        simulateur.setRevenusNetDeclarant1(revenuNetDeclarant1);
        simulateur.setRevenusNetDeclarant2(0);
        simulateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        simulateur.setNbEnfantsACharge(nbEnfantsACharge);
        simulateur.setNbEnfantsSituationHandicap(nbEnfantsSituationHandicap);
        simulateur.setParentIsole(parentIsole);

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(impotAttendu, simulateur.getImpotSurRevenuNet());
    }


    // Tests des validations supplémentaires
    @DisplayName("Tests des cas d'erreur supplémentaires")
    @Test
    public void testCasErreursSupplementaires() {
        // Test erreur lors de la modification du nombre d'enfants handicapés après définition du nombre d'enfants
        simulateur.setNbEnfantsACharge(2);
        simulateur.setNbEnfantsSituationHandicap(3);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test erreur avec situation familiale null
        simulateur.setSituationFamiliale(null);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test erreur avec nombre d'enfants négatif
        simulateur.setNbEnfantsACharge(-1);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test erreur avec nombre d'enfants handicapés négatif
        simulateur.setNbEnfantsSituationHandicap(-1);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test erreur avec revenus négatifs pour déclarant 1
        simulateur.setRevenusNetDeclarant1(-1);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test erreur avec revenus négatifs pour déclarant 2
        simulateur.setRevenusNetDeclarant2(-1);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());
    }

    @DisplayName("Tests des cas d'erreur pour les situations familiales incompatibles")
    @Test
    public void testCasErreursIncompatibilites() {
        // Test parent isolé marié
        simulateur.setSituationFamiliale(SituationFamiliale.MARIE);
        simulateur.setNbEnfantsACharge(1);
        simulateur.setParentIsole(true);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test parent isolé pacsé
        simulateur.setSituationFamiliale(SituationFamiliale.PACSE);
        simulateur.setNbEnfantsACharge(1);
        simulateur.setParentIsole(true);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test second revenu pour célibataire
        simulateur.setSituationFamiliale(SituationFamiliale.CELIBATAIRE);
        simulateur.setRevenusNetDeclarant2(10000);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test second revenu pour divorcé
        simulateur.setSituationFamiliale(SituationFamiliale.DIVORCE);
        simulateur.setRevenusNetDeclarant2(10000);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test second revenu pour veuf
        simulateur.setSituationFamiliale(SituationFamiliale.VEUF);
        simulateur.setRevenusNetDeclarant2(10000);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());
    }

    @DisplayName("Tests des limites du nombre d'enfants")
    @Test
    public void testLimitesNombreEnfants() {
        // Test dépassement nombre maximum d'enfants
        simulateur.setNbEnfantsACharge(8);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());

        // Test modification du nombre d'enfants après définition des enfants handicapés
        simulateur.setNbEnfantsSituationHandicap(2);
        simulateur.setNbEnfantsACharge(1);
        assertThrows(IllegalArgumentException.class, () -> simulateur.calculImpotSurRevenuNet());
    }

    @DisplayName("Tests des cas limites de la décote")
    @ParameterizedTest
    @CsvSource({
            // Format: revenuNet, situationFamiliale, decoteAttendue
            "19000, CELIBATAIRE, 584",  // Décote maximale célibataire
            "40000, MARIE, 777",       // Décote maximale couple
            "50000, CELIBATAIRE, 0",    // Pas de décote (au-dessus du seuil)
            "100000, MARIE, 0"          // Pas de décote couple (au-dessus du seuil)
    })
    public void testCasLimitesDecote(int revenuNet, String situationFamiliale, int decoteAttendue) {
        // Arrange
        simulateur.setRevenusNetDeclarant1(revenuNet);
        simulateur.setRevenusNetDeclarant2(0);
        simulateur.setSituationFamiliale(SituationFamiliale.valueOf(situationFamiliale));
        simulateur.setNbEnfantsACharge(0);
        simulateur.setNbEnfantsSituationHandicap(0);
        simulateur.setParentIsole(false);

        // Act
        simulateur.calculImpotSurRevenuNet();

        // Assert
        assertEquals(decoteAttendue, simulateur.getDecote());
    }
}
