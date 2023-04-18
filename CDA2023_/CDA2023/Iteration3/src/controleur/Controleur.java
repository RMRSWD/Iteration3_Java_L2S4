package controleur;

import modele.*;
import vue.Ihm;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controleur {
    private Ihm ihm;
     private Damier damier;

     private Joueur joueur1;
     private Joueur joueur2;

     private Joueur joueurCourant ;

     private Joueur joueurAdversaire;

     private Map<String,AI_Strategie> lesStrategieAI = new HashMap<>();
    public Controleur (Ihm ihm){
        this.ihm = ihm;
        ihm.setControleur(this);
        this.damier = new Damier();
        ihm.saisirNomJoueur1();
    }
    public String afficherDamier() {
        return damier.toString();
    }

    public void jouer() {
        damier.creerUnDamier();
        this.joueurCourant = joueur1;
        ihm.afficherDamier();
        do{
            ihm.demandeCoup(this.joueurCourant);
            ihm.afficherDamier();
            changerTourJoueur();
        }
        while(testJeuFini(joueurCourant));
        JoueurGargnePartie();
        ihm.demandeRejouer(joueur1, joueur2);

    }
    public void JoueurGargnePartie(){
        if(damier.JeuTermine(joueur1.getNomJ(), joueur2.getNomJ()).equals(joueur1.getNomJ())){
            joueur1.gagnerPartie();
            ihm.afficherGagneeUneParie(joueur1, damier.compterPionNoir());
            ihm.afficherPerduAdversaire(joueur2, damier.compterPionBlanc());
        }
        else if(damier.JeuTermine(joueur1.getNomJ(), joueur2.getNomJ()).equals(joueur2.getNomJ())){
            joueur2.gagnerPartie();
            ihm.afficherGagneeUneParie(joueur2, damier.compterPionBlanc());
            ihm.afficherPerduAdversaire(joueur1, damier.compterPionNoir());
        }
        else if(damier.JeuTermine(joueur1.getNomJ(), joueur2.getNomJ()).equals("ex aequo")){
            joueur1.casEgaux();
            ihm.afficherEgaux();
        }
    }
    public Joueur changerTourJoueur(){
        if(this.joueurCourant == joueur1){
            this.joueurCourant = joueur2;
        }
        else if (this.joueurCourant == joueur2){
            this.joueurCourant = joueur1;
        }
        return joueurCourant;
    }
    public void nomJoueur(String nomJ1, String nomJ2) {
        this.joueur1= new Joueur(nomJ1,"\u26AB");
        this.joueur2 = new Joueur(nomJ2, "\u26AA");
    }

    public boolean gererCoup(int numColonne, char lettre, Joueur joueurCourant) {
        Coup new_coup = new Coup(numColonne, lettre);
        boolean testCoup = damier.verifierCoup(new_coup, joueurCourant);
        return testCoup;
    }
    public void effectuerCoup(int numColonne, char lettre, Joueur joueurCourant){
        Coup new_Coup = new Coup(numColonne, lettre);
        damier.effectuerCoup(new_Coup, joueurCourant);
    }


    public boolean testJeuFini(Joueur joueurCourant){
        Joueur j1 = joueurCourant;
        Joueur j2 = getAdversaire(joueurCourant);
        boolean test = damier.JeuFini(j1,j2);
        return test;
    }
    public Joueur getAdversaire(Joueur joueurCourant) {
        if(joueurCourant == joueur1){
            joueurAdversaire = joueur2;
        }
        else if (joueurCourant == joueur2){
            joueurAdversaire = joueur1;
        }
        return joueurAdversaire;
    }
    public void AI (String codeAI){
        AddStrategie("1", new AINaif());
        AddStrategie("2",new AI_Intelligent());
        AI_Strategie ai_strategie = lesStrategieAI.get(codeAI);
        damier.creerUnDamier();
        this.joueurCourant = joueur1;
        ihm.afficherDamier();
        do{
            if(this.joueurCourant.getNomJ().equals("AI")) {
                Coup coupAI =  ai_strategie.CoupAI(damier, joueur1, joueur2,joueurCourant);
                if(coupAI != null){
                    damier.effectuerCoup(coupAI, joueurCourant);
                    ihm.afficherCoupIA(coupAI.getNumLigne(), coupAI.getLettre());
                }
                else{
                    ihm.passerTourIA();
                }
            }
            else{
                ihm.demandeCoup(this.joueurCourant);
            }
            ihm.afficherDamier();
            changerTourJoueur();
        }
        while(testJeuFini(joueurCourant));
        JoueurGargnePartie();
        ihm.demandeRejouerAI(joueur1, joueur2);
    }
    public void AddStrategie (String code, AI_Strategie strategie){
        this.lesStrategieAI.put(code, strategie);
    }

    public int CoupPossibleDuJoueur(Joueur joueur){
        List<Coup> coupDuJoueurCourant= damier.prendTousLesCoupsPossible(joueur);
        int numbreCoupPossible = coupDuJoueurCourant.size();
        return numbreCoupPossible;
    }
    public Coup PrendreUnCoupExemple(Joueur joueur){
        List<Coup> exempleUnCoupPourJoueur = damier.prendTousLesCoupsPossible(joueur);
        Coup coupExemple = exempleUnCoupPourJoueur.get(0);
        return coupExemple;
    }
    public int NumLigneExemple(Joueur joueur){
        Coup coupExemple=PrendreUnCoupExemple(joueur);
        int ligneExemple = coupExemple.getNumLigne();
        return  ligneExemple;
    }
    public char CharColonneExemple(Joueur joueur){
        Coup coupExemple = PrendreUnCoupExemple(joueur);
        char colonneExemple = coupExemple.getLettre();
        return colonneExemple;
    }
}