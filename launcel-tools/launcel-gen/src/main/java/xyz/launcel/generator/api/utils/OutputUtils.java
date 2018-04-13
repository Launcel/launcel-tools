package xyz.launcel.generator.api.utils;

import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;

import java.util.Set;
import java.util.TreeSet;

/**
 * @author Launcel
 */
public class OutputUtils {

    private static final String lineSeparator;

    static {
        String ls = System.getProperty("line.separator");
        if (ls == null) {
            ls = "\n";
        }
        lineSeparator = ls;
    }

    private OutputUtils() {
        super();
    }

    public static void javaIndent(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append("    ");
        }
    }

    public static void xmlIndent(StringBuilder sb, int indentLevel) {
        for (int i = 0; i < indentLevel; i++) {
            sb.append("    ");
        }
    }

    public static void newLine(StringBuilder sb) {
        sb.append(lineSeparator);
    }

    public static Set<String> calculateImports(
            Set<FullyQualifiedJavaType> importedTypes) {
        StringBuilder sb = new StringBuilder();
        Set<String> importStrings = new TreeSet<>();
        for (FullyQualifiedJavaType fqjt : importedTypes) {
            for (String importString : fqjt.getImportList()) {
                sb.setLength(0);
                sb.append("import ");
                sb.append(importString);
                sb.append(';');
                importStrings.add(sb.toString());
            }
        }

        return importStrings;
    }

}
