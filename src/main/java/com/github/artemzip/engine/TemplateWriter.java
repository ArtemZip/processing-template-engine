package com.github.artemzip.engine;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.annotation.processing.Filer;
import javax.tools.JavaFileObject;

import java.io.IOException;
import java.io.Writer;

import static lombok.AccessLevel.MODULE;

@AllArgsConstructor(access = MODULE)
public class TemplateWriter {
    @Getter(MODULE)
    private final String content;

    /**
     * @param canonicalName canonical (fully qualified) name of the principal type being declared in this file
     */
    public void write(Filer filer, String canonicalName) throws IOException {
        final JavaFileObject sourceFile = filer.createSourceFile(canonicalName);
        write(sourceFile);
    }

    public void write(final JavaFileObject sourceFile) throws IOException {
        try (Writer writer = sourceFile.openWriter()){
            writer.append(content);
        }
    }
}
