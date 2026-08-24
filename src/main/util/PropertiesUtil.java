package main.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class PropertiesUtil {

    public static final Properties PROPERTIES = new Properties();

    static {
        try {
            loadProperties();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private PropertiesUtil() {
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() throws IOException {
        try(InputStream inputStream = PropertiesUtil.class.getClassLoader().
                getResourceAsStream("application.properties")){
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException (e);
        }

    }

}
