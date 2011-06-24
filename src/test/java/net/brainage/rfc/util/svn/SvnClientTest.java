/*
 * (#) net.brainage.rfc.util.svn.SvnClientTest.java
 * Created on 2011. 6. 22.
 */
package net.brainage.rfc.util.svn;

import static org.junit.Assert.*;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler;
import net.brainage.rfc.util.svn.handler.DefaultSvnDiffStatusHandler;

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

    @Test
    public void testAdd() {
        String filePath = "C:/Users/exia/rfcmanager/workspaces/wc/pcw-cmm/pcw-test-ear/bbb/b.txt";
        List<File> commitEntries = new ArrayList<File>();
        try {
            svnClient.add(new File(filePath), commitEntries);
            System.out.println("SvnClientTest.testAdd() :: commit entries size = "
                    + commitEntries.size());
            assertTrue(commitEntries.size() == 2);
        } catch ( SVNException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testGetFile() {
        String repoUrlPath = "svn://121.253.45.167/pcw-smg/trunk/pcw-smg-ear";
        String urlPath = "pcw-smg-svc/src/java/com/sec/pcw/smg/service/PushServiceResourceImpl.java";
        long revision = 522;
        String targetPath = "./workspaces/tmp/pcw-smg/pcw-smg-ear/" + urlPath;
        try {
            svnClient.getFile(repoUrlPath, urlPath, revision, targetPath);
        } catch ( SVNException e ) {
            e.printStackTrace();
        }
    }

    /**
     * Test method for {@link net.brainage.rfc.util.svn.SvnClientImpl#diffStatus(java.lang.String, long, java.lang.String, net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler)}.
     */
    @Test
    public void testDiffStatus() {
        // svn://121.253.45.167/pcw-cmm/trunk/pcw-test-ear/bbb/b.txt
        // String urlPath1 = "svn://121.253.45.167/pcw-smg/trunk/pcw-smg-ear/pcw-smg-svc/src/java/com/sec/pcw/smg/service/PushServiceResourceImpl.java";
        String urlPath1 = "svn://121.253.45.167/pcw-cmm/trunk/pcw-test-ear/bbb/b.txt";
        // long revision1 = 522;
        long revision1 = 182;

        // String urlPath2 = "svn://121.253.45.167/pcw-smg/branches/pcw-smg-ear/pcw-smg-svc/src/java/com/sec/pcw/smg/service/PushServiceResourceImpl.java";
        String urlPath2 = "svn://121.253.45.167/pcw-cmm/branches/pcw-test-ear/bbb/b.txt";

        try {
            AbstractSvnDiffStatusHandler handler = new DefaultSvnDiffStatusHandler();
            char actual = svnClient.diffStatus(urlPath1, revision1, urlPath2, handler);
            System.out.println(actual);
            assertEquals('M', actual);
        } catch ( SVNException e ) {
            e.printStackTrace();
        }
    }

    @Test
    public void testDelete() {
        try {
            String t = "C:/Users/exia/rfcmanager/workspaces/wc/pcw-cmm/pcw-test-ear/a.txt";
            svnClient.delete(new File(t));
        } catch ( SVNException e ) {
            e.printStackTrace();
        }
    }

}
