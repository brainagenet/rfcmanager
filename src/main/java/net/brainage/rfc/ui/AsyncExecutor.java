/*
 * (#) net.brainage.rfc.ui.AsyncExecutor.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.ui;

import org.eclipse.swt.widgets.Display;

import net.brainage.rfc.model.WorkPhaseContext;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public abstract class AsyncExecutor
{

    private WorkPhaseContext context;

    public AsyncExecutor(WorkPhaseContext _context) {
        this.context = _context;
    }

    public WorkPhaseContext getContext() {
        return context;
    }

    protected void preExecute() {
    }

    public abstract void internalExecute();

    protected void postExecute() {
    }

    public void execute(Display display) {
        display.asyncExec(new Runnable() {
            public void run() {
                preExecute();
                internalExecute();
                postExecute();
            }
        });
    }

}
