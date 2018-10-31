package xyz.launcel.generator.api.rename;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.api.dom.java.Interface;
import org.mybatis.generator.api.dom.java.TopLevelClass;
import xyz.launcel.generator.api.utils.Conston;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author Launcel
 */
public class JavaDAOMapperRenamePlugin extends AbstractRenamePlugin
{
    public JavaDAOMapperRenamePlugin() { }

    @Override
    protected void initProp()
    {
        super.initProp();
        Conston.JAVADAOPlugPropertites.initPropertites(properties);
    }

    public boolean validate(List<String> warnings)
    {
        this.initProp();
        return Objects.nonNull(pattern) || Conston.JAVADAOPlugPropertites.isUseBaseDAO();
    }

    public void initialized(IntrospectedTable introspectedTable)
    {
        String  oldName = introspectedTable.getMyBatis3JavaMapperType();
        Matcher matcher = this.pattern.matcher(oldName);
        String  newName = matcher.replaceAll(replaceCharacter);
        introspectedTable.setMyBatis3JavaMapperType(newName);
    }

    public boolean clientGenerated(Interface interfaze, TopLevelClass topLevelClass, IntrospectedTable introspectedTable)
    {
        if (!Conston.JAVADAOPlugPropertites.isUseBaseDAO())
        {
            super.clientGenerated(interfaze, topLevelClass, introspectedTable);
            return true;
        }
        Integer index = Conston.JAVADAOPlugPropertites.getBaseDAOPackage().lastIndexOf(".");
        if (Conston.JAVADAOPlugPropertites.isUseGeneralizate())
        {
            String superClass = Conston.JAVADAOPlugPropertites.getBaseDAOPackage().substring(index + 1);
            superClass += "<" + introspectedTable.getBaseRecordType() + " > ";
            FullyQualifiedJavaType fqjt = new FullyQualifiedJavaType(superClass);
            interfaze.addSuperInterface(fqjt);
        }
        FullyQualifiedJavaType imp = new FullyQualifiedJavaType(Conston.JAVADAOPlugPropertites.getBaseDAOPackage());
        interfaze.addImportedType(imp);
        interfaze.getMethods().clear();
        
        return true;
    }
}
