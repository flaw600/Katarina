package io.github.s0cks.kat.handler;

import com.google.common.collect.ImmutableList;
import com.google.common.eventbus.Subscribe;
import com.google.common.reflect.ClassPath;
import com.google.inject.Key;
import com.google.inject.TypeLiteral;
import io.github.s0cks.kat.Katarina;
import io.github.s0cks.kat.anno.Commands;
import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.event.IRCEvent;
import io.github.s0cks.kat.thread.CommandDiscovererThread;

import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public final class CommandHandler{
    private static CommandHandler instance;

    public static CommandHandler instance(){
        return instance == null ? instance = new CommandHandler() : instance;
    }

    private final ExecutorService runner = Executors.newCachedThreadPool();
    private final List<Command> commands = new LinkedList<>();

    public CommandHandler(){
        try{
            ClassPath cp = ClassPath.from(ClassLoader.getSystemClassLoader());
            for(ClassPath.ClassInfo info : cp.getTopLevelClassesRecursive("io.github.s0cks.kat.cmd.cmds")){
                this.commands.add((Command) Katarina.injector.getInstance(info.load()));
            }
            List<Command> moduleCmds = Katarina.injector.getInstance(Key.get(new TypeLiteral<List<Command>>(){}, Commands.class));
            this.commands.addAll(moduleCmds);
        } catch (Exception e){
            throw new RuntimeException(e);
        }
    }

    public List<Command> commands(){
        return ImmutableList.copyOf(commands);
    }

    @Subscribe
    public void onChannelMessage(IRCEvent.ChannelMessageEvent msg)
    throws Exception{
        try{
            if(msg.message.startsWith(">")){
                String cmd = msg.message.split(" ")[0].substring(1);

                Future<Command> commandFuture = this.runner.submit(new CommandDiscovererThread(cmd, this.commands));
                Command c = commandFuture.get();
                if(c != null){
                    if(msg.message.split(" ").length > 1){
                        String[] args = new String[msg.message.split(" ").length - 1];
                        System.arraycopy(msg.message.split(" "), 1, args, 0, args.length);
                        c.execute(msg, args);
                    } else{
                        c.execute(msg);
                    }
                } else{
                    msg.connection().message(msg.channel, "Cannot find command:" + cmd);
                }
            }
        } catch(Exception e){
            e.printStackTrace(System.err);
        }
    }
}