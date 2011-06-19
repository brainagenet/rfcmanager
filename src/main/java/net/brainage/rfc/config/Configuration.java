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
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author exia
 *
 */
public class Configuration
{

    private static final Logger log = LoggerFactory.getLogger(Configuration.class);

    private static final Configuration _INSTANCE = new Configuration();

    private Properties props = new Properties();
    private String filename = "./config/settings.conf";

    public static Configuration getInstance() {
        return _INSTANCE;
    }

    private Configuration() {
        try {
            initialize();
        } catch (IOException e) {
            log.error("설정 파일을 로딩하는 중에 오류가 발생했습니다.",e);
        }
    }

    public String getString(String key) {
        return props.getProperty(key, "");
    }

    public String getString(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }

    public long getInt(String key) {
        String v = props.getProperty(key, "0");
        return Integer.parseInt(v);
    }

    public long getInt(String key, int defaultValue) {
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
        props.put(key, value);
    }
    
    public void setInt(String key, int value) {
        props.put(key, Integer.toString(value));
    }
    
    public void setLong(String key, long value) {
        props.put(key, Long.toString(value));
    }
    
    

    public void store() {
        OutputStream out = null;
        try {
            out = new BufferedOutputStream(new FileOutputStream(new File(filename)));
            props.store(out, "PCW\\/pCloud Change Request Manager Settings");
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
