package com.example.jpa.entity;

import javax.persistence.*;

@Entity
@Table(name = "utilisateur",
        uniqueConstraints = {@UniqueConstraint(columnNames = {"email"})})
public class Utilisateur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 80)
    private String nom;

    @Column(nullable = false, length = 120)
    private String email;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "salle_id", nullable = false)
    private Salle salle;

    // ---------- Constructeurs ----------
    public Utilisateur() {}

    public Utilisateur(String nom, String email, Salle salle) {
        this.nom = nom;
        this.email = email;
        this.salle = salle;
    }

    // ---------- Getters / Setters ----------
    public Long getId() { return id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Salle getSalle() { return salle; }
    public void setSalle(Salle salle) { this.salle = salle; }
}