/**
 * 
 */
package net.brainage.rfc.model;

/**
 * @author exia
 *
 */
public class ChangeRequestResource extends AbstractModelObject
{

    private int no;
    private String resource;
    private long revision;
    private String type;
    private String status;
    private char modType;

    /**
     * 
     */
    public ChangeRequestResource() {
    }

    /**
     * @return the no
     */
    public int getNo() {
        return no;
    }

    /**
     * @param no the no to set
     */
    public void setNo(int no) {
        int oldValue = this.no;
        this.no = no;
        firePropertyChange("no", oldValue, this.no);
    }

    /**
     * @return the resource
     */
    public String getResource() {
        return resource;
    }

    /**
     * @param resource the resource to set
     */
    public void setResource(String resource) {
        String oldValue = this.resource;
        this.resource = resource;
        firePropertyChange("resource", oldValue, this.resource);
    }

    /**
     * @return the revision
     */
    public long getRevision() {
        return revision;
    }

    /**
     * @param revision the revision to set
     */
    public void setRevision(long revision) {
        long oldValue = this.revision;
        this.revision = revision;
        firePropertyChange("revision", oldValue, this.revision);
    }

    /**
     * @return the type
     */
    public String getType() {
        return type;
    }

    /**
     * @param type the type to set
     */
    public void setType(String type) {
        String oldValue = this.type;
        this.type = type;
        firePropertyChange("type", oldValue, this.type);
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

    /**
     * @return the modType
     */
    public char getModType() {
        return modType;
    }

    /**
     * @param modType the modType to set
     */
    public void setModType(char modType) {
        this.modType = modType;
    }

}
