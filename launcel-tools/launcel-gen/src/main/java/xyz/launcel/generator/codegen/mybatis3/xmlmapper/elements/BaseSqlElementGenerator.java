package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.ListUtilities;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

import java.util.Iterator;

/**
 * @author Launcel
 */
public class BaseSqlElementGenerator extends AbstractXmlElementGenerator {
    public BaseSqlElementGenerator() {
    }

    public void addElements(XmlElement parentElement) {
        LXmlElement answer = new LXmlElement("sql");
        answer.addAttribute(new Attribute("id", "BaseSql"));
        this.context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        boolean and = false;
        Iterator<IntrospectedColumn> var5 = this.introspectedTable.getPrimaryKeyColumns().iterator();

        IntrospectedColumn introspectedColumn;
        while (var5.hasNext()) {
            introspectedColumn = (IntrospectedColumn) var5.next();
            if (and) {
                sb.append("  AND ");
            } else {
                sb.append(" WHERE ");
                and = true;
            }

            sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "obj."));
            answer.addElement(new LTextElement(sb.toString()));
        }

        if (and) {
            var5 = ListUtilities.removeGeneratedAlwaysColumns(this.introspectedTable.getNonPrimaryKeyColumns()).iterator();

            while (var5.hasNext()) {
                introspectedColumn = (IntrospectedColumn) var5.next();
                LXmlElement isNotNullElement = new LXmlElement("if");
                sb.setLength(0);
                sb.append("obj.").append(introspectedColumn.getJavaProperty());
                sb.append(" != null");
                isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
                sb.setLength(0);
                sb.append("AND ");
                sb.append(MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn));
                sb.append(" = ");
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "obj."));
                isNotNullElement.addElement(new LTextElement(sb.toString()));
                answer.addElement(isNotNullElement);
            }
        }

        if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }

    }
}
