/*
 * (#) net.brainage.rfc.util.svn.handler.DefaultSvnDiffStatusHandler.java
 * Created on 2011. 6. 22.
 */
package net.brainage.rfc.util.svn.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.SVNNodeKind;
import org.tmatesoft.svn.core.wc.SVNDiffStatus;
import org.tmatesoft.svn.core.wc.SVNStatusType;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class DefaultSvnDiffStatusHandler extends AbstractSvnDiffStatusHandler
{

    /**
     * 
     */
    private static final Logger log = LoggerFactory.getLogger(DefaultSvnDiffStatusHandler.class);

    /* (non-Javadoc)
     * @see org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler#handleDiffStatus(org.tmatesoft.svn.core.wc.SVNDiffStatus)
     */
    public void handleDiffStatus(SVNDiffStatus diffStatus) throws SVNException {
        if ( SVNNodeKind.FILE == diffStatus.getKind() ) {
            if ( log.isDebugEnabled()) {
                String path = diffStatus.getURL().getPath();
                log.debug("path: {}", path);
                SVNStatusType statusType = diffStatus.getModificationType();
                log.debug("  - id  : {}", statusType.getID());
                log.debug("  - name: {}", statusType.toString());
                log.debug("  - code: {}", statusType.getCode());
            }
            setModificationId(diffStatus.getModificationType().getID());
            setModificationText(diffStatus.getModificationType().toString());
            setModificationType(diffStatus.getModificationType().getCode());
        }
    }


}
