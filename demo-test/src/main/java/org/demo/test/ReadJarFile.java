package org.demo.test;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class ReadJarFile {

    public static void main(String[] args) throws Exception{
        ClassLoader classLoader = ReadJarFile.class.getClassLoader();
        Enumeration<URL> urls = classLoader.getResources("META-INF/spring.factories");
        while (urls.hasMoreElements()) {
            URL url = urls.nextElement();
            Properties props = new Properties();
            props.load(url.openConnection().getInputStream());
            System.out.println(url.getFile()+"--"+props.size());
//            System.out.println(url.openConnection().getInputStream().read());
            InputStream inputStream = url.openConnection().getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
            System.out.println(reader.readLine());
        }
    }
}
