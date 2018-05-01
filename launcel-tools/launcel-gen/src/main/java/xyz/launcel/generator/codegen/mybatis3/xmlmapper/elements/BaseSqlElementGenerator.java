package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import xyz.launcel.generator.api.utils.Conston;
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

//        boolean and = false;
        
        Iterator<IntrospectedColumn> var5 = this.introspectedTable.getAllColumns().iterator();
        
        IntrospectedColumn introspectedColumn;
        
        if (Conston.useEnabledColumn) {
            sb.setLength(0);
            sb.append("WHERE").append(" ").append("`").append(Conston.enabledColumnName).append("`").append("=").append(Conston.enabledColumnValue);
            answer.addElement(new LTextElement(sb.toString()));
        } else {
            sb.setLength(0);
            sb.append("WHERE");
            sb.append(" 1=1");
            answer.addElement(new LTextElement(sb.toString()));
        }
        
        while (var5.hasNext()) {
            
            sb.setLength(0);
            introspectedColumn = var5.next();
            String columnName = MyBatis3FormattingUtilities.getEscapedColumnName(introspectedColumn);
            if (Conston.useEnabledColumn || !columnName.equals(Conston.enabledColumnName)) {
                LXmlElement isNotNullElement = new LXmlElement("if");
                sb.setLength(0);
                sb.append("param.").append(introspectedColumn.getJavaProperty());
                sb.append(" != null");
                isNotNullElement.addAttribute(new Attribute("test", sb.toString()));
                sb.setLength(0);
                sb.append("AND ");
                sb.append("`").append(columnName).append("`");
                sb.append(" = ");
                sb.append(MyBatis3FormattingUtilities.getParameterClause(introspectedColumn, "param."));
                isNotNullElement.addElement(new LTextElement(sb.toString()));
                answer.addElement(isNotNullElement);
            }
        }
        
        if (this.context.getPlugins().sqlMapSelectByPrimaryKeyElementGenerated(answer, this.introspectedTable)) {
            parentElement.addElement(answer);
        }
        
    }
}
