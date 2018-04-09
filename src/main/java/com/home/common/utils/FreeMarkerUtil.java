package com.home.common.utils;

import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.Map;

import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class FreeMarkerUtil {
	public static String renderString(String templateString, Map<String, ?> model) {
		try {
			Configuration e = new Configuration(Configuration.VERSION_2_3_23);
			e.setClassicCompatible(true);
			StringWriter result = new StringWriter();
			Template t = new Template("default", new StringReader(templateString), e);
			t.process(model, result);
			return result.toString();
		} catch (Exception arg4) {
			return null;
		}
	}

	public static String renderTemplate(Template template, Object model) {
		try {
			StringWriter e = new StringWriter();
			template.process(model, e);
			return e.toString();
		} catch (Exception arg2) {
			return null;
		}
	}

	public static Configuration buildConfiguration(String directory) throws IOException {
		Configuration cfg = new Configuration(Configuration.VERSION_2_3_23);
		Resource path = new DefaultResourceLoader().getResource(directory);
		cfg.setDirectoryForTemplateLoading(path.getFile());
		cfg.setClassicCompatible(true);
		return cfg;
	}
}