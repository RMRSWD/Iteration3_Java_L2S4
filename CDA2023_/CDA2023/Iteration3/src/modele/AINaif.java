package modele;

import java.util.List;
import java.util.Random;

public class AINaif implements AI_Strategie {
    public AINaif(){
    }
    public Coup CoupAI(Damier damier, Joueur joueur1, Joueur joueur2, Joueur joueurCourant ){
        Random random = new Random();
        Coup coupAI;
        List <Coup> tousLesCoupPossibleAI = damier.prendTousLesCoupsPossible(joueurCourant);
        int taille_table = tousLesCoupPossibleAI.size();
        if(taille_table > 0) {
            int randomNumber = random.nextInt(taille_table);
            coupAI = tousLesCoupPossibleAI.get(randomNumber);
            return coupAI;
        }
        else{
            return null;
        }
    }
}
