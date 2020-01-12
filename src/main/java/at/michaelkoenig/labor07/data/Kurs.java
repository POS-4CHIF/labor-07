package at.michaelkoenig.labor07.data;

import at.michaelkoenig.labor07.data.Dozent;
import java.time.LocalDate;
import java.util.Objects;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author 20160451
 */
public class Kurs {

    private Integer id;
    private Dozent doz;
    private String bezeichnung;
    private LocalDate beginndatum;
    private Kurstyp kurstyp;

    public Kurs() {
    }

    public Kurs(Integer id, Dozent doz, String bezeichnung, LocalDate beginndatum, Kurstyp kurstyp) {
        this.id = id;
        this.doz = doz;
        this.bezeichnung = bezeichnung;
        this.beginndatum = beginndatum;
        this.kurstyp = kurstyp;
    }

    public int getId() {
        return id;
    }

    public final void setId(Integer id) {
        if (this.id == null) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID existiert bereits");
        }
    }

    public Dozent getDoz() {
        return doz;
    }

    public void setDoz(Dozent doz) {
        this.doz = doz;
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    public LocalDate getBeginndatum() {
        return beginndatum;
    }

    public void setBeginndatum(LocalDate beginndatum) {
        this.beginndatum = beginndatum;
    }

    public Kurstyp getKurstyp() {
        return kurstyp;
    }

    public void setKurstyp(Kurstyp kurstyp) {
        this.kurstyp = kurstyp;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 61 * hash + Objects.hashCode(this.id);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Kurs other = (Kurs) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
