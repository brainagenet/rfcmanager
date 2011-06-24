/*
 * (#) net.brainage.rfc.phase.impl.ResourceCopyWorkPhaseChain.java
 * Created on 2011. 6. 23.
 */
package net.brainage.rfc.phase.impl;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.ChangeRequestResource;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.phase.WorkPhaseChain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class ResourceCopyWorkPhaseChain extends WorkPhaseChain
{

    private static final Logger log = LoggerFactory.getLogger(ResourceCopyWorkPhaseChain.class);

    private static final String WORKPHASE_NAME = "Resource Copy Work Phase";

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
        File wcDir = new File(context.getWorkingCopyPath());
        File tmpDir = new File(context.getTmpPath());

        ChangeRequest cr = context.getChangeRequest();
        int i = 0;
        for ( ChangeRequestResource r : cr.getResources() ) {
            if ( "deleted".equals(r.getType())) {
                continue;
            }
            String resPath = r.getResource();
            context.setPhaseDescription("copy '" + resPath + "'...");
            // tmp --> wc
            File from = new File(tmpDir, resPath);
            File to = new File(wcDir, resPath);
            try {
                copy(from, to);
            } catch ( IOException e ) {
                log.error("File error occurred while copying.", e);
            }
            i++;
            context.setProgressSelection(i);
            if ( log.isDebugEnabled() ) {
                log.debug("copyed '" + resPath + "'...");
            }
        }
        context.setPhaseDescription("A total of " + i + " files were copyed.");
    }

    private void copy(File from, File to) throws IOException {
        if ( to.getParentFile().exists() == false ) {
            to.getParentFile().mkdirs();
        }

        InputStream r = null;
        OutputStream w = null;

        try {
            r = new BufferedInputStream(new FileInputStream(from));
            w = new BufferedOutputStream(new FileOutputStream(to));
            byte[] buf = new byte[32];
            int len = -1;
            while ( (len = r.read(buf)) != -1 ) {
                w.write(buf, 0, len);
            }
            w.flush();
        } finally {
            if ( w != null ) {
                w.close();
                w = null;
            }

            if ( r != null ) {
                r.close();
                r = null;
            }
        }
    }

}
