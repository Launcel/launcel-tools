package xyz.launcel.generator.api.utils;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.InnerClass;

public interface ShowDocUtils {

    static void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        if (!Conston.showDoc) {
            return;
        }
        StringBuilder sb = new StringBuilder();
        innerClass.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedTable.getFullyQualifiedTable());
        innerClass.addJavaDocLine(sb.toString());
        innerClass.addJavaDocLine(" */");
    }

    static void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (!Conston.showDoc) {
            return;
        }

        StringBuilder sb = new StringBuilder();

        field.addJavaDocLine("/**");
        sb.append(" * ");
        sb.append(introspectedColumn.getRemarks());
        field.addJavaDocLine(sb.toString());
        field.addJavaDocLine(" */");
    }

}
