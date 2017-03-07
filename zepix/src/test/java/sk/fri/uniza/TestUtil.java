package sk.fri.uniza;

import java.io.File;

public class TestUtil {

    public static File loadFileWithName(String fileName) {
        ClassLoader classLoader = TestUtil.class.getClassLoader();
        return new File(classLoader.getResource(fileName).getFile());
    }
}
