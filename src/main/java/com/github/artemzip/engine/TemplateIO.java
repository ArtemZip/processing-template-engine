package com.github.artemzip.engine;

import com.github.artemzip.engine.exceptions.TemplateException;
import com.github.artemzip.engine.exceptions.TemplateNotFoundException;
import lombok.NoArgsConstructor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.github.artemzip.engine.Constants.TEMPLATE_NOT_FOUND;
import static com.github.artemzip.engine.TemplateProcessor.Processor.USE_OPERATOR_PROCESSOR;
import static com.github.artemzip.engine.TemplateProcessor.Processor.VAR_PROCESSOR;
import static com.github.artemzip.engine.TemplateProcessor.get;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;
import static org.apache.commons.io.FileUtils.openInputStream;


@NoArgsConstructor(access = PRIVATE)
class TemplateIO {

    static void loadTemplate(Template template) throws IOException, TemplateException {
        if(template.getContent() != null && !template.getContent().isBlank()) {
            return;
        }

        var file = new File(template.getPath());
        if(!file.exists() || !file.isFile() || !file.canRead()) {
            throw new TemplateNotFoundException(format(TEMPLATE_NOT_FOUND, template.getName(), template.getPath()));
        }

        String content = IOUtils.toString(openInputStream(file), StandardCharsets.UTF_8);
        template.setContent(content);
    }

    static TemplateWriter parseTemplate(Template template, TemplateContext context) throws TemplateException {
        String result = get(USE_OPERATOR_PROCESSOR).process(template, context);

        Template inProgress = new Template(result);
        inProgress.setName(template.getName());

        return new TemplateWriter(get(VAR_PROCESSOR).process(inProgress, context));
    }
}
