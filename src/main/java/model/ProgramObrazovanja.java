package model;

import jakarta.persistence.*;
import java.util.List;

@Entity
@Table(name = "ProgramObrazovanja")
public class ProgramObrazovanja {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ProgramObrazovanjaId")
    private int id;

    @Column(name = "Naziv")
    private String naziv;

    @Column(name = "CSVET")
    private int csvetBodovi;

    @OneToMany(mappedBy = "programObrazovanja", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Upis> upisi;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public int getCsvetBodovi() {
        return csvetBodovi;
    }

    public void setCsvetBodovi(int csvetBodovi) {
        this.csvetBodovi = csvetBodovi;
    }

    public List<Upis> getUpisi() {
        return upisi;
    }

    public void setUpisi(List<Upis> upisi) {
        this.upisi = upisi;
    }
}
