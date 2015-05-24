package io.github.s0cks.kat.cmd.cmds;

import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.event.IRCEvent;

public final class CommandTest
implements Command{
    @Override
    public String getName() {
        return "test";
    }

    @Override
    public void execute(IRCEvent.ChannelMessageEvent event, String... args) {
        event.connection().message(event.channel, "Hello World");
    }
}