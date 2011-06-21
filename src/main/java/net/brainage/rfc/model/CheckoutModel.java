/*
 * (#) net.brainage.rfc.model.CheckoutModel.java
 * Created on 2011. 6. 21.
 */
package net.brainage.rfc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class CheckoutModel extends AbstractModelObject
{

    private String module;
    private List<SvnResource> resources = new ArrayList<SvnResource>();
    private int topIndex = -1;
    private String status;

    /**
     * 
     */
    public CheckoutModel() {
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module the module to set
     */
    public void setModule(String module) {
        String oldValue = this.module;
        this.module = module;
        firePropertyChange("module", oldValue, this.module);
    }

    /**
     * @return the resources
     */
    public List<SvnResource> getResources() {
        return resources;
    }

    public void addResource(SvnResource r) {
        this.resources.add(r);
        firePropertyChange("resources", null, this.resources);
    }

    public void removeResource(SvnResource r) {
        this.resources.remove(r);
        firePropertyChange("resources", null, this.resources);
    }

    public void clearResources() {
        this.resources.clear();
        firePropertyChange("resources", null, this.resources);
    }

    public int sizeOfResources() {
        return this.resources.size();
    }

    /**
     * @return the topIndex
     */
    public int getTopIndex() {
        return topIndex;
    }

    /**
     * @param topIndex the topIndex to set
     */
    public void setTopIndex(int resIndex) {
        int oldValue = this.topIndex;
        this.topIndex = resIndex;
        firePropertyChange("topIndex", oldValue, this.topIndex);
    }

    /**
     * @return the status
     */
    public String getStatus() {
        return status;
    }

    /**
     * @param status the status to set
     */
    public void setStatus(String status) {
        String oldValue = this.status;
        this.status = status;
        firePropertyChange("status", oldValue, this.status);
    }

}
