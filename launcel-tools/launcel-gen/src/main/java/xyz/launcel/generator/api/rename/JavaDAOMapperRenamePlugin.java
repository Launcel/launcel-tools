package xyz.launcel.generator.api.rename;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import xyz.launcel.lang.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author Launcel
 */
public class JavaDAOMapperRenamePlugin extends AbstractRenamePlugin {
    private Boolean useBaseDAOPackage = false;
    private Boolean useGeneralizate = false;
    private String baseDAOPackage = "xyz.launcel.dao.BaseDAO";

    public JavaDAOMapperRenamePlugin() {
    }

    @Override
    protected void initProp() {
        super.initProp();
        String baseDAOPackageTemp = properties.getProperty("baseDAOPackage");
        if (StringUtils.isNotBlank(baseDAOPackageTemp)) {
            baseDAOPackage = baseDAOPackageTemp;
        }
        String useGeneralizateTemp = properties.getProperty("useGeneralizate");
        if (StringUtils.isNotBlank(useGeneralizateTemp)) {
            useGeneralizate = true;
        }
        String useBaseDAOPackageTemp = properties.getProperty("useBaseDAOPackage");
        if (StringUtils.isTrue(useBaseDAOPackageTemp)) {
            useBaseDAOPackage = true;
        }
    }

    public boolean validate(List<String> warnings) {
        this.initProp();
        return Objects.nonNull(pattern) || useBaseDAOPackage;
    }

    public void initialized(IntrospectedTable introspectedTable) {
        String oldName = introspectedTable.getMyBatis3JavaMapperType();
        Matcher matcher = this.pattern.matcher(oldName);
        String newName = matcher.replaceAll(replaceCharacter);
        introspectedTable.setMyBatis3JavaMapperType(newName);
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable) {
        if (useBaseDAOPackage) {
            Integer index = baseDAOPackage.lastIndexOf(".");
            String superClass = baseDAOPackage.substring(index + 1);
            if (useGeneralizate) {
                superClass += "<" + introspectedTable.getBaseRecordType() + " > ";
            }
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(superClass);
            FullyQualifiedJavaType imp = new FullyQualifiedJavaType(baseDAOPackage);
            interfaze.addSuperInterface(fqjt);
            interfaze.addImportedType(imp);
            interfaze.getMethods().clear();
        } else {
            super.clientGenerated(interfaze, topLevelClass, introspectedTable);
        }

        return true;
    }
}
