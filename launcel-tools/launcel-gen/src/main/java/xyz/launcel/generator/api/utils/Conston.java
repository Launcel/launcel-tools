package xyz.launcel.generator.api.utils;

import org.mybatis.generator.config.Context;
import xyz.launcel.lang.StringUtils;

import java.util.Properties;

public class Conston
{

    private static boolean addDefaultMethod = true;

    private static boolean useEnabledColumn = true;

    private static String enabledColumnName = "enabled";

    private static String enabledColumnValue = "1";

    private static boolean useAnnotation = true;

    private static boolean addRemark = true;

    private static boolean showDoc = false;


    public static boolean isAddDefaultMethod()
    {
        return addDefaultMethod;
    }

    public static boolean isUseEnabledColumn()
    {
        return useEnabledColumn;
    }

    public static String getEnabledColumnName()
    {
        return enabledColumnName;
    }

    public static String getEnabledColumnValue()
    {
        return enabledColumnValue;
    }

    public static boolean isUseAnnotation()
    {
        return useAnnotation;
    }

    public static boolean isAddRemark()
    {
        return addRemark;
    }

    public static boolean isShowDoc()
    {
        return showDoc;
    }

    public static void initPropertites(Context context)
    {
        if ("false".equalsIgnoreCase(context.getProperty("addDefaultMethod")))
        {
            addDefaultMethod = false;
        }
        if ("false".equalsIgnoreCase(context.getProperty("useEnabledColumn")))
        {
            useEnabledColumn = false;
        }
        String enabledColumnNameTemp = context.getProperty("enabledColumnName");
        if (StringUtils.isNotBlank(enabledColumnNameTemp))
        {
            enabledColumnName = enabledColumnNameTemp;
        }
        String enabledColumnValueTemp = context.getProperty("enabledColumnValue");
        if (StringUtils.isNotBlank(enabledColumnValueTemp))
        {
            enabledColumnValue = enabledColumnValueTemp;
        }
        if (StringUtils.isFalse(context.getProperty("useAnnotation")))
        {
            useAnnotation = false;
        }
        if (StringUtils.isFalse(context.getProperty("addRemark")))
        {
            addRemark = false;
        }
        if (StringUtils.isTrue(context.getProperty("showDoc")))
        {
            showDoc = true;
        }

    }

    public static class JAVADAOPlugPropertites
    {
        private static String  baseDAOPackage  = "xyz.launcel.dao.BaseRepository";
        private static boolean useGeneralizate = false;
        private static boolean useBaseDAO      = true;

        public static void initPropertites(Properties properties)
        {
            String baseDAOPackageTemp = properties.getProperty("baseDAOPackage");
            if (StringUtils.isNotBlank(baseDAOPackageTemp))
            {
                baseDAOPackage = baseDAOPackageTemp;
            }
            String useGeneralizateTemp = properties.getProperty("useGeneralizate");
            if (StringUtils.isTrue(useGeneralizateTemp))
            {
                useGeneralizate = true;
            }
            String useBaseDAOTemp = properties.getProperty("useBaseDAO");
            if (StringUtils.isTrue(useBaseDAOTemp))
            {
                useBaseDAO = true;
            }
        }

        public static String getBaseDAOPackage()
        {
            return baseDAOPackage;
        }

        public static boolean isUseGeneralizate()
        {
            return useGeneralizate;
        }

        public static boolean isUseBaseDAO()
        {
            return useBaseDAO;
        }
    }
}
