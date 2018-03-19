package xyz.launcel.generator;

import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.PluginAdapter;
import org.mybatis.generator.api.dom.java.FullyQualifiedJavaType;
import org.mybatis.generator.internal.util.StringUtility;
import org.mybatis.generator.internal.util.messages.Messages;

import java.util.List;
import java.util.regex.Pattern;

public class RenameJavaModelPlugin extends PluginAdapter {

    private String suffix;

    public RenameJavaModelPlugin() {
    }

    @Override
    public boolean validate(List<String> warnings) {
        this.suffix = this.properties.getProperty("suffix");
        return StringUtility.stringHasValue(suffix);
    }

    public void initialized(IntrospectedTable introspectedTable) {
        String replaceModelName = introspectedTable.getBaseRecordType() + suffix;
        introspectedTable.setBaseRecordType(replaceModelName);
    }
}
