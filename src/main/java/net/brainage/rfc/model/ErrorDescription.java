/**
 * 
 */
package net.brainage.rfc.model;

/**
 * @author exia
 *
 */
public class ErrorDescription extends AbstractModelObject
{

    public static final String PROP_ERROR_NO = "no";
    public static final String PROP_ERROR_RESOURCE = "resource";
    public static final String PROP_ERROR_DESCRIPTION = "description";

    private int no;
    private String resource;
    private String description;

    /**
     * 
     */
    public ErrorDescription() {
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
        firePropertyChange(PROP_ERROR_NO, oldValue, this.no);
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
        firePropertyChange(PROP_ERROR_RESOURCE, oldValue, this.resource);
    }

    /**
     * @return the description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description the description to set
     */
    public void setDescription(String description) {
        String oldValue = this.description;
        this.description = description;
        firePropertyChange(PROP_ERROR_DESCRIPTION, oldValue, this.description);
    }

}
