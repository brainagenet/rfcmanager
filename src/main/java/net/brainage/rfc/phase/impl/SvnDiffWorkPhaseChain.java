/*
 * (#) net.brainage.rfc.phase.impl.SvnDiffWorkPhaseChain.java
 * Created on 2011. 6. 22.
 */
package net.brainage.rfc.phase.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.ChangeRequestResource;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.phase.WorkPhaseChain;
import net.brainage.rfc.util.StringUtils;
import net.brainage.rfc.util.svn.SvnClient;
import net.brainage.rfc.util.svn.SvnClientImpl;
import net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler;
import net.brainage.rfc.util.svn.handler.DefaultSvnDiffStatusHandler;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class SvnDiffWorkPhaseChain extends WorkPhaseChain
{

    private static final Logger log = LoggerFactory.getLogger(SvnDiffWorkPhaseChain.class);

    private static final String WORKPHASE_NAME = "Subversion Diff Work Phase";

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
        context.setPhaseDescription("start working to confirm the change...");

        SvnClient svnClient = SvnClientImpl.getClient();
        try {
            ChangeRequest cr = context.getChangeRequest();
            List<ChangeRequestResource> resources = cr.getResources();
            int i = 0;
            for ( ChangeRequestResource resource : resources ) {
                StringBuffer urlPath1 = new StringBuffer(cr.getConnectionUrl2());
                StringBuffer urlPath2 = new StringBuffer(cr.getConnectionUrl());
                if ( resource.getResource().startsWith("/") == false ) {
                    urlPath1.append("/");
                    urlPath2.append("/");
                }
                urlPath1.append(resource.getResource());
                urlPath2.append(resource.getResource());

                AbstractSvnDiffStatusHandler diffStatusHandler = new DefaultSvnDiffStatusHandler();
                svnClient.diffStatus(urlPath1.toString(), resource.getRevision(),
                        urlPath2.toString(), diffStatusHandler);
                resource.setStatus("NOT OK");
                if ( StringUtils.hasText(resource.getType())
                        && resource.getType().equals(diffStatusHandler.getModificationText()) ) {
                    resource.setStatus("OK");
                }
                
                context.setProgressSelection(++i);
            }
        } catch ( SVNException svnex ) {
        }
    }

}
