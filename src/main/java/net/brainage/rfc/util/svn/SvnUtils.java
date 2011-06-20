/*
 * (#) net.brainage.crmanager.svn.SvnUtils.java
 * Created on 2011. 6. 10.
 */
package net.brainage.rfc.util.svn;


import net.brainage.rfc.config.Configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.auth.ISVNAuthenticationManager;
import org.tmatesoft.svn.core.internal.io.dav.DAVRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.fs.FSRepositoryFactory;
import org.tmatesoft.svn.core.internal.io.svn.SVNRepositoryFactoryImpl;
import org.tmatesoft.svn.core.wc.ISVNOptions;
import org.tmatesoft.svn.core.wc.SVNClientManager;
import org.tmatesoft.svn.core.wc.SVNWCUtil;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public final class SvnUtils
{

    /**
     * 
     */
    private static final Logger log = LoggerFactory.getLogger(SvnUtils.class);
    
    private static Configuration config = Configuration.getInstance();

    /**
     * 
     */
    private SvnUtils() {
    }

    /**
     * @param username
     * @param password
     * @return
     */
    public static SVNClientManager createClientManager(String username, String password) {
        ISVNAuthenticationManager authManager = SVNWCUtil.createDefaultAuthenticationManager(
                username, password);
        ISVNOptions options = SVNWCUtil.createDefaultOptions(true);
        return SVNClientManager.newInstance(options, authManager);
    }

    /**
     * @return
     */
    public static SVNClientManager createClientManager() {
        String username = config.getString(Configuration.Key.SVN_AUTH_USERNAME);
        String password = config.getString(Configuration.Key.SVN_AUTH_PASSWORD);
        return createClientManager(username, password);
    }

    /**
     * 
     */
    public static void setup() {
        int svnProtocol = config.getInt(Configuration.Key.SVN_PROTOCOL_SELECT);
        if (log.isInfoEnabled()) {
            log.info("svn protocol = " + svnProtocol);
        }

        switch (svnProtocol) {
            case 0:
            case 1:
                setupDavProtocol();
            break;
            
            case 2:
            case 3:
                setupSvnProtocol();
            break;
            
            case 4:
                setupFsProtocol();
            break;

            default:
                setupSvnProtocol();
            break;
        }
        
    }

    public static void setupSvnProtocol() {
        SVNRepositoryFactoryImpl.setup();
    }

    public static void setupDavProtocol() {
        DAVRepositoryFactory.setup();
    }

    public static void setupFsProtocol() {
        FSRepositoryFactory.setup();
    }

}
