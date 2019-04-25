package jetchart;

import jetchart.utils.FileUtil;
import jetchart.utils.POJOUtil;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        /* SQL File path (optional) */
        String pathFrom = getPathFrom(args);
        System.out.println("Path from: " + pathFrom);
        /* Folder target (optional) */
        String pathTo = getPathTo(args);
        System.out.println("Path to: " + pathTo);
        /* Find files to parse */
        List<String> filesPath = FileUtil.findFiles(pathFrom);
        System.out.println("Files to parse: " + filesPath);
        /* Create POJOS */
        filesPath.forEach(filePath -> POJOUtil.createPOJO(filePath, pathTo));
    }

    private static String getPathTo(String[] args) {
        return args != null && args.length > 1 && args[1] != null? args[1] : "./";
    }

    private static String getPathFrom(String[] args) {
        return args != null && args.length > 0 && args[0] != null? args[0] : "./";
    }

}
