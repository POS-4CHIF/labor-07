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

import javax.xml.transform.Result;
import java.sql.*;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
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
        try (Statement st = con.createStatement()) {
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
        if (k == null) {
            throw new IllegalArgumentException("Kunde may not be null");
        }
        if (k.getId() != -1) {
            throw new IllegalArgumentException("Kunde already has an ID");
        }

        try (PreparedStatement pst = con.prepareStatement("INSERT INTO Kunde(kunde_zuname, kunde_vorname) VALUES(?, ?) RETURNING kunde_id as id")) {
            pst.setString(1, k.getZuname());
            pst.setString(2, k.getVorname());
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                k.setId(rs.getInt(1));
                return true;
            }
            return false;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteKunde(Kunde k) throws KursDBException {
        if (k == null) {
            throw new IllegalArgumentException("Kunde may not be null");
        }

        try (PreparedStatement pstPivot = con.prepareStatement("DELETE FROM kurs_kunde WHERE kunde_id = ?");
             PreparedStatement pstKunde = con.prepareStatement("DELETE FROM kunde WHERE kunde_id = ?")) {
            pstPivot.setInt(1, k.getId());
            pstPivot.executeUpdate();
            pstKunde.setInt(1, k.getId());
            return pstKunde.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public List<Dozent> getDozenten() throws KursDBException {
        List<Dozent> dozenten = new ArrayList<>();
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT doz_id, doz_zuname, doz_vorname FROM dozent;");
            while (rs.next()) {
                dozenten.add(new Dozent(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
        return dozenten;
    }

    public Dozent getDozentById(int dozId) throws KursDBException {
        try (PreparedStatement pst = con.prepareStatement("SELECT doz_zuname, doz_vorname FROM dozent WHERE doz_id = ?;")) {
            pst.setInt(1, dozId);
            ResultSet rs = pst.executeQuery();
            if (rs.next()) {
                return new Dozent(dozId, rs.getString(1), rs.getString(2));
            }
            return null;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public List<Kurstyp> getKurstypen() throws KursDBException {
        List<Kurstyp> kurstypen = new ArrayList<>();
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT kurstyp_id, kurstyp_bezeichnung FROM kurstyp;");
            while (rs.next()) {
                kurstypen.add(new Kurstyp(rs.getString(1).charAt(0), rs.getString(2)));
            }
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
        return kurstypen;
    }

    @Override
    public List<Kurs> getKurse() throws KursDBException {
        List<Kurs> kurse = new ArrayList<>();
        try (Statement st = con.createStatement()) {
            ResultSet rs = st.executeQuery("SELECT kurs_id, kurs_typ, kurs_doz_id, kurs_bezeichnung, kurs_beginndatum FROM kurs;");
            while (rs.next()) {
                Dozent doz = getDozentById(rs.getInt(3));
                Kurstyp kurstyp = getKursTypById(rs.getString(2).charAt(0));
                Date beginndatum = rs.getDate(5);
                kurse.add(new Kurs(rs.getInt(1), kurstyp, doz, rs.getString(4), beginndatum));
            }
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
        return kurse;
    }

    public Kurstyp getKursTypById(char id) throws KursDBException {
        try (PreparedStatement pst = con.prepareStatement("SELECT kurstyp_bezeichnung FROM kurstyp WHERE kurstyp_id = ?;")) {
            pst.setString(1, String.valueOf(id));
            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                return new Kurstyp(id, rs.getString(1));
            }

            return null;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public boolean insertKurstyp(Kurstyp kt) throws KursDBException {
        if (kt == null) {
            throw new IllegalArgumentException("Kurstyp may not be null");
        }

        try (PreparedStatement pst = con.prepareStatement("INSERT INTO kurstyp(kurstyp_id, kurstyp_bezeichnung) VALUES(?, ?)")) {
            pst.setString(1, String.valueOf(kt.getId()));
            pst.setString(2, kt.getBezeichnung());
            return pst.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public boolean deleteKurstyp(Kurstyp kt) throws KursDBException {
        if (kt == null) {
            throw new IllegalArgumentException("Kurstyp may not be null");
        }

        try (PreparedStatement pivotPst = con.prepareStatement(
                "DELETE FROM kurs_kunde WHERE kurs_id IN (SELECT kurs_id FROM kurs WHERE kurs_typ = ?);");
             PreparedStatement kursPst = con.prepareStatement("DELETE FROM kurs WHERE kurs_typ = ?;");
             PreparedStatement kurstypPst = con.prepareStatement("DELETE FROM kurstyp WHERE kurstyp_id = ?;")) {

            pivotPst.setString(1, String.valueOf(kt.getId()));
            kursPst.setString(1, String.valueOf(kt.getId()));
            kurstypPst.setString(1, String.valueOf(kt.getId()));

            pivotPst.executeUpdate();
            kursPst.executeUpdate();

            return kurstypPst.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public boolean insertKurs(Kurs kurs) throws KursDBException {
        if (kurs == null) {
            throw new IllegalArgumentException("Kurs may not be null");
        }
        if (kurs.getId() != -1) {
            throw new IllegalArgumentException("Kurs already has an ID");
        }

        try (PreparedStatement pst = con.prepareStatement("INSERT INTO kurs(kurs_typ, kurs_doz_id, kurs_bezeichnung, kurs_beginndatum) VALUES(?,?,?,?) RETURNING kurs_id as id")) {
            if (kurs.getKurstyp() != null) {
                pst.setString(1, String.valueOf(kurs.getKurstyp().getId()));
            }
            if (kurs.getDoz() != null) {
                pst.setInt(2, kurs.getDoz().getId());
            }
            pst.setString(3, kurs.getBezeichnung());
            pst.setDate(4, new java.sql.Date(kurs.getBeginndatum().getTime()));

            ResultSet rs = pst.executeQuery();

            if (rs.next()) {
                kurs.setId(rs.getInt(1));
                return true;
            }
            return false;

        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public List<Kunde> getKundenFromKurs(Kurs kurs) throws KursDBException {
        if (kurs == null) {
            throw new IllegalArgumentException("Kurs may not be null");
        }

        List<Kunde> kunden = new ArrayList<>();
        try (PreparedStatement pst = con.prepareStatement("SELECT kunde.kunde_id, kunde.kunde_zuname, kunde.kunde_vorname\n"
                + "FROM kurs_kunde INNER JOIN kunde ON kurs_kunde.kunde_id = kunde.kunde_id\n"
                + "WHERE kurs_kunde.kurs_id = ?;")) {
            pst.setInt(1, kurs.getId());
            ResultSet rs = pst.executeQuery();
            while (rs.next()) {
                kunden.add(new Kunde(rs.getInt(1), rs.getString(2), rs.getString(3)));
            }
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
        return kunden;
    }

    @Override
    public boolean bucheKurs(Kunde kunde, Kurs kurs) throws KursDBException {
        if (kunde == null || kurs == null) {
            throw new IllegalArgumentException("Kunde and Kurs may not be null");
        }

        try (PreparedStatement pst = con.prepareStatement("INSERT INTO kurs_kunde(kunde_id, kurs_id) VALUES(?, ?);")) {
            pst.setInt(1, kunde.getId());
            pst.setInt(2, kurs.getId());
            return pst.executeUpdate() == 1;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public boolean storniereKurs(Kunde kunde, Kurs kurs) throws KursDBException {
        if (kunde == null || kurs == null) {
            throw new IllegalArgumentException("Kunde and Kurs may not be null");
        }

        try (PreparedStatement pst = con.prepareStatement("DELETE FROM kurs_kunde WHERE kunde_id = ? AND kurs_id = ?;")) {
            pst.setInt(1, kunde.getId());
            pst.setInt(2, kurs.getId());
            return pst.executeUpdate() > 0;
        } catch (SQLException ex) {
            throw new KursDBException(ex.getMessage());
        }
    }

    @Override
    public Connection getCon() {
        return con;
    }
}
