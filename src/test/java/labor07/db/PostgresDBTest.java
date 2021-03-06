package labor07.db;

import at.michaelkoenig.labor07.data.*;
import at.michaelkoenig.labor07.db.IKurseDAO;
import at.michaelkoenig.labor07.db.KurseDAO;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import org.junit.After;
import org.junit.AfterClass;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

/**
 * @author reio
 */
public class PostgresDBTest {

    private static IKurseDAO dao = new KurseDAO();

    public PostgresDBTest() {
    }

    @BeforeClass
    public static void setUpClass() throws KursDBException {
        dao.connect("jdbc:postgresql://10.128.6.5:5432/4C_10_lab7", "unterricht", "unterricht");
    }

    @AfterClass
    public static void tearDownClass() throws KursDBException {
        dao.disconnect();
    }

    @Before
    public void setUp() throws KursDBException, SQLException, IOException {
        Statement st = dao.getCon().createStatement();
        st.execute(new String(Files.readAllBytes(Paths.get("src/test/sql/create.sql"))));
    }

    @After
    public void tearDown() throws KursDBException {

    }

    /**
     * Test of getKunden method, of class PostgresDB.
     */
    @Test
    public void testGetKunden() throws KursDBException {
        System.out.println("getKunden");
        List<Kunde> result = dao.getKunden();
        assertEquals(6, result.size());

    }

    /**
     * Test of insertKunde method, of class PostgresDB.
     */
    @Test
    public void testInsertKunde() throws KursDBException {
        System.out.println("insertKunde");
        Kunde k = new Kunde();
        k.setVorname("Mustermann");
        k.setZuname("Max");
        boolean result = dao.insertKunde(k);
        assertTrue(result);
        assertTrue(k.getId() > 0);
        dao.deleteKunde(k);
    }

    @Test(expected = java.lang.IllegalArgumentException.class)
    public void testInsertKunde1() throws KursDBException {
        System.out.println("insertKunde1");
        // Kunde mit gespeicherter id (PK) kann nicht eingefügt werden
        Kunde k = new Kunde(17, "Werner", "Franz");
        dao.insertKunde(k);
    }

    /**
     * Test of deleteKunde method, of class PostgresDB.
     */
    @Test
    public void testDeleteKunde() throws KursDBException {
        System.out.println("deleteKunde");
        // Liste aller Kunden holen
        List<Kunde> kunden = dao.getKunden();
        int anzahl = kunden.size();
        // Letzen Kunden holen
        Kunde k = kunden.get(anzahl - 1);
        // und löschen
        boolean result = dao.deleteKunde(k);
        assertTrue(result);
        kunden = dao.getKunden();
        assertEquals(anzahl - 1, kunden.size());
    }

    /**
     * Test of getDozenten method, of class PostgresDB.
     */
    @Test
    public void testGetDozenten() throws KursDBException {
        System.out.println("getDozenten");
        List<Dozent> result = dao.getDozenten();
        assertEquals(7, result.size());
    }

    /**
     * Test of getKurstypen method, of class PostgresDB.
     */
    @Test
    public void testGetKurstypen() throws KursDBException {
        System.out.println("getKurstypen");

        List<Kurstyp> result = dao.getKurstypen();
        assertEquals(3, result.size());
    }

    /**
     * Test of getKurs method, of class PostgresDB.
     */
    @Test
    public void testGetKurs() throws KursDBException {
        System.out.println("getKurs");

        List<Kurs> result = dao.getKurse();
        assertEquals(6, result.size());
    }

    /**
     * Test of insertKurstyp method, of class PostgresDB.
     */
    @Test
    public void testInsertKurstyp() throws KursDBException {
        System.out.println("insertKurstyp");

        Kurstyp k1 = new Kurstyp('M', "Mediendesign");
        dao.insertKurstyp(k1);
        assertTrue(k1.getId() > 0);
    }

    /**
     * Test of deleteKurstyp method, of class PostgresDB.
     */
    @Test
    public void testDeleteKurstyp() throws KursDBException {
        System.out.println("deleteKurstyp");

        List<Kurstyp> kt = dao.getKurstypen();
        int anzahl = kt.size();

        Kurstyp lastKt = kt.get(anzahl - 1);
        // und löschen
        boolean result = dao.deleteKurstyp(lastKt);
        assertTrue(result);
        kt = dao.getKurstypen();
        assertEquals(anzahl - 1, kt.size());
    }

    /**
     * Test of saveKurs method, of class PostgresDB.
     */
    @Test
    public void testSaveKurs() throws KursDBException {
        System.out.println("saveKurs");

        int count = dao.getKurse().size();
        Dozent doz = new Dozent();
        doz.setId(3);
        Kurstyp kt = new Kurstyp();
        kt.setId('P');

        Kurs k = new Kurs(null, kt, doz, "Rust", Date.valueOf("2020-04-27"));
        dao.insertKurs(k);

        assertEquals(count + 1, dao.getKurse().size());
        assertTrue(k.getId() > 0);
    }

    /**
     * Test of getKundenFromKurs method, of class PostgresDB.
     */
    @Test
    public void testGetKundenFromKurs() throws KursDBException {
        System.out.println("getKundenFromKurs");
        Kurs kurs = dao.getKurse().get(0);
        assertEquals(1, kurs.getId());
        List<Kunde> kunden = dao.getKundenFromKurs(kurs);
        assertEquals(3, kunden.size());
        assertEquals(3, kunden.get(0).getId());
        assertEquals(4, kunden.get(1).getId());
        assertEquals(6, kunden.get(2).getId());
    }

    /**
     * Test of bucheKurs method, of class PostgresDB.
     */
    @Test
    public void testBucheKurs() throws KursDBException {
        System.out.println("bucheKurs");

        Kunde kunde = new Kunde();
        kunde.setId(1);
        Kurs kurs = new Kurs();
        kurs.setId(1);

        dao.bucheKurs(kunde, kurs);
        assertTrue(dao.getKundenFromKurs(kurs).stream().anyMatch(k -> k.equals(kunde)));
    }

    /**
     * Test of storniereKurs method, of class PostgresDB.
     */
    @Test
    public void testStorniereKurs() throws KursDBException {
        System.out.println("storniereKurs");

        Kurs kurs = new Kurs();
        kurs.setId(1);

        int count = dao.getKundenFromKurs(kurs).size();
        dao.getKundenFromKurs(kurs).stream().forEach(k -> System.out.println("asdf"+k.getZuname()));

        Kunde kunde = new Kunde();
        kunde.setId(3);

        dao.storniereKurs(kunde, kurs);

        assertEquals(count-1, dao.getKundenFromKurs(kurs).size());
    }

}
