package at.michaelkoenig.labor07.data;

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
public class Kurstyp {

    private Character id;
    private String bezeichnung;

    public Kurstyp() {
    }

    public Kurstyp(char id, String bezeichnung) {
        this.id = id;
        this.bezeichnung = bezeichnung;
    }

    public Character getId() {
        return id;
    }

    public final void setId(Character id) {
        if (this.id == null) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID existiert bereits");
        }
    }

    public String getBezeichnung() {
        return bezeichnung;
    }

    public void setBezeichnung(String bezeichnung) {
        this.bezeichnung = bezeichnung;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 59 * hash + Objects.hashCode(this.id);
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
        final Kurstyp other = (Kurstyp) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
