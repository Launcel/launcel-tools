/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.20
 * Version: 1.0
 */

package xyz.launcel.common.support;

import lombok.var;
import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.lang.NonNull;

public interface BeanDefinitionRegistryTool
{

    static void registryBean(@NonNull String beanName, BeanDefinitionRegistry registry, @NonNull AnnotatedGenericBeanDefinition abd)
    {
        var definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    static AnnotatedGenericBeanDefinition decorateAbd(@NonNull Class clazz)
    {
        var scopeMetadataResolver = new AnnotationScopeMetadataResolver();
        var abd                   = new AnnotatedGenericBeanDefinition(clazz);
        var metadata              = scopeMetadataResolver.resolveScopeMetadata(abd);
        abd.setScope(metadata.getScopeName());
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        return abd;
    }
}
