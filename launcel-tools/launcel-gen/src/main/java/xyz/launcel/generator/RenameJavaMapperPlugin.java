package xyz.launcel.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Launcel
 */
public class RenameJavaMapperPlugin extends PluginAdapter {
    private String searchString;
    private String replaceString;
    private Pattern pattern;
    private boolean reName = true;
    private boolean noMethod = true;
    private String baseDAOPackage = "xyz.launcel.base.dao.BaseDAO";


    public RenameJavaMapperPlugin() {
        String temp = properties.getProperty("baseDAOPackage");
        if (StringUtility.stringHasValue(temp))
            baseDAOPackage = temp;
    }

    public boolean validate(List<String> warnings) {
        if (!this.reName) {
            return true;
        } else {
            this.searchString = this.properties.getProperty("searchString");
            this.replaceString = this.properties.getProperty("replaceString");
            boolean valid = StringUtility.stringHasValue(this.searchString) && StringUtility.stringHasValue(this.replaceString);
            if (valid) {
                this.pattern = Pattern.compile(this.searchString);
            } else {
                if (!StringUtility.stringHasValue(this.searchString)) {
                    warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "searchString"));
                }

                if (!StringUtility.stringHasValue(this.replaceString)) {
                    warnings.add(Messages.getString("ValidationError.18", "RenameExampleClassPlugin", "replaceString"));
                }
            }

            return valid;
        }
    }

    public void initialized(IntrospectedTable introspectedTable) {
        String oldType = introspectedTable.getMyBatis3JavaMapperType();
        Matcher matcher = this.pattern.matcher(oldType);
        oldType = matcher.replaceAll(this.replaceString);
        introspectedTable.setMyBatis3JavaMapperType(oldType);
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (this.noMethod) {
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType("BaseDAO<" + introspectedTable.getBaseRecordType() + ">");
            FullyQualifiedJavaType imp = new FullyQualifiedJavaType(this.baseDAOPackage);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(imp);
            interfaze.getMethods().clear();
        } else {
            super.clientGenerated(interfaze, topLevelClass, introspectedTable);
        }

        return true;
    }
}
