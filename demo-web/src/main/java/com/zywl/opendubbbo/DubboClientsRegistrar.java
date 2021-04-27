package com.zywl.opendubbbo;

import org.springframework.beans.factory.annotation.AnnotatedBeanDefinition;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.BeanDefinitionHolder;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.support.BeanDefinitionReaderUtils;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.context.EnvironmentAware;
import org.springframework.context.ResourceLoaderAware;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.annotation.ImportBeanDefinitionRegistrar;
import org.springframework.core.env.Environment;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.type.AnnotationMetadata;
import org.springframework.core.type.ClassMetadata;
import org.springframework.core.type.filter.AbstractClassTestingTypeFilter;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.util.ClassUtils;
import org.springframework.util.StringUtils;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author ljq 72088838
 * @version 1.0.0
 * @Description
 * @createTime 2021年04月23日 09:27:00
 */
public class DubboClientsRegistrar implements ImportBeanDefinitionRegistrar, ResourceLoaderAware, EnvironmentAware {

    private ResourceLoader resourceLoader;

    private Environment environment;

    @Override
    public void setEnvironment(Environment environment) {
        this.environment = environment;
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
    protected ClassPathScanningCandidateComponentProvider getScanner() {
        return new ClassPathScanningCandidateComponentProvider(false, this.environment) {
            @Override
            protected boolean isCandidateComponent(
                    AnnotatedBeanDefinition beanDefinition) {
                boolean isCandidate = false;
                if (beanDefinition.getMetadata().isIndependent()) {
                    if (!beanDefinition.getMetadata().isAnnotation()) {
                        isCandidate = true;
                    }
                }
                return isCandidate;
            }
        };
    }

    protected Set<String> getBasePackages(AnnotationMetadata importingClassMetadata) {
        Map<String, Object> attributes = importingClassMetadata
                .getAnnotationAttributes(EnableDubboClients.class.getCanonicalName());

        Set<String> basePackages = new HashSet<>();
        for (String pkg : (String[]) attributes.get("value")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        /*for (String pkg : (String[]) attributes.get("basePackages")) {
            if (StringUtils.hasText(pkg)) {
                basePackages.add(pkg);
            }
        }
        for (Class<?> clazz : (Class[]) attributes.get("basePackageClasses")) {
            basePackages.add(ClassUtils.getPackageName(clazz));
        }*/

        if (basePackages.isEmpty()) {
            basePackages.add(
                    ClassUtils.getPackageName(importingClassMetadata.getClassName()));
        }
        return basePackages;
    }

    @Override
    public void registerBeanDefinitions(AnnotationMetadata metadata, BeanDefinitionRegistry registry) {

        ClassPathScanningCandidateComponentProvider scanner = getScanner();
        scanner.setResourceLoader(this.resourceLoader);
        scanner.addIncludeFilter(new AnnotationTypeFilter(DubboClient.class));
        Set<String> basePackages;

        Map<String, Object> attrs = metadata.getAnnotationAttributes(EnableDubboClients.class.getName());
        AnnotationTypeFilter annotationTypeFilter = new AnnotationTypeFilter(
                DubboClient.class);
        final Class<?>[] clients = attrs == null ? null
                : (Class<?>[]) attrs.get("clients");
        if (clients == null || clients.length == 0) {
            scanner.addIncludeFilter(annotationTypeFilter);
            basePackages = getBasePackages(metadata);
        }
        else {
            final Set<String> clientClasses = new HashSet<>();
            basePackages = new HashSet<>();
            for (Class<?> clazz : clients) {
                basePackages.add(ClassUtils.getPackageName(clazz));
                clientClasses.add(clazz.getCanonicalName());
            }
            AbstractClassTestingTypeFilter filter = new AbstractClassTestingTypeFilter() {
                @Override
                protected boolean match(ClassMetadata metadata) {
                    String cleaned = metadata.getClassName().replaceAll("\\$", ".");
                    return clientClasses.contains(cleaned);
                }
            };
//            scanner.addIncludeFilter(new FeignClientsRegistrar.AllTypeFilter(Arrays.asList(filter, annotationTypeFilter)));
        }

        for (String basePackage : basePackages) {
            Set<BeanDefinition> candidateComponents = scanner
                    .findCandidateComponents(basePackage);
            for (BeanDefinition candidateComponent : candidateComponents) {
                if (candidateComponent instanceof AnnotatedBeanDefinition) {
                    // verify annotated class is an interface
                    AnnotatedBeanDefinition beanDefinition = (AnnotatedBeanDefinition) candidateComponent;
                    AnnotationMetadata annotationMetadata = beanDefinition.getMetadata();


                    Map<String, Object> attributes = annotationMetadata
                            .getAnnotationAttributes(DubboClient.class.getCanonicalName());

//                    String name = getClientName(attributes);
                    String name ="";
                    registerClientConfiguration(registry, name,
                            attributes.get("configuration"));
                    registerFeignClient(registry, annotationMetadata, attributes);
                }
            }
        }
    }
    private void registerClientConfiguration(BeanDefinitionRegistry registry, Object name,
                                             Object configuration) {
//        BeanDefinitionBuilder builder = BeanDefinitionBuilder
//                .genericBeanDefinition(FeignClientSpecification.class);
//        builder.addConstructorArgValue(name);
//        builder.addConstructorArgValue(configuration);
//        registry.registerBeanDefinition(
//                name + "." + FeignClientSpecification.class.getSimpleName(),
//                builder.getBeanDefinition());
    }

    private void registerFeignClient(BeanDefinitionRegistry registry,
                                     AnnotationMetadata annotationMetadata, Map<String, Object> attributes) {
        String className = annotationMetadata.getClassName();
        BeanDefinitionBuilder definition = BeanDefinitionBuilder
                .genericBeanDefinition(DubboClientFactoryBean.class);
//        validate(attributes);
//        definition.addPropertyValue("url", getUrl(attributes));
//        definition.addPropertyValue("path", getPath(attributes));
//        String name = getName(attributes);
//        definition.addPropertyValue("name", name);
//        String contextId = getContextId(attributes);
//        definition.addPropertyValue("contextId", contextId);
        definition.addPropertyValue("type", className);
//        definition.addPropertyValue("decode404", attributes.get("decode404"));
//        definition.addPropertyValue("fallback", attributes.get("fallback"));
//        definition.addPropertyValue("fallbackFactory", attributes.get("fallbackFactory"));
//        definition.setAutowireMode(AbstractBeanDefinition.AUTOWIRE_BY_TYPE);
//
//        String alias = contextId + "FeignClient";
        AbstractBeanDefinition beanDefinition = definition.getBeanDefinition();
//
//        boolean primary = (Boolean) attributes.get("primary"); // has a default, won't be
//        // null
//
//        beanDefinition.setPrimary(primary);
//
//        String qualifier = getQualifier(attributes);
//        if (StringUtils.hasText(qualifier)) {
//            alias = qualifier;
//        }
//
        BeanDefinitionHolder holder = new BeanDefinitionHolder(beanDefinition, className,
                new String[] { "tts" });
        BeanDefinitionReaderUtils.registerBeanDefinition(holder, registry);
    }
}
