/**
 * 
 */
package net.brainage.rfc.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author exia
 *
 */
public class WorkPhaseContext extends AbstractModelObject
{

    public static class Property
    {

        public static final String PHASE_NAME = "phaseName";
        public static final String PHASE_DESC = "phaseDescription";
        public static final String ERRORS = "errors";

        private Property() {
        }
    }

    private ChangeRequest changeRequest;

    private String phaseName;
    private String phaseDescription;
    private List<ErrorDescription> errors = new ArrayList<ErrorDescription>();

    /**
     * 
     */
    public WorkPhaseContext(ChangeRequest changeRequest) {
        this.changeRequest = changeRequest;
    }

    /**
     * @return the changeRequest
     */
    public ChangeRequest getChangeRequest() {
        return changeRequest;
    }

    /**
     * @return the phaseName
     */
    public String getPhaseName() {
        return phaseName;
    }

    /**
     * @param phaseName the phaseName to set
     */
    public void setPhaseName(String phaseName) {
        String oldValue = this.phaseName;
        this.phaseName = phaseName;
        firePropertyChange(Property.PHASE_NAME, oldValue, this.phaseName);
    }

    /**
     * @return the phaseDescription
     */
    public String getPhaseDescription() {
        return phaseDescription;
    }

    /**
     * @param phaseDescription the phaseDescription to set
     */
    public void setPhaseDescription(String phaseDescription) {
        String oldValue = this.phaseDescription;
        this.phaseDescription = phaseDescription;
        firePropertyChange(Property.PHASE_DESC, oldValue, this.phaseDescription);
    }

    /**
     * @param resource
     * @param msg
     */
    public void addError(String resource, String msg) {
        ErrorDescription error = new ErrorDescription();
        error.setNo(this.sizeOfErrors() + 1);
        error.setResource(resource);
        error.setDescription(msg);

        addError(error);
    }

    /**
     * @param error
     */
    public void addError(ErrorDescription error) {
        this.errors.add(error);
        firePropertyChange(Property.ERRORS, null, this.errors);
    }

    /**
     * @param error
     */
    public void removeError(ErrorDescription error) {
        this.errors.remove(error);
        firePropertyChange(Property.ERRORS, null, this.errors);
    }

    /**
     * 
     */
    public void clearErrors() {
        this.errors.clear();
        firePropertyChange(Property.ERRORS, null, this.errors);
    }

    /**
     * @return
     */
    public int sizeOfErrors() {
        return this.errors.size();
    }

    /**
     * @return the errors
     */
    public List<ErrorDescription> getErrors() {
        return errors;
    }

    /**
     * @return
     */
    public boolean hasErrors() {
        return (this.errors.size() > 0);
    }

    /**
     * 
     */
    public void initialize() {
        setPhaseName(null);
        setPhaseDescription(null);
        clearErrors();
    }

}
