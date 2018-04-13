package xyz.launcel.generator.api;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import xyz.launcel.generator.api.utils.Conston;
import xyz.launcel.generator.api.utils.ShowDocUtils;

/**
 * @author Launcel
 */
public class DefaultCommentGenerator extends AbstractCommentGenerator {

    public DefaultCommentGenerator() {
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
        ShowDocUtils.addClassComment(innerClass, introspectedTable);
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        ShowDocUtils.addFieldComment(field, introspectedTable, introspectedColumn);
        if (Conston.useAnnotation) {
            if (introspectedColumn.getActualColumnName().toLowerCase().equals("id")) {
                field.addAnnotation("@Id");
                field.addAnnotation("@GeneratedValue");
            }

            StringBuilder sb = (new StringBuilder("@Column(name=\"")).append(introspectedColumn.getActualColumnName()).append("\"");
            if (Conston.addRemark) {
                if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
                    sb.append(", describe=\"").append(introspectedColumn.getRemarks()).append("\"");
                }
            }
            sb.append(")");
            field.addAnnotation(sb.toString());
        }
    }

    public void addJavaFileComment(CompilationUnit clazz) {
        if (Conston.useAnnotation) {
            if (!clazz.isJavaInterface()) {
                FullyQualifiedJavaType t;
                t = new FullyQualifiedJavaType("javax.persistence.Table");
                t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.Column"));
                t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.Entity"));
                t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.GeneratedValue"));
                t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.Id"));
                clazz.addImportedType(t);
            }
        }
    }

    public void addModelClassComment(TopLevelClass clazz, IntrospectedTable introspectedTable) {
        if (Conston.useAnnotation) {
            if (!clazz.isJavaInterface()) {
                clazz.addAnnotation("@Entity");
                StringBuilder sb = (new StringBuilder("@Table(name=\"")).append(introspectedTable.getFullyQualifiedTable()).append("\"");
                if (Conston.addRemark) {
                    if (StringUtility.stringHasValue(introspectedTable.getRemarks())) {
                        sb.append(", describe=\"").append(introspectedTable.getRemarks()).append("\"");
                    }
                }
                sb.append(")");
                clazz.addAnnotation(sb.toString());
            }
        }
    }

}
