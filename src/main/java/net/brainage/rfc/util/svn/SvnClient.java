/*
 * (#) net.brainage.rfc.util.svn.SvnClient.java
 * Created on 2011. 6. 10.
 */
package net.brainage.rfc.util.svn;

import java.io.File;
import java.util.Collection;
import java.util.List;

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
     * @param file
     * @param collection
     * @throws SVNException
     */
    public void add(File file, Collection<File> collection) throws SVNException;

    /**
     * @param file
     * @throws SVNException
     */
    public void add(File file) throws SVNException;

    /**
     * @param entries
     * @param commitComment
     * @throws SVNException
     */
    public void commit(List<File> entries, String commitComment) throws SVNException;

    /**
     * @param entries
     * @param commitComment
     * @throws SVNException
     */
    public void commit(File[] entries, String commitComment) throws SVNException;

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

    public void getFile(String repoUrlPath, String urlPath, long revision, String targetPath)
            throws SVNException;

    public void getFile(String repoUrlPath, String urlPath, long revision, File target)
            throws SVNException;

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

    /**
     * @param target
     * @throws SVNException
     */
    public void delete(File target) throws SVNException;

}
