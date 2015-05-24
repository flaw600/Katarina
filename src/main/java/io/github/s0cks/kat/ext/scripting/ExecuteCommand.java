package io.github.s0cks.kat.ext.scripting;

import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.event.IRCEvent;
import kawa.standard.Scheme;

import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

public final class ExecuteCommand
implements Command{
    private static final Scheme scheme = new Scheme();

    @Override
    public String getName() {
        return "exc";
    }

    @Override
    public void execute(IRCEvent.ChannelMessageEvent event, String... args) {
        try{
            Path script = Scripts.scriptsFolder.resolve(args[0].trim() + ".scm");
            if(!Files.exists(script)){
                event.connection().message(event.channel(), "Cannot find script: " + args[0]);
            }
            String data = new String(Files.readAllBytes(script), StandardCharsets.UTF_8);
            Object ret = scheme.eval(data);
            event.connection().message(event.channel(), "Scheme> " + ret);
        } catch(Throwable e){
            event.connection().message(event.channel(), "Error Executing Script (" + args[0] + ")");
            e.printStackTrace(System.err);
        }
    }
}