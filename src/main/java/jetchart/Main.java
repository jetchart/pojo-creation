package jetchart;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserUtil;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.ColDataType;
import net.sf.jsqlparser.statement.create.table.ColumnDefinition;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.create.table.Index;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class Main {

    public static void main(String[] args) {
        String path = args[0];
        StringBuffer pojo = new StringBuffer();
        String createSQL = readFile(path) ;

        try {

            writePackageSection(pojo);
            writeImportSection(pojo);

            Statement createTable = CCJSqlParserUtil.parse(createSQL);
            String tableName = ((CreateTable) createTable).getTable().getName();
            List<String> keys = getPrimaryKey(((CreateTable) createTable).getIndexes());
            List<String> foreignsKey = getForeignsKey(((CreateTable) createTable).getIndexes());

            writeHeaderClassSection(pojo, (CreateTable) createTable, tableName);

            /* Fields definition */
            for (ColumnDefinition col: ((CreateTable) createTable).getColumnDefinitions()) {
                String type = getType(col.getColDataType());
                String precisionScale = getPrecisionScale(type, col.getColDataType().getArgumentsStringList());
                String length = getLength(type, col.getColDataType().getArgumentsStringList());
                /* Write POJO */
                writeIdAnnotation(pojo, keys, col);
                writeNotNullableAnnotation(pojo, col);
                writeFieldDefinition(pojo, foreignsKey, col, type, precisionScale, length);
            }

            /* Fields setter & getter */
            for (ColumnDefinition col: ((CreateTable) createTable).getColumnDefinitions()) {
                String type = getType(col.getColDataType());
                String className = convertToCamelCase(col.getColumnName(), "_", true, "");
                String propertyName = convertToCamelCase(col.getColumnName(), "_", false, "");
                /* Write POJO */
                writeGetterMethod(pojo, foreignsKey, col, type, className, propertyName);
                writeSetterMethod(pojo, foreignsKey, col, type, className, propertyName);
            }

            writeEndClassSection(pojo);

            System.out.println(pojo);

            writeFile("./" + convertToCamelCase(tableName, "_", true, "") + ".java", pojo.toString());

        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    private static void writeGetterMethod(StringBuffer pojo, List<String> foreignsKey, ColumnDefinition col, String type, String className, String propertyName) {
        if (foreignsKey.contains(col.getColumnName())) {
            pojo.append("\tpublic ").append(className).append("Entity get").append(className).append("() {\n");
            pojo.append("\t\treturn this.").append(propertyName).append(";\n");
            pojo.append("\t}\n\n");
        } else {
            pojo.append("\tpublic ").append(type).append(" get").append(className).append("() {\n");
            pojo.append("\t\treturn this.").append(propertyName).append(";\n");
            pojo.append("\t}\n\n");
        }
    }

    private static void writeSetterMethod(StringBuffer pojo, List<String> foreignsKey, ColumnDefinition col, String type, String className, String propertyName) {
        if (foreignsKey.contains(col.getColumnName())) {
            pojo.append("\tpublic void ").append("set").append(className).append(" (").append(className).append("Entity ").append(propertyName).append(") {\n");
            pojo.append("\t\tthis.").append(propertyName).append(" = ").append(propertyName).append(";\n");
            pojo.append("\t}\n\n");
        } else {
            pojo.append("\tpublic void ").append("set").append(className).append("(").append(type).append(" ").append(propertyName).append(") {\n");
            pojo.append("\t\tthis.").append(propertyName).append(" = ").append(propertyName).append(";\n");
            pojo.append("\t}\n\n");
        }
    }

    private static void writeEndClassSection(StringBuffer pojo) {
        pojo.append("}");
    }

    private static void writeFieldDefinition(StringBuffer pojo, List<String> foreignsKey, ColumnDefinition col, String type, String precisionScale, String length) {
        if (foreignsKey.contains(col.getColumnName())) {
            pojo.append("\t@ManyToOne\n");
            pojo.append("\t@JoinColumn(name = \"" + col.getColumnName() + "\")\n");
            pojo.append("\tprivate ").append(convertToCamelCase(col.getColumnName(), "_", true, "")).append("Entity ").append(convertToCamelCase(col.getColumnName(), "_", false, "")).append(";\n\n");
        } else {
            pojo.append("\t@Column(name = \"" + col.getColumnName() + "\"" + precisionScale + length + ")\n");
            pojo.append("\tprivate ").append(type).append(" ").append(convertToCamelCase(col.getColumnName(), "_", false, "")).append(";\n\n");
        }
    }

    private static void writeNotNullableAnnotation(StringBuffer pojo, ColumnDefinition col) {
        if (ifNotNullable(col.getColumnSpecStrings()))
            pojo.append("\t@NotNullable\n");
    }

    private static void writeIdAnnotation(StringBuffer pojo, List<String> keys, ColumnDefinition col) {
        if (keys.contains(col.getColumnName()))
            pojo.append("\t@Id\n");
    }

    private static void writeHeaderClassSection(StringBuffer pojo, CreateTable createTable, String tableName) {
        pojo.append("@Entity\n");
        pojo.append("@Table(name = \"" + tableName + "\", schema = \"" + createTable.getTable().getSchemaName() + "\")\n");
        pojo.append("public class ").append(convertToCamelCase(tableName, "_", true, "")).append("Entity {\n\n");
    }

    private static void writeImportSection(StringBuffer pojo) {
        pojo.append("import javax.persistence.*;\n\n");
    }

    private static void writePackageSection(StringBuffer pojo) {
        pojo.append("package FILL_PACKAGE_PATH_HERE;\n\n");
    }

    private static List<String> getForeignsKey(List<Index> indexes) {
        for (Index index : indexes) {
            if (index.getType().toUpperCase().equals("FOREIGN KEY"))
                return index.getColumnsNames();
        }
        return new ArrayList();
    }

    private static String getLength(String type, List<String> argumentsStringList) {
        if (!type.equals("String"))
            return "";
        return ", length = " + argumentsStringList.get(0);
    }

    private static String getPrecisionScale(String type, List<String> argumentsStringList) {
        if (!type.equals("BigDecimal") || argumentsStringList.size() != 2)
            return "";
        return ", precision = " + argumentsStringList.get(0) + ", scale = " + argumentsStringList.get(1);
    }

    private static List<String> getPrimaryKey(List<Index> indexes) {
        for (Index index : indexes) {
            if (index.getType().toUpperCase().equals("PRIMARY KEY"))
                return index.getColumnsNames();
        }
        return new ArrayList();
    }

    private static Boolean ifNotNullable(List<String> columnSpecStrings) {
        if (columnSpecStrings == null)
            return false;
        String specs = "";
        for (String columnSpecString : columnSpecStrings) {
            specs += " " + columnSpecString;
        }
        return specs.toUpperCase().contains("NOT NULL");
    }

    private static String convertToCamelCase(String columnName, String separator, Boolean firstUpper, String separatorToAdd) {
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

    private static String getType(ColDataType colType) {
        String type = colType.getDataType().toUpperCase();
        //System.out.println(colType.getArgumentsStringList());
        if (type.equals("VARCHAR"))
            return "String";
        if (type.equals("CHAR"))
            return "String";
        if (type.equals("TIMESTAMP"))
            return "Date";
        if (type.equals("INT"))
            return "Integer";
        if (type.equals("LONG"))
            return "Long";
        if (type.equals("DECIMAL"))
            return "BigDecimal";
        return type;
    }

    private static String readFile(String filePath) {
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
