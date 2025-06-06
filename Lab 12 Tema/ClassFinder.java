import java.io.File;
import java.io.IOException;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.stream.Stream;

public class ClassFinder {
    public static List<String> findClasses(File input) throws IOException {
        if (input.isDirectory()) {
            return findClassesInDirectory(input);
        } else if (input.getName().endsWith(".jar")) {
            return findClassesInJar(input);
        } else if (input.getName().endsWith(".class")) {
            return List.of(extractClassNameFromFile(input));
        }
        throw new IllegalArgumentException("Unsupported input type: " + input);
    }

    private static List<String> findClassesInDirectory(File directory) throws IOException {
        List<String> classNames = new ArrayList<>();
        Path start = directory.toPath();
        
        try (Stream<Path> stream = Files.walk(start)) {
            stream.filter(Files::isRegularFile)
                  .filter(path -> path.toString().endsWith(".class"))
                  .forEach(path -> {
                      String relative = start.relativize(path).toString();
                      String className = relative.replace(File.separatorChar, '.')
                                                .replace(".class", "");
                      classNames.add(className);
                  });
        }
        return classNames;
    }

    private static List<String> findClassesInJar(File jarFile) throws IOException {
        List<String> classNames = new ArrayList<>();
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                if (name.endsWith(".class")) {
                    String className = name.replace('/', '.')
                                          .substring(0, name.length() - 6);
                    classNames.add(className);
                }
            }
        }
        return classNames;
    }

    private static String extractClassNameFromFile(File classFile) {
        String path = classFile.getAbsolutePath();
        int classIndex = path.lastIndexOf(".class");
        if (classIndex == -1) throw new IllegalArgumentException("Not a .class file");
        return path.substring(0, classIndex).replace(File.separatorChar, '.');
    }
}