package com.github.artemzip.engine;

import com.github.artemzip.engine.exceptions.MissingTemplateVarException;
import com.github.artemzip.engine.exceptions.TemplateNotFoundException;
import com.github.artemzip.engine.exceptions.UseOperatorException;
import org.junit.jupiter.api.Test;

import static com.github.artemzip.engine.ProccessingTest.RESOURCE_FOLDER;
import static java.lang.String.format;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ExceptionTest {

    @Test
    void fileNotFoundTest() {
        assertThrows(TemplateNotFoundException.class, () ->
            TemplateEngine.getInstance()
                          .registerTemplate("file-not-found", format(RESOURCE_FOLDER, "file.jpt"))
                          .processTemplate("file-not-found", new TemplateContext())
        );
    }

    @Test
    void missingVarTest() {
        assertThrows(MissingTemplateVarException.class, () ->
                TemplateEngine.getInstance()
                              .registerTemplate("missing-var", format(RESOURCE_FOLDER, "correct/SingleVar.jpt"))
                              .processTemplate("missing-var", new TemplateContext())
        );

        assertThrows(MissingTemplateVarException.class, () ->
                TemplateEngine.getInstance()
                              .processTemplate("missing-var", new TemplateContext().addVar("VAR", null))
        );
    }

    @Test
    void emptyBracketsTest() {
        assertThrows(MissingTemplateVarException.class, () ->
            TemplateEngine.getInstance()
                      .registerTemplate("empty-brackets", format(RESOURCE_FOLDER, "incorrect/EmptyBrackets.jpt"))
                      .processTemplate("empty-brackets", new TemplateContext())
        );
    }

    @Test
    void emptyUseOperatorTest() {
        assertThrows(UseOperatorException.class, () ->
                TemplateEngine.getInstance()
                              .registerTemplate("empty-use-operator", format(RESOURCE_FOLDER, "incorrect/EmptyUseOperator.jpt"))
                              .processTemplate("empty-use-operator", new TemplateContext())
        );
    }

    @Test
    void endWithoutUseBlockTest() {
        assertThrows(UseOperatorException.class, () ->
                TemplateEngine.getInstance()
                              .registerTemplate("end-without-use-block", format(RESOURCE_FOLDER, "incorrect/EndWithoutUseBlock.jpt"))
                              .processTemplate("end-without-use-block", new TemplateContext())
        );
    }

    @Test
    void toUpperCaseWithoutVarTest() {
        assertThrows(MissingTemplateVarException.class, () ->
                TemplateEngine.getInstance()
                              .registerTemplate("to-upper-case-without-var", format(RESOURCE_FOLDER, "incorrect/ToUpperCaseWithoutVar.jpt"))
                              .processTemplate("to-upper-case-without-var", new TemplateContext())
        );
    }

    @Test
    void useWithoutEndBlock() {
        assertThrows(UseOperatorException.class, () ->
                TemplateEngine.getInstance()
                              .registerTemplate("use-without-end-block", format(RESOURCE_FOLDER, "incorrect/UseWithoutEndBlock.jpt"))
                              .processTemplate("use-without-end-block", new TemplateContext())
        );
    }
}
