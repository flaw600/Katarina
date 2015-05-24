package io.github.s0cks.kat.util;

import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public final class Utils{
    public static final EnumSet<StandardOpenOption> WRITE = EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE);
}