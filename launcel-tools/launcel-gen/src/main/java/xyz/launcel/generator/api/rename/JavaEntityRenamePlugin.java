package xyz.launcel.generator.api.rename;

import org.mybatis.generator.api.IntrospectedTable;
import xyz.launcel.lang.StringUtils;

import java.util.List;

public class JavaEntityRenamePlugin extends AbstractRenamePlugin {

    private String suffix = "";

    public JavaEntityRenamePlugin() {
    }

    @Override
    protected void initProp() {
        super.initProp();
        String suffixTemp = properties.getProperty("suffix");
        if (StringUtils.isNotBlank(suffixTemp)) {
            suffix = suffixTemp;
        }
    }

    @Override
    public boolean validate(List<String> warnings) {
        this.initProp();
        return StringUtils.isNotBlank(suffix);
    }

    public void initialized(IntrospectedTable introspectedTable) {
        introspectedTable.setBaseRecordType(introspectedTable.getBaseRecordType() + suffix);
    }
}
