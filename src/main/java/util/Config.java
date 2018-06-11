package util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by xuchunyang on 2018/6/11 16点41分
 */
public class Config {

    private final Logger logger = LoggerFactory.getLogger(Config.class);
    private Properties properties = new Properties();

    public Config(String configFileName) throws FileNotFoundException,
            IOException {
        loadPropFile(configFileName);
    }

    public String getValue(String key, String defaultValue) {
        return this.properties.getProperty(key, defaultValue);
    }

    public String getValue(String key) {
        return this.properties.getProperty(key);
    }

    public int getIntValue(String key) {
        return getIntValue(key, 0);
    }

    public int getIntValue(String key, int defaultV) {
        String vString = getValue(key);
        if (vString == null)
            return defaultV;

        if (!(vString.trim().equals(""))) {
            int vInt = Integer.parseInt(vString.trim());
            return vInt;
        }
        return defaultV;
    }

    private void loadPropFile(String configFileName)
            throws FileNotFoundException, IOException {
        if ((StringUtils.isNotEmpty(configFileName))
                && (StringUtils.isNotBlank(configFileName))) {
            InputStream is = null;
            try {
                is = super.getClass().getResourceAsStream("/" + configFileName);
                this.properties.load(is);
            } catch (FileNotFoundException e) {
                logger.equals("errors:" + e.getLocalizedMessage()
                        + e.getStackTrace());
            } catch (IOException e) {
            } finally {
                if (is != null)
                    try {
                        is.close();
                    } catch (IOException e) {
                    }
            }
        }
    }


}
