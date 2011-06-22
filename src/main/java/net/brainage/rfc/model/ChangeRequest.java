/*
 * 
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
public class ChangeRequest extends AbstractModelObject
{

    private String file;
    private String component;
    private int componentSelect = -1;
    private String module;
    private int moduleSelect = -1;
    private String requester;
    private String requestDate;
    private String summary;
    private String comment;
    private String connectionUrl;
    private String connectionUrl2;

    private List<ChangeRequestResource> resources = new ArrayList<ChangeRequestResource>();

    /**
     * 
     */
    public ChangeRequest() {
    }

    /**
     * @return the file
     */
    public String getFile() {
        return file;
    }

    /**
     * @param file
     *            the file to set
     */
    public void setFile(String file) {
        String oldValue = this.file;
        this.file = file;
        firePropertyChange("file", oldValue, this.file);
    }

    /**
     * @return the component
     */
    public String getComponent() {
        return component;
    }

    /**
     * @param component
     *            the component to set
     */
    public void setComponent(String component) {
        String oldValue = this.component;
        this.component = component;
        firePropertyChange("component", oldValue, this.component);
    }

    /**
     * @return the componentSelect
     */
    public int getComponentSelect() {
        return componentSelect;
    }

    /**
     * @param componentSelect
     *            the componentSelect to set
     */
    public void setComponentSelect(int componentSelect) {
        int oldValue = this.componentSelect;
        this.componentSelect = componentSelect;
        firePropertyChange("componentSelect", oldValue, this.componentSelect);
    }

    /**
     * @return the module
     */
    public String getModule() {
        return module;
    }

    /**
     * @param module
     *            the module to set
     */
    public void setModule(String module) {
        String oldValue = this.module;
        this.module = module;
        firePropertyChange("module", oldValue, this.module);
    }

    /**
     * @return the moduleSelect
     */
    public int getModuleSelect() {
        return moduleSelect;
    }

    /**
     * @param moduleSelect
     *            the moduleSelect to set
     */
    public void setModuleSelect(int moduleSelect) {
        int oldValue = this.moduleSelect;
        this.moduleSelect = moduleSelect;
        firePropertyChange("moduleSelect", oldValue, this.moduleSelect);
    }

    /**
     * @return the requester
     */
    public String getRequester() {
        return requester;
    }

    /**
     * @param requester
     *            the requester to set
     */
    public void setRequester(String requester) {
        String oldValue = this.requester;
        this.requester = requester;
        firePropertyChange("requester", oldValue, this.requester);
    }

    /**
     * @return the requestDate
     */
    public String getRequestDate() {
        return requestDate;
    }

    /**
     * @param requestDate
     *            the requestDate to set
     */
    public void setRequestDate(String requestDate) {
        String oldValue = this.requestDate;
        this.requestDate = requestDate;
        firePropertyChange("requestDate", oldValue, this.requestDate);
    }

    /**
     * @return the summary
     */
    public String getSummary() {
        return summary;
    }

    /**
     * @param summary
     *            the summary to set
     */
    public void setSummary(String summary) {
        String oldValue = this.summary;
        this.summary = summary;
        firePropertyChange("summary", oldValue, this.summary);
    }

    /**
     * @return the comment
     */
    public String getComment() {
        return comment;
    }

    /**
     * @param comment
     *            the comment to set
     */
    public void setComment(String comment) {
        String oldValue = this.comment;
        this.comment = comment;
        firePropertyChange("comment", oldValue, this.comment);
    }

    /**
     * @return the connectionUrl
     */
    public String getConnectionUrl() {
        return connectionUrl;
    }

    /**
     * @param connectionUrl
     *            the connectionUrl to set
     */
    public void setConnectionUrl(String connectionUrl) {
        String oldValue = this.connectionUrl;
        this.connectionUrl = connectionUrl;
        firePropertyChange("connectionUrl", oldValue, this.connectionUrl);
    }

    /**
     * @return the connectionUrl2
     */
    public String getConnectionUrl2() {
        return connectionUrl2;
    }

    /**
     * @param connectionUrl2 the connectionUrl2 to set
     */
    public void setConnectionUrl2(String connectionUrl2) {
        this.connectionUrl2 = connectionUrl2;
    }

    /**
     * @param resource
     */
    public void addResource(ChangeRequestResource resource) {
        this.resources.add(resource);
        firePropertyChange("resources", null, this.resources);
    }

    /**
     * @param resource
     */
    public void removeResource(ChangeRequestResource resource) {
        this.resources.remove(resource);
        firePropertyChange("resources", null, this.resources);
    }

    /**
     * 
     */
    public void clearResources() {
        this.resources.clear();
        firePropertyChange("resources", null, this.resources);
    }

    /**
     * @return
     */
    public int sizeOfResources() {
        return this.resources.size();
    }

    /**
     * @return the resources
     */
    public List<ChangeRequestResource> getResources() {
        return resources;
    }

    /**
     * 
     */
    public void initialize() {
        setComponent(null);
        setComponentSelect(-1);

        setModule(null);
        setModuleSelect(-1);

        setRequester(null);
        setRequestDate(null);

        setSummary(null);
        setComment(null);

        setConnectionUrl(null);
        setConnectionUrl2(null);

        clearResources();
    }

}
