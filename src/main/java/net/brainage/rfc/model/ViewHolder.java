/*
 * (#) net.brainage.rfc.model.ViewHolder.java
 * Created on 2011. 6. 21.
 */
package net.brainage.rfc.model;

import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class ViewHolder
{

    private Display display;
    private Shell shell;

    /**
     * 
     */
    public ViewHolder() {
    }

    /**
     * @return the display
     */
    public Display getDisplay() {
        return display;
    }

    /**
     * @param display the display to set
     */
    public void setDisplay(Display display) {
        this.display = display;
    }

    /**
     * @return the shell
     */
    public Shell getShell() {
        return shell;
    }

    /**
     * @param shell the shell to set
     */
    public void setShell(Shell shell) {
        this.shell = shell;
    }

}
