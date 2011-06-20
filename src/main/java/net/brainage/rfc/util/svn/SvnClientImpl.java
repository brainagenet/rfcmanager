/*
 * (#) net.brainage.rfc.util.svn.SvnClientImpl.java
 * Created on 2011. 6. 10.
 */
package net.brainage.rfc.util.svn;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNRevision;
import org.tmatesoft.svn.core.wc.SVNUpdateClient;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class SvnClientImpl implements SvnClient
{

    /**
     * 
     */
    private static final Logger log = LoggerFactory.getLogger(SvnClientImpl.class);

    /**
     * singleton instance
     */
    private static SvnClient svnClient = new SvnClientImpl();

    /**
     * 
     */
    private SVNClientManager svnClientManager;

    /**
     * 
     */
    private SvnClientImpl() {
        SvnUtils.setup();
        svnClientManager = SvnUtils.createClientManager();
    }

    /**
     * @return
     */
    public static SvnClient getClient() {
        return svnClient;
    }

    /* (non-Javadoc)
     * @see net.brainage.crmanager.svn.SvnClient#checkout(java.lang.String, java.lang.String)
     */
    public void checkout(String urlPath, String destPath) throws SVNException {
        checkout(urlPath, new File(destPath), null);
    }

    public void checkout(String urlPath, File destPath) throws SVNException {
        checkout(urlPath, destPath, null);
    }

    public void checkout(String urlPath, File destPath, ISVNEventHandler handler)
            throws SVNException {
        if (log.isInfoEnabled()) {
            log.info("checkout '{}' to '{}'...", urlPath, destPath);
        }
        SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
        if (handler != null) {
            updateClient.setEventHandler(handler);
        }

        SVNURL url = SVNURL.parseURIDecoded(urlPath);
        updateClient.doCheckout(url, destPath, SVNRevision.HEAD, SVNRevision.HEAD,
                SVNDepth.INFINITY,
                true);
    }

    /* (non-Javadoc)
     * @see net.brainage.crmanager.svn.SvnClient#isVersionedDirectory(java.lang.String)
     */
    public boolean isVersionedDirectory(String path) {
        if (log.isInfoEnabled()) {
            log.info("isVersionedDirectory({})", path);
        }
        return isVersionedDirectory(new File(path));
    }

    /* (non-Javadoc)
     * @see net.brainage.crmanager.svn.SvnClient#isVersionedDirectory(java.io.File)
     */
    public boolean isVersionedDirectory(File path) {
        if (log.isInfoEnabled()) {
            log.info("isVersionedDirectory({})", path.getAbsolutePath());
        }

        if (path.exists() == false) {
            throw new IllegalStateException("Input file '" + path.getAbsoluteFile()
                    + "' dose not exist.");
        }

        if (path.isDirectory() == false) {
            throw new IllegalStateException("Input file '" + path.getAbsoluteFile()
                    + "' is not directory.");
        }

        return SVNWCUtil.isVersionedDirectory(path);
    }

    /**
     * @param rootPath
     * @return
     * @throws SVNException 
     */
    public boolean isWorkingCopyRoot(String rootPath) throws SVNException {
        if (log.isInfoEnabled()) {
            log.info("isWorkingCopyRoot({})", rootPath);
        }
        return isWorkingCopyRoot(new File(rootPath));
    }

    /**
     * @param rootPath
     * @return
     * @throws SVNException 
     */
    public boolean isWorkingCopyRoot(File rootPath) throws SVNException {
        if (log.isInfoEnabled()) {
            log.info("isWorkingCopyRoot({})", rootPath.getAbsolutePath());
        }

        if (rootPath.exists() == false) {
            throw new IllegalStateException("Input file '" + rootPath.getAbsoluteFile()
                    + "' dose not exist.");
        }

        if (rootPath.isDirectory() == false) {
            throw new IllegalStateException("Input file '" + rootPath.getAbsoluteFile()
                    + "' is not directory.");
        }

        return SVNWCUtil.isWorkingCopyRoot(rootPath);
    }

}
