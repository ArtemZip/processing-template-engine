package com.github.artemzip.engine;

import com.github.artemzip.engine.exceptions.TemplateException;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static com.github.artemzip.engine.TemplateIO.loadTemplate;
import static com.github.artemzip.engine.TemplateIO.parseTemplate;

public class TemplateEngine {
    private static TemplateEngine instance;
    private final Map<String, Template> templates;

    private TemplateEngine() {
        templates = new LinkedHashMap<>();
    }

    /**
     * Registering template for future processing
     *
     * @param name - unique name of your new template, if @param name already exists, template will be replaced
     * @param templatePath - template path from resource folder
     * @return TemplateWriter for writing file into java source file.
     */
    public TemplateEngine registerTemplate(String name, String templatePath) {
        templates.put(name, new Template(name, templatePath));
        return instance;
    }

    /**
     * Loading template if not loaded and applying context to it
     *
     * @param name of registered template
     * @param context - variables' values used for template
     * @return TemplateWriter for writing processed template to java source file
     *
     * @throws TemplateException, if template is invalid
     */
    public TemplateWriter processTemplate(String name, TemplateContext context) throws IOException, TemplateException {
        loadTemplate(templates.get(name));
        return parseTemplate(templates.get(name), context);
    }

    public static TemplateEngine getInstance() {
        if(instance == null) {
            instance = new TemplateEngine();
        }
        return instance;
    }
}
