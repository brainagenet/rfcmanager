/*
 * (#) net.brainage.rfc.phase.impl.CheckoutWorkPhaseChain.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.phase.impl;

import java.io.File;

import net.brainage.rfc.config.Configuration;
import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.phase.WorkPhaseChain;
import net.brainage.rfc.util.svn.SvnClient;
import net.brainage.rfc.util.svn.SvnClientImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;
import org.tmatesoft.svn.core.wc.SVNEventAction;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class CheckoutWorkPhaseChain extends WorkPhaseChain
{

    private static final Logger log = LoggerFactory.getLogger(CheckoutWorkPhaseChain.class);

    private static final String WORKPHASE_NAME = "CheckoutWorkPhase";

    /* (non-Javadoc)
     * @see net.brainage.rfc.phase.WorkPhase#getName()
     */
    public String getName() {
        return WORKPHASE_NAME;
    }

    protected void internalProcess(final WorkPhaseContext context) {
        context.setPhaseDescription("check working copy directory...");

        ChangeRequest changeRequest = context.getChangeRequest();
        Configuration config = Configuration.getInstance();

        String wcdir = config.getString(Configuration.Key.WORKSPACE_WC);
        StringBuffer buf = new StringBuffer(wcdir);
        buf.append("/").append(changeRequest.getComponent());
        buf.append("/build/").append(changeRequest.getModule());

        File buildDirectory = new File(buf.toString());
        if (log.isDebugEnabled()) {
            log.debug("    - working copy dir for {} = {}", changeRequest.getComponent(),
                    buildDirectory.getAbsolutePath());
        }

        if (buildDirectory.exists() == false) {
            buildDirectory.mkdirs();
            context.setPhaseDescription("create build directory for '"
                    + changeRequest.getComponent() + "' component...");
        }

        // svnclient 생성
        SvnClient svnClient = SvnClientImpl.getClient();

        try {
            // build directory가 버전 관리가 되는 디렉토리가 아니라면 checkout
            if (svnClient.isWorkingCopyRoot(buildDirectory) == false) {
                String urlPath = changeRequest.getConnectionUrl();
                svnClient.checkout(urlPath, buildDirectory, new ISVNEventHandler() {
                    public void checkCancelled() throws SVNCancelException {
                    }

                    public void handleEvent(SVNEvent event, double progress) throws SVNException {
                        SVNEventAction eventAction = event.getAction();
                        File eventFile = event.getFile();
                        String msg = "[" + eventAction.toString() + "] checkout '"
                                + eventFile.getAbsolutePath() + "'.";
                        context.setPhaseDescription(msg);
                    }
                });
            }
        } catch (SVNException e) {
            context.addError(getName(), e.getMessage());
        }
    }

}
