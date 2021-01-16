package com.github.artemzip.engine;

import lombok.NoArgsConstructor;

import static lombok.AccessLevel.PRIVATE;

@NoArgsConstructor(access = PRIVATE)
class Constants {
    //Exceptions msgs patterns
    //VARs
    static final String MISSING_VAR = "TemplateContext doesn't have defined %s for %s";
    static final String MISSING_VAR_VALUE = "Value of %s is null in TemplateContext for %s";
    static final String EMPTY_BRACKETS = "Brackets {{}} is empty in template %s";

    //Templates
    static final String TEMPLATE_NOT_FOUND = "Cannot find template %s on path '%s'";

    //Use operator
    static final String MISSING_USE_START = "There is {{END}} block without using {{USE:...}}";
    static final String MISSING_USE_END = "Missing {{END}} block for %s";
    static final String MISSING_USE_VAR = "Missing context var for %s";
    static final String MISSING_USE_VAR_VALUE = "Missing context var value for %s";
    static final String CANNOT_CAST_USE_CTX = "Context var %s is not instance of TemplateContext or Collection<TemplateContext>";

    //Header
    static final String FILE_HEADER = "" +
                                      "";
}
