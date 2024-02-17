package ltd.weiyiyi.mybatis.io;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;


/**
 * @author Wei Han
 * @description
 * @date 2024/2/17 12:56
 * @domain www.weiyiyi.ltd
 */
public class Resources {

    /**
     * xxx_mapper.xml -> io reader
     *
     * @param resource mapper.xml path
     * @return io reader
     * @throws IOException not found file
     */
    public static Reader getSourceAsReader(String resource) throws IOException {
        return new InputStreamReader(getResourceAsStream(resource));
    }

    private static InputStream getResourceAsStream(String resource) throws IOException {
        ClassLoader[] classLoaders = getClassLoaders();
        for(ClassLoader classLoader : classLoaders) {
            InputStream resourceAsStream = classLoader.getResourceAsStream(resource);
            if(resourceAsStream != null) {
                return resourceAsStream;
            }
        }
        throw new IOException("Could not found resource " + resource);
    }

    private static ClassLoader[] getClassLoaders() {
        return new ClassLoader[] {
                ClassLoader.getSystemClassLoader(),
                Thread.currentThread().getContextClassLoader()
        };
    }

    /**
     * Loads a class
     *
     * @param className - the class to fetch
     * @return The loaded class
     * @throws ClassNotFoundException If the class cannot be found
     */
    public static Class<?> classForName(String className) throws ClassNotFoundException {
        return Class.forName(className);
    }
}
