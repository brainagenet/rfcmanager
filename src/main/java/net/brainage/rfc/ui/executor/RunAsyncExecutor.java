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

    private static final Logger logger = LoggerFactory.getLogger(RunAsyncExecutor.class);

    /**
     * @param _context
     */
    public RunAsyncExecutor(WorkPhaseContext _context) {
        super(_context);
    }

    /* (non-Javadoc)
     * @see net.brainage.rfc.ui.AsyncExecutor#internalExecute()
     */
    public void internalExecute() {
        try {
            WorkPhaseChain phaseChain = WorkPhaseChainFactory.getWorkPhaseChain();
            phaseChain.start(getContext());
        } catch (Exception ex) {
            logger.error("Occur error during run '" + getContext().getPhaseName()
                    + "' work phase...", ex);
        }
    }

}
