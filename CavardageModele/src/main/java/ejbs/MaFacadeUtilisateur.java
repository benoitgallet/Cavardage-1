package ejbs;

import dtos.*;
import entities.*;
import exceptions.*;

import javax.ejb.Local;
import java.util.List;
import java.util.Map;

@Local
public interface MaFacadeUtilisateur {

    Reservation reserverPlace(String login, int idTrajet, int nbPlaces, String idVilleArrivee) throws VilleNonTrouvee;
    Appreciation donnerAppreciation(String login, int idTrajet, String commentaire, int note, String loginDestinataire);
    List<Appreciation> avoirNotesTrajet(int idTrajet);
    List<AppreciationDTO> avoirToutesAppreciations(String login);
    float moyenneNotes(String login) throws DivisionParZeroException;
    Vehicule ajouterVehicule(String login, String nomVehicule, String modele, String gabarit, int nbPlaces) throws VehiculeDejaExistantException;
    boolean supprimerVehicule(String login, int idVehicule);
    List<VehiculeDTO> listeVehicules(String login);
    boolean annulerTrajet(String login, int idTrajet) throws PasConducteurException;
    List<ReservationDTO> avoirReservationsAcceptees(String login, int idTrajet) throws PasConducteurException;
    List<ReservationDTO> avoirReservationsEnAttente(String login, int idTrajet) throws PasConducteurException;
    boolean refuserReservation(String login, int idReservation) throws PasConducteurException;
    boolean accepterReservation(String login, int idReservation) throws PasConducteurException;
    boolean supprimerNotification(String login, int idNotification);
    List<HistoriqueDTO> historiqueUtilisateur(String login);
    HistoriqueDTO uniqueHistoriqueUtilisateur(String login, int id);
    List<Gabarit> listeGabarits();
    boolean changerMotDePasse(String login, String motDePasse);
    boolean verifierMotDePasse(String login, String motDePasse);
    boolean supprimerUtilisateur(String login);
    List<Ville> getListeVilles();
    List<VilleDTO> getListeVilleDTO();
    List<TrajetDTO> rechercheTrajet(String villeDepart, String departementDepart, String villeArrive, String departementArrive, String date, String prix);
    void ajouterTrajet(String login, String villeDepart, String villeArrivee, String nomVehicule, String[] etapes, String date, String heure, String minute, String prix) throws PrixInferieurException;
    TrajetDTO avoirTrajet(int idTrajet);
    Notification creerNotification(String login,String message);
    int avoirNbPlacesRestantes(int idTrajet);
    List<TrajetDTO> avoirListeTrajet(String login);
    List<UtilisateurDTO> avoirPersonnesTrajet(String login, int idTrajet);
}
