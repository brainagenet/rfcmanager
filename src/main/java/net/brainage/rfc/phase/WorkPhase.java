/*
 * (#) net.brainage.rfc.phase.WorkPhase.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.phase;

import net.brainage.rfc.model.WorkPhaseContext;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public interface WorkPhase
{

    /**
     * @return
     */
    String getName();

    /**
     * @param context
     */
    void process(WorkPhaseContext context);
    
}
