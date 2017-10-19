package entities;

import javax.persistence.*;
import java.util.List;

@Entity
public class Vehicule {

    @Id @GeneratedValue
    private int idVehicule;
    private String modele;
    private int nombrePlaces;

    @ManyToOne
    //@JoinColumn(name="idGabarit")
    private Gabarit gabarit;

    @OneToMany(mappedBy = "vehiculeTrajet")
    private List<Trajet> listeTrajet;

    public Vehicule() {
    }

    public Vehicule(String modele, int nombrePlaces) {
        this.modele = modele;
        this.nombrePlaces = nombrePlaces;
    }

    public int getIdVehicule() {
        return idVehicule;
    }

    public void setIdVehicule(int idVehicule) {
        this.idVehicule = idVehicule;
    }

    public String getModele() {
        return modele;
    }

    public void setModele(String modele) {
        this.modele = modele;
    }

    public int getNombrePlaces() {
        return nombrePlaces;
    }

    public void setNombrePlaces(int nombrePlaces) {
        this.nombrePlaces = nombrePlaces;
    }
}
