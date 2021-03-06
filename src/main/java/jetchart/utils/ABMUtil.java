package jetchart.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

public class ABMUtil {

    public static void createABM(String name, String entity, String dto, String typeId, String folderTarget) {
        folderTarget = folderTarget == null || folderTarget.isEmpty() ? "./" : folderTarget;
        folderTarget = folderTarget.endsWith("/") || folderTarget.endsWith("\\") ? folderTarget : folderTarget + "\\";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        createLayer(name, entity, dto, typeId, classLoader, "Controller", folderTarget);
        createLayer(name, entity, dto, typeId, classLoader, "Service", folderTarget);
        createLayer(name, entity, dto, typeId, classLoader, "Business", folderTarget);
        createLayer(name, entity, dto, typeId, classLoader, "Repository", folderTarget);
    }

    private static void createLayer(String name, String entity, String dto, String typeId, ClassLoader classLoader, String layer, String folderTarget) {
        if (entity == null || entity.isEmpty())
            entity = name + "Entity";
        if (dto == null || dto.isEmpty())
            dto = name + "Dto";
        if (typeId == null || typeId.isEmpty())
            typeId = "Long";

        String layerContent = FileUtil.readResource(classLoader,"template/" + layer + ".java");
        layerContent = layerContent.replaceAll("::NAME::", name);
        layerContent = layerContent.replaceAll("::ENTITY::", entity);
        layerContent = layerContent.replaceAll("::DTO::", dto);
        layerContent = layerContent.replaceAll("::TYPEID::", typeId);
        String folderPath = folderTarget + layer.toLowerCase() + "\\";
        FileUtil.createFolder(folderPath);
        FileUtil.writeFile(folderPath + name + layer + ".java", layerContent);
        System.out.println(layer + " created!");
    }

    private static void createVueLayer(String name, ClassLoader classLoader, String layer, String extension, Boolean joinLayerFileName, String folderTarget) {
        String finalName = joinLayerFileName ? layer + name : layer;
        String layerContent = FileUtil.readResource(classLoader,"template/vue/" + layer + extension);
        layerContent = layerContent.replaceAll("::name::", name);
        layerContent = layerContent.replaceAll("::name_lower::", firstLower(name));
        String folderPath = folderTarget + "\\";
        FileUtil.createFolder(folderPath);
        FileUtil.writeFile(folderPath + finalName + extension, layerContent);
        System.out.println(layer + " created!");
    }

    private static String firstLower(String name) {
        return name.substring(0, 1).toLowerCase() + name.substring(1);
    }

    public static void createVueABM(String name, String folderTarget) {
        folderTarget = folderTarget == null || folderTarget.isEmpty() ? "./" : folderTarget;
        folderTarget = folderTarget.endsWith("/") || folderTarget.endsWith("\\") ? folderTarget : folderTarget + "\\";

        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        createVueLayer(name, classLoader, "Container", ".vue", true, folderTarget);
        createVueLayer(name, classLoader, "Lista", ".vue", true, folderTarget);
        createVueLayer(name, classLoader, "Detalle", ".vue", true, folderTarget);
        createVueLayer(name, classLoader, "Alta", ".vue", true, folderTarget);
        createVueLayer(name, classLoader, "Service", ".js", true, folderTarget);
        createVueLayer(name, classLoader, "router", ".js", false, folderTarget);
    }
}
