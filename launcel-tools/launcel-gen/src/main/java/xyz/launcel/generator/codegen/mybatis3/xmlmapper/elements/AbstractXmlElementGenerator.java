package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

/**
 * @author Launcel
 */
public abstract class AbstractXmlElementGenerator extends org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator {

    private boolean useEnabledColumn = true;

    protected String enabledColumn = "enabled";

    protected String enabledValue = "1";

    public AbstractXmlElementGenerator() {
        super();
        String temp = getContext().getProperty("useEnabledColumn");
        if (StringUtility.isTrue(temp)) {
            useEnabledColumn = true;
        } else {
            useEnabledColumn = false;
        }
        if (useEnabledColumn) {
            String tempColumn = getContext().getProperty("enabledColumnName");
            if (StringUtility.stringHasValue(tempColumn)) {
                enabledColumn = tempColumn;
            }
            String tempValue = getContext().getProperty("enabledColumnValue");
            if (StringUtility.stringHasValue(tempValue)) {
                enabledValue = tempValue;
            }
        }
    }

    protected XmlElement getBaseColumnListElement() {
        LXmlElement answer = new LXmlElement("include");
        answer.addAttribute(new Attribute("refid", "BaseColumn"));
        return answer;
    }

    protected XmlElement getBaseSql() {
        LXmlElement answer = new LXmlElement("include");
        answer.addAttribute(new Attribute("refid", "BaseSql"));
        return answer;
    }

    protected XmlElement getUpdateSql() {
        LXmlElement answer = new LXmlElement("include");
        answer.addAttribute(new Attribute("refid", "UpdateSql"));
        return answer;
    }

    protected String getParamType() {
        String parameterType;
        if (introspectedTable.getRules().generatePrimaryKeyClass()) {
            parameterType = introspectedTable.getPrimaryKeyType();
        } else {
            if (introspectedTable.getPrimaryKeyColumns().size() > 1) {
                parameterType = "map";
            } else {
                parameterType = introspectedTable.getPrimaryKeyColumns().get(0)
                        .getFullyQualifiedJavaType().toString();
            }
        }
        return parameterType;
    }

    public boolean isUseEnabledColumn() {
        return useEnabledColumn;
    }

    public String getEnabledColumn() {
        return enabledColumn;
    }

    public String getEnabledValue() {
        return enabledValue;
    }
}
