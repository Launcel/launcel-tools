//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package xyz.launcel.generator.codegen.mybatis3.xmlmapper;

import org.mybatis.generator.api.FullyQualifiedTable;
import org.mybatis.generator.api.dom.xml.Attribute;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractXmlGenerator;
import org.mybatis.generator.internal.util.messages.Messages;
import xyz.launcel.generator.api.utils.Conston;
import xyz.launcel.generator.api.dom.xml.LTextElement;
import xyz.launcel.generator.api.dom.xml.LXmlElement;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.AbstractXmlElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.AddElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.BaseColumnElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.BaseSqlElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.CountElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.DeleteByKeyElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.GetElement;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.QueryElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.QueryPagingElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.UpdateSelectiveElementGenerator;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.elements.UpdateSqlElementGenerator;

public class XMLMapperGenerator extends AbstractXmlGenerator {

    public XMLMapperGenerator() {
    }

    protected LXmlElement getSqlMapElement() {
        FullyQualifiedTable table = this.introspectedTable.getFullyQualifiedTable();
        this.progressCallback.startTask(Messages.getString("Progress.12", table.toString()));
        LXmlElement answer = new LXmlElement("mapper");
        String namespace = this.introspectedTable.getMyBatis3SqlMapNamespace();
        answer.addAttribute(new Attribute("namespace", namespace));
        this.context.getCommentGenerator().addRootComment(answer);

        if (Conston.addDefaultMethod) {
            this.addBaseColumnElement(answer);

            this.addBaseSql(answer);
            this.addUpdateSql(answer);

            this.addGetElement(answer);
            this.addQueryElementGenerator(answer);

            this.addAddElement(answer);

            this.addUpdateByPrimaryKeySelectiveElement(answer);

            this.addDeleteByKeyElement(answer);

            this.addCountByElement(answer);
            this.addQuetyPagingElement(answer);
        }
        return answer;
    }

    protected void addBaseColumnElement(LXmlElement parentElement) {
//        if (this.introspectedTable.getRules().generateBaseColumnList()) {
        AbstractXmlElementGenerator elementGenerator = new BaseColumnElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
//        }
    }

    protected void addBaseSql(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new BaseSqlElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addUpdateSql(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new UpdateSqlElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addQueryElementGenerator(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new QueryElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addAddElement(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new AddElementGenerator(true);
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }


    protected void addUpdateByPrimaryKeySelectiveElement(LXmlElement parentElement) {
//        if (this.introspectedTable.getRules().generateUpdateByPrimaryKeySelective()) {
        AbstractXmlElementGenerator elementGenerator = new UpdateSelectiveElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
//        }
    }

    protected void addCountByElement(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new CountElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addQuetyPagingElement(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new QueryPagingElementGenerator();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addDeleteByKeyElement(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new DeleteByKeyElementGenerator(true);
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void addGetElement(LXmlElement parentElement) {
        AbstractXmlElementGenerator elementGenerator = new GetElement();
        this.initializeAndExecuteGenerator(elementGenerator, parentElement);
    }

    protected void initializeAndExecuteGenerator(AbstractXmlElementGenerator elementGenerator, LXmlElement parentElement) {
        elementGenerator.setContext(this.context);
        elementGenerator.setIntrospectedTable(this.introspectedTable);
        elementGenerator.setProgressCallback(this.progressCallback);
        elementGenerator.setWarnings(this.warnings);
        parentElement.addElement(new LTextElement(""));
        elementGenerator.addElements(parentElement);
    }

    public Document getDocument() {
        Document document = new Document("-//mybatis.org//DTD Mapper 3.0//EN",
                "http://mybatis.org/dtd/mybatis-3-mapper.dtd");
        document.setRootElement(this.getSqlMapElement());
        if (!this.context.getPlugins().sqlMapDocumentGenerated(document, this.introspectedTable)) {
            document = null;
        }

        return document;
    }

}
