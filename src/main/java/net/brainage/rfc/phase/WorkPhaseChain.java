/*
 * (#) net.brainage.rfc.phase.WorkPhaseChain.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.phase;

import net.brainage.rfc.model.WorkPhaseContext;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public abstract class WorkPhaseChain implements WorkPhase
{

    private static final Logger log = LoggerFactory.getLogger(WorkPhaseChain.class);

    private WorkPhaseChain next;

    /**
     * @return the next
     */
    public WorkPhaseChain getNext() {
        return next;
    }

    /**
     * @param next the next to set
     */
    public void setNext(WorkPhaseChain next) {
        this.next = next;
    }

    public void start(WorkPhaseContext context) {
        if (log.isInfoEnabled()) {
            log.info("chaining '{}' work phase...", getName());
        }

        process(context);
        if (context.hasErrors()) {
            context.setPhaseDescription("Occur errors. Show errors tab.");
            return;
        }

        if (next != null) {
            next.start(context);
        }
    }

    public void process(WorkPhaseContext context) {
        if (log.isInfoEnabled()) {
            log.info(">> start '{}' work phase...", getName());
        }

        context.setPhaseName(getName());
        context.setPhaseDescription("start '" + getName() + "' work phase...");

        boolean flag = preProcess(context);
        if (flag) {
            internalProcess(context);
            postProcess(context);
        }
    }

    protected boolean preProcess(WorkPhaseContext context) {
        return true;
    }

    protected abstract void internalProcess(WorkPhaseContext context);

    protected void postProcess(WorkPhaseContext context) {
    }

}
