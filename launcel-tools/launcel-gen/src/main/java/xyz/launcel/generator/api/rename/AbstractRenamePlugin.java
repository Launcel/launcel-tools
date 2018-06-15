package xyz.launcel.generator.api.rename;

import org.mybatis.generator.api.PluginAdapter;
import xyz.launcel.lang.StringUtils;

import java.util.regex.Pattern;

public abstract class AbstractRenamePlugin extends PluginAdapter {

    protected String searchCharacter;

    protected String replaceCharacter;

    protected Pattern pattern;

    protected AbstractRenamePlugin() {
    }

    protected void initProp() {
        String searchTemp = properties.getProperty("searchCharacter");
        String replaceTemp = properties.getProperty("replaceCharacter");
        if (StringUtils.isNotBlank(searchTemp) && StringUtils.isNotBlank(replaceTemp)) {
            searchCharacter = searchTemp;
            replaceCharacter = replaceTemp;
            pattern = Pattern.compile(searchCharacter);
        }
    }


    protected String getSearchCharacter() {
        return searchCharacter;
    }

    protected void setSearchCharacter(String searchCharacter) {
        this.searchCharacter = searchCharacter;
    }

    protected String getReplaceCharacter() {
        return replaceCharacter;
    }

    protected void setReplaceCharacter(String replaceCharacter) {
        this.replaceCharacter = replaceCharacter;
    }

    protected Pattern getPattern() {
        return pattern;
    }

    protected void setPattern(Pattern pattern) {
        this.pattern = pattern;
    }
}
