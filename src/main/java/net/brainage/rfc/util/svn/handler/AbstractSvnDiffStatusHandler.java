/*
 * (#) net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler.java
 * Created on 2011. 6. 22.
 */
package net.brainage.rfc.util.svn.handler;

import net.brainage.rfc.util.StringUtils;

import org.tmatesoft.svn.core.wc.ISVNDiffStatusHandler;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public abstract class AbstractSvnDiffStatusHandler implements ISVNDiffStatusHandler
{

    private char modificationType = ' ';
    private int modificationId = -1;
    private String modificationText = StringUtils.EMPTY;

    /**
     * @return the modificationType
     */
    public char getModificationType() {
        return modificationType;
    }

    /**
     * @param modificationType the modificationType to set
     */
    public void setModificationType(char modificationType) {
        this.modificationType = modificationType;
    }

    /**
     * @return the modificationId
     */
    public int getModificationId() {
        return modificationId;
    }

    /**
     * @param modificationId the modificationId to set
     */
    public void setModificationId(int modificationId) {
        this.modificationId = modificationId;
    }

    /**
     * @return the modificationText
     */
    public String getModificationText() {
        return modificationText;
    }

    /**
     * @param modificationText the modificationText to set
     */
    public void setModificationText(String modificationText) {
        this.modificationText = modificationText;
    }

}
