/*
 * (#) net.brainage.rfc.phase.impl.SvnCommitWorkPhaseChain.java
 * Created on 2011. 6. 24.
 */
package net.brainage.rfc.phase.impl;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
public class SvnCommitWorkPhaseChain extends WorkPhaseChain
{

    private static final Logger log = LoggerFactory.getLogger(SvnCommitWorkPhaseChain.class);

    private static final String WORKPHASE_NAME = "Subversion Commit Work Phase";

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
        SvnClient svnClient = SvnClientImpl.getClient();

        ChangeRequest cr = context.getChangeRequest();

        // commit할 entry를 저장하기 위한 저장소
        List<File> entries = new ArrayList<File>();

        File wcDir = new File(context.getWorkingCopyPath());

        int i = 0;
        for ( ChangeRequestResource r : cr.getResources() ) {
            String resPath = r.getResource();
            File target = new File(wcDir, resPath);
            try {
                if ( "added".equals(r.getType()) ) {
                    svnClient.add(target, entries);
                    if ( log.isDebugEnabled() ) {
                        log.debug("add {} to {}", resPath, context.getSnapshotRepoUrlPath());
                    }
                    context.setPhaseDescription("add '" + r.getResource() + "'");
                } else if ( "deleted".equals(r.getType()) ) {
                    svnClient.delete(target);
                    entries.add(target);
                    if ( log.isDebugEnabled() ) {
                        log.debug("delete {} from {}", resPath, context.getSnapshotRepoUrlPath());
                    }
                    context.setPhaseDescription("delete '" + r.getResource() + "'");
                } else if ( "modified".equals(r.getType()) ) {
                    entries.add(target);
                    if ( log.isDebugEnabled() ) {
                        log.debug("modified {} to {}", resPath, context.getSnapshotRepoUrlPath());
                    }
                    context.setPhaseDescription("modified '" + r.getResource() + "'");
                }
                r.setStatus("OK");
            } catch ( SVNException ex ) {
                r.setStatus("error");
                context.addError(r.getResource(), ex.getMessage());
                log.error(ex.getMessage(), ex);
            }
            i++;
            context.setProgressSelection(i);
        }

        try {
            if ( log.isDebugEnabled() ) {
                log.debug("111");
            }
            generateCommitMessage(cr);
            if ( log.isDebugEnabled() ) {
                log.debug("222");
            }
            if ( log.isDebugEnabled() ) {
                log.debug(cr.getComment());
            }
            svnClient.commit(entries, cr.getComment());
        } catch ( SVNException e ) {
            e.printStackTrace();
        }
        
        context.setPhaseDescription("All requested resources were committed.");
    }

    /**
     * @param cr
     */
    private void generateCommitMessage(ChangeRequest cr) {
        Date workDate = new Date();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

        StringBuilder buf = new StringBuilder();
        buf.append("** Auto Commit Resources by AA\n");
        buf.append("   - workdate: ").append(dateFormat.format(workDate)).append("\n");
        buf.append("   - requester: ").append(cr.getRequester()).append("\n");
        buf.append("   - request date: ").append(cr.getRequestDate()).append("\n");
        buf.append("** ----------------------------------------------------- **\n");
        buf.append("** ").append(cr.getSummary()).append("\n");
        buf.append("** ----------------------------------------------------- **\n");
        
        if ( log.isDebugEnabled()) {
            log.debug("generated commit comment\n{}", buf.toString());
        }
        cr.setComment(buf.toString());
    }

}
