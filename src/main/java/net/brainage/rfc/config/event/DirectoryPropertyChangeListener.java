/*
 * (#) net.brainage.rfc.config.event.DirectoryPropertyChangeListener.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.config.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;

import net.brainage.rfc.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class DirectoryPropertyChangeListener implements PropertyChangeListener
{

    private static final Logger log = LoggerFactory
            .getLogger(DirectoryPropertyChangeListener.class);

    private static int next = 0;

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    @Override
    public void propertyChange(PropertyChangeEvent evt) {
        Object newValue = evt.getNewValue();
        if (newValue == null) {
            return;
        }

        final String newDirPath = newValue.toString().trim();
        if (StringUtils.isEmpty(newDirPath)) {
            return;
        }

        if (log.isInfoEnabled()) {
            String propertyName = evt.getPropertyName();
            Object oldValue = evt.getOldValue();
            log.info(">> changed '{}' property value", propertyName);
            log.info("    - old value: {}", oldValue.toString());
            log.info("    - new value: {}", newValue.toString());
        }

        new Thread(new Runnable() {
            public void run() {
                File targetDir = new File(newDirPath);
                if (targetDir.exists() == false) {
                    targetDir.mkdirs();
                }
            }
        }, "DirectoryListenerThread-" + getNext()).start();
    }

    /**
     * @return
     */
    protected static int getNext() {
        return ++next;
    }

}
