package jetchart.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
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

    public static void writeFile(String path, String content) {
        try {
            FileWriter myWriter = new FileWriter(path);
            myWriter.write(content);
            myWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
