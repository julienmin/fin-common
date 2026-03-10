package com.minbing.common.utils;

import com.google.common.collect.Maps;
import groovy.lang.GroovyClassLoader;
import groovy.lang.GroovyObject;
import groovy.text.SimpleTemplateEngine;
import groovy.text.Template;
import groovy.text.TemplateEngine;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;

public class GroovyUtil {

    private static final TemplateEngine GROOVY_TEMPLATE_ENGINE = new SimpleTemplateEngine();
    private static final Map<String, Template> TEMPLATE_CACHES = Maps.newConcurrentMap();

    private static final GroovyClassLoader GROOVY_CLASS_LOADER = new GroovyClassLoader(GroovyUtil.class.getClassLoader());
    private static final Map<String, Class<?>> GROOVY_OBJECT_CACHES = Maps.newConcurrentMap();

    /**
     * 跟进groovy脚本生成模板
     * @param templateStr
     * @return
     */
    public static Template createTemplate(String templateStr) {
        if (StringUtils.isBlank(templateStr)) {
            return null;
        }

        Template template = TEMPLATE_CACHES.get(templateStr);
        if (template == null) {
            synchronized (TEMPLATE_CACHES) {
                try {
                    template = GROOVY_TEMPLATE_ENGINE.createTemplate(templateStr);
                    TEMPLATE_CACHES.put(templateStr, template);
                } catch (Exception e) {
                    throw new RuntimeException("Invalid templateStr=" + templateStr, e);
                }
            }
        }

        return template;
    }

    /**
     * 跟进groovy脚本生成模板并渲染
     * @param templateStr
     * @param context
     * @return
     */
    public static String renderTemplateStr(String templateStr, Map<String, Object> context) {
        if (StringUtils.isBlank(templateStr)) {
            return "";
        }

        try {
            Object ret = createTemplate(templateStr).make(context);
            return ret != null ? ret.toString() : "";
        } catch (Exception e) {
            throw new RuntimeException("Groovy script cannot render, templateStr=" + templateStr, e);
        }
    }

    /**
     * 跟进groovy脚本生成对象实例
     * @param groovyScriptPath
     * @return
     */
    public static GroovyObject parseInstance(String groovyScriptPath) {
        if (StringUtils.isBlank(groovyScriptPath)) {
            return null;
        }

        Class<?> clazz = GROOVY_OBJECT_CACHES.get(groovyScriptPath);
        if (clazz == null) {
            synchronized (GROOVY_OBJECT_CACHES) {
                try {
                    clazz = GROOVY_CLASS_LOADER.parseClass(groovyScriptPath);
                } catch (Exception e) {
                    throw new RuntimeException("Invalid groovyScriptPath=" + groovyScriptPath, e);
                }
            }
        }

        try {
            return (GroovyObject) clazz.newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Groovy script cannot create instance, groovyScriptPath=" + groovyScriptPath, e);
        }
    }
}
