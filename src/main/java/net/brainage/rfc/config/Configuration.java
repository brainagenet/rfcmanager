/**
 * 
 */
package net.brainage.rfc.config;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author exia
 *
 */
public class Configuration
{

    public static final class Key
    {

        public static final String SVN_PROTOCOL = "svn.protocol";
        public static final String SVN_PROTOCOL_SELECT = "svn.protocol.select";
        public static final String SVN_HOST = "svn.host";
        public static final String SVN_PORT = "svn.port";
        public static final String SVN_AUTH_USERNAME = "svn.auth.username";
        public static final String SVN_AUTH_PASSWORD = "svn.auth.password";
        public static final String WORKSPACE_WC = "workspace.wcdir";
        public static final String WORKSPACE_TMP = "workspace.tmpdir";

        private Key() {
        }
    }

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private static final Configuration _INSTANCE = new Configuration();

    private boolean dirty = false;

    private Properties props = new Properties();
    private String filename = "./config/settings.conf";

    public static Configuration getInstance() {
        return _INSTANCE;
    }

    private Configuration() {
        try {
            initialize();
        } catch (IOException e) {
            log.error("설정 파일을 로딩하는 중에 오류가 발생했습니다.", e);
        }
    }

    public String getString(String key) {
        return props.getProperty(key, "");
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public int getInt(String key) {
        String v = props.getProperty(key, "0");
        return Integer.parseInt(v);
    }

    public int getInt(String key, int defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.length() == 0)
            return defaultValue;
        return Integer.parseInt(value);
    }

    public long getLong(String key) {
        String v = props.getProperty(key, "0");
        return Long.parseLong(v);
    }

    public long getLong(String key, long defaultValue) {
        String value = props.getProperty(key);
        if (value == null || value.length() == 0)
            return defaultValue;
        return Long.parseLong(value);
    }

    public void setString(String key, String value) {
        setProperty(key, value);
    }

    public void setInt(String key, int value) {
        setProperty(key, Integer.toString(value));
    }

    public void setLong(String key, long value) {
        setProperty(key, Long.toString(value));
    }

    /**
     * @param t
     * @see java.util.Hashtable#putAll(java.util.Map)
     */
    public void putAll(Map<String, String> t) {
        for (String key : t.keySet()) {
            String value = t.get(key);
            setProperty(key, value);
        }
    }

    /**
     * @param key
     * @param value
     * @return
     * @see java.util.Properties#setProperty(java.lang.String, java.lang.String)
     */
    private void setProperty(String key, String value) {
        String oldValue = props.getProperty(key);
        if (oldValue.equals(value)) {
            return;
        }
        props.setProperty(key, value);

        if (log.isDebugEnabled()) {
            log.debug(">> set property value for '{}'", key);
            log.debug("    - old value: {}", oldValue);
            log.debug("    - new value: {}", value);
        }
        dirty = true;
    }

    /**
     * @param key
     * @return
     * @see java.util.Hashtable#containsKey(java.lang.Object)
     */
    public boolean containsKey(Object key) {
        return props.containsKey(key);
    }

    public void store() {
        if (isDirty() == false) {
            return;
        }
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(new File(filename)));
            props.store(out, "PCW/pCloud Change Request Manager Settings");
            out.flush();
        } catch (IOException ioex) {
            log.error("설정을 저장하는 중에 오류가 발생했습니다.", ioex);
        } finally {
            if (out != null) {
                try {
                    out.close();
                    out = null;
                } catch (IOException e) {
                }
            }
        }
    }

    /**
     * @return the dirty
     */
    protected boolean isDirty() {
        return dirty;
    }

    private void initialize() throws IOException {
        InputStream in = null;
        try {
            in = new BufferedInputStream(new FileInputStream(new File(filename)));
            props.load(in);
        } finally {
            if (in != null) {
                in.close();
                in = null;
            }
        }

    }

}
