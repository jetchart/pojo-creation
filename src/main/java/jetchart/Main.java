package jetchart;

import jetchart.utils.ABMUtil;
import jetchart.utils.FileUtil;
import jetchart.utils.POJOUtil;

import java.net.URISyntaxException;
import java.util.List;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws URISyntaxException {
        System.out.println("Example command line to view help: help");
        System.out.println("Example command line to create POJO: pojo /home/jetchart/person.sql /home/jetchart/pojos/");
        System.out.println("Example command line to create ABM: abm Person PersonEntity PersonDto Long /home/jetchart/layers/ \n");
        System.out.println("Example command line to create Vue ABM: vue-abm Person /home/jetchart/layers/ \n");

        if (args != null && args.length > 0 && args[0] != null) {
            commandLine(args);
            return;
        }

        System.out.println("Select one:");
        System.out.println("\t1. POJO");
        System.out.println("\t2. ABM");
        System.out.println("\t3. Vue ABM");
        System.out.println("\t3. Help POJO");
        System.out.print("Option: ");
        Scanner scanner = new Scanner(System.in);
        String option = scanner.next();
        while (!option.equals("1") && !option.equals("2") && !option.equals("3"))
            option = scanner.next();
        if (option.equals("1"))
            POJOptions();
        if (option.equals("2"))
            ABMOptions();
        if (option.equals("3"))
            VueABMOptions();
        if (option.equals("4"))
            showHelp();
    }

    private static void VueABMOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Layers name (example: Person): ");
        String name = scanner.next();
        System.out.print("Target folder: ");
        String folder = scanner.next();
        ABMUtil.createVueABM(name, folder);
    }

    private static void commandLine(String[] args) throws URISyntaxException {
        if (args[0].toUpperCase().equals("HELP")) {
            showHelp();
        }
        if (args[0].toUpperCase().equals("POJO"))
            doPOJO(args[1], args[2]);
        if (args[0].toUpperCase().equals("ABM"))
            ABMUtil.createABM(args[1], args[2], args[3], args[4], args[5]);
    }

    private static void  showHelp() {
        System.out.println("\n\nExample: SQL Structure to be parsed\n");
        System.out.println("CREATE TABLE ORACLE.PERSON_HEADER (\n" +
                "   ID   \t\tINT           NOT NULL,\n" +
                "   FIRST_NAME \tVARCHAR (20)  NOT NULL,\n" +
                "   AGE  \t\tINT           NOT NULL,\n" +
                "   ADDRESS_ID  \tINT (25) ,\n" +
                "   SALARY   \tDECIMAL (18, 2),       \n" +
                "   PRIMARY KEY (ID),\n" +
                "   FOREIGN KEY (ADDRESS_ID) REFERENCES PERSON_ADDRESS(ID)\n" +
                ");\n");
    }

    private static void POJOptions() {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Path sql file or folder: ");
        String pathFrom = scanner.next();
        System.out.print("Target folder: ");
        String pathTo = scanner.next();
        doPOJO(pathFrom, pathTo);

    }

    private static void doPOJO(String pathFrom, String pathTo) {
        /* Find files to parse */
        List<String> filesPath = FileUtil.findFiles(pathFrom);
        System.out.println("Files to parse: " + filesPath);
        /* Create POJOS */
        filesPath.forEach(filePath -> POJOUtil.createPOJO(filePath, pathTo));
    }

    private static void ABMOptions() throws URISyntaxException {
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
