package com.github.artemzip.engine;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@ToString
@Getter @Setter
class Template {
    private String name;
    private String path;
    private String content;

    Template(String name, String templatePath) {
        this.name = name;
        this.path = templatePath;
        this.content = null;
    }

    Template(String content) {
        this.name = "Anonymous var template";
        this.content = content;
    }
}
