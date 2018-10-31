package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

/**
 * @author Launcel
 */
public abstract class AbstractXmlElementGenerator extends org.mybatis.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator
{

    public AbstractXmlElementGenerator()
    {
        super();
    }

    protected XmlElement getBaseColumnListElement()
    {
        LXmlElement answer = new LXmlElement("include");
        answer.addAttribute(new Attribute("refid", "BaseColumn"));
        return answer;
    }

    XmlElement getBaseSql()
    {
        LXmlElement answer = new LXmlElement("include");
        answer.addAttribute(new Attribute("refid", "BaseSql"));
        return answer;
    }

    XmlElement getUpdateSql()
    {
        LXmlElement answer = new LXmlElement("include");
        answer.addAttribute(new Attribute("refid", "UpdateSql"));
        return answer;
    }

    String getParamType()
    {
        if (introspectedTable.getRules().generatePrimaryKeyClass())
        {
            return introspectedTable.getPrimaryKeyType();
        }
        if (introspectedTable.getPrimaryKeyColumns().size() > 1)
        {
            return "map";
        }
        return introspectedTable.getPrimaryKeyColumns().get(0).getFullyQualifiedJavaType().toString();
    }

}
