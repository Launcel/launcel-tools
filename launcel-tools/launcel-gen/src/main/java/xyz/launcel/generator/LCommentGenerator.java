package xyz.launcel.generator;

import org.mybatis.generator.api.CommentGenerator;
import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.java.Field;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.InnerClass;
import org.mybatis.generator.api.dom.java.InnerEnum;
import org.mybatis.generator.api.dom.java.Method;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

/**
 * @author Launcel
 */
public class LCommentGenerator implements CommentGenerator {
    private boolean useAnnotation = true;
    private boolean addRemark = true;

    public LCommentGenerator() {
    }

//    protected String getDateString() {
//        String result = null;
//        if (!this.suppressDate) {
//            result = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
//        }
//        return result;
//    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean arg2) {
    }

    public void addComment(XmlElement xmlElement) {
    }

    public void addConfigurationProperties(Properties propertie) {
        this.useAnnotation = StringUtility.isTrue(propertie.getProperty("suppressDate"));
        this.addRemark = StringUtility.isTrue(propertie.getProperty("addRemark"));

    }

    public void addEnumComment(InnerEnum innerEnum, IntrospectedTable table) {
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
        if (useAnnotation) {
            if (introspectedColumn.getActualColumnName().toLowerCase().equals("id")) {
                field.addAnnotation("@Id");
                field.addAnnotation("@GeneratedValue");
            }

            StringBuilder sb = (new StringBuilder("@Column(name=\"")).append(introspectedColumn.getActualColumnName()).append("\"");
            if (addRemark) {
                if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
                    sb.append(", describe=\"").append(introspectedColumn.getRemarks()).append("\"");
                }
            }
            sb.append(")");
            field.addAnnotation(sb.toString());
        }
    }

    public void addGeneralMethodComment(Method method, IntrospectedTable table) {
    }

    public void addGetterComment(Method method, IntrospectedTable table, IntrospectedColumn column) {
    }

    public void addJavaFileComment(CompilationUnit clazz) {
        if (useAnnotation) {
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
        if (useAnnotation) {
            if (!clazz.isJavaInterface()) {
                clazz.addAnnotation("@Entity");
                StringBuilder sb = (new StringBuilder("@Table(name=\"")).append(introspectedTable.getFullyQualifiedTable()).append("\"");
                if (addRemark) {
                    if (StringUtility.stringHasValue(introspectedTable.getRemarks())) {
                        sb.append(", describe=\"").append(introspectedTable.getRemarks()).append("\"");
                    }
                }
                sb.append(")");
                clazz.addAnnotation(sb.toString());
            }
        }
    }

    public void addRootComment(XmlElement arg0) {
    }

    public void addSetterComment(Method method, IntrospectedTable table, IntrospectedColumn column) {
    }
}
