package com.github.artemzip.engine;

import com.github.artemzip.engine.exceptions.MissingTemplateVarException;
import com.github.artemzip.engine.exceptions.TemplateException;
import com.github.artemzip.engine.exceptions.UseOperatorException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collection;
import java.util.regex.Pattern;

import static com.github.artemzip.engine.Constants.CANNOT_CAST_USE_CTX;
import static com.github.artemzip.engine.Constants.EMPTY_BRACKETS;
import static com.github.artemzip.engine.Constants.MISSING_USE_END;
import static com.github.artemzip.engine.Constants.MISSING_USE_START;
import static com.github.artemzip.engine.Constants.MISSING_USE_VAR;
import static com.github.artemzip.engine.Constants.MISSING_USE_VAR_VALUE;
import static com.github.artemzip.engine.Constants.MISSING_VAR;
import static com.github.artemzip.engine.Constants.MISSING_VAR_VALUE;
import static com.github.artemzip.engine.TemplateProcessor.Processor.VAR_PROCESSOR;
import static java.lang.String.format;
import static lombok.AccessLevel.PRIVATE;

interface TemplateProcessor {
    String process(Template template, TemplateContext context) throws TemplateException;

    @Getter(PRIVATE)
    @AllArgsConstructor(access = PRIVATE)
    enum Processor {
        VAR_PROCESSOR(new VarProcessor()),
        USE_OPERATOR_PROCESSOR(new UseOperatorProcessor());

        private TemplateProcessor instance;
    }

    static TemplateProcessor get(Processor processor) {
        return processor.getInstance();
    }

    static void validateTemplateContext(String name, String variable, TemplateContext ctx) throws MissingTemplateVarException {
        if(!ctx.getEnv().containsKey(variable)) {
            throw new MissingTemplateVarException(format(MISSING_VAR, variable, name));
        }
        if(ctx.getEnv().get(variable) == null) {
            throw new MissingTemplateVarException(format(MISSING_VAR_VALUE, variable, name));
        }
    }

    /**
     * Class targeted to process USE block
     *
     * USE block is a loop structure for processing repeatable parts of code (For example: getters, setters)
     */
    @NoArgsConstructor(access = PRIVATE)
    class UseOperatorProcessor implements TemplateProcessor {
        private static final Pattern START_USE_PATTERN = Pattern.compile("\\{\\{\\s*USE:(.*?)\\s*}}\\s*");
        private static final Pattern END_USE_PATTERN = Pattern.compile("\\{\\{\\s*END\\s*}}");

        @Override
        @SuppressWarnings("unchecked")
        public String process(Template template, TemplateContext context) throws TemplateException {
            var content = new StringBuilder(template.getContent());
            while (true) {
                var startMatcher = START_USE_PATTERN.matcher(content);
                var endMatcher = END_USE_PATTERN.matcher(content);
                var startExist = startMatcher.find();
                var endExist = endMatcher.find();

                if(startExist != endExist) {
                    throw new UseOperatorException(endExist ? MISSING_USE_START : format(MISSING_USE_END, startMatcher.group()));
                }

                if(!startExist && !endExist) {
                    break;
                }

                var useContext = startMatcher.group(1);
                if(useContext == null || useContext.isBlank()) {
                    throw new UseOperatorException(format(MISSING_USE_VAR, startMatcher.group()));
                } else {
                    validateTemplateContext(template.getName(), useContext, context);
                }

                var useScope = content.substring(startMatcher.end(), endMatcher.start());
                validateUseContext(useContext, context);
                var contextCollection = (Collection<TemplateContext>) context.getEnv().get(useContext);

                content.delete(startMatcher.start(), endMatcher.end());
                for(TemplateContext ctx : contextCollection) {
                    content.insert(startMatcher.start(), get(VAR_PROCESSOR).process(new Template(useScope), ctx));
                }
            }
            return content.toString();
        }

        private void validateUseContext(String useContext, TemplateContext context) throws TemplateException {
            var contextCollection = context.getEnv().get(useContext);
            if(contextCollection == null) {
                throw new UseOperatorException(format(MISSING_USE_VAR_VALUE, useContext));
            }

            var errorMsg = format(CANNOT_CAST_USE_CTX, useContext);
            if(contextCollection instanceof Collection) {
                var iterator = ((Collection) contextCollection).iterator();
                if(iterator.hasNext() && !(iterator.next() instanceof TemplateContext)) {
                    throw new UseOperatorException(errorMsg);
                }
            } else {
                throw new UseOperatorException(errorMsg);
            }
        }
    }

    /**
     * Variable processor
     *
     * {{VAR}} will be replaced with value in @TemplateContext
     *
     * if '^' is placed before var name (example: {{^VAR}}), var value will have upper case first latter.
     * for making more letters upper cased, you can use as more '^' as you need
     */
    @NoArgsConstructor(access = PRIVATE)
    class VarProcessor implements TemplateProcessor {
        private static final Pattern ENV_PATTERN = Pattern.compile("\\{\\{\\s*(.*?)\\s*}}");

        @Override
        public String process(Template template, TemplateContext context) throws TemplateException {
            var content = new StringBuilder(template.getContent());

            while (true) {
                var matcher = ENV_PATTERN.matcher(content);
                if(!matcher.find()) {
                    break;
                }

                var variableName = matcher.group(1);
                content.delete(matcher.start(), matcher.end());
                content.insert(matcher.start(), getValue(variableName, template.getName(), context));
            }
            return content.toString();
        }

        private String getValue(String env, String templateName, TemplateContext context) throws TemplateException {
            if(env == null || env.isBlank()) {
                throw new MissingTemplateVarException(format(EMPTY_BRACKETS, templateName));
            }
            var variableOp = new StringBuilder(env);
            int toUpperCase = 0;
            while (!variableOp.toString().isBlank() && variableOp.charAt(0) == '^') {
                toUpperCase++;
                variableOp.delete(0, 1);
            }

            validateTemplateContext(templateName, variableOp.toString(), context);
            var variableValue = new StringBuilder(context.getEnv().get(variableOp.toString()).toString());

            for(int i = 0; i < toUpperCase; i++) {
                variableValue.setCharAt(i, Character.toUpperCase(variableValue.charAt(i)));
            }
            return variableValue.toString();
        }
    }
}
