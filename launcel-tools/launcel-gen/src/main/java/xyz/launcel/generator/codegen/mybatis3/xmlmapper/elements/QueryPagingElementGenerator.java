package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.internal.util.StringUtility;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

/**
 * @author Launcel
 */
public class QueryPagingElementGenerator extends AbstractXmlElementGenerator {
    public QueryPagingElementGenerator() {
    }

    public void addElements(XmlElement parentElement) {
        LXmlElement answer = new LXmlElement("select");
        answer.addAttribute(new Attribute("id", "queryPage"));
        answer.addAttribute(new Attribute("resultType", this.introspectedTable.getBaseResultMapId()));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        sb.append("SELECT ");
        if (StringUtility.stringHasValue(this.introspectedTable.getSelectByPrimaryKeyQueryId())) {
            sb.append('\'');
            sb.append(this.introspectedTable.getSelectByPrimaryKeyQueryId());
            sb.append("' as QUERYID,");
        }

        answer.addElement(new LTextElement(sb.toString()));
        answer.addElement(this.getBaseColumnListElement());
        answer.addElement(new LTextElement("FROM "));
        sb.setLength(0);
        sb.append(this.introspectedTable.getAliasedFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement("    " + sb.toString()));
        answer.addElement(this.getBaseSql());
        if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
