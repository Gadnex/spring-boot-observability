package net.binarypaper.productservice;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClasspathProperties {

    public static Properties load(String fileName) {
        try (InputStream input = DataIntegrityViolationExceptionHandler.class.getClassLoader()
                .getResourceAsStream(fileName)) {
            if (input == null) {
                throw new RuntimeException("Could not load " + fileName);
            }
            Properties properties = new Properties();
            properties.load(input);
            return properties;
        } catch (IOException ex) {
            throw new RuntimeException("Could not read the file " + fileName);
        }
    }

}
