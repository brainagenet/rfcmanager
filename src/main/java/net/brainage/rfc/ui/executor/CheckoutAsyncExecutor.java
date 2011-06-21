/*
 * (#) net.brainage.rfc.ui.executor.CheckoutAsyncExecutor.java
 * Created on 2011. 6. 21.
 */
package net.brainage.rfc.ui.executor;

import java.io.File;

import net.brainage.rfc.config.Configuration;
import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.CheckoutModel;
import net.brainage.rfc.model.SvnResource;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.phase.impl.CheckoutWorkPhaseChain;
import net.brainage.rfc.ui.AsyncExecutor;
import net.brainage.rfc.util.StringUtils;
import net.brainage.rfc.util.svn.SvnClient;
import net.brainage.rfc.util.svn.SvnClientImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNCancelException;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;
import org.tmatesoft.svn.core.wc.SVNEvent;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class CheckoutAsyncExecutor extends AsyncExecutor
{

    private static final Logger log = LoggerFactory.getLogger(CheckoutWorkPhaseChain.class);

    private CheckoutModel model;

    public CheckoutAsyncExecutor(WorkPhaseContext _context, CheckoutModel _model) {
        super(_context);
        this.model = _model;
    }

    protected void internalProcess() {
        WorkPhaseContext context = getContext();
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
                model.setTopIndex(-1);
                model.clearResources();
                String urlPath = changeRequest.getConnectionUrl();
                svnClient.checkout(urlPath, buildDirectory, new ISVNEventHandler() {
                    public void checkCancelled() throws SVNCancelException {
                    	if ( log.isInfoEnabled()) {
                    		log.info("#### called checkCancelled()");
                    	}
                    }

                    public void handleEvent(SVNEvent event, double progress) throws SVNException {
                        String action = event.getAction().toString();
                        String path = event.getFile().getAbsolutePath();
                        String mimeType = event.getMimeType();
                        if (StringUtils.isEmpty(mimeType)) {
                            mimeType = StringUtils.EMPTY;
                        }

                        SvnResource r = new SvnResource();
                        r.setAction(action);
                        r.setPath(path);
                        r.setMimeType(mimeType);
                        model.addResource(r);
                        model.setTopIndex(model.getTopIndex() + 1);
                    }
                });
            } else {
                // TODO: 버전 관리가 되고 있다면 Update를 한다.
            }
        } catch (SVNException e) {
            context.addError("CheckoutAsyncExecutor", e.getMessage());
        }
    }
}
