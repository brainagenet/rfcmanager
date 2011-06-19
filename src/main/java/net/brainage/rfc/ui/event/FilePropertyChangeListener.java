/**
 * 
 */
package net.brainage.rfc.ui.event;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import net.brainage.rfc.model.ChangeRequest;
import net.brainage.rfc.util.ChangeRequestParser;

/**
 * @author exia
 *
 */
public class FilePropertyChangeListener implements PropertyChangeListener
{

    private static final Logger logger = LoggerFactory.getLogger(FilePropertyChangeListener.class);

    private ChangeRequest changeRequest;

    private ChangeRequestParser requestParser;

    /**
     * @param _changeRequest
     */
    public FilePropertyChangeListener(ChangeRequest _changeRequest) {
        this.changeRequest = _changeRequest;
    }

    /* (non-Javadoc)
     * @see java.beans.PropertyChangeListener#propertyChange(java.beans.PropertyChangeEvent)
     */
    public void propertyChange(PropertyChangeEvent evt) {
        if (evt.getNewValue() == null) {
            return;
        }

        final String newFilename = evt.getNewValue().toString().trim();
        if (newFilename.length() == 0) {
            return;
        }

        if (requestParser == null) {
            this.requestParser = new ChangeRequestParser(changeRequest);
        }

        new Thread(new Runnable() {
            public void run() {
                try {
                    requestParser.parse(newFilename);
                } catch (Exception ex) {
                    logger.error("요청 양식을 파싱하면서 에러가 발생했습니다.", ex);
                }
            }
        }).start();
    }

}
