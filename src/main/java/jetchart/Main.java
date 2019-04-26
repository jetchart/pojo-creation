package jetchart;

import jetchart.utils.ABMUtil;
import jetchart.utils.FileUtil;
import jetchart.utils.POJOUtil;

import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("Select one:");
        System.out.println("\t1. POJO");
        System.out.println("\t2. ABM");
        System.out.print("Option: ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();
        while (!option.equals("1") && !option.equals("2"))
            option = scanner.next();
        if (option.equals("1"))
            POJOptions();
        if (option.equals("2"))
            ABMOptions();
    }

    private static void POJOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Path sql file or folder: ");
        String pathFrom = scanner.next();
        System.out.print("Target folder: ");
        String pathTo = scanner.next();

        /* Find files to parse */
        List<String> filesPath = FileUtil.findFiles(pathFrom);
        System.out.println("Files to parse: " + filesPath);
        /* Create POJOS */
        filesPath.forEach(filePath -> POJOUtil.createPOJO(filePath, pathTo));
    }

    private static void ABMOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Layers name (example: Person): ");
        String name = scanner.next();
        System.out.print("Entity name: ");
        String entity = scanner.next();
        System.out.print("Dto name: ");
        String dto = scanner.next();
        System.out.print("ID Type: ");
        String typeId = scanner.next();
        System.out.print("Target folder: ");
        String folder = scanner.next();
        ABMUtil.createABM(name, entity, dto, typeId, folder);
    }

    private static String getPathTo(String[] args) {
        return args != null && args.length > 1 && args[1] != null? args[1] : "./";
    }

    private static String getPathFrom(String[] args) {
        return args != null && args.length > 0 && args[0] != null? args[0] : "./";
    }

}
