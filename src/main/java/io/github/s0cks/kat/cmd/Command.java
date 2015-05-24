package io.github.s0cks.kat.cmd;

import io.github.s0cks.kat.event.IRCEvent;

public interface Command{
    public String getName();
    public void execute(IRCEvent.ChannelMessageEvent event, String... args);
}