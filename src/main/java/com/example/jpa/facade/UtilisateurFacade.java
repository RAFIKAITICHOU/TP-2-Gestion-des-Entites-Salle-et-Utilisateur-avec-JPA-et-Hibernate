package com.example.jpa.facade;

import com.example.jpa.entity.Utilisateur;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class UtilisateurFacade extends AbstractFacade<Utilisateur> {

    public UtilisateurFacade() {
        super(Utilisateur.class);
    }

    public Utilisateur findByEmail(String email) {
        List<Utilisateur> result = em.createQuery(
                        "SELECT u FROM Utilisateur u WHERE u.email = :mail", Utilisateur.class)
                .setParameter("mail", email)
                .getResultList();
        return result.isEmpty() ? null : result.get(0);
    }
}