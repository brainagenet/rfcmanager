/*
 * (#) net.brainage.rfc.ui.executor.CheckoutAsyncExecutor.java
 * Created on 2011. 6. 21.
 */
package net.brainage.rfc.ui.executor;

import java.io.File;

import net.brainage.rfc.model.CheckoutModel;
import net.brainage.rfc.model.SvnResource;
import net.brainage.rfc.model.WorkPhaseContext;
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

    private static final Logger log = LoggerFactory.getLogger(CheckoutAsyncExecutor.class);

    private CheckoutModel model;

    public CheckoutAsyncExecutor(WorkPhaseContext _context, CheckoutModel _model) {
        super(_context);
        this.model = _model;
    }

    protected void internalProcess() {
        WorkPhaseContext context = getContext();

        File buildDirectory = new File(context.getWorkingCopyPath());
        if ( buildDirectory.exists() == false ) {
            buildDirectory.mkdirs();
        }

        // svnclient 생성
        SvnClient svnClient = SvnClientImpl.getClient();

        try {
            // build directory가 버전 관리가 되는 디렉토리가 아니라면 checkout
            if ( svnClient.isWorkingCopyRoot(buildDirectory) == false ) {
                model.setTopIndex(-1);
                model.clearResources();
                String urlPath = context.getSnapshotRepoUrlPath();
                svnClient.checkout(urlPath, buildDirectory, new ISVNEventHandler()
                {
                    public void checkCancelled() throws SVNCancelException {
                    }

                    public void handleEvent(SVNEvent event, double progress) throws SVNException {
                        String action = event.getAction().toString();
                        String path = event.getFile().getAbsolutePath();
                        String mimeType = event.getMimeType();
                        if ( StringUtils.isEmpty(mimeType) ) {
                            mimeType = StringUtils.EMPTY;
                        }

                        SvnResource r = new SvnResource();
                        r.setAction(action);
                        r.setPath(path);
                        r.setMimeType(mimeType);
                        model.addResource(r);
                        model.setTopIndex(model.getTopIndex() + 1);
                        if ( log.isDebugEnabled() ) {
                            log.debug("# checkout {}", path);
                        }
                    }
                });
            } else {
                // TODO: 버전 관리가 되고 있다면 Update를 한다.
            }
        } catch ( SVNException e ) {
            context.addError("CheckoutAsyncExecutor", e.getMessage());
        }
    }
}
