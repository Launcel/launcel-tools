/**
 * Copyright (C), 2018
 * Author: Launcel
 * Date: 18.1.20
 * Version: 1.0
 */

package xyz.launcel.hook;

import org.springframework.beans.factory.annotation.AnnotatedGenericBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.annotation.AnnotationConfigUtils;
import org.springframework.context.annotation.AnnotationScopeMetadataResolver;
import org.springframework.context.annotation.ScopeMetadata;
import org.springframework.context.annotation.ScopeMetadataResolver;

public class BeanDefinitionRegistryTool {

    public static void registryBean(String beanName, BeanDefinitionRegistry registry, AnnotatedGenericBeanDefinition abd) {
//        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(clazz);
//        ScopeMetadata scopeMetadata = new AnnotationScopeMetadataResolver().resolveScopeMetadata(abd);
//        abd.setScope(scopeMetadata.getScopeName());
//        String beanName = (dataBeanName != null ? dataBeanName : scopeMetadata.getScopeName());
//        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        BeanDefinitionHolder definitionHolder = new BeanDefinitionHolder(abd, beanName);
        BeanDefinitionReaderUtils.registerBeanDefinition(definitionHolder, registry);
    }

    // 装饰获取abd
    public static AnnotatedGenericBeanDefinition decorateAbd(Class clazz) {
        ScopeMetadataResolver scopeMetadataResolver = new AnnotationScopeMetadataResolver();
        AnnotatedGenericBeanDefinition abd = new AnnotatedGenericBeanDefinition(clazz);
        ScopeMetadata metadata = scopeMetadataResolver.resolveScopeMetadata(abd);
        abd.setScope(metadata.getScopeName());
        AnnotationConfigUtils.processCommonDefinitionAnnotations(abd);
        return abd;
    }
}
