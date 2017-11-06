package ejbs;

import dtos.StatistiquesDTO;
import dtos.VilleDTO;
import entities.Ville;
import exceptions.VilleNonTrouvee;

import javax.ejb.Local;
import java.util.List;

@Local
public interface MaFacadeAdministrateur {

    boolean ajouterVille(String nomVille,String departement);

    boolean supprimerVille(String nomVille, String departement) throws VilleNonTrouvee;

    boolean ajouterGabarit(String nomGabarit);

    boolean supprimerGabarit(String gabaritASupprimer, String gabaritARemplacer);

    List<Ville> getListeVilles();

    List<String> getListeGabarits();

    List<VilleDTO> getListeVilleDTO();

    StatistiquesDTO recupererStatistiques();
}
