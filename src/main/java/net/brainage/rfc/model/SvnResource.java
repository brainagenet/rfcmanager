/*
 * (#) net.brainage.rfc.model.SvnResource.java
 * Created on 2011. 6. 21.
 */
package net.brainage.rfc.model;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class SvnResource extends AbstractModelObject
{

    private String action;
    private String path;
    private String mimeType;

    /**
     * 
     */
    public SvnResource() {
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        String oldValue = this.action;
        this.action = action;
        firePropertyChange("action", oldValue, this.action);
    }

    /**
     * @return the path
     */
    public String getPath() {
        return path;
    }

    /**
     * @param path the path to set
     */
    public void setPath(String path) {
        String oldValue = this.path;
        this.path = path;
        firePropertyChange("path", oldValue, this.path);
    }

    /**
     * @return the mimeType
     */
    public String getMimeType() {
        return mimeType;
    }

    /**
     * @param mimeType the mimeType to set
     */
    public void setMimeType(String mimeType) {
        String oldValue = this.mimeType;
        this.mimeType = mimeType;
        firePropertyChange("mimeType", oldValue, this.mimeType);
    }

}
