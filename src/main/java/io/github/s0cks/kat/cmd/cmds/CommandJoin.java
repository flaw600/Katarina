package io.github.s0cks.kat.cmd.cmds;

import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.event.IRCEvent;

public final class CommandJoin
implements Command{
    @Override
    public String getName() {
        return "join";
    }

    @Override
    public void execute(IRCEvent.ChannelMessageEvent event, String... args) {
        event.connection().join(args[0].trim());
    }
}