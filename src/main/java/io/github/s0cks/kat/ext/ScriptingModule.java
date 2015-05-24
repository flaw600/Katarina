package io.github.s0cks.kat.ext;

import com.google.inject.AbstractModule;
import io.github.s0cks.kat.anno.Commands;
import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.ext.scripting.ExecuteCommand;
import io.github.s0cks.kat.ext.scripting.InstallCommand;

import javax.inject.Inject;
import java.util.List;

public final class ScriptingModule
extends AbstractModule{
    private final List<Command> commands;

    @Inject
    private ScriptingModule(@Commands List<Command> commands){
        this.commands = commands;
    }

    @Override
    protected void configure() {
        this.commands.add(new ExecuteCommand());
        this.commands.add(new InstallCommand());
    }
}