package io.github.s0cks.kat.ext.scripting;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Scripts{
    public static final Path scriptsFolder = Paths.get(System.getProperty("user.dir"), "scripts");

    static{
        try {
            Files.createDirectories(scriptsFolder);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}