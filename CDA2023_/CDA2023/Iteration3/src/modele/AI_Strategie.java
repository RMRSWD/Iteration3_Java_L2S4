package modele;

import vue.Ihm;

public interface AI_Strategie {

    Coup CoupAI(Damier damier, Joueur joueur1, Joueur joueur2, Joueur joueurCourant);
}
