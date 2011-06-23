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

    private int progressMax;
    private int progressSelection = 0;

    // generated path properties
    private String workingCopyPath;
    private String tmpPath;
    private String baseRepoUrlPath;
    private String mainStremRepoUrlPath;
    private String snapshotRepoUrlPath;

    private ViewHolder viewHolder;

    /**
     * 
     */
    public WorkPhaseContext(ChangeRequest changeRequest) {
        this.changeRequest = changeRequest;

        this.progressMax = this.changeRequest.sizeOfResources();
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
     * @return the viewHolder
     */
    public ViewHolder getViewHolder() {
        return viewHolder;
    }

    /**
     * @param viewHolder the viewHolder to set
     */
    public void setViewHolder(ViewHolder viewHolder) {
        this.viewHolder = viewHolder;
    }

    /**
     * @return the progressMax
     */
    public int getProgressMax() {
        return progressMax;
    }

    /**
     * @param progressMax the progressMax to set
     */
    public void setProgressMax(int progressMax) {
        int oldValue = this.progressMax;
        this.progressMax = progressMax;
        firePropertyChange("progressMax", oldValue, this.progressMax);
    }

    /**
     * @return the progressSelection
     */
    public int getProgressSelection() {
        return progressSelection;
    }

    /**
     * @param progressSelection the progressSelection to set
     */
    public void setProgressSelection(int progressSelection) {
        int oldValue = this.progressSelection;
        this.progressSelection = progressSelection;
        firePropertyChange("progressSelection", oldValue, this.progressSelection);
    }

    /**
     * @return the workingCopyPath
     */
    public String getWorkingCopyPath() {
        return workingCopyPath;
    }

    /**
     * @param workingCopyPath the workingCopyPath to set
     */
    public void setWorkingCopyPath(String workingCopyPath) {
        this.workingCopyPath = workingCopyPath;
    }

    /**
     * @return the tmpPath
     */
    public String getTmpPath() {
        return tmpPath;
    }

    /**
     * @param tmpPath the tmpPath to set
     */
    public void setTmpPath(String tmpPath) {
        this.tmpPath = tmpPath;
    }

    /**
     * @return the baseRepoUrlPath
     */
    public String getBaseRepoUrlPath() {
        return baseRepoUrlPath;
    }

    /**
     * @param baseRepoUrlPath the baseRepoUrlPath to set
     */
    public void setBaseRepoUrlPath(String baseRepoUrlPath) {
        this.baseRepoUrlPath = baseRepoUrlPath;
    }

    /**
     * @return the mainStremRepoUrlPath
     */
    public String getMainStremRepoUrlPath() {
        return mainStremRepoUrlPath;
    }

    /**
     * @param mainStremRepoUrlPath the mainStremRepoUrlPath to set
     */
    public void setMainStremRepoUrlPath(String mainStremRepoUrlPath) {
        this.mainStremRepoUrlPath = mainStremRepoUrlPath;
    }

    /**
     * @return the snapshotRepoUrlPath
     */
    public String getSnapshotRepoUrlPath() {
        return snapshotRepoUrlPath;
    }

    /**
     * @param snapshotRepoUrlPath the snapshotRepoUrlPath to set
     */
    public void setSnapshotRepoUrlPath(String snapshotRepoUrlPath) {
        this.snapshotRepoUrlPath = snapshotRepoUrlPath;
    }

    /**
     * 
     */
    public void initialize() {
        setPhaseName(null);
        setPhaseDescription(null);
        clearErrors();
        setProgressMax(this.changeRequest.sizeOfResources());
        setProgressSelection(0);
    }

}
