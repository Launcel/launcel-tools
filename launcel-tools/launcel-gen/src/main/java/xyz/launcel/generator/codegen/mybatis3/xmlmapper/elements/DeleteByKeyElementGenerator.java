package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getEscapedColumnName;
import static org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities.getParameterClause;
import static xyz.launcel.generator.api.dom.OutputUtilities.xmlIndent;

/**
 * @author Launcel
 */
public class DeleteByKeyElementGenerator extends AbstractXmlElementGenerator {

    public DeleteByKeyElementGenerator(boolean isSimple) {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {

        LXmlElement answer = new LXmlElement("update");
        answer.addAttribute(new Attribute("id", "delete"));
        answer.addAttribute(new Attribute("parameterType", getParamType()));
        context.getCommentGenerator().addComment(answer);
        StringBuilder sb = new StringBuilder();
        answer.addElement(new LTextElement("UPDATE "));
        xmlIndent(sb, 3);
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement(sb.toString()));
        sb.setLength(0);
        sb.append("SET").append(" enabled=#{enabled}");
        answer.addElement(new LTextElement(sb.toString()));
        sb.setLength(0);
        sb.append("WHERE id=#{id}");
        answer.addElement(new LTextElement(sb.toString()));


        if (context.getPlugins().sqlMapDeleteByPrimaryKeyElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }

    @SuppressWarnings("unused")
    private void addUpdateElements(LXmlElement answer) {
        answer.addElement(new LTextElement("UPDATE"));
        StringBuilder sb = new StringBuilder();
        xmlIndent(sb, 2);
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement(sb.toString()));

        for (IntrospectedColumn introspectedColumn : introspectedTable.getNonPrimaryKeyColumns()) {
            sb.setLength(0);
            String name = getEscapedColumnName(introspectedColumn);
            if (name.equalsIgnoreCase("enabled")) {
                sb.append("SET ").append(name);
                sb.append(" = ").append(getParameterClause(introspectedColumn));
            }
        }
        answer.addElement(new LTextElement(sb.toString()));

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

    @SuppressWarnings("unused")
    private void addDeleteElements(LXmlElement answer) {

        answer.addElement(new LTextElement("DELETE FROM "));
        StringBuilder sb = new StringBuilder();
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement("    " + sb.toString()));

        boolean and = false;
        for (IntrospectedColumn introspectedColumn : introspectedTable
                .getPrimaryKeyColumns()) {
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
}
