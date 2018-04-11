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
    private Properties properties = new Properties();
    private boolean suppressDate = false;
//    private boolean suppressAllComments = false;

    public LCommentGenerator() {
    }

    protected String getDateString() {
        String result = null;
        if (!this.suppressDate) {
            result = (new SimpleDateFormat("yyyy-MM-dd")).format(new Date());
        }

        return result;
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable) {
    }

    public void addClassComment(InnerClass innerClass, IntrospectedTable introspectedTable, boolean arg2) {
    }

    public void addComment(XmlElement arg0) {
    }

    public void addConfigurationProperties(Properties arg0) {
        this.properties.putAll(arg0);
        this.suppressDate = StringUtility.isTrue(this.properties.getProperty("suppressDate"));
//        this.suppressAllComments = StringUtility.isTrue(this.properties.getProperty("suppressAllComments"));
    }

    public void addEnumComment(InnerEnum arg0, IntrospectedTable arg1) {
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable) {
    }

    public void addFieldComment(Field field, IntrospectedTable introspectedTable, IntrospectedColumn introspectedColumn) {
//        if (!this.suppressAllComments) {
        if (introspectedColumn.getActualColumnName().toLowerCase().equals("id")) {
            field.addAnnotation("@Id");
            field.addAnnotation("@GeneratedValue");
        }

        StringBuilder sb = (new StringBuilder("@Column(name=\"")).append(introspectedColumn.getActualColumnName()).append("\"");
        if (StringUtility.stringHasValue(introspectedColumn.getRemarks())) {
            sb.append(", describe=\"").append(introspectedColumn.getRemarks()).append("\"");
        }

        sb.append(")");
        field.addAnnotation(sb.toString());
//        }
    }

    public void addGeneralMethodComment(Method arg0, IntrospectedTable arg1) {
    }

    public void addGetterComment(Method arg0, IntrospectedTable arg1, IntrospectedColumn arg2) {
    }

    public void addJavaFileComment(CompilationUnit arg0) {
        if (!arg0.isJavaInterface()) {
            FullyQualifiedJavaType t;
            t = new FullyQualifiedJavaType("javax.persistence.Table");
            t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.Column"));
            t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.Entity"));
            t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.GeneratedValue"));
            t.addTypeArgument(new FullyQualifiedJavaType("javax.persistence.Id"));
            arg0.addImportedType(t);
        }

    }

    public void addModelClassComment(TopLevelClass arg0, IntrospectedTable introspectedTable) {
        if (!arg0.isJavaInterface()) {
            arg0.addAnnotation("@Entity");
            StringBuilder sb = (new StringBuilder("@Table(name=\"")).append(introspectedTable.getFullyQualifiedTable()).append("\"");
            if (StringUtility.stringHasValue(introspectedTable.getRemarks())) {
                sb.append(", describe=\"").append(introspectedTable.getRemarks()).append("\"");
            }

            sb.append(")");
            arg0.addAnnotation(sb.toString());
        }

    }

    public void addRootComment(XmlElement arg0) {
    }

    public void addSetterComment(Method arg0, IntrospectedTable arg1, IntrospectedColumn arg2) {
    }
}
