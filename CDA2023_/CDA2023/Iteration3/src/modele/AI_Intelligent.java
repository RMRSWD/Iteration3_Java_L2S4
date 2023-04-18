package modele;

import java.util.List;

public class AI_Intelligent extends Damier implements AI_Strategie {
    public AI_Intelligent() {
        super();
    }

    //Fonction d'évaluation pour une couleur donnée
    public int evaluationDamier(Damier damier_, Joueur joueur1, Joueur joueur2, Joueur joueurCourant) {
        int note = 0;
        //Verifier si le jeu est fini
        //JeuFini retourne true s'il reste encore le coup à faire
        //Ici, quand l'ordinateur faire sa partie, il reste encore des coups à faire. Donc on met not pour continuer d'evaluation
        if (!damier_.JeuFini(joueur1, joueur2)) {
            String vainqueur = damier_.JeuTermine(joueur1.getNomJ(), joueur2.getNomJ());
            if (vainqueur.equals(joueurCourant.getNomJ())) {
                return 1000;
            } else {
                return -1000;
            }
        }
        for (int i = 0; i < getLigne(); i++) {
            for (int j = 0; j < getColonne(); j++) {
                String couleurJoueurCourant = this.getInit()[i][j];
                //vérifier la posion dans un coin
                if (couleurJoueurCourant == joueurCourant.getCouleur()) {

                    if (
                            (i == 0 && j == 0) ||
                                    (i == 0 && j == getColonne() - 1) ||
                                    (i == getLigne() - 1 && j == 0) ||
                                    (i == getLigne() - 1 && j == getColonne() - 1)
                    ) {
                        note += 11;
//                        verifie si la position est au bord
                    } else if (
                            i == 0 || i == getLigne() - 1 || j == 0 || j == getColonne() - 1
                    ) {
                        note += 6;
//                        verifie si la position est autre ailleurs
                    } else {
                        note += 1;
                    }
                }
            }
        }
        return note;
    }

    public int Minimax(Damier damier_, int profondeur, boolean isMaximizingPlayer, Joueur j1, Joueur j2, Joueur joueurCourant) {
        if (profondeur == 0 || damier_.JeuFini(j1, j2)) {
            return evaluationDamier(damier_, j1, j2, joueurCourant);
        }
        if (isMaximizingPlayer) {
            int meilleurNote = Integer.MIN_VALUE;
            List<Coup> coupsPossibles = damier_.prendTousLesCoupsPossible(joueurCourant);
            for (Coup list : coupsPossibles) {
                Damier new_Damier = new Damier(damier_.getInit());
                Coup new_Coup = new Coup(list.getNumLigne(), list.getLettre());
                new_Damier.effectuerCoup(new_Coup, joueurCourant);
                int note = Minimax(new_Damier, profondeur - 1, false, j1, j2, joueurCourant);
                meilleurNote = Math.max(meilleurNote, note);
            }
            return meilleurNote;
        } else {
            int meilleurNote = Integer.MAX_VALUE;
            Joueur Adversaire = joueurCourant == j1 ? j2 : j1;
            List<Coup> coupsPossibles_ = damier_.prendTousLesCoupsPossible(Adversaire);
            for (Coup list : coupsPossibles_) {
                Damier new_Damier = new Damier(damier_.getInit());
                Coup new_Coup = new Coup(list.getNumLigne(), list.getLettre());
                new_Damier.effectuerCoup(new_Coup, joueurCourant);
                int note = Minimax(new_Damier, profondeur - 1, true, j1, j2, joueurCourant);
                meilleurNote = Math.min(meilleurNote, note);
            }
            return meilleurNote;
        }
    }
    public Coup ChercherMeilleurCoup(Damier damier_, Joueur j1, Joueur j2, Joueur joueurCourant, int profondeur) {
        List<Coup> possibleCoups = damier_.prendTousLesCoupsPossible(joueurCourant);
        if (possibleCoups.isEmpty()) {
            return null;
        }
        Coup meilleurCoup = possibleCoups.get(0);//prendre le coup à la première position dans List
        int meilleurValeur = Integer.MIN_VALUE;
        for (Coup coup : possibleCoups) {
            int valueur = Minimax(damier_, profondeur - 1, false, j1, j2, joueurCourant);
            if (valueur > meilleurValeur) {
                meilleurValeur = valueur;
                meilleurCoup = coup;
            }
        }
        return meilleurCoup;
    }

    @Override
    public Coup CoupAI(Damier damier, Joueur joueur1, Joueur joueur2, Joueur joueurCourant) {
        return ChercherMeilleurCoup(damier, joueur1, joueur2, joueurCourant, 5);
    }
}





