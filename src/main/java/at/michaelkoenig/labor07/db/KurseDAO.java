/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package at.michaelkoenig.labor07.db;

import at.michaelkoenig.labor07.data.KursDBException;
import at.michaelkoenig.labor07.data.Kurs;
import at.michaelkoenig.labor07.data.Kurstyp;
import at.michaelkoenig.labor07.data.Kunde;
import at.michaelkoenig.labor07.data.Dozent;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author 20160451
 */
public class KurseDAO implements IKurseDAO {

    private Connection con;

    static {
        try {
            Class.forName("org.postgresql.Driver");
        } catch (ClassNotFoundException ex) {
            System.err.println("Load driver fail: " + ex.getMessage());
        }
    }

    @Override
    public void connect(String dbUrl, String dbUser, String dbPwd) throws KursDBException {
        try {
            con = DriverManager.getConnection(dbUrl, dbUser, dbPwd);
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public void disconnect() throws KursDBException {
        try {
            con.close();
            con = null;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }

    }

    @Override
    public boolean isConnected() {
        return con != null;
    }

    @Override
    public List<Kunde> getKunden() throws KursDBException {
        List<Kunde> kunden = new ArrayList<>();
        try ( Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT kunde_id, kunde_zuname, kunde_vorname FROM kunde;");
            while (rs.next()) {
                kunden.add(new Kunde(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
        return kunden;
    }

    @Override
    public boolean insertKunde(Kunde k) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteKunde(Kunde k) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Dozent> getDozenten() throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Kurstyp> getKurstypen() throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Kurs> getKurse() throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insertKurstyp(Kurstyp kt) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean deleteKurstyp(Kurstyp kt) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean insertKurs(Kurs kurs) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Kunde> getKundenFromKurs(Kurs kurs) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean bucheKurs(Kunde kunde, Kurs kurs) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean storniereKurs(Kunde kunde, Kurs kurs) throws KursDBException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
