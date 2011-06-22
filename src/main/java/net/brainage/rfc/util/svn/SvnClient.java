/*
 * (#) net.brainage.rfc.util.svn.SvnClient.java
 * Created on 2011. 6. 10.
 */
package net.brainage.rfc.util.svn;

import java.io.File;

import net.brainage.rfc.util.svn.handler.AbstractSvnDiffStatusHandler;

import org.tmatesoft.svn.core.SVNException;
import org.tmatesoft.svn.core.wc.ISVNEventHandler;

/**
 * 
 * 
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public interface SvnClient
{

    /**
     * @param urlPath
     * @param destPath
     * @throws SVNException
     */
    public void checkout(String urlPath, String destPath) throws SVNException;

    /**
     * @param urlPath
     * @param destPath
     * @throws SVNException
     */
    public void checkout(String urlPath, File destPath) throws SVNException;

    /**
     * @param urlPath
     * @param destPath
     * @param handler
     * @throws SVNException
     */
    public void checkout(String urlPath, File destPath, ISVNEventHandler handler)
            throws SVNException;

    public char diffStatus(String urlPath1, long urlPath1Revision, String urlPath2,
            AbstractSvnDiffStatusHandler handler) throws SVNException;

    /**
     * @param path
     * @return
     */
    public boolean isVersionedDirectory(String path);

    /**
     * @param path
     * @return
     */
    public boolean isVersionedDirectory(File path);

    /**
     * @param rootPath
     * @return
     * @throws SVNException
     */
    public boolean isWorkingCopyRoot(String rootPath) throws SVNException;

    /**
     * @param rootPath
     * @return
     * @throws SVNException
     */
    public boolean isWorkingCopyRoot(File rootPath) throws SVNException;

}
