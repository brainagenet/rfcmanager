/*
 * (#) net.brainage.rfc.config.ConfigurationTest.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.config;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class ConfigurationTest
{
    
    private Configuration config;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        config = Configuration.getInstance();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#getString(java.lang.String)}.
     */
    @Test
    public void testGetStringString() {
        String expected = "svn://";
        String actual = config.getString(Configuration.Key.SVN_PROTOCOL);
        assertEquals(expected, actual);
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#getString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testGetStringStringString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#getInt(java.lang.String)}.
     */
    @Test
    public void testGetIntString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#getInt(java.lang.String, int)}.
     */
    @Test
    public void testGetIntStringInt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#getLong(java.lang.String)}.
     */
    @Test
    public void testGetLongString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#getLong(java.lang.String, long)}.
     */
    @Test
    public void testGetLongStringLong() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#setString(java.lang.String, java.lang.String)}.
     */
    @Test
    public void testSetString() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#setInt(java.lang.String, int)}.
     */
    @Test
    public void testSetInt() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#setLong(java.lang.String, long)}.
     */
    @Test
    public void testSetLong() {
        fail("Not yet implemented");
    }

    /**
     * Test method for {@link net.brainage.rfc.config.Configuration#store()}.
     */
    @Test
    public void testStore() {
        config.setString(Configuration.Key.SVN_AUTH_PASSWORD, "1744");
        config.store();
        
        String expected = "1744";
        String actual = config.getString(Configuration.Key.SVN_AUTH_PASSWORD);
        assertEquals(expected, actual);
    }

}
