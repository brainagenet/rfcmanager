/*
 * (#) net.brainage.rfc.ui.event.ConnectionUrlChangeListener.java
 * Created on 2011. 6. 20.
 */
package net.brainage.rfc.ui.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import net.brainage.rfc.config.Configuration;
import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.util.StringUtils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 *
 * @author ms29.seo@gmail.com
 * @version 1.0
 */
public class ConnectionUrlChangeListener implements PropertyChangeListener
{

    /**
     * 
     */
    private static final Logger log = LoggerFactory.getLogger(ConnectionUrlChangeListener.class);

    /**
     * 
     */
    private ChangeRequest changeRequest;

    /**
     * @param _changeRequest
     */
    public ConnectionUrlChangeListener(ChangeRequest _changeRequest) {
        this.changeRequest = _changeRequest;
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if ( evt.getNewValue() == null ) {
            return;
        }

        final String newValue = evt.getNewValue().toString().trim();
        if ( StringUtils.isEmpty(newValue) ) {
            return;
        }

        new Thread(new Runnable()
        {
            public void run() {
                changeRequest.setConnectionUrl(getConnectionUrl("branches"));
                changeRequest.setConnectionUrl2(getConnectionUrl("trunk"));
            }

            public String getConnectionUrl(String type) {
                Configuration config = Configuration.getInstance();
                String protocol = config.getString(Configuration.Key.SVN_PROTOCOL);
                StringBuffer buf = new StringBuffer(protocol);
                buf.append(config.getString(Configuration.Key.SVN_HOST));
                String port = config.getString(Configuration.Key.SVN_PORT);
                if ( StringUtils.hasText(port) ) {
                    buf.append(":").append(port);
                }
                buf.append("/").append(changeRequest.getComponent());
                buf.append("/").append(type).append("/").append(changeRequest.getModule());

                if ( log.isDebugEnabled() ) {
                    log.debug("connection url: {}", buf.toString());
                }
                return buf.toString();
            }
        }).start();
    }

}
