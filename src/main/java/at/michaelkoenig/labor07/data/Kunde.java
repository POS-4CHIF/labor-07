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
public class Kunde {

    private Integer id;
    private String zuname;
    private String vorname;

    public Kunde() {
    }

    public Kunde(Integer id, String zuname, String vorname) {
        this.id = id;
        this.zuname = zuname;
        this.vorname = vorname;
    }

    public int getId() {
        return id != null ? id : -1;
    }

    public final void setId(Integer id) {
        if (this.id == null) {
            this.id = id;
        } else {
            throw new IllegalArgumentException("ID existiert bereits");
        }
    }

    public String getZuname() {
        return zuname;
    }

    public void setZuname(String zuname) {
        this.zuname = zuname;
    }

    public String getVorname() {
        return vorname;
    }

    public void setVorname(String vorname) {
        this.vorname = vorname;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 67 * hash + Objects.hashCode(this.id);
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
        final Kunde other = (Kunde) obj;
        if (!Objects.equals(this.id, other.id)) {
            return false;
        }
        return true;
    }

}
