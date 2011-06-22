/*
 * (#) net.brainage.rfc.phase.WorkPhaseChainFactory.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.phase;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class WorkPhaseChainFactory
{

    private static final Logger log = LoggerFactory.getLogger(WorkPhaseChainFactory.class);

    private static String[] CHAIN_CLASSES = new String[] { "net.brainage.rfc.phase.impl.SvnDiffWorkPhaseChain" };

    private static WorkPhaseChain phaseChain;

    private WorkPhaseChainFactory() {
    }

    public static WorkPhaseChain getWorkPhaseChain() throws Exception {
        if ( phaseChain != null ) {
            if ( log.isDebugEnabled() ) {
                log.debug("already instantiated then return static instance...");
            }
            return phaseChain;
        }

        WorkPhaseChain previous = null, current = null;
        for ( String chainClazz : CHAIN_CLASSES ) {
            if ( log.isDebugEnabled() ) {
                log.debug("chain class: " + chainClazz);
            }
            current = (WorkPhaseChain) Class.forName(chainClazz).newInstance();
            if ( phaseChain == null ) {
                phaseChain = current;
                previous = phaseChain;
                continue;
            }
            previous.setNext(current);
            previous = current;
        }
        return phaseChain;
    }

}
