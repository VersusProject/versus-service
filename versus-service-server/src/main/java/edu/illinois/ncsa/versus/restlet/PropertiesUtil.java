/**
 *
 */
package edu.illinois.ncsa.versus.restlet;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Properties on disk in versus.properties
 *
 * @author Luigi Marini <lmarini@ncsa.illinois.edu>
 */
public class PropertiesUtil {

    public static Properties load() throws IOException {
        Properties defaultProps = new Properties();
        File props = new File("versus.properties");

        InputStream inputStream;
        if (props.exists()) {
            inputStream = new FileInputStream(props);
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.INFO, 
                    "Loading properties from versus.properties file");
        } else {
            inputStream = PropertiesUtil.class.getClassLoader()
                    .getResourceAsStream("versus.properties");
            Logger.getLogger(PropertiesUtil.class.getName()).log(Level.INFO, 
                    "Loading properties from versus.properties jar resource");
        }
        try {
            defaultProps.load(inputStream);
        } finally {
            inputStream.close();
        }

        return defaultProps;
    }
}
