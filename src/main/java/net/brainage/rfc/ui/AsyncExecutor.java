/*
 * (#) net.brainage.rfc.ui.AsyncExecutor.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.ui;

import net.brainage.rfc.model.WorkPhaseContext;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public abstract class AsyncExecutor implements Runnable
{

    private WorkPhaseContext context;

    public AsyncExecutor(WorkPhaseContext _context) {
        this.context = _context;
    }

    public WorkPhaseContext getContext() {
        return context;
    }

    public void run() {
        boolean flag = preProcess();
        if (flag) {
            internalProcess();
            postProcess();
        }
    }

    protected boolean preProcess() {
        return true;
    }

    protected abstract void internalProcess();

    protected void postProcess() {
    }

}
