package testsUnitaires;

import java.time.LocalDate;
import java.util.Objects;

public class Testdate {

    private final int idEmploye;
    private String prenom;
    private String nom;
    private String email;
    private LocalDate dateArrivee;
    private LocalDate dateDepart;

    public Testdate(int idEmploye, String prenom, String nom, String email,
                    LocalDate dateArrivee, LocalDate dateDepart) {
        this.idEmploye = idEmploye;
        this.prenom = Objects.requireNonNull(prenom, "Le prénom est obligatoire");
        this.nom = Objects.requireNonNull(nom, "Le nom est obligatoire");
        this.email = Objects.requireNonNull(email, "L'email est obligatoire");
        setDates(dateArrivee, dateDepart);
    }

    public int getIdEmploye() { return idEmploye; }
    public String getPrenom() { return prenom; }
    public String getNom() { return nom; }
    public String getEmail() { return email; }
    public LocalDate getDateArrivee() { return dateArrivee; }
    public LocalDate getDateDepart() { return dateDepart; }

    public void setDateArrivee(LocalDate dateArrivee) {
        setDates(dateArrivee, this.dateDepart);
    }

    public void setDateDepart(LocalDate dateDepart) {
        setDates(this.dateArrivee, dateDepart);
    }

    public void setDates(LocalDate arrivee, LocalDate depart) {
        if (arrivee == null) {
            throw new IllegalArgumentException("La date d'arrivée est obligatoire");
        }
        if (depart != null && depart.isBefore(arrivee)) {
            throw new IllegalArgumentException(
                "La date de départ ne peut pas être antérieure à la date d'arrivée");
        }
        this.dateArrivee = arrivee;
        this.dateDepart = depart;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Testdate)) {
            return false;
        }
        Testdate other = (Testdate) o;
        return idEmploye == other.idEmploye;
    }

    @Override
    public int hashCode() {
        return Integer.hashCode(idEmploye);
    }
}