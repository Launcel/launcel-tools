package xyz.launcel.generator.api.dom.xml;

import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Element;
import org.mybatis.generator.api.dom.xml.XmlElement;
import xyz.launcel.generator.api.dom.OutputUtilities;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Launcel
 */
public class LXmlElement extends XmlElement {

    private List<Attribute> attributes;

    private List<Element> elements;

    private String name;

    public LXmlElement(String name) {
        super(name);
        attributes = new ArrayList<>();
        elements = new ArrayList<>();
        this.name = name;
    }

    public LXmlElement(LXmlElement original) {
        super(original);
        attributes = new ArrayList<>();
        attributes.addAll(original.attributes);
        elements = new ArrayList<>();
        elements.addAll(original.elements);
        this.name = original.name;
    }

    public List<Attribute> getAttributes() {
        return attributes;
    }

    public void addAttribute(Attribute attribute) {
        attributes.add(attribute);
    }

    public List<Element> getElements() {
        return elements;
    }

    public void addElement(Element element) {
        elements.add(element);
    }

    public void addElement(int index, Element element) {
        elements.add(index, element);
    }

    public String getName() {
        return name;
    }

    @Override
    public String getFormattedContent(int indentLevel) {
        StringBuilder sb = new StringBuilder();

        OutputUtilities.xmlIndent(sb, indentLevel);
        sb.append('<');
        sb.append(name);

        Collections.sort(attributes);
        for (Attribute att : attributes) {
            sb.append(' ');
            sb.append(att.getFormattedContent());
        }

        if (elements.size() > 0) {
            sb.append(">");
            for (Element element : elements) {
                OutputUtilities.newLine(sb);
                sb.append(element.getFormattedContent(indentLevel + 1));
            }
            OutputUtilities.newLine(sb);
            OutputUtilities.xmlIndent(sb, indentLevel);
            sb.append("</");
            sb.append(name);
            sb.append('>');

        } else {
            sb.append(" />");
        }

        return sb.toString();
    }

    public void setName(String name) {
        this.name = name;
    }
}
