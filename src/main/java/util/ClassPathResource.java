package util;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author wsz
 * @create 2021-12-30 16:21
 */
public class ClassPathResource {
    private final String path;
    private ClassLoader classLoader;
    private Class<?> clazz;

    public ClassPathResource(String path, ClassLoader classLoader) {
        if (path.startsWith("/")) {
            path = path.substring(1);
        }
        this.path = path;
        this.classLoader = (classLoader != null ? classLoader : getDefaultClassLoader());
    }

    public ClassPathResource(String path) {
        this(path, (ClassLoader) null);
    }

    public final String getPath() {
        return this.path;
    }

    public InputStream getInputStream() throws IOException {
        InputStream is;
        if (this.clazz != null) {
            is = this.clazz.getResourceAsStream(this.path);
        } else if (this.classLoader != null) {
            is = this.classLoader.getResourceAsStream(this.path);
        } else {
            is = ClassLoader.getSystemResourceAsStream(this.path);
        }
        if (is == null) {
            throw new FileNotFoundException("文件不存在");
        }
        return is;
    }

    public static ClassLoader getDefaultClassLoader() {
        ClassLoader cl = null;
        try {
            cl = Thread.currentThread().getContextClassLoader();
        } catch (Throwable ex) {
            // Cannot access thread context ClassLoader - falling back...
        }
        if (cl == null) {
            // No thread context class loader -> use class loader of this class.
            cl = ClassPathResource.class.getClassLoader();
            if (cl == null) {
                // getClassLoader() returning null indicates the bootstrap ClassLoader
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (Throwable ex) {
                    // Cannot access system ClassLoader - oh well, maybe the caller can live with null...
                }
            }
        }
        return cl;
    }


    public static void getProperty() throws IOException {
        //获取输入流
        ClassPathResource resource = new ClassPathResource("/test.properties");
        InputStream in = resource.getInputStream();
        System.out.println(resource.path);
        Properties p = new Properties();
        p.load(in);

        String name = p.getProperty("user.name");
        System.out.println(name);
    }
//    public static void main(String[] args) throws IOException {
//        getProperty();
//    }

}

