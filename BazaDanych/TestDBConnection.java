package BazaDanych; /**
 * Created by damia_000 on 07.06.2016.
 */
import BazaDanych.DBConnection;
import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;
import junit.framework.*;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;

public class TestDBConnection extends TestCase {
    DBConnection dbc = new DBConnection();
    private String Adres = "test";
    private String Pesel = "test";
    private String Surname = "test";
    private String Name = "test@test";

    /*TestDBConnection(DBConnection dbc)
    {
        this.dbc = dbc;
    }*/

    protected void setUp(){}
    public void testAdd(){}
    @Test
    public void testAddUser() throws SQLException {
        dbc.add(Name,Surname,Pesel,Adres);
        String result = dbc.getUserByPesel(Pesel).get(0);
        assertEquals((dbc.getLastID() + " " + Name + " " + Surname + " " + Pesel + " " + Adres + " " + 0), result);
    }
    @Test
    public void testSetCash()
    {
        dbc.setCashByPesel(9,Pesel);
        String result = dbc.getUserByPesel(Pesel).get(0);
        assertEquals(9, (Integer.parseInt(result.charAt(result.length() - 1)+"")));
    }
    @Test
    public void testRemoveUser()
    {
        dbc.removeByPesel(Pesel);
        ArrayList<String> result = dbc.getUserByPesel(Pesel);
        assertTrue(result.isEmpty());
    }
    @Test
    public void testGetCon()
    {
        assertNotNull(dbc.getConn());
    }

}
