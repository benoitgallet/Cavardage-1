package ejbs;

import dtos.StatistiquesDTO;
import dtos.VilleDTO;
import entities.*;
import exceptions.*;

import javax.annotation.security.DeclareRoles;
import javax.annotation.security.RolesAllowed;
import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.persistence.*;
import java.util.*;

@SuppressWarnings("unchecked")
@DeclareRoles({"admin","utilisateur"})
@Stateless(name = "AdministrateurBean")
public class MaFacadeAdministrateurBean implements MaFacadeAdministrateur {

    @PersistenceContext(unitName = "monUnite")
    private EntityManager em;
    @EJB
    private RechercheBean recherche;
    @EJB
    private Automate automate;

    @RolesAllowed("admin")
    @Override
    public boolean ajouterVille(String nomVille,String departement) throws VilleExistante {
        String ville = nomVille+"_"+departement;
        Ville v = em.find(Ville.class,ville);
        if(null != v){
            throw new VilleExistante("Cette ville existe déjà");
        }
        v = new Ville(nomVille,departement);
        em.persist(v);
        return true;
    }

    @RolesAllowed("admin")
    @Override
    public List<VilleDTO> getListeVilleDTO(){
        return recherche.getListeVillesDTO();
    }

    @RolesAllowed("admin")
    @Override
    public List<String> getListeGabarits(){
        Query q = em.createQuery("SELECT g FROM Gabarit g");
        List<Gabarit> listeTemp = q.getResultList();
        if(!listeTemp.isEmpty()){
            List<String> listeGabarits = new ArrayList<>();
            for(Gabarit g : listeTemp){
                listeGabarits.add(g.getType());
            }
            return listeGabarits;
        }
        return new ArrayList<>();
    }

    @RolesAllowed("admin")
    @Override
    public boolean supprimerVille(String nomVille, String departement) throws VilleNonTrouvee {
        String idVille = nomVille + "_" + departement;
        Ville ville = em.find(Ville.class, idVille);
        if(null == ville){
            throw new VilleNonTrouvee("La ville n'existe pas dans la base de données");
        }else{
            Query q = em.createQuery("SELECT t FROM Trajet t where t.villeDepart.nomVille=:villeSupp or t.villeArrivee.nomVille=:villeSupp");
            q.setParameter("villeSupp", idVille);
            List<Trajet> listeTrajets = q.getResultList();
            for(Trajet t : listeTrajets){
                // On récupère le véhicule du trajet, et on supprime le trajet de ce dernier
                Vehicule v = t.getVehiculeTrajet();
                automate.creerNotification(v.getUtilisateur().getLogin(), "Votre trajet au départ de " + t.getVilleDepart().getNomVille()
                        + " le " + t.getDate() + " et arrivant à " + t.getVilleArrivee().getNomVille() + " a été annulée par l'administrateur "
                        + "du site car cette ville n'est plus desservie actuellement.");
                v.getListeTrajet().remove(t);

                List<Reservation> listeRes = t.getListeReservation();
                for(Reservation r : listeRes){
                    supprimerReservation(r, t);
                }

                List<Etape> listeEtapes = t.getListeEtape();
                for(Etape e : listeEtapes){
                    em.remove(e);
                }

                em.remove(t);
            }

            q = em.createQuery("SELECT e FROM Etape e where e.villeEtape.nomVille=:villeSupp");
            q.setParameter("villeSupp", idVille);
            List<Etape> listeEtapes = q.getResultList();
            for(Etape e : listeEtapes){
                Trajet t = e.getTrajet();
                t.getListeEtape().remove(e);

                List<Reservation> listeRes = t.getListeReservation();
                for(Reservation r : listeRes){
                    if(null != r.getDescendA() && r.getDescendA().getVilleEtape().getNomVille().equals(idVille)){
                        supprimerReservation(r, t);
                    }
                }

                em.remove(e);
            }

            em.remove(ville);
            return true;
        }
    }

    /**
     * Supprime une réservation
     * @param r     La réservation à supprimer
     * @param t     Le trajet associé à la réservation
     */
    @RolesAllowed("admin")
    private void supprimerReservation(Reservation r, Trajet t){
        Utilisateur u = r.getUtilisateurReservation();
        u.getListeReservation().remove(r);

        List<Appreciation> listeAppreciation = u.getNote();
        for(Appreciation a : listeAppreciation){
            if(a.getNoteTrajet().getIdTrajet() == t.getIdTrajet()){
                u.getNote().remove(a);
                em.remove(a);
            }
        }

        listeAppreciation = u.getEstNote();
        for(Appreciation a : listeAppreciation){
            if(a.getNoteTrajet().getIdTrajet() == t.getIdTrajet()){
                u.getEstNote().remove(a);
                em.remove(a);
            }
        }

        automate.creerNotification(u.getLogin(), "Votre réservation au départ de " + t.getVilleDepart().getNomVille()
                + " le " + t.getDate() + " et arrivant à " + t.getVilleArrivee().getNomVille() + " a été annulée par l'administrateur "
                + "du site car cette ville n'est plus desservie actuellement.");
        em.remove(r);
    }

    @RolesAllowed("admin")
    @Override
    public boolean ajouterGabarit(String nomGabarit) throws GabaritException {
        Query q = em.createQuery("SELECT g FROM Gabarit g WHERE g.type=:gabarit");
        q.setParameter("gabarit", nomGabarit);
        List<Gabarit> gabarits = q.getResultList();
        if(gabarits.size() == 0){
            Gabarit gabarit = new Gabarit(nomGabarit);
            em.persist(gabarit);
            return true;
        } else {
            throw new GabaritException("Ce gabarit existe déjà");
        }
    }

    @RolesAllowed("admin")
    @Override
    public void supprimerGabarit(String gabaritASupprimer, String gabaritARemplacer) throws GabaritException {
        Query qSupp = em.createQuery("SELECT g FROM Gabarit g WHERE g.type=:gabarit");
        qSupp.setParameter("gabarit", gabaritASupprimer);
        Query qRemp = em.createQuery("SELECT g FROM Gabarit g WHERE g.type=:gabarit");
        qRemp.setParameter("gabarit", gabaritARemplacer);
        try {
            Gabarit gSupp = (Gabarit) qSupp.getSingleResult();
            Gabarit gRemp = (Gabarit) qRemp.getSingleResult();
            Query vehiculesAChanger = em.createQuery("SELECT v FROM Vehicule v WHERE v.gabarit= :gabarit");
            vehiculesAChanger.setParameter("gabarit",gSupp);
            List<Vehicule> vehicules = vehiculesAChanger.getResultList();
            for (Vehicule vehicule : vehicules){
                vehicule.setGabarit(gRemp);
                automate.creerNotification(vehicule.getUtilisateur().getLogin(),"L'administrateur du site a supprimé " +
                        "le gabarit "+gSupp.getType()+" et l'a remplacé par "+gRemp.getType()+". Ce gabarit était celui " +
                        "de votre véhicule "+vehicule.getNom()+".");
            }
            em.remove(gSupp);
        }catch(NoResultException e){
            throw new GabaritException("Le gabarit que vous essayez de supprimer n'existe pas");
        }
    }

    @RolesAllowed("admin")
    @Override
    public StatistiquesDTO recupererStatistiques(){

        long startTime = System.nanoTime();

        Query q = em.createQuery("SELECT u FROM Utilisateur u WHERE u.roleUtilisateur.message = 'utilisateur'");
        List<Utilisateur> listeUtilisateur = q.getResultList();
        int nbUtilisateur = listeUtilisateur.size();

        int nbConducteurs = 0;
        for(Utilisateur u : listeUtilisateur){
            midLoop:
            for(Vehicule v : u.getListeVehicule()){
                for(Trajet t : v.getListeTrajet()){
                    if(t.getStatut().equals("fini")){
                        nbConducteurs++;
                        break midLoop;
                    }
                }
            }
        }

        q = em.createQuery("SELECT DISTINCT res.utilisateurReservation.login FROM Reservation res WHERE res.statut=:statutReservation");
        q.setParameter("statutReservation", "accepte");
        int nbPassagers = q.getResultList().size();

        q = em.createQuery("SELECT res FROM Reservation res where res.statut=:statutReservation");
        q.setParameter("statutReservation", "accepte");
        int nbTrajetsAcceptes = q.getResultList().size();

        q = em.createQuery("SELECT res.trajetReservation.prix FROM Reservation res where res.statut=:statutReservation and res.trajetReservation.statut=:statutTrajet");
        q.setParameter("statutReservation", "accepte");
        q.setParameter("statutTrajet", "fini");
        List<Integer> listePrix = q.getResultList();
        int prixTotal = 0;
        for(int n : listePrix){
            prixTotal += n;
        }

        q = em.createQuery("SELECT COUNT (t) FROM Trajet t WHERE t.statut = 'fini'");
        int nbTrajetsFinis = ((Long)q.getSingleResult()).intValue();

        q = em.createQuery("SELECT COUNT (v) FROM Ville v");
        int nbVilles = ((Long) q.getSingleResult()).intValue();

        q = em.createQuery("SELECT t.villeDepart.nomVille FROM Trajet t GROUP BY t.villeDepart ORDER BY COUNT (t.villeDepart) DESC");
        List<String> listeVilles = q.getResultList();
        String topVilleDepart;
        if(!listeVilles.isEmpty()) {
            topVilleDepart = listeVilles.get(0);
        }else{
            topVilleDepart = "/";
        }

        q = em.createQuery("SELECT t.villeArrivee.nomVille FROM Trajet t GROUP BY t.villeArrivee ORDER BY COUNT (t.villeArrivee) DESC");
        listeVilles = q.getResultList();
        String topVilleArrivee;
        if(!listeVilles.isEmpty()) {
            topVilleArrivee = listeVilles.get(0);
        }else{
            topVilleArrivee = "/";
        }

        long endTime = System.nanoTime();
        long duration = (endTime - startTime) / 1000000;

        return new StatistiquesDTO(nbUtilisateur, nbPassagers, nbConducteurs, nbTrajetsAcceptes,
                prixTotal, nbTrajetsFinis, nbVilles, topVilleDepart, topVilleArrivee, duration);
    }

    @RolesAllowed("admin")
    @Override
    public void creerCompteAdmin(String login, String nom, String mdp) throws LoginExistantException {
        Utilisateur temp = em.find(Utilisateur.class, login);
        if(null == temp){
            Query q = em.createQuery("SELECT r FROM Role r where r.message=:role");
            q.setParameter("role", "admin");
            Role r = (Role) q.getSingleResult();
            String mdpHash = automate.recupererHash(mdp);
            Utilisateur u = new Utilisateur(login, nom, mdpHash, r);
            em.persist(u);
        }else{
            throw new LoginExistantException("Ce login est déjà pris");
        }
    }
}
