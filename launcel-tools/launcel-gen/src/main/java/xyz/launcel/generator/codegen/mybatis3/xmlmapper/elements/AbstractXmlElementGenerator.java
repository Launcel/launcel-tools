package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.Conston;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

/**
 * @author Launcel
 */
public abstract class AbstractXmlElementGenerator extends org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator {

    public AbstractXmlElementGenerator() {
        super();
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
        return Conston.useEnabledColumn;
    }

    public String getEnabledColumn() {
        return Conston.enabledColumnName;
    }

    public String getEnabledValue() {
        return Conston.enabledColumnValue;
    }
}
