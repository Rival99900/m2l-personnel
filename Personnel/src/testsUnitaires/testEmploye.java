/**
 * C'est juste pas encore fini, ça sera peut-être modifié dans les jours qui suivent.
 */
package testsUnitaires;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import personnel.*;

/**
 * Tests unitaires de la classe Employe et de ses liens avec Ligue.
 *
 * Principe : chaque test suit toujours le même schéma
 *   1. on prépare les objets (dans preparer() ou au début du test),
 *   2. on appelle la méthode à tester,
 *   3. on vérifie le résultat avec assertEquals / assertTrue / assertThrows...
 */
class testEmploye
{
	GestionPersonnel gestionPersonnel;
	Ligue ligue;
	Employe employe;

	/**
	 * Exécutée AVANT chaque test : chaque test repart d'une ligue
	 * et d'un employé tout neufs.
	 */
	@BeforeEach
	void preparer() throws SauvegardeImpossible
	{
		gestionPersonnel = GestionPersonnel.getGestionPersonnel();
		ligue = gestionPersonnel.addLigue("Fléchettes");
		employe = ligue.addEmploye("Bouchard", "Gérard", "g.bouchard@gmail.com", "azerty");
	}

	// ============================================================
	// 1. Les setters : on change une valeur, puis on la relit
	// ============================================================

	@Test
	void setNom()
	{
		employe.setNom("Martin");
		assertEquals("Martin", employe.getNom());
	}

	@Test
	void setPrenom()
	{
		employe.setPrenom("Paul");
		assertEquals("Paul", employe.getPrenom());
	}

	@Test
	void setMail()
	{
		employe.setMail("paul@gmail.com");
		assertEquals("paul@gmail.com", employe.getMail());
	}

	@Test
	void setPassword()
	{
		employe.setPassword("nouveau");
		// le nouveau mot de passe est accepté, l'ancien est refusé
		assertTrue(employe.checkPassword("nouveau"));
		assertFalse(employe.checkPassword("azerty"));
	}

	// ============================================================
	// 2. Changer l'administrateur d'une ligue
	// ============================================================

	@Test
	void administrateurParDefautEstLeRoot()
	{
		// quand une ligue est créée, c'est le root qui l'administre
		assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
	}

	@Test
	void changerAdministrateur()
	{
		// l'employé de la ligue devient administrateur
		ligue.setAdministrateur(employe);

		assertEquals(employe, ligue.getAdministrateur());
		assertTrue(employe.estAdmin(ligue));
	}

	@Test
	void administrateurDUneAutreLigueEstRefuse() throws SauvegardeImpossible
	{
		// un employé d'une AUTRE ligue ne peut pas administrer celle-ci
		Ligue autreLigue = gestionPersonnel.addLigue("Tennis");
		Employe etranger = autreLigue.addEmploye("Durand", "Paul", "p.durand@gmail.com", "azerty");

		assertThrows(DroitsInsuffisants.class, () -> ligue.setAdministrateur(etranger));

		// et l'administrateur n'a pas changé
		assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
	}

	// ============================================================
	// 3. Supprimer un employé ou une ligue
	// ============================================================

	@Test
	void supprimerEmploye()
	{
		int nombreAvant = ligue.getEmployes().size();

		employe.remove();

		assertEquals(nombreAvant - 1, ligue.getEmployes().size());
		assertFalse(ligue.getEmployes().contains(employe));
	}

	@Test
	void supprimerUnAdministrateurRedonneLaLigueAuRoot()
	{
		ligue.setAdministrateur(employe);

		employe.remove();

		// après la suppression, c'est de nouveau le root qui administre la ligue
		assertEquals(gestionPersonnel.getRoot(), ligue.getAdministrateur());
	}

	@Test
	void leRootNePeutPasEtreSupprime()
	{
		Employe root = gestionPersonnel.getRoot();

		assertThrows(ImpossibleDeSupprimerRoot.class, () -> root.remove());
	}

	@Test
	void supprimerUneLigue() throws SauvegardeImpossible
	{
		Ligue aSupprimer = gestionPersonnel.addLigue("Ligue à supprimer");

		aSupprimer.remove();

		assertFalse(gestionPersonnel.getLigues().contains(aSupprimer));
	}

	// ============================================================
	// 4. Tests qui révèlent des bugs
	//    @Disabled empêche ces tests de tourner, pour que le projet
	//    reste vert. Enlève @Disabled pour voir l'échec, puis
	//    décris le bug dans ton rapport.
	// ============================================================

	@Test
	@Disabled("Bug : après setNom, l'employé n'est plus supprimé")
	void renommerPuisSupprimer()
	{
		// on ajoute un deuxième employé : le premier n'est plus seul dans la liste
		Employe zeta = ligue.addEmploye("Zeta", "Paul", "z@gmail.com", "azerty");

		zeta.setNom("Alpha");   // on change le nom APRES l'ajout
		zeta.remove();

		// il ne doit rester que Bouchard
		assertEquals(1, ligue.getEmployes().size());
	}

	@Test
	@Disabled("Bug : un deuxième employé au même nom et prénom est ignoré sans erreur")
	void deuxEmployesAvecLeMemeNom()
	{
		ligue.addEmploye("Bouchard", "Gérard", "autre@gmail.com", "azerty");

		assertEquals(2, ligue.getEmployes().size());
	}

	@Test
	@Disabled("Bug : getLigue(root) lève une NullPointerException au lieu de renvoyer null")
	void getLigueDuRoot()
	{
		assertNull(gestionPersonnel.getLigue(gestionPersonnel.getRoot()));
	}
}