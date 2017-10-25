package ejbs;

import entities.*;
import exceptions.UtilisateurNonInscritException;
import exceptions.VilleNonTrouvee;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@Stateless(name = "UtilisateurBean")
public class MaFacadeUtilisateurBean implements MaFacadeUtilisateur {

    @PersistenceContext(unitName = "monUnite")
    EntityManager em;

    @Override
    public Reservation reserverPlace(String login, int idTrajet, int nbPlaces, int idVilleArrivee) throws VilleNonTrouvee {
        Utilisateur utilisateur = em.find(Utilisateur.class, login);
        Trajet trajet = em.find(Trajet.class,idTrajet);
        List<Etape> etapes = trajet.getListeEtape();
        Reservation reservation = new Reservation();
        reservation.setStatut("attente");
        reservation.setNbPlace(nbPlaces);
        reservation.setTrajetReservation(trajet);
        reservation.setUtilisateurReservation(utilisateur);

        // Teste si la ville d'arrivée est une étape
        if(trajet.getVilleArrivee().getIdVille() != idVilleArrivee) {
            Etape arrivee = null;
            for (Etape etape : etapes) {
                if (etape.getIdEtape() == idVilleArrivee) {
                    arrivee = etape;
                }
            }
            if(null == arrivee){
                throw new VilleNonTrouvee();
            }
            reservation.setDescendA(arrivee);
        } else {
            reservation.setDescendA(null);
        }

        return reservation;
    }

    @Override
    public boolean faireCommentaire(String login, int idTrajet) {
        return false;
    }

    @Override
    public boolean donnerNote(String login, int idTrajet) {
        return false;
    }

    @Override
    public List<Appreciation> avoirNotesTrajet(String login, int idTrajet) {
        return null;
    }

    @Override
    public List<Appreciation> avoirNotesTotal(String login) {
        return null;
    }

    @Override
    public float moyenneNotes(String login) {
        return 0;
    }

    @Override
    public Trajet proposerTrajet(String login, String villeDepart, String villeArrivee, List<String> etapes, String date) {
        return null;
    }

    @Override
    public Vehicule ajouterVehicule(String login, String nomVehicule, String modele, int idGabarit, int nbPlaces) {
        return null;
    }

    @Override
    public boolean annulerTrajet(String login, int idTrajet) {
        return false;
    }

    @Override
    public List<Reservation> avoirReservations(String login, int idTrajet) {
        return null;
    }

    @Override
    public boolean refuserReservation(String login, int idReservation) {
        return false;
    }

    @Override
    public boolean accepterReservation(String login, int idreservation) {
        return false;
    }
}