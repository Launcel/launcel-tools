package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.dom.OutputUtilities;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;

public class UpdateSelectiveElementGenerator extends AbstractXmlElementGenerator {

    public UpdateSelectiveElementGenerator() {
        super();
    }

    @Override
    public void addElements(XmlElement parentElement) {
        LXmlElement answer = new LXmlElement("update");

        answer.addAttribute(new Attribute("id", "update"));

        String parameterType = introspectedTable.getBaseRecordType();

        answer.addAttribute(new Attribute("parameterType", parameterType));

        context.getCommentGenerator().addComment(answer);

        StringBuilder sb = new StringBuilder();

        sb.append("UPDATE");
        OutputUtilities.newLine(sb);
        OutputUtilities.xmlIndent(sb, 3);
        sb.append(introspectedTable.getFullyQualifiedTableNameAtRuntime());
        answer.addElement(new LTextElement(sb.toString()));

        answer.addElement(getUpdateSql());

        if (context.getPlugins().sqlMapUpdateByPrimaryKeySelectiveElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
}