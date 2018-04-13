package xyz.launcel.generator.api.rename;

import org.mybatis.generator.api.IntrospectedTable;

import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;

/**
 * @author Launcel
 */
public class SqlMapperRenamePlugin extends AbstractRenamePlugin {

    public SqlMapperRenamePlugin() {
    }

    @Override
    protected void initProp() {
        super.initProp();
    }

    public boolean validate(List<String> warnings) {
        return Objects.nonNull(pattern);
    }

    public void initialized(IntrospectedTable introspectedTable) {
        String oldName = introspectedTable.getMyBatis3XmlMapperFileName();
        Matcher matcher = pattern.matcher(oldName);
        String newName = matcher.replaceAll(searchCharacter);
        introspectedTable.setMyBatis3XmlMapperFileName(newName);
    }
}
