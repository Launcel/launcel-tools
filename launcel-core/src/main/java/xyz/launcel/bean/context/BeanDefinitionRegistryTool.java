/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.20
 * Version: 1.0
 */

package xyz.launcel.bean.context;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;

public interface BeanDefinitionRegistryTool
{

    static void registryBean(String beanName, BeanDefinitionRegistry registry, AnnotatedGenericBeanDefinition abd)
    {
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    static AnnotatedGenericBeanDefinition decorateAbd(Class clazz)
    {
        ScopeMetadataResolver          scopeMetadataResolver = new AnnotationScopeMetadataResolver();
        AnnotatedGenericBeanDefinition abd                   = new AnnotatedGenericBeanDefinition(clazz);
        ScopeMetadata                  metadata              = scopeMetadataResolver.resolveScopeMetadata(abd);
        abd.setScope(metadata.getScopeName());
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        return abd;
    }
}
