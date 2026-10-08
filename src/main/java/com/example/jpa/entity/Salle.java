package com.example.jpa.entity;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "salle")
public class Salle {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 100)
    private String nom;

    @Column(nullable = false)
    private Integer capacite;

    @OneToMany(mappedBy = "salle", cascade = CascadeType.ALL,
            orphanRemoval = true, fetch = FetchType.LAZY)
    private Set<Utilisateur> utilisateurs = new HashSet<>();

    // ---------- Constructeurs ----------
    public Salle() {}

    public Salle(String nom, Integer capacite) {
        this.nom = nom;
        this.capacite = capacite;
    }

    // ---------- Getters / Setters ----------
    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Integer getCapacite() { return capacite; }
    public void setCapacite(Integer capacite) { this.capacite = capacite; }

    public Set<Utilisateur> getUtilisateurs() { return utilisateurs; }

    public void addUtilisateur(Utilisateur u) {
        utilisateurs.add(u);
        u.setSalle(this);
    }

    public void removeUtilisateur(Utilisateur u) {
        utilisateurs.remove(u);
        u.setSalle(null);
    }
}