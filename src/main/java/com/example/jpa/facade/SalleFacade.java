package com.example.jpa.facade;

import com.example.jpa.entity.Salle;
import javax.ejb.Stateless;
import java.util.List;

@Stateless
public class SalleFacade extends AbstractFacade<Salle> {

    public SalleFacade() {
        super(Salle.class);
    }

    // Méthode métier : recherche par capacité minimale
    public List<Salle> findByCapaciteGreaterThan(int minCapacite) {
        return em.createQuery(
                        "SELECT s FROM Salle s WHERE s.capacite > :min", Salle.class)
                .setParameter("min", minCapacite)
                .getResultList();
    }
}