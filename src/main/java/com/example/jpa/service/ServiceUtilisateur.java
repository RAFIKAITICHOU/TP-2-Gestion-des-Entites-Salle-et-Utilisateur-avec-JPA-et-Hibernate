package com.example.jpa.service;

import com.example.jpa.entity.Salle;
import com.example.jpa.entity.Utilisateur;
import com.example.jpa.facade.SalleFacade;
import com.example.jpa.facade.UtilisateurFacade;

import javax.ejb.EJB;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@Stateless
public class ServiceUtilisateur {

    @EJB
    private SalleFacade salleFacade;

    @EJB
    private UtilisateurFacade utilisateurFacade;

    /**
     * Crée un utilisateur et l'associe à une salle si la capacité le permet.
     */
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public void inscrireUtilisateur(String nom, String email, Long salleId)
            throws IllegalArgumentException {

        // 1. Vérifier l'unicité de l'e-mail
        if (utilisateurFacade.findByEmail(email) != null) {
            throw new IllegalArgumentException("Un utilisateur avec cet e-mail existe déjà.");
        }

        // 2. Récupérer la salle
        Salle salle = salleFacade.find(salleId);
        if (salle == null) {
            throw new IllegalArgumentException("Salle introuvable.");
        }

        // 3. Vérifier la capacité
        if (salle.getUtilisateurs().size() >= salle.getCapacite()) {
            throw new IllegalArgumentException("La salle est déjà pleine.");
        }

        // 4. Créer et persister l'utilisateur
        Utilisateur u = new Utilisateur(nom, email, salle);
        utilisateurFacade.create(u);

        // 5. Synchroniser la collection côté salle
        salle.addUtilisateur(u);
    }

    /**
     * Recherche les utilisateurs d'une salle donnée.
     */
    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public List<Utilisateur> listerUtilisateursParSalle(Long salleId) {
        Salle salle = salleFacade.find(salleId);
        if (salle == null) {
            return Collections.emptyList();
        }
        return new ArrayList<>(salle.getUtilisateurs());
    }
}