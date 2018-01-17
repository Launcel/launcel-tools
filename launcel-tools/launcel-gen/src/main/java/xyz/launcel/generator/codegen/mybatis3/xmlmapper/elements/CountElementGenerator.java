package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.dom.OutputUtilities;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

/**
 * @author Launcel
 */
public class CountElementGenerator extends AbstractXmlElementGenerator {
    public CountElementGenerator() {
    }

    public void addElements(XmlElement parentElement) {
        LXmlElement answer = new LXmlElement("select");
        answer.addAttribute(new Attribute("id", "count"));
        answer.addAttribute(new Attribute("resultType", "java.lang.Integer"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder("SELECT count(1)");
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 2);
        sb.append("FROM");
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 3);
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement(sb.toString()));
        answer.addElement(this.getBaseSql());
        if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}