package com.kerware.simulateurreusine;

public enum SituationFamiliale {
    CELIBATAIRE,
    PACSE,
    MARIE,
    DIVORCE,
    VEUF;

    public boolean estCouple() {
        return this == MARIE || this == PACSE;
    }
}
