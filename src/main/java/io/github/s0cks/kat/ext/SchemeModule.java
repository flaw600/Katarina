package io.github.s0cks.kat.ext;

import com.google.inject.AbstractModule;
import io.github.s0cks.kat.anno.Commands;
import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.ext.scheme.SchemeCommand;

import javax.inject.Inject;
import java.util.List;

@SuppressWarnings("unused")
public final class SchemeModule
extends AbstractModule{
    private final List<Command> commands;

    @Inject
    private SchemeModule(@Commands List<Command> cmds){
        this.commands = cmds;
    }

    @Override
    protected void configure() {
        this.commands.add(new SchemeCommand());
    }
}