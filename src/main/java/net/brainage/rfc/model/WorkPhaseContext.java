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
        firePropertyChange("phaseName", oldValue, this.phaseName);
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
        firePropertyChange("phaseDescription", oldValue, this.phaseDescription);
    }

    /**
     * @param error
     */
    public void addError(ErrorDescription error) {
        this.errors.add(error);
        firePropertyChange("errors", null, this.errors);
    }

    /**
     * @param error
     */
    public void removeError(ErrorDescription error) {
        this.errors.remove(error);
        firePropertyChange("errors", null, this.errors);
    }

    /**
     * 
     */
    public void clearErrors() {
        this.errors.clear();
        firePropertyChange("errors", null, this.errors);
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
     * 
     */
    public void dispose() {
        setPhaseName(null);
        setPhaseDescription(null);
        clearErrors();
    }
    
}
