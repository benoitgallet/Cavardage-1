package entities;

import javax.persistence.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

@SuppressWarnings({"unused", "RedundantIfStatement"})

@Entity
public class Trajet {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idTrajet;
    @Temporal(TemporalType.DATE)
    private Date date;
    @Temporal(TemporalType.TIME)
    private Date heure;
    // aVenir, fini, annule
    private String statut;
    private int prix;

    @ManyToOne
    private Ville villeDepart;
    @ManyToOne
    private Ville villeArrivee;

    @OneToMany(mappedBy = "trajetReservation")
    private List<Reservation> listeReservation;

    @ManyToOne
    private Vehicule vehiculeTrajet;

    @OneToMany(mappedBy = "trajet")
    private List<Etape> listeEtape;

    public Trajet() {
    }

    public Trajet(Date date, Date heure) {
        this.date = date;
        this.heure = heure;
    }

    public int getIdTrajet() {
        return idTrajet;
    }

    public void setIdTrajet(int idTrajet) {
        this.idTrajet = idTrajet;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Date getHeure() {
        return heure;
    }

    public void setHeure(Date heure) {
        this.heure = heure;
    }

    public Ville getVilleDepart() {
        return villeDepart;
    }

    public void setVilleDepart(Ville villeDepart) {
        this.villeDepart = villeDepart;
    }

    public Ville getVilleArrivee() {
        return villeArrivee;
    }

    public void setVilleArrivee(Ville villeArrivee) {
        this.villeArrivee = villeArrivee;
    }

    public List<Reservation> getListeReservation() {
        return listeReservation;
    }

    public void setListeReservation(List<Reservation> listeReservation) {
        this.listeReservation = listeReservation;
    }

    public Vehicule getVehiculeTrajet() {
        return vehiculeTrajet;
    }

    public void setVehiculeTrajet(Vehicule vehiculeTrajet) {
        this.vehiculeTrajet = vehiculeTrajet;
    }

    public List<Etape> getListeEtape() {
        return listeEtape;
    }

    public void setListeEtape(List<Etape> listeEtape) {
        this.listeEtape = listeEtape;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public int getPrix() {
        return prix;
    }

    public void setPrix(int prix) {
        this.prix = prix;
    }

    public String getStringDate(){
        SimpleDateFormat formatDate = new SimpleDateFormat("dd/MM/yyyy");
        return formatDate.format(getDate()).toString();
    }

    public String getStringHeure(){
        SimpleDateFormat formatDate = new SimpleDateFormat("HH:mm");
        return formatDate.format(getHeure()).toString();
    }

    public boolean supprimerReservation(Reservation reservation){
        if(getListeReservation().contains(reservation)){
           listeReservation.remove(reservation);
           return true;
        }
        return  false;
    }

    public boolean supprimerEtape(Etape etape) {
        if(listeEtape.contains(etape)){
            listeEtape.remove(etape);
            return true;
        }
        return false;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        if(((Trajet)obj).getIdTrajet() == (this.idTrajet)){
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "Trajet{" +
                "idTrajet=" + idTrajet +
                ", date='" + date + '\'' +
                ", statut='" + statut + '\'' +
                ", prix=" + prix +
                ", villeDepart=" + villeDepart +
                ", villeArrivee=" + villeArrivee +
                ", listeReservation=" + listeReservation +
                ", vehiculeTrajet=" + vehiculeTrajet +
                ", listeEtape=" + listeEtape +
                '}';
    }

}
