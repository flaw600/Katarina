package io.github.s0cks.kat.cmd.cmds;

import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.event.IRCEvent;
import io.github.s0cks.kat.handler.CommandHandler;

import java.util.List;

public final class CommandList
implements Command{
    @Override
    public String getName() {
        return "ls";
    }

    @Override
    public void execute(IRCEvent.ChannelMessageEvent event, String... args) {
        StringBuilder str = new StringBuilder();
        List<Command> commands = CommandHandler.instance().commands();
        for(int i = 0; i < commands.size(); i++){
            str.append(commands.get(i));

            if(i < commands.size() - 1){
                str.append(", ");
            }
        }

        event.connection().message(event.channel(), "Commands: " + str);
    }
}