package xyz.launcel.generator.api.dom.xml;

import org.mybatis.generator.api.dom.xml.Element;
import xyz.launcel.generator.api.dom.OutputUtilities;

/**
 * @author Launcel
 */
public class LTextElement extends Element {
    private String content;

    public LTextElement(String content) {
        super();
        this.content = content;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();
        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append(content);
        return sb.toString();
    }

    /**
     * Gets the content.
     *
     * @return the content
     */
    public String getContent() {
        return content;
    }
}
