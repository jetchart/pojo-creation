package jetchart.utils;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POJOUtil {

    public static void createPOJO(String pathFrom, String pathTo) {
        if (!pathTo.endsWith("/") && !pathTo.endsWith("\\"))
            pathTo += "\\";

        StringBuffer pojo = new StringBuffer();
        String createSQL = parseCustom(FileUtil.readFile(pathFrom));
        try {

            writePackageSection(pojo);
            writeImportSection(pojo);

            Statement createTable = CCJSqlParserUtil.parse(createSQL);
            String tableName = ((CreateTable) createTable).getTable().getName();
            System.out.println("Parsing: " + tableName);
            List<String> keys = getPrimaryKey(((CreateTable) createTable).getIndexes());
            Map<String, String> foreignsKey = getForeignsKey(((CreateTable) createTable).getIndexes());
            Map<String, String> propertyNameforeignsKey = getPropertyNameForeignsKey(((CreateTable) createTable).getIndexes());
            writeHeaderClassSection(pojo, (CreateTable) createTable, tableName);

            /* Fields definition */
            for (ColumnDefinition col: ((CreateTable) createTable).getColumnDefinitions()) {
                String type = getType(col.getColDataType());
                String precisionScale = getPrecisionScale(type, col.getColDataType().getArgumentsStringList());
                String length = getLength(type, col.getColDataType().getArgumentsStringList());
                /* Write POJO */
                writeIdAnnotation(pojo, keys, col);
                writeNotNullableAnnotation(pojo, col);
                writeFieldDefinition(pojo, keys, foreignsKey, propertyNameforeignsKey, col, type, precisionScale, length);
            }

            /* Fields setter & getter */
            for (ColumnDefinition col: ((CreateTable) createTable).getColumnDefinitions()) {
                String type = getType(col.getColDataType());
                String className = convertToCamelCase(col.getColumnName(), "_", true, "");
                String propertyName = convertToCamelCase(col.getColumnName(), "_", false, "");
                /* Write POJO */
                writeGetterMethod(pojo, keys, foreignsKey, propertyNameforeignsKey, col, type, className, propertyName);
                writeSetterMethod(pojo, keys, foreignsKey, propertyNameforeignsKey, col, type, className, propertyName);
            }

            writeEndClassSection(pojo);

            String fileName = convertToCamelCase(tableName, "_", true, "") + "Entity.java";
            FileUtil.writeFile(pathTo + fileName, pojo.toString());
            System.out.println("POJO created: " + fileName);

        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    private static String parseCustom(String createSQL) {
        createSQL = createSQL.replaceAll("\"", "")
                .replaceAll(" ENABLE,", ",")
                .replaceAll("\\) ENABLE", ")");

        if (createSQL.contains("USING INDEX")) {
            Integer i = createSQL.indexOf("USING INDEX");
            if (createSQL.indexOf(",", i) != -1) {
                Integer comaIndex = createSQL.indexOf(",", i);
                createSQL = createSQL.replace(createSQL.substring(i, comaIndex), "");
            } else {
                Integer comaIndex = createSQL.indexOf(";", i);
                createSQL = createSQL.replace(createSQL.substring(i, comaIndex), ")");
            }
        }
        if (createSQL.contains("PCTFREE")) {
            Integer i = createSQL.indexOf("PCTFREE");
            Integer comaIndex = createSQL.indexOf(";", i);
            createSQL = createSQL.replace(createSQL.substring(i, comaIndex), "");
        }
        if (createSQL.contains("CREATE UNIQUE")) {
            Integer i = createSQL.indexOf("CREATE UNIQUE");
            Integer comaIndex = createSQL.indexOf(";", i);
            createSQL = createSQL.replace(createSQL.substring(i, comaIndex + 1), "");
        }
        if (createSQL.contains("CREATE INDEX")) {
            Integer i = createSQL.indexOf("CREATE INDEX");
            Integer comaIndex = createSQL.indexOf(";", i);
            createSQL = createSQL.replace(createSQL.substring(i, comaIndex + 1), "");
        }
        if (createSQL.contains("COMMENT ON")) {
            Integer i = createSQL.indexOf("COMMENT ON");
            Integer comaIndex = createSQL.indexOf(";", i);
            createSQL = createSQL.replace(createSQL.substring(i, comaIndex + 1), "");
        }
        return createSQL;
    }

    public static Boolean existsValue(String value, Map<String, String> map) {
        return map.values().stream().anyMatch(v -> v.equals(value));
    }

    public static String getClassNameNumber(String value, Map<String, String> map) {
        if (!existsValue(value, map))
            return value;
        Integer i = 1;
        while (existsValue(value, map)) {
            i++;
            value = value + i.toString();
        }
        return value;
    }


    public static void writeGetterMethod(StringBuffer pojo, List<String> keys, Map<String, String> foreignsKey, Map<String, String> propertyNameforeignsKey, ColumnDefinition col, String type, String className, String propertyName) {
        if (foreignsKey.containsKey(col.getColumnName())) {
            String referencesClassName = convertToCamelCase(foreignsKey.get(col.getColumnName()), "_", true, "");
            String propertyNameFKUpper = convertToCamelCase(propertyNameforeignsKey.get(col.getColumnName()), "_", true, "");
            String propertyNameFKLower = convertToCamelCase(propertyNameforeignsKey.get(col.getColumnName()), "_", false, "");
            pojo.append("\tpublic ").append(referencesClassName).append("Entity get").append(propertyNameFKUpper).append("() {\n");
            pojo.append("\t\treturn this.").append(propertyNameFKLower).append(";\n");
            pojo.append("\t}\n\n");
        } else {
            Boolean isSingleKey = false;
            if (keys.size() == 1 && keys.contains(col.getColumnName()))
                isSingleKey = true;
            pojo.append("\tpublic ").append(type).append(" get").append(isSingleKey? "Id" : className).append("() {\n");
            pojo.append("\t\treturn this.").append(isSingleKey? "id" : propertyName).append(";\n");
            pojo.append("\t}\n\n");
        }
    }

    public static void writeSetterMethod(StringBuffer pojo, List<String> keys, Map<String, String> foreignsKey, Map<String, String> propertyNameforeignsKey, ColumnDefinition col, String type, String className, String propertyName) {
        if (foreignsKey.containsKey(col.getColumnName())) {
            String referencesClassName = convertToCamelCase(foreignsKey.get(col.getColumnName()), "_", true, "");
            String propertyNameFKUpper = convertToCamelCase(propertyNameforeignsKey.get(col.getColumnName()), "_", true, "");
            String propertyNameFKLower = convertToCamelCase(propertyNameforeignsKey.get(col.getColumnName()), "_", false, "");
            pojo.append("\tpublic void ").append("set").append(propertyNameFKUpper).append(" (").append(referencesClassName).append("Entity ").append(propertyName).append(") {\n");
            pojo.append("\t\tthis.").append(propertyNameFKLower).append(" = ").append(propertyNameFKLower).append(";\n");
            pojo.append("\t}\n\n");
        } else {
            Boolean isSingleKey = false;
            if (keys.size() == 1 && keys.contains(col.getColumnName()))
                isSingleKey = true;
            pojo.append("\tpublic void ").append("set").append(isSingleKey? "Id" : className).append("(").append(type).append(" ").append(isSingleKey? "id" : propertyName).append(") {\n");
            pojo.append("\t\tthis.").append(isSingleKey? "id" : propertyName).append(" = ").append(isSingleKey? "id" : propertyName).append(";\n");
            pojo.append("\t}\n\n");
        }
    }

    public static void writeEndClassSection(StringBuffer pojo) {
        pojo.append("}");
    }

    public static void writeFieldDefinition(StringBuffer pojo, List<String> keys, Map<String, String> foreignsKey, Map<String, String> propertyNameforeignsKey, ColumnDefinition col, String type, String precisionScale, String length) {
        if (foreignsKey.containsKey(col.getColumnName())) {
            String referencesClassName = convertToCamelCase(foreignsKey.get(col.getColumnName()), "_", true, "");
            String propertyNameFKLower = convertToCamelCase(propertyNameforeignsKey.get(col.getColumnName()), "_", false, "");
            pojo.append("\t@ManyToOne\n");
            pojo.append("\t@JoinColumn(name = \"" + col.getColumnName() + "\")\n");
            pojo.append("\tprivate ").append(referencesClassName).append("Entity ").append(propertyNameFKLower).append(";\n\n");
        } else {
            Boolean isSingleKey = false;
            if (keys.size() == 1 && keys.contains(col.getColumnName()))
                isSingleKey = true;
            pojo.append("\t@Column(name = \"" + col.getColumnName() + "\"" + precisionScale + length + ")\n");
            pojo.append("\tprivate ").append(type).append(" ").append(convertToCamelCase(isSingleKey? "id" : col.getColumnName(), "_", false, "")).append(";\n\n");
        }
    }

    public static void writeNotNullableAnnotation(StringBuffer pojo, ColumnDefinition col) {
        if (ifNotNullable(col.getColumnSpecStrings()))
            pojo.append("\t@NotNull\n");
    }

    public static void writeIdAnnotation(StringBuffer pojo, List<String> keys, ColumnDefinition col) {
        if (keys.contains(col.getColumnName()))
            pojo.append("\t@Id\n");
    }

    public static void writeHeaderClassSection(StringBuffer pojo, CreateTable createTable, String tableName) {
        pojo.append("@Entity\n");
        pojo.append("@Table(name = \"" + tableName + "\", schema = \"" + createTable.getTable().getSchemaName() + "\")\n");
        pojo.append("public class ").append(convertToCamelCase(tableName, "_", true, "")).append("Entity {\n\n");
    }

    public static void writeImportSection(StringBuffer pojo) {
        pojo.append("import javax.persistence.*;\nimport javax.validation.constraints.NotNull;\nimport java.util.Date;\n\n");
    }

    public static void writePackageSection(StringBuffer pojo) {
        pojo.append("package FILL_PACKAGE_PATH_HERE;\n\n");
    }

    public static Map<String, String> getForeignsKey(List<Index> indexes) {
        Map<String, String> fkMap = new HashMap<>();
        for (Index index : indexes) {
            if (index.getType().toUpperCase().equals("FOREIGN KEY")) {
                fkMap.put(index.getColumnsNames().get(0), getReferencesTable(index));
            }
        }
        return fkMap;
    }

    public static Map<String, String> getPropertyNameForeignsKey(List<Index> indexes) {
        Map<String, String> fkMap = new HashMap<>();
        for (Index index : indexes) {
            if (index.getType().toUpperCase().equals("FOREIGN KEY")) {
                String propertyName = getClassNameNumber(getReferencesTable(index), fkMap);
                fkMap.put(index.getColumnsNames().get(0), propertyName);
            }
        }
        return fkMap;
    }

    private static String getReferencesTable(Index index) {
        String table = index.toString();
        table = table.substring(table.indexOf("REFERENCES") + "REFERENCES".length()).replaceAll(" ", "");
        Integer i = table.contains("(") ? table.indexOf("(") : table.length();
        table = table.substring(0, i);
        return table.contains(".") ? table.split("\\.")[1] : table;
    }

    public static String getLength(String type, List<String> argumentsStringList) {
        //Do this if long too?
        if (!type.equals("String"))
            return "";
        return ", length = " + argumentsStringList.get(0).replaceAll(" BYTE", "");
    }

    public static String getPrecisionScale(String type, List<String> argumentsStringList) {
        if ((!type.equals("BigDecimal") && !type.equals("Long"))|| argumentsStringList == null || argumentsStringList.size() != 2)
            return "";
        return ", precision = " + argumentsStringList.get(0) + ", scale = " + argumentsStringList.get(1);
    }

    public static List<String> getPrimaryKey(List<Index> indexes) {
        for (Index index : indexes) {
            if (index.getType().toUpperCase().equals("PRIMARY KEY"))
                return index.getColumnsNames();
        }
        return new ArrayList();
    }

    public static Boolean ifNotNullable(List<String> columnSpecStrings) {
        if (columnSpecStrings == null)
            return false;
        String specs = "";
        for (String columnSpecString : columnSpecStrings) {
            specs += " " + columnSpecString;
        }
        return specs.toUpperCase().contains("NOT NULL");
    }

    public static String convertToCamelCase(String columnName, String separator, Boolean firstUpper, String separatorToAdd) {
        String response = "";
        Boolean toUpperCase = false;
        for (int i = 0; i < columnName.length(); i ++) {
            String c = columnName.substring(i, i + 1).toLowerCase();
            if (firstUpper && i == 0)
                toUpperCase = true;
            if (c.equals(separator)) {
                toUpperCase = true;
            } else {
                response += toUpperCase? separatorToAdd + c.toUpperCase() : c;
                toUpperCase = false;
            }
        }
        return response;
    }

    public static String getType(ColDataType colType) {
        String type = colType.getDataType().toUpperCase();
        if (type.startsWith("VARCHAR"))
            return "String";
        if (type.startsWith("CHAR"))
            return "String";
        if (type.equals("TIMESTAMP"))
            return "Date";
        if (type.equals("DATE"))
            return "Date";
        if (type.equals("INT"))
            return "Integer";
        if (type.equals("LONG"))
            return "Long";
        if (type.equals("BIGINT"))
            return "Long";
        if (type.equals("DECIMAL"))
            return "BigDecimal";
        if (type.equals("NUMBER") && colType.getArgumentsStringList() != null && colType.getArgumentsStringList().size() > 1 && !colType.getArgumentsStringList().get(1).equals("0"))
            return "BigDecimal";
        if (type.equals("NUMBER") && colType.getArgumentsStringList() != null && colType.getArgumentsStringList().size() > 1 && colType.getArgumentsStringList().get(1).equals("0"))
            return "Long";
        if (type.equals("NUMBER") && colType.getArgumentsStringList() != null && colType.getArgumentsStringList().size() == 1)
            return "Long";
        if (type.equals("NUMBER") && colType.getArgumentsStringList() == null)
            return "Long";
        return type;
    }

}
