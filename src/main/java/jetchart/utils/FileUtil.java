package jetchart.utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class FileUtil {

    public static List<String> findFiles(String path) {
        List<String> files = new ArrayList<>();
        File absolutePath = new File(path);
        if (absolutePath.isDirectory()) {
            files = Arrays.stream(Objects.requireNonNull(absolutePath.listFiles()))
                    .map(File::getAbsolutePath).collect(Collectors.toList())
                    .stream().filter(name -> name.endsWith(".sql")).collect(Collectors.toList());
        } else {
            files.add(path);
        }
        return files;
    }

    public static String readFile(String filePath) {
        String content = "";
        try {
            content = new String ( Files.readAllBytes( Paths.get(filePath) ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static String readResource(ClassLoader classLoader, String path) {
        InputStream in = classLoader.getResourceAsStream(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(in));
        return reader.lines().collect(Collectors.joining("\n"));
    }

    public static String readFile(Path path) {
        String content = "";
        try {
            content = new String ( Files.readAllBytes( path ) );
        }
        catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static void writeFile(String path, String content) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void createFolder(String folderPath) {
        File folder = new File(folderPath);
        if (!folder.exists())
            folder.mkdirs();
    }
}
