package com.example.jpa;

import com.example.jpa.entity.Salle;
import com.example.jpa.entity.Utilisateur;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("tp2PU-SE");
        EntityManager em = emf.createEntityManager();

        try {
            // 1. Créer une salle de capacité 2
            em.getTransaction().begin();
            Salle salle = new Salle("Salle A", 2);
            em.persist(salle);
            em.getTransaction().commit();
            System.out.println("Salle créée : " + salle.getNom() + " (id=" + salle.getId() + ")");

            // 2. Inscrire trois utilisateurs (le 3e doit échouer)
            inscrire(em, "Alice", "alice@example.com", salle.getId());
            inscrire(em, "Bob", "bob@example.com", salle.getId());
            inscrire(em, "Charlie", "charlie@example.com", salle.getId());

            // 3. Tester l'unicité de l'e-mail
            inscrire(em, "Alice2", "alice@example.com", salle.getId());

            // 4. Lister les utilisateurs de la salle
            List<Utilisateur> users = em.createQuery(
                            "SELECT u FROM Utilisateur u WHERE u.salle.id = :id", Utilisateur.class)
                    .setParameter("id", salle.getId())
                    .getResultList();

            System.out.println("\nUtilisateurs de " + salle.getNom() + " :");
            for (Utilisateur u : users) {
                System.out.println(" - " + u.getNom() + " <" + u.getEmail() + ">");
            }
        } finally {
            em.close();
            emf.close();
        }
    }

    /** Équivalent de ServiceUtilisateur.inscrireUtilisateur, avec transaction manuelle. */
    private static void inscrire(EntityManager em, String nom, String email, Long salleId) {
        try {
            em.getTransaction().begin();

            Long nb = em.createQuery(
                            "SELECT COUNT(u) FROM Utilisateur u WHERE u.email = :mail", Long.class)
                    .setParameter("mail", email)
                    .getSingleResult();
            if (nb > 0) {
                throw new IllegalArgumentException("Un utilisateur avec cet e-mail existe déjà.");
            }

            Salle salle = em.find(Salle.class, salleId);
            if (salle == null) {
                throw new IllegalArgumentException("Salle introuvable.");
            }
            if (salle.getUtilisateurs().size() >= salle.getCapacite()) {
                throw new IllegalArgumentException("La salle est déjà pleine.");
            }

            Utilisateur u = new Utilisateur(nom, email, salle);
            salle.addUtilisateur(u);
            em.persist(u);

            em.getTransaction().commit();
            System.out.println("Inscrit : " + nom);
        } catch (IllegalArgumentException e) {
            if (em.getTransaction().isActive()) {
                em.getTransaction().rollback();
            }
            System.out.println("Échec pour " + nom + " : " + e.getMessage());
        }
    }
}