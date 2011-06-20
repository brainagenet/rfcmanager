/*
 * (#) net.brainage.rfc.ui.executor.RunAsyncExecutor.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.ui.executor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.phase.WorkPhaseChain;
import net.brainage.rfc.phase.WorkPhaseChainFactory;
import net.brainage.rfc.ui.AsyncExecutor;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class RunAsyncExecutor extends AsyncExecutor
{

    private static final Logger log = LoggerFactory.getLogger(RunAsyncExecutor.class);

    public RunAsyncExecutor(WorkPhaseContext _context) {
        super(_context);
    }

    protected void internalProcess() {
        if ( log.isInfoEnabled()) {
            log.info(">> call internal process...");
        }
        try {
            WorkPhaseChain phaseChain = WorkPhaseChainFactory.getWorkPhaseChain();
            phaseChain.start(getContext());
        } catch (Exception ex) {
            log.error("Occur error during run '" + getContext().getPhaseName()
                    + "' work phase...", ex);
        }
    }

}
