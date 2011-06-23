/*
 * (#) net.brainage.rfc.util.svn.SvnClientImpl.java
 * Created on 2011. 6. 10.
 */
package net.brainage.rfc.util.svn;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

import net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler;
import net.brainage.rfc.util.svn.handler.DefaultSvnDiffStatusHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNDepth;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNURL;
import org.tmatesoft.svn.core.io.SVNRepository;
import org.tmatesoft.svn.core.io.SVNRepositoryFactory;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNDiffClient;
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
    private Map<String, SVNRepository> repoMap = new HashMap<String, SVNRepository>();

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

    /*
     * (non-Javadoc)
     * 
     * @see net.brainage.crmanager.svn.SvnClient#checkout(java.lang.String,
     * java.lang.String)
     */
    public void checkout(String urlPath, String destPath) throws SVNException {
        checkout(urlPath, new File(destPath), null);
    }

    public void checkout(String urlPath, File destPath) throws SVNException {
        checkout(urlPath, destPath, null);
    }

    public void checkout(String urlPath, File destPath, ISVNEventHandler handler)
            throws SVNException {
        if ( log.isInfoEnabled() ) {
            log.info("checkout '{}' to '{}'...", urlPath, destPath);
        }
        SVNUpdateClient updateClient = svnClientManager.getUpdateClient();
        if ( handler != null ) {
            updateClient.setEventHandler(handler);
        }

        SVNURL url = SVNURL.parseURIDecoded(urlPath);
        updateClient.doCheckout(url, destPath, SVNRevision.HEAD, SVNRevision.HEAD,
                SVNDepth.INFINITY, true);
    }

    public char diffStatus(String urlPath1, long urlPath1Revision, String urlPath2,
            AbstractSvnDiffStatusHandler handler) throws SVNException {

        SVNURL url1 = SVNURL.parseURIDecoded(urlPath1);
        SVNRevision revision1 = SVNRevision.create(urlPath1Revision);
        SVNURL url2 = SVNURL.parseURIDecoded(urlPath2);

        SVNDiffClient diffClient = svnClientManager.getDiffClient();
        if ( handler == null ) {
            handler = new DefaultSvnDiffStatusHandler();
        }

        diffClient.doDiffStatus(url1, revision1, url2, SVNRevision.HEAD, SVNDepth.IMMEDIATES,
                false, handler);

        return handler.getModificationType();
    }

    /**
     * @param repoUrlPath
     * @param urlPath
     * @param revision
     * @param targetPath
     */
    public void getFile(String repoUrlPath, String urlPath, long revision, String targetPath)
            throws SVNException {
        File target = new File(targetPath);
        getFile(repoUrlPath, urlPath, revision, target);
    }

    /**
     * @param repoUrlPath
     * @param urlPath
     * @param revision
     * @param target
     */
    public void getFile(String repoUrlPath, String urlPath, long revision, File target)
            throws SVNException {
        SVNRepository repository = getRepository(repoUrlPath);

        File targetFile = new File(target, urlPath);
        if ( targetFile.exists() == false ) {
            File parent = targetFile.getParentFile();
            if ( parent.exists() == false ) {
                parent.mkdirs();
            }
        }

        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(targetFile));
            repository.getFile(urlPath, revision, null, out);
            out.flush();
        } catch ( IOException e ) {
        } finally {
            try {
                out.close();
            } catch ( IOException ioex ) {
            }
        }
    }

    private SVNRepository getRepository(String repoUrlPath) throws SVNException {
        SVNRepository repo = repoMap.get(repoUrlPath);
        if ( repo == null ) {
            SVNURL repoUrl = SVNURL.parseURIDecoded(repoUrlPath);
            repo = SVNRepositoryFactory.create(repoUrl);
        }
        return repo;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.brainage.crmanager.svn.SvnClient#isVersionedDirectory(java.lang.String
     * )
     */
    public boolean isVersionedDirectory(String path) {
        if ( log.isInfoEnabled() ) {
            log.info("isVersionedDirectory({})", path);
        }
        return isVersionedDirectory(new File(path));
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.brainage.crmanager.svn.SvnClient#isVersionedDirectory(java.io.File)
     */
    public boolean isVersionedDirectory(File path) {
        if ( log.isInfoEnabled() ) {
            log.info("isVersionedDirectory({})", path.getAbsolutePath());
        }

        if ( path.exists() == false ) {
            throw new IllegalStateException("Input file '" + path.getAbsoluteFile()
                    + "' dose not exist.");
        }

        if ( path.isDirectory() == false ) {
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
        if ( log.isInfoEnabled() ) {
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
        if ( log.isInfoEnabled() ) {
            log.info("isWorkingCopyRoot({})", rootPath.getAbsolutePath());
        }

        if ( rootPath.exists() == false ) {
            throw new IllegalStateException("Input file '" + rootPath.getAbsoluteFile()
                    + "' dose not exist.");
        }

        if ( rootPath.isDirectory() == false ) {
            throw new IllegalStateException("Input file '" + rootPath.getAbsoluteFile()
                    + "' is not directory.");
        }

        return SVNWCUtil.isWorkingCopyRoot(rootPath);
    }

}
