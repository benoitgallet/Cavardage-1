package servlets;

import dtos.*;
import ejbs.MaFacadeUtilisateur;
import entities.Gabarit;
import entities.Ville;
import exceptions.PasConducteurException;
import exceptions.PrixInferieurException;
import exceptions.VilleNonTrouvee;

import javax.ejb.EJB;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@WebServlet("ControleurUtilisateur")
public class ControleurUtilisateur extends HttpServlet {

    @EJB
    MaFacadeUtilisateur maFacade;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String aFaire = request.getParameter("afaire");
        if (null == aFaire) {
            if (null != request.getSession().getAttribute("utilisateur")) {
                //display homepage
            } else {
                request.getRequestDispatcher("/WEB-INF/accueil.jsp");
            }
        } else {
            switch (aFaire) {
                case "trajetsEnCours":
                    voirTrajetsEnCours(request, response);
                    break;
                case "creerTrajet":
                    voirCreerTrajet(request, response);
                    break;
                case "voirVehicules":
                    voirVehicules(request, response);
                    break;
                case "voirHistorique":
                    voirHistorique(request, response);
                    break;
                case "enregistrerVehicule":
                    enregistrerVehicule(request, response);
                case "voirAppreciations":
                    voirAppreciations(request, response);
                    break;
                case "parametres":
                    parametres(request, response);
                    break;
                case "ajouterVehicule":
                    ajouterVehicule(request, response);
                    break;
                case "enregistrerTrajet":
                    enregistrerTrajet(request, response);
                    break;
                case "changerMotDePasse":
                    changerMotDepasse(request, response);
                    break;
                case "supprimerCompte":
                    supprimerCompte(request, response);
                    break;
                case "rechercherTrajet":
                    rechercherTrajet(request, response);
                    break;
                case "afficherRechercheTrajet":
                    afficherRechercheTrajet(request, response);
                    break;
                case "deconnexion":
                    aFaire = null;
                    request.getSession().removeAttribute("utilisateur");
                    request.getSession().invalidate();
                    response.sendRedirect(request.getContextPath());
                    break;
                case "gererTrajet":
                    gererTrajet(request,response);
                    break;
                case "detailsTrajet":
                    detailsTrajet(request, response);
                    break;
                case "reserverTrajet":
                    reserverTrajet(request, response);
                    break;
                default:
                    //display homepage
            }
        }
    }

    private void enregistrerTrajet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = (String) request.getSession().getAttribute("utilisateur");
        String villeDepart = request.getParameter("villeDepart");
        String villeArrivee = request.getParameter("villeArrivee");
        String date = request.getParameter("date");
        String heure = request.getParameter("heure");
        String minute = request.getParameter("minute");
        String nomVehicule = request.getParameter("vehicule");
        String prixVoyage = request.getParameter("prixVoyage");
        String[] etapes = request.getParameterValues("etape");
        String message;
        try {
            //maFacade.preAjoutVille(login, villeDepart, villeArrivee, nomVehicule, etapes, date, heure, prixVoyage);
            maFacade.ajouterTrajet(login, villeDepart, villeArrivee, nomVehicule, etapes, date, heure, minute, prixVoyage);
            message = "Trajet créé";
        } catch (PrixInferieurException e) {
            message = "Erreur : " + e.getMessage();
        }
        request.setAttribute("message", message);
        voirCreerTrajet(request, response);
    }

    private void voirTrajetsEnCours(HttpServletRequest request, HttpServletResponse response) {

    }

    private void voirCreerTrajet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = (String) request.getSession().getAttribute("utilisateur");
        List<VehiculeDTO> vehiculeDTOS = maFacade.listeVehicules(login);
        List<VilleDTO> listeVilles = maFacade.getListeVilleDTO();
        request.setAttribute("listeVehicules", vehiculeDTOS);
        request.setAttribute("listeVilles", listeVilles);
        request.setAttribute("aAfficher", "creerTrajet");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void voirVehicules(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = (String) request.getSession().getAttribute("utilisateur");
        List<VehiculeDTO> vehiculesDTO = maFacade.listeVehicules(login);
        request.setAttribute("listeVehicules", vehiculesDTO);
        List<Gabarit> gabarits = maFacade.listeGabarits();
        request.setAttribute("listeGabarits", gabarits);
        request.setAttribute("aAfficher", "vehicules");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void voirHistorique(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = (String) request.getSession().getAttribute("utilisateur");
        List<HistoriqueDTO> listeHistorique = maFacade.historiqueUtilisateur(login);
        request.setAttribute("listeHistorique", listeHistorique);
        request.setAttribute("aAfficher", "historique");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void voirAppreciations(HttpServletRequest request, HttpServletResponse response) {

    }

    private void parametres(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("aAfficher", "parametres");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void ajouterVehicule(HttpServletRequest request, HttpServletResponse response) {
        String nomvehicule = request.getParameter("nomVehicule");
        String modeleVehicule = request.getParameter("modeleVehicule");
        String gabaritVehicule = request.getParameter("gabaritVehicule");
        int nbPlaces = Integer.parseInt(request.getParameter("nbPlaces"));
    }

    private void enregistrerVehicule(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

    }

    private void changerMotDepasse(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String login = (String) request.getSession().getAttribute("utilisateur");
        String motDePasse1 = request.getParameter("nouveauMdP1");
        String motDePasse2 = request.getParameter("nouveauMdP2");
        String message;
        if (motDePasse1.equals("") || motDePasse2.equals("")) {
            message = "Un champs n'est pas rempli";
        } else if (!motDePasse1.equals(motDePasse2)) {
            message = "Les mots de passe ne sont pas identiques";
        } else {
            boolean b = maFacade.changerMotDePasse(login, motDePasse1);
            if (!b) {
                message = "Le nouveau mot de passe est identique à l'actuel";
            } else {
                message = "Votre mot de passe a bien été changé";
            }
        }
        request.setAttribute("message", message);
        request.setAttribute("aAfficher", "parametres");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void supprimerCompte(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String confirmation = request.getParameter("confirmation");
        if (null == confirmation) {
            request.setAttribute("aAfficher", "suppressionCompte");
            request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
        } else {
            if (confirmation.equals("ok")) {
                String login = (String) request.getSession().getAttribute("utilisateur");
                String motDePasse = request.getParameter("motDePasse");
                if (maFacade.verifierMotDePasse(login, motDePasse)) {
                    maFacade.supprimerUtilisateur(login);
                    request.getSession().setAttribute("utilisateur", null);
                    response.sendRedirect("ControleurAnonyme");
                } else {
                    request.setAttribute("message", "Mot de passe incorrect");
                    request.setAttribute("aAfficher", "suppressionCompte");
                    request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
                }
            }
        }
    }

    private void rechercherTrajet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        List<VilleDTO> listeVilles = maFacade.getListeVilleDTO();
        request.setAttribute("listeVilles", listeVilles);
        request.setAttribute("aAfficher", "rechercherTrajet");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void afficherRechercheTrajet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        String villeDepart = request.getParameter("nomVilleDepart");
        String nomVilleDepart = villeDepart.substring(0, villeDepart.length() - 4);
        String departementVilleDepart = villeDepart.substring(villeDepart.length() - 3, villeDepart.length() - 1);
        String villeArrive = request.getParameter("nomVilleArrivee");
        String nomVilleArrivee = villeArrive.substring(0, villeArrive.length() - 4);
        String departementVilleArrivee = villeArrive.substring(villeArrive.length() - 3, villeArrive.length() - 1);
        String date = request.getParameter("date");
        String prix = request.getParameter("prix");
        List<TrajetDTO> listeTrajetRecherche = maFacade.rechercheTrajet(nomVilleDepart, departementVilleDepart, nomVilleArrivee, departementVilleArrivee, date, prix);
        request.setAttribute("listeTrajetRecherche", listeTrajetRecherche);
        List<VilleDTO> listeVilles = maFacade.getListeVilleDTO();
        request.setAttribute("listeVilles", listeVilles);
        request.setAttribute("villeDepart", villeDepart);
        request.setAttribute("villeArrivee", villeArrive);
        request.setAttribute("date", date);
        if (!prix.equals("")) {
            request.setAttribute("prix", prix);
        }
        for(TrajetDTO t : listeTrajetRecherche){
            System.out.println(t.toString());
        }
        request.setAttribute("aAfficher", "rechercherTrajet");
        request.setAttribute("resultatsRecherche", "afficher");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void detailsTrajet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idTrajet = Integer.parseInt(request.getParameter("idTrajet"));
        TrajetDTO trajetDTO = maFacade.avoirTrajet(idTrajet);
        request.setAttribute("trajet", trajetDTO);
        request.setAttribute("aAfficher", "detailsTrajet");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void reserverTrajet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String idVilleArrivee;
        if(request.getParameter("etapeArrivee").equals("")){
           idVilleArrivee = request.getParameter("villeArrivee");
        } else {
            idVilleArrivee = request.getParameter("etapeArrivee");
        }
        int nbPlaces = Integer.parseInt(request.getParameter("nbPlacesReservees"));
        int idTrajet = Integer.parseInt(request.getParameter("idTrajet"));
        String login = (String) request.getSession().getAttribute("utilisateur");
        try {
            maFacade.reserverPlace(login, idTrajet, nbPlaces, idVilleArrivee);
        } catch (VilleNonTrouvee villeNonTrouvee) {
            villeNonTrouvee.printStackTrace();
            maFacade.creerNotification(login, "La ville d'arrivée n'a pas été trouvée.");
        }
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }

    private void gererTrajet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int idTrajet = Integer.parseInt(request.getParameter("idTrajet"));
        TrajetDTO trajetDTO = maFacade.avoirTrajet(idTrajet);
        int nbPlacesRestantes = maFacade.avoirNbPlacesRestantes(idTrajet);
        String login = (String) request.getSession().getAttribute("utilisateur");
        List<ReservationDTO> reservationsAttente = new ArrayList<>();
        List<ReservationDTO> reservationsAcceptees = new ArrayList<>();
        try {
            reservationsAttente = maFacade.avoirReservationsEnAttente(login, idTrajet);
            reservationsAcceptees = maFacade.avoirReservationsAcceptees(login, idTrajet);
         }catch (PasConducteurException e){
            maFacade.creerNotification(login,"Vous avez essayé de modifier un trajet dont vous n'êtes pas le conducteur.");
            e.printStackTrace();
        }
        request.setAttribute("reservationsAttente",reservationsAttente);
        request.setAttribute("reservationsAcceptees",reservationsAcceptees);
        request.setAttribute("trajet", trajetDTO);
        request.setAttribute("nbPlacesRestantes", nbPlacesRestantes);
        request.setAttribute("aAfficher", "gestionTrajet");
        request.getRequestDispatcher("/WEB-INF/homePage/homePage.jsp").forward(request, response);
    }
}
