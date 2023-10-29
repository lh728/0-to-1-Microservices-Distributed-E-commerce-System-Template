package io.renren.common.utils;

import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.core.type.filter.AssignableTypeFilter;

import java.lang.annotation.Annotation;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 查询特定的类，该工具下的方法应该在程序启动完成之后才可以使用
 */
public class BeanFindUtils {

    private static ClassPathScanningCandidateComponentProvider scanner;

    /**
     * 查询指定了特定注解的类
     * @param basePackage 类所存放的包名
     * @param annotation 特定的注解
     * @return 目标类
     */
    public static Set<Class<?>> findAnnotation(String basePackage, Class<? extends Annotation> annotation) {
        ClassPathScanningCandidateComponentProvider scanner = getInstance();
        scanner.addIncludeFilter(new AnnotationTypeFilter(annotation));
        return getClasses(basePackage, scanner);
    }

    /**
     * 查询实现了特定接口的类
     * @param basePackage 类所存放的包名
     * @param interfaceClass 特定的接口
     * @return 目标类
     */
    public static Set<Class<?>> findInterface(String basePackage, Class<?> interfaceClass) {
        ClassPathScanningCandidateComponentProvider scanner = getInstance();
        scanner.addIncludeFilter(new AssignableTypeFilter(interfaceClass));
        return getClasses(basePackage, scanner);
    }


    private static Set<Class<?>> getClasses(String basePackage, ClassPathScanningCandidateComponentProvider scanner) {
        Set<BeanDefinition> beanDefinitions = scanner.findCandidateComponents(basePackage);
        return beanDefinitions.stream()
                .map(BeanDefinition::getBeanClassName)
                .map(className -> {
                    try {
                        return Class.forName(className);
                    } catch (ClassNotFoundException e) {
                        throw new RuntimeException(e);
                    }
                })
                .collect(Collectors.toSet());
    }

    private static ClassPathScanningCandidateComponentProvider getInstance(){
        if (scanner == null){
            scanner = new ClassPathScanningCandidateComponentProvider(false);
        }
        return scanner;
    }

}