package at.michaelkoenig.labor07.db;

import at.michaelkoenig.labor07.data.KursDBException;
import at.michaelkoenig.labor07.data.Kunde;
import at.michaelkoenig.labor07.data.Kurs;
import at.michaelkoenig.labor07.data.Kurstyp;
import at.michaelkoenig.labor07.data.Dozent;
import java.util.List;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
public interface IKurseDAO {

    /**
     * Verbindet das DAO-Objekt mit der DB
     */
    void connect(String dbUrl, String dbUser, String dbPwd) throws KursDBException;

    /**
     * Löst die Verbindung mit der Datenbank
     *
     * @throws KursDBException
     */
    void disconnect() throws KursDBException;

    /**
     * Prüft, ob eine Verbindung besteht
     *
     * @return true bei bestehender Verbindung, false sonst
     */
    boolean isConnected();

    /**
     * Liefert alle Kunden aus der DB als typsichere Liste aufsteigend nach
     * ihren IDs sortiert
     *
     * @return Liste aller Kunden, leere Liste wenn keine Kunden existieren
     * @throws KursDBException bei einem DB Fehler
     */
    List<Kunde> getKunden() throws KursDBException;

    /**
     * Speichert einen neuen Kunden in die DB. Wenn der übergebene Kunde bereits
     * eine ID ungleich null besitzt, wird eine IllegalArgumentException
     * geworfen
     *
     * @param k ein neuer Kunde
     * @return true bei Erfolg, false bei Misserfolg
     * @throws KursDBException bei einem DB Fehler
     * @throws IllegalArgumentException vgl. oben
     */
    boolean insertKunde(Kunde k) throws KursDBException;

    /**
     * Loescht einen Kunden aus der DB
     *
     * @param k zu loeschender Kunde
     * @return true bei Erfolg, false bei Misserfolg
     * @throws KursDBException bei einem DB Fehler
     */
    boolean deleteKunde(Kunde k) throws KursDBException;

    /**
     * Liefert alle Dozenten aus der DB als typsichere Liste aufsteigend nach
     * ihren IDs sortiert
     *
     * @return Liste aller Dozenten, leere Liste wenn keine Dozenten existieren
     * @throws KursDBException bei einem DB Fehler
     */
    List<Dozent> getDozenten() throws KursDBException;

    /**
     * Liefert alle Kurstypen aus der DB als typsichere Liste aufsteigend nach
     * ihren IDs sortiert
     *
     * @return Liste aller Kurstypen
     * @throws KursDBException bei einem DB Fehler
     */
    List<Kurstyp> getKurstypen() throws KursDBException;

    /**
     * Liefert alle Kurse aus der DB als typsichere Liste aufsteigend nach ihren
     * IDs sortiert
     *
     * @return Liste aller Kurse
     * @throws KursDBException bei einem DB Fehler
     */
    List<Kurs> getKurse() throws KursDBException;

    /**
     * Speichert einen neuen Kurstyp in die DB
     *
     * @param kt ein neuer Kurstyp
     * @return true bei Erfolg, false bei Misserfolg
     * @throws KursDBException bei einem DB Fehler
     */
    boolean insertKurstyp(Kurstyp kt) throws KursDBException;

    /**
     * Loescht einen Kurstyp aus der DB
     *
     * @param kt zu loeschender Kurstyp
     * @return true bei Erfolg, false bei Misserfolg
     * @throws KursDBException bei einem DB Fehler
     */
    boolean deleteKurstyp(Kurstyp kt) throws KursDBException;

    /**
     * Speichert einen neuen Kurs in die DB
     *
     * @param kurs ein neuer Kurs
     * @return true bei Erfolg, false bei Misserfolg
     * @throws KursDBException bei einem DB Fehler
     */
    boolean insertKurs(Kurs kurs) throws KursDBException;

    /**
     * Liefert alle Kunden, die einen bestimmeten Kurs gebucht haben
     *
     * @param kurs Kurs an den Kunden teilnehmen
     * @return Liste aller Kunden eines Kurses
     * @throws KursDBException bei einem DB Fehler
     */
    List<Kunde> getKundenFromKurs(Kurs kurs) throws KursDBException;

    /**
     * Bucht einen Kunden auf einen Kurs Ein Kunde kann nicht zwei mal auf einen
     * Kurs gebucht werden
     *
     * @param kunde Kunde, der gebucht werden soll
     * @param kurs, der gebucht werden soll
     * @return true bei Erfolg, false sonst
     * @throws KursDBException bei einem DB Fehler
     */
    boolean bucheKurs(Kunde kunde, Kurs kurs) throws KursDBException;

    /**
     * Storniert einen Kunden fuer einen Kurs
     *
     * @param kunde Kunde, der storniert werden soll
     * @param kurs Kurs aus dem storniert werden soll
     * @return true bei Erfolg, false sonst
     * @throws KursDBException bei einem DB Fehler
     */
    boolean storniereKurs(Kunde kunde, Kurs kurs) throws KursDBException;

}
