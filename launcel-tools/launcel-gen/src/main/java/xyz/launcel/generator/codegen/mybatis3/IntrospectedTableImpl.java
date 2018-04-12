package xyz.launcel.generator.codegen.mybatis3;

import org.mybatis.generator.api.GeneratedJavaFile;
import org.mybatis.generator.api.GeneratedXmlFile;
import org.mybatis.generator.api.IntrospectedTable;
import org.mybatis.generator.api.ProgressCallback;
import org.mybatis.generator.api.dom.java.CompilationUnit;
import org.mybatis.generator.api.dom.xml.Document;
import org.mybatis.generator.codegen.AbstractGenerator;
import org.mybatis.generator.codegen.AbstractJavaClientGenerator;
import org.mybatis.generator.codegen.AbstractJavaGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.AnnotatedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.JavaMapperGenerator;
import org.mybatis.generator.codegen.mybatis3.javamapper.MixedClientGenerator;
import org.mybatis.generator.codegen.mybatis3.model.BaseRecordGenerator;
import org.mybatis.generator.codegen.mybatis3.model.ExampleGenerator;
import org.mybatis.generator.codegen.mybatis3.model.PrimaryKeyGenerator;
import org.mybatis.generator.codegen.mybatis3.model.RecordWithBLOBsGenerator;
import org.mybatis.generator.config.Context;
import org.mybatis.generator.config.ModelType;
import org.mybatis.generator.config.PropertyRegistry;
import org.mybatis.generator.internal.ObjectFactory;
import org.mybatis.generator.internal.util.StringUtility;
import xyz.launcel.generator.Conston;
import xyz.launcel.generator.codegen.mybatis3.xmlmapper.XMLMapperGenerator;

import java.util.ArrayList;
import java.util.List;

public class IntrospectedTableImpl extends IntrospectedTable {

//    protected MyBatisGeneratorConfigurationParser parser;

    protected List<AbstractJavaGenerator> javaModelGenerators;

    protected List<AbstractJavaGenerator> clientGenerators;

    protected XMLMapperGenerator xmlMapperGenerator;

    public IntrospectedTableImpl() {
        super(TargetRuntime.MYBATIS3);
        javaModelGenerators = new ArrayList<>();
        clientGenerators = new ArrayList<>();
        setContext(new Context(ModelType.CONDITIONAL));
        initProp();
        super.setBaseResultMapId(getBaseRecordType());
    }

    private void initProp() {
        if ("false".equalsIgnoreCase(context.getProperty("addDefaultMethod"))) {
            Conston.addDefaultMethod = false;
        }
        if ("false".equalsIgnoreCase(context.getProperty("useEnabledColumn"))) {
            Conston.useEnabledColumn = false;
        }
        String enabledColumnNameTemp = context.getProperty("enabledColumnName");
        if (StringUtility.stringHasValue(enabledColumnNameTemp)) {
            Conston.enabledColumnName = enabledColumnNameTemp;
        }
        String enabledColumnValueTemp = context.getProperty("enabledColumnValue");
        if (StringUtility.stringHasValue(enabledColumnValueTemp)) {
            Conston.enabledColumnValue = enabledColumnValueTemp;
        }
        if ("false".equalsIgnoreCase(context.getProperty("useAnnotation"))) {
            Conston.useAnnotation = false;
        }
        if ("false".equalsIgnoreCase(context.getProperty("addRemark"))) {
            Conston.addRemark = false;
        }
    }

    @Override
    public String getBaseRecordType() {
        return super.getBaseRecordType();
    }

    @Override
    public String getBaseResultMapId() {
        return super.getBaseRecordType();
    }

    @Override
    public void calculateGenerators(List<String> warnings,
                                    ProgressCallback progressCallback) {
        calculateJavaModelGenerators(warnings, progressCallback);

        AbstractJavaClientGenerator javaClientGenerator = calculateClientGenerators(warnings, progressCallback);

        calculateXmlMapperGenerator(javaClientGenerator, warnings, progressCallback);
    }

    /**
     * Calculate xml mapper generator.
     *
     * @param javaClientGenerator the java client generator
     * @param warnings            the warnings
     * @param progressCallback    the progress callback
     */
    protected void calculateXmlMapperGenerator(AbstractJavaClientGenerator javaClientGenerator,
                                               List<String> warnings,
                                               ProgressCallback progressCallback) {
        xmlMapperGenerator = new XMLMapperGenerator();
        initializeAbstractGenerator(xmlMapperGenerator, warnings, progressCallback);
    }

    /**
     * Calculate client generators.
     *
     * @param warnings         the warnings
     * @param progressCallback the progress callback
     * @return true if an XML generator is required
     */
    protected AbstractJavaClientGenerator calculateClientGenerators(List<String> warnings,
                                                                    ProgressCallback progressCallback) {
        if (!rules.generateJavaClient()) {
            return null;
        }

        AbstractJavaClientGenerator javaGenerator = createJavaClientGenerator();
        if (javaGenerator == null) {
            return null;
        }

        initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
        clientGenerators.add(javaGenerator);

        return javaGenerator;
    }

    /**
     * Creates the java client generator.
     *
     * @return the abstract java client generator
     */
    protected AbstractJavaClientGenerator createJavaClientGenerator() {
        if (context.getJavaClientGeneratorConfiguration() == null) {
            return null;
        }

        String type = context.getJavaClientGeneratorConfiguration()
                .getConfigurationType();

        AbstractJavaClientGenerator javaGenerator;
        if ("XMLMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator();
        } else if ("MIXEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new MixedClientGenerator();
        } else if ("ANNOTATEDMAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new AnnotatedClientGenerator();
        } else if ("MAPPER".equalsIgnoreCase(type)) { //$NON-NLS-1$
            javaGenerator = new JavaMapperGenerator();
        } else {
            javaGenerator = (AbstractJavaClientGenerator) ObjectFactory.createInternalObject(type);
        }

        return javaGenerator;
    }

    /**
     * Calculate java model generators.
     *
     * @param warnings         the warnings
     * @param progressCallback the progress callback
     */
    protected void calculateJavaModelGenerators(List<String> warnings, ProgressCallback progressCallback) {
        if (getRules().generateExampleClass()) {
            AbstractJavaGenerator javaGenerator = new ExampleGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generatePrimaryKeyClass()) {
            AbstractJavaGenerator javaGenerator = new PrimaryKeyGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateBaseRecordClass()) {
            AbstractJavaGenerator javaGenerator = new BaseRecordGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }

        if (getRules().generateRecordWithBLOBsClass()) {
            AbstractJavaGenerator javaGenerator = new RecordWithBLOBsGenerator();
            initializeAbstractGenerator(javaGenerator, warnings, progressCallback);
            javaModelGenerators.add(javaGenerator);
        }
    }

    /**
     * Initialize abstract generator.
     *
     * @param abstractGenerator the abstract generator
     * @param warnings          the warnings
     * @param progressCallback  the progress callback
     */
    protected void initializeAbstractGenerator(AbstractGenerator abstractGenerator, List<String> warnings, ProgressCallback progressCallback) {
        if (abstractGenerator == null) {
            return;
        }

        abstractGenerator.setContext(context);
        abstractGenerator.setIntrospectedTable(this);
        abstractGenerator.setProgressCallback(progressCallback);
        abstractGenerator.setWarnings(warnings);
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.IntrospectedTable#getGeneratedJavaFiles()
     */
    @Override
    public List<GeneratedJavaFile> getGeneratedJavaFiles() {
        List<GeneratedJavaFile> answer = new ArrayList<>();

        for (AbstractJavaGenerator javaGenerator : javaModelGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        context.getJavaModelGeneratorConfiguration().getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter());
                answer.add(gjf);
            }
        }

        for (AbstractJavaGenerator javaGenerator : clientGenerators) {
            List<CompilationUnit> compilationUnits = javaGenerator.getCompilationUnits();
            for (CompilationUnit compilationUnit : compilationUnits) {
                GeneratedJavaFile gjf = new GeneratedJavaFile(compilationUnit,
                        context.getJavaClientGeneratorConfiguration().getTargetProject(),
                        context.getProperty(PropertyRegistry.CONTEXT_JAVA_FILE_ENCODING),
                        context.getJavaFormatter());
                answer.add(gjf);
            }
        }

        return answer;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.IntrospectedTable#getGeneratedXmlFiles()
     */
    @Override
    public List<GeneratedXmlFile> getGeneratedXmlFiles() {
        List<GeneratedXmlFile> answer = new ArrayList<>();

        if (xmlMapperGenerator != null) {
            Document document = xmlMapperGenerator.getDocument(context);
            GeneratedXmlFile gxf = new GeneratedXmlFile(document,
                    getMyBatis3XmlMapperFileName(), getMyBatis3XmlMapperPackage(),
                    context.getSqlMapGeneratorConfiguration().getTargetProject(),
                    true, context.getXmlFormatter());
            if (context.getPlugins().sqlMapGenerated(gxf, this)) {
                answer.add(gxf);
            }
        }

        return answer;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.IntrospectedTable#getGenerationSteps()
     */
    @Override
    public int getGenerationSteps() {
        return javaModelGenerators.size() + clientGenerators.size() + (xmlMapperGenerator == null ? 0 : 1);
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.IntrospectedTable#isJava5Targeted()
     */
    @Override
    public boolean isJava5Targeted() {
        return true;
    }

    /* (non-Javadoc)
     * @see org.mybatis.generator.api.IntrospectedTable#requiresXMLGenerator()
     */
    @Override
    public boolean requiresXMLGenerator() {
        AbstractJavaClientGenerator javaClientGenerator = createJavaClientGenerator();
        return javaClientGenerator != null && javaClientGenerator.requiresXMLGenerator();
    }

}
