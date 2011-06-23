/*
 * (#) net.brainage.rfc.phase.impl.SvnDiffWorkPhaseChain.java
 * Created on 2011. 6. 22.
 */
package net.brainage.rfc.phase.impl;

import java.util.List;

import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.ChangeRequestResource;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.phase.WorkPhaseChain;
import net.brainage.rfc.util.StringUtils;
import net.brainage.rfc.util.svn.SvnClient;
import net.brainage.rfc.util.svn.SvnClientImpl;
import net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler;
import net.brainage.rfc.util.svn.handler.DefaultSvnDiffStatusHandler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class SvnDiffWorkPhaseChain extends WorkPhaseChain
{

    private static final Logger log = LoggerFactory.getLogger(SvnDiffWorkPhaseChain.class);

    private static final String WORKPHASE_NAME = "Subversion Diff Verification Work Phase";

    /*
     * (non-Javadoc)
     * 
     * @see net.brainage.rfc.phase.WorkPhase#getName()
     */
    @Override
    public String getName() {
        return WORKPHASE_NAME;
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * net.brainage.rfc.phase.WorkPhaseChain#internalProcess(net.brainage.rfc
     * .model.WorkPhaseContext)
     */
    @Override
    protected void internalProcess(WorkPhaseContext context) {
        context.setPhaseDescription("start working for diff verification...");

        SvnClient svnClient = SvnClientImpl.getClient();
        try {
            ChangeRequest cr = context.getChangeRequest();
            List<ChangeRequestResource> resources = cr.getResources();
            int i = 0;
            for ( ChangeRequestResource r : resources ) {
                context.setProgressSelection(++i);
                if ( r.getModType() == 'A' ) {
                    continue;
                }
                context.setPhaseDescription("diff for '" + r.getResource() + "'");
                StringBuffer urlPath1 = new StringBuffer(context.getMainStremRepoUrlPath());
                StringBuffer urlPath2 = new StringBuffer(context.getSnapshotRepoUrlPath());
                if ( r.getResource().startsWith("/") == false ) {
                    urlPath1.append("/");
                    urlPath2.append("/");
                }
                urlPath1.append(r.getResource());
                urlPath2.append(r.getResource());

                if ( log.isDebugEnabled() ) {
                    log.debug("url1: {}", urlPath1.toString());
                    log.debug("url2: {}", urlPath2.toString());
                }

                AbstractSvnDiffStatusHandler diffStatusHandler = new DefaultSvnDiffStatusHandler();
                svnClient.diffStatus(urlPath1.toString(), r.getRevision(), urlPath2.toString(),
                        diffStatusHandler);
                if ( StringUtils.hasText(r.getType())
                        && r.getType().equals(diffStatusHandler.getModificationText()) ) {
                    r.setStatus("OK");
                } else {
                    r.setStatus("");
                    context.addError(r.getResource(), "modification type mismatch.");
                }
            }
            context.setPhaseDescription("Diff verification completed.");
        } catch ( SVNException svnex ) {
        }
    }

}
