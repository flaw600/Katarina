package io.github.s0cks.kat.thread;

import io.github.s0cks.kat.cmd.Command;

import java.util.List;
import java.util.concurrent.Callable;
import java.util.regex.Pattern;

public final class CommandDiscovererThread
implements Callable<Command>{
    private final String name;
    private final List<Command> pool;

    public CommandDiscovererThread(String name, List<Command> pool) {
        this.name = name;
        this.pool = pool;
    }

    @Override
    public Command call()
    throws Exception {
        for(Command cmd : this.pool){
            if(Pattern.compile(cmd.getName()).matcher(this.name).find()){
                return cmd;
            }
        }

        return null;
    }
}