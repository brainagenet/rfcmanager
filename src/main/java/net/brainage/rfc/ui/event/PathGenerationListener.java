/*
 * (#) net.brainage.rfc.ui.event.PathGenerationListener.java
 * Created on 2011. 6. 23.
 */
package net.brainage.rfc.ui.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.brainage.rfc.config.Configuration;
import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.model.WorkPhaseContext;
import net.brainage.rfc.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class PathGenerationListener implements PropertyChangeListener
{

    private static final Logger log = LoggerFactory.getLogger(PathGenerationListener.class);

    private Configuration config = Configuration.getInstance();

    private WorkPhaseContext phaseContext;

    private ChangeRequest changeRequest;

    public PathGenerationListener(WorkPhaseContext context) {
        this.phaseContext = context;
        this.changeRequest = context.getChangeRequest();
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        if ( evt.getNewValue() == null ) {
            return;
        }

        String newValue = evt.getNewValue().toString();
        if ( newValue == null || newValue.length() == 0 || "null".equals(newValue) ) {
            return;
        }

        String propertyName = evt.getPropertyName();
        if ( log.isDebugEnabled() ) {
            log.debug("@property name: {}", propertyName);
        }

        new Thread(new Runnable()
        {
            public void run() {
                // generate working copy path
                String workingCopyPath = generateWorkCopyPath();
                phaseContext.setWorkingCopyPath(workingCopyPath);
                if ( log.isDebugEnabled() ) {
                    log.debug("  - working copy path: " + workingCopyPath);
                }

                // generate tmp path
                String tmpPath = generateTmpPath();
                phaseContext.setTmpPath(tmpPath);
                if ( log.isDebugEnabled() ) {
                    log.debug("  - temporary path: " + tmpPath);
                }

                // base repository url path (trunk, branches의 상위 path)
                String baseRepoUrlPath = generateBaseRepositoryUrlPath();
                phaseContext.setBaseRepoUrlPath(baseRepoUrlPath);
                if ( log.isDebugEnabled() ) {
                    log.debug("  - base repository url path: " + baseRepoUrlPath);
                }

                // main stream repository url path
                String mainStremRepoUrlPath = generateMainStreamRepositoryUrlPath(baseRepoUrlPath);
                phaseContext.setMainStremRepoUrlPath(mainStremRepoUrlPath);
                if ( log.isDebugEnabled() ) {
                    log.debug("  - mainstream repository url path: " + mainStremRepoUrlPath);
                }

                // snapshot repository url path
                String snapshotRepoUrlPath = generateSnapshopRepositoryUrlPath(baseRepoUrlPath);
                phaseContext.setSnapshotRepoUrlPath(snapshotRepoUrlPath);
                if ( log.isDebugEnabled() ) {
                    log.debug("  - snapshot repository url path: " + snapshotRepoUrlPath);
                }
            }
        }, "PathGenerationListener").start();

    }

    private String generateWorkCopyPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(config.getString(Configuration.Key.WORKSPACE_WC));
        sb.append("/").append(changeRequest.getComponent());
        sb.append("/").append(changeRequest.getModule());
        return sb.toString();
    }

    private String generateTmpPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(config.getString(Configuration.Key.WORKSPACE_TMP));
        sb.append("/").append(changeRequest.getComponent());
        sb.append("/").append(changeRequest.getModule());
        return sb.toString();
    }

    private String generateBaseRepositoryUrlPath() {
        StringBuilder sb = new StringBuilder();
        sb.append(config.getString(Configuration.Key.SVN_PROTOCOL));
        sb.append(config.getString(Configuration.Key.SVN_HOST));
        String port = config.getString(Configuration.Key.SVN_PORT);
        if ( StringUtils.hasText(port) ) {
            sb.append(":").append(port);
        }
        sb.append("/").append(changeRequest.getComponent());
        return sb.toString();
    }

    private String generateMainStreamRepositoryUrlPath(String baseRepoUrlPath) {
        StringBuilder sb = new StringBuilder(baseRepoUrlPath);
        sb.append("/trunk/").append(changeRequest.getModule());
        return sb.toString();
    }

    private String generateSnapshopRepositoryUrlPath(String baseRepoUrlPath) {
        StringBuilder sb = new StringBuilder(baseRepoUrlPath);
        sb.append("/branches/").append(changeRequest.getModule());
        return sb.toString();
    }

}
