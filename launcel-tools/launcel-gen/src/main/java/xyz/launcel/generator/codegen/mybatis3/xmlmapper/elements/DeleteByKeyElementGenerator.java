package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;
import xyz.launcel.lang.StringUtils;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static xyz.launcel.generator.api.utils.OutputUtils.xmlIndent;

/**
 * @author Launcel
 */
public class DeleteByKeyElementGenerator extends AbstractXmlElementGenerator {

    public DeleteByKeyElementGenerator(boolean isSimple) {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        String opt = isUseEnabledColumn() ? "update" : "delete";

        LXmlElement answer = new LXmlElement(opt);
        answer.addAttribute(new Attribute("id", "delete"));
        answer.addAttribute(new Attribute("parameterType", getParamType()));
        context.getCommentGenerator().addComment(answer);

        if (isUseEnabledColumn()) {
            addUpdateElements(answer);
        } else {
            addDeleteElements(answer);
        }

        if (context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    private void addUpdateElements(LXmlElement answer) {
        StringBuilder sb = new StringBuilder();
        answer.addElement(new LTextElement("UPDATE "));
        xmlIndent(sb, 1);
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement(sb.toString()));
        sb.setLength(0);
        sb.append("SET").append(" ").append(getEnabledColumn()).append("=#{").append(StringUtils.capitalize(getEnabledColumn())).append("}");
        answer.addElement(new LTextElement(sb.toString()));
        sb.setLength(0);
        sb.append("WHERE id=#{id}");
        answer.addElement(new LTextElement(sb.toString()));
//        whereCase(answer, sb);
    }

    private void whereCase(LXmlElement answer, StringBuilder sb) {
        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable.getPrimaryKeyColumns()) {
            sb.setLength(0);
            if (and) {
                sb.append("  AND ");
            } else {
                sb.append("WHERE ");
                and = true;
            }

            sb.append(getEscapedColumnName(introspectedColumn));
            sb.append(" = ");
            sb.append(getParameterClause(introspectedColumn));
            answer.addElement(new LTextElement(sb.toString()));
        }
    }

    private void addDeleteElements(LXmlElement answer) {
        answer.addElement(new LTextElement("DELETE FROM "));
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement(sb.toString()));
        sb.setLength(0);
        sb.append("WHERE id=#{id}");
        answer.addElement(new LTextElement(sb.toString()));
//        whereCase(answer, sb);
    }
}
