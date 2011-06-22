/*
 * (#) net.brainage.rfc.util.svn.SvnClientTest.java
 * Created on 2011. 6. 22.
 */
package net.brainage.rfc.util.svn;

import static org.junit.Assert.*;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.tmatesoft.svn.core.SVNException;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class SvnClientTest
{

    /**
     * 
     */
    private SvnClient svnClient;

    /**
     * @throws java.lang.Exception
     */
    @Before
    public void setUp() throws Exception {
        svnClient = SvnClientImpl.getClient();
    }

    /**
     * @throws java.lang.Exception
     */
    @After
    public void tearDown() throws Exception {
    }

    /**
     * Test method for {@link net.brainage.rfc.util.svn.SvnClientImpl#diffStatus(java.lang.String, long, java.lang.String, net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler)}.
     */
    @Test
    public void testDiffStatus() {
        String urlPath1 = "svn://121.253.45.167/pcw-smg/trunk/pcw-smg-ear/pcw-smg-svc/src/java/com/sec/pcw/smg/service/PushServiceResourceImpl.java";
        long revision1 = 522;

        String urlPath2 = "svn://121.253.45.167/pcw-smg/branches/pcw-smg-ear/pcw-smg-svc/src/java/com/sec/pcw/smg/service/PushServiceResourceImpl.java";
        
        try {
            char actual = svnClient.diffStatus(urlPath1, revision1, urlPath2, null);
            assertEquals('M', actual);
        } catch ( SVNException e ) {
        }
        
    }

}
