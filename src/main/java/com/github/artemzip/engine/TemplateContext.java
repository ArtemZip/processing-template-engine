package com.github.artemzip.engine;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.ToString;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@ToString
public class TemplateContext {

    @Getter(AccessLevel.MODULE)
    private Map <String, Object> env;

    public TemplateContext() {
        env = new HashMap<>();
    }

    /**
     * Storing variables' values for being placed in template
     *
     * if @param name already exists, value will be replaced
     * if @param value is instance of Class<?> it will be replaced with it's canonical name (useful for adding imports)
     */
    public TemplateContext addVar(String name, Object value) {
        if(value instanceof TemplateContext) {
            this.env.put(name, List.of(value));
        } else if(value instanceof Class<?>) {
            this.env.put(name, ((Class<?>) value).getCanonicalName());
        } else {
            this.env.put(name, value);
        }
        return this;
    }
}
