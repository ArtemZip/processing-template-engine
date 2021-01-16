package com.github.artemzip.engine;


import lombok.SneakyThrows;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.String.format;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.StringContains.containsString;
import static org.junit.jupiter.api.Assertions.assertEquals;

class ProccessingTest {
    static final String RESOURCE_FOLDER = "src/test/resources/%s";
    private final TemplateContext testContext = new TemplateContext().addVar("VAR", "test");

    @Test
    @SneakyThrows
    void singleVarTest() {
        var content = TemplateEngine.getInstance()
                         .registerTemplate("single-var", format(RESOURCE_FOLDER, "correct/SingleVar.jpt"))
                         .processTemplate("single-var", testContext)
                         .getContent();

        assertEquals("test", content);
    }

    @Test
    @SneakyThrows
    void upperCaseVarTest() {
        var content = TemplateEngine.getInstance()
                                    .registerTemplate("upper-case-var", format(RESOURCE_FOLDER, "correct/UpperCaseVar.jpt"))
                                    .processTemplate("upper-case-var", testContext)
                                    .getContent();

        assertEquals("Test", content);
    }

    @Test
    @SneakyThrows
    void useOperatorTest() {
        var contexts = List.of(
                new TemplateContext().addVar("VAR", "Test"),
                new TemplateContext().addVar("VAR", "useOperatorTest")
        );

        var content = TemplateEngine.getInstance()
                                  .registerTemplate("use-operator", format(RESOURCE_FOLDER, "correct/UseOperator.jpt"))
                                  .processTemplate("use-operator", new TemplateContext().addVar("VARS", contexts))
                                  .getContent();

        assertThat(content, containsString("Test"));
        assertThat(content, containsString("useOperatorTest"));

        content = TemplateEngine.getInstance()
                                .processTemplate("use-operator", new TemplateContext().addVar("VARS", testContext))
                                .getContent();

        assertThat(content, containsString("test"));
    }
}
