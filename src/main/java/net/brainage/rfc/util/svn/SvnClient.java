/*
 * (#) net.brainage.rfc.util.svn.SvnClient.java
 * Created on 2011. 6. 10.
 */
package net.brainage.rfc.util.svn;

import java.io.File;

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
    
    public void checkout(String urlPath, File destPath) throws SVNException;
    
    public void checkout(String urlPath, File destPath, ISVNEventHandler handler) throws SVNException;

    public boolean isVersionedDirectory(String path);

    public boolean isVersionedDirectory(File path);

    public boolean isWorkingCopyRoot(String rootPath) throws SVNException;

    public boolean isWorkingCopyRoot(File rootPath) throws SVNException;
}
