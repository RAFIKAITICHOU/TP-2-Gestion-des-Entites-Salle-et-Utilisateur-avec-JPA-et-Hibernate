package com.example.jpa.test;

import com.example.jpa.entity.Salle;
import com.example.jpa.facade.SalleFacade;
import com.example.jpa.service.ServiceUtilisateur;
import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.WebArchive;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.ejb.EJB;

@RunWith(Arquillian.class)
public class ServiceUtilisateurIT {

    @Deployment
    public static WebArchive createDeployment() {
        return ShrinkWrap.create(WebArchive.class)
                .addPackage("com.example.jpa.entity")
                .addPackage("com.example.jpa.facade")
                .addPackage("com.example.jpa.service")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private ServiceUtilisateur service;

    @EJB
    private SalleFacade salleFacade;

    @Test
    public void testInscriptionUtilisateur() throws Exception {
        // Crée une salle de capacité 2
        Salle s = new Salle("Salle A", 2);
        salleFacade.create(s);

        // Inscrit deux utilisateurs
        service.inscrireUtilisateur("Alice", "alice@example.com", s.getId());
        service.inscrireUtilisateur("Bob", "bob@example.com", s.getId());

        // Le troisième doit échouer
        try {
            service.inscrireUtilisateur("Charlie", "charlie@example.com", s.getId());
            Assert.fail("Une exception aurait dû être levée lorsque la capacité est dépassée.");
        } catch (IllegalArgumentException e) {
            Assert.assertEquals("La salle est déjà pleine.", e.getMessage());
        }
    }
}