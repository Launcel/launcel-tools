package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.utils.Conston;
import xyz.launcel.generator.api.utils.OutputUtils;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

public class GetElement extends AbstractXmlElementGenerator
{

    @Override
    public void addElements(XmlElement parentElement)
    {

        LXmlElement answer = new LXmlElement("select");
        answer.addAttribute(new Attribute("id", "get"));
        answer.addAttribute(new Attribute("parameterType", getParamType()));
        answer.addAttribute(new Attribute("resultType", this.introspectedTable.getBaseResultMapId()));

        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        answer.addElement(new LTextElement("SELECT "));
        answer.addElement(this.getBaseColumnListElement());
        sb.append("FROM ");
        OutputUtils.newLine(sb);
        OutputUtils.xmlIndent(sb, 3);
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement(sb.toString()));
        sb.setLength(0);
        sb.append("WHERE `id`=#{id}");
        answer.addElement(new LTextElement(sb.toString()));
        if (Conston.isUseEnabledColumn())
        {
            sb.setLength(0);
            sb.append("AND ")
                    .append("`")
                    .append(Conston.getEnabledColumnName())
                    .append("`")
                    .append("=")
                    .append(Conston.getEnabledColumnValue());
            answer.addElement(new LTextElement(sb.toString()));
        }

        if (context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, introspectedTable))
        {
            parentElement.addElement(answer);
        }


    }
}
