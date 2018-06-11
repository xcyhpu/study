package util;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by xuchunyang on 2018/6/11 16点40分
 */
public class ConfigManager {


    private static Logger logger = LoggerFactory.getLogger(ConfigManager.class);

    public static Config getConfig(String fileName) {
        Config vObj = null;
        try {
            vObj = new Config(fileName);
        } catch (FileNotFoundException fnfe) {
            logger.warn("获得【" + fileName + "】配置信息的时候发生异常！", fnfe);
        } catch (IOException ioe) {
            logger.warn("获得【" + fileName + "】配置信息的时候发生异常！", ioe);
        }
        return vObj;
    }

    private static long getFileLastModified(String fileName)
            throws FileNotFoundException {
        if ((StringUtils.isNotEmpty(fileName))
                && (StringUtils.isNotBlank(fileName))) {
            File file = new File(fileName);
            if (!(file.exists()))
                throw new FileNotFoundException();

            if (file.isDirectory())
                throw new FileNotFoundException();

            return file.lastModified();
        }
        throw new FileNotFoundException();
    }


}
