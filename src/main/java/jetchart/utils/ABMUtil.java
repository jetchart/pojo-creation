package jetchart.utils;

public class ABMUtil {

    public static void createABM(String name, String entity, String dto, String typeId, String folderTarget) {
        folderTarget = folderTarget == null || folderTarget.isEmpty() ? "./" : folderTarget;
        folderTarget = folderTarget.endsWith("/") || folderTarget.endsWith("\\") ? folderTarget : folderTarget + "/";

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
        String path = classLoader.getResource("template/" + layer + ".java").getFile().replaceFirst("/", "");
        String layerContent = FileUtil.readFile(path);
        layerContent = layerContent.replaceAll("::NAME::", name);
        layerContent = layerContent.replaceAll("::ENTITY::", entity);
        layerContent = layerContent.replaceAll("::DTO::", dto);
        layerContent = layerContent.replaceAll("::TYPEID::", typeId);
        FileUtil.writeFile(folderTarget + name + layer + ".java", layerContent);
        System.out.println(layer + " created!");
    }

}
