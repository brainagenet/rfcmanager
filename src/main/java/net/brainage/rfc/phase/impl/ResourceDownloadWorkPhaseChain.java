/*
 * (#) net.brainage.rfc.phase.impl.ResourceDownloadWorkPhaseChain.java
 * Created on 2011. 6. 23.
 */
package net.brainage.rfc.phase.impl;

import java.io.File;

import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.ChangeRequestResource;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.phase.WorkPhaseChain;
import net.brainage.rfc.util.svn.SvnClient;
import net.brainage.rfc.util.svn.SvnClientImpl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class ResourceDownloadWorkPhaseChain extends WorkPhaseChain
{

    private static final Logger log = LoggerFactory.getLogger(ResourceDownloadWorkPhaseChain.class);

    private static final String WORKPHASE_NAME = "Resource Download Work Phase";

    /* (non-Javadoc)
     * @see net.brainage.rfc.phase.WorkPhase#getName()
     */
    @Override
    public String getName() {
        return WORKPHASE_NAME;
    }

    /* (non-Javadoc)
     * @see net.brainage.rfc.phase.WorkPhaseChain#preProcess(net.brainage.rfc.model.WorkPhaseContext)
     */
    @Override
    protected boolean preProcess(WorkPhaseContext context) {
        // set progress selection to zero 
        context.setProgressSelection(0);
        if ( log.isDebugEnabled() ) {
            log.debug("set progress selection to zero");
        }

        // clear Change Request Resource Status
        context.getChangeRequest().clearResourceStatus();
        if ( log.isDebugEnabled() ) {
            log.debug("clear Change Request Resource Status");
        }

        return true;
    }

    /* (non-Javadoc)
     * @see net.brainage.rfc.phase.WorkPhaseChain#internalProcess(net.brainage.rfc.model.WorkPhaseContext)
     */
    @Override
    protected void internalProcess(WorkPhaseContext context) {
        File baseTmpDir = new File(context.getTmpPath());
        if ( baseTmpDir.exists() ) {
            baseTmpDir.delete();
            if ( log.isDebugEnabled() ) {
                log.debug("The base tmp dir exist and then delete directory.");
            }
        } else {
            baseTmpDir.mkdirs();
            if ( log.isDebugEnabled() ) {
                log.debug("The base tmp dir dose not exist and then make directories.");
            }
        }

        SvnClient svnClient = SvnClientImpl.getClient();

        ChangeRequest cr = context.getChangeRequest();
        int i = 1;
        for ( ChangeRequestResource r : cr.getResources() ) {
            if ( "deleted".equals(r.getType()) ) {
                continue;
            }
            try {
                svnClient.getFile(context.getMainStremRepoUrlPath(), r.getResource(),
                        r.getRevision(), context.getTmpPath());
                context.setPhaseDescription("downloaded '" + r.getResource() + "'");
                r.setStatus("OK");
                if ( log.isDebugEnabled() ) {
                    log.debug("downloaded '" + r.getResource() + "'");
                }

            } catch ( SVNException e ) {
            }
            context.setProgressSelection(i++);
        }

        context.setPhaseDescription("A total of " + cr.sizeOfResources()
                + " files were downloaded.");

    }

}
