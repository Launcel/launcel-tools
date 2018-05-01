/**
 * Copyright 2006-2016 the original author or authors.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements;

import org.mybatis.generator.api.IntrospectedColumn;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.XmlElement;
import org.mybatis.generator.codegen.mybatis3.MyBatis3FormattingUtilities;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;
import xyz.launcel.lang.StringUtils;

import java.util.Iterator;

import static xyz.launcel.generator.api.utils.OutputUtils.newLine;
import static xyz.launcel.generator.api.utils.OutputUtils.xmlIndent;

/**
 * @author Launcel
 */
public class BaseColumnElementGenerator extends AbstractXmlElementGenerator {
    
    public BaseColumnElementGenerator() {
        super();
    }
    
    @Override
    public void addElements(XmlElement parentElement) {
        LXmlElement answer = new LXmlElement("sql");
        
        answer.addAttribute(new Attribute("id", "BaseColumn"));
        
        context.getCommentGenerator().addComment(answer);
        
        StringBuilder                sb     = new StringBuilder();
        boolean                      hasLen = false;
        Iterator<IntrospectedColumn> iter   = introspectedTable.getNonBLOBColumns().iterator();
        while (iter.hasNext()) {
            String        column      = MyBatis3FormattingUtilities.getSelectListPhrase(iter.next());
            StringBuilder aliasColumn = new StringBuilder();
            if (column.contains("_")) {
                hasLen = true;
                String[] columnSeg = column.split("_");
                for (int i = 0; i < columnSeg.length; i++) {
                    if (i == 0) {
                        aliasColumn.append(columnSeg[i]);
                    } else {
                        aliasColumn.append(StringUtils.capitalize(columnSeg[i]));
                    }
                }
            }
            sb.append("`" + column + "`");
            if (hasLen) {
                sb.append(" as ").append(aliasColumn);
            }
            if (iter.hasNext()) {
                sb.append(",");
                newLine(sb);
                xmlIndent(sb, 2);
            }
            hasLen = false;
        }
        
        if (sb.length() > 0) {
            answer.addElement(new LTextElement(sb.toString()));
        }
        
        if (context.getPlugins().sqlMapBaseColumnListElementGenerated(answer, introspectedTable)) {
            parentElement.addElement(answer);
        }
    }
    
    
}
