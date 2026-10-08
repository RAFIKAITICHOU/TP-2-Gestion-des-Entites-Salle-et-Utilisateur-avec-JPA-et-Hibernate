package com.example.jpa.service;

import com.example.jpa.entity.Salle;

import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

@Stateless
@TransactionAttribute(TransactionAttributeType.REQUIRED)
public class ExempleService {

    @PersistenceContext(unitName = "tp2PU")
    private EntityManager em;

    public void creerSalle(Salle s) {
        em.persist(s);   // La transaction est gérée par le conteneur
    }

    @TransactionAttribute(TransactionAttributeType.SUPPORTS)
    public Salle trouverSalle(Long id) {
        return em.find(Salle.class, id);   // Lecture sans transaction explicite
    }
}