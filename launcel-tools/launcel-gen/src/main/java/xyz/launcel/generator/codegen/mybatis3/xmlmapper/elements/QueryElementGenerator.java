package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

/**
 * @author Launcel
 */
public class QueryElementGenerator extends AbstractXmlElementGenerator {
    public QueryElementGenerator() {
    }

    public void addElements(XmlElement parentElement) {
        LXmlElement answer = new LXmlElement("select");
        answer.addAttribute(new Attribute("id", "query"));
        answer.addAttribute(new Attribute("resultType", this.introspectedTable.getBaseResultMapId()));

        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        answer.addElement(new LTextElement("SELECT "));
        answer.addElement(this.getBaseColumnListElement());
        sb.append("FROM ");
        answer.addElement(new LTextElement(sb.toString()));
        answer.addElement(new LTextElement("    " + this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime()));
        answer.addElement(this.getBaseSql());
        if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
