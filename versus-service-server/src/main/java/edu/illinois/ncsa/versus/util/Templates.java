/**
 * 
 */
package edu.illinois.ncsa.versus.util;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

/**
 * FreeMarker utilities.
 * 
 * @author Luigi Marini
 * 
 */
public class Templates {

	public static String create(Configuration freeMarkerConfig,
			String fileTemplate, Object data, String dataId)
			throws IOException, TemplateException {
		Template template = freeMarkerConfig.getTemplate(fileTemplate);
		Writer out = new StringWriter();
		Map<String, Object> root = new HashMap<String, Object>();
		root.put(dataId, data);
		template.process(root, out);
		return out.toString();
	}

	public static String create(Configuration freeMarkerConfig,
			String fileTemplate, Map<String, Object> data) throws IOException,
			TemplateException {
		Template template = freeMarkerConfig.getTemplate(fileTemplate);
		Writer out = new StringWriter();
		template.process(data, out);
		return out.toString();
	}

}
