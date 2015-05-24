package io.github.s0cks.kat.irc;

import javax.inject.Inject;
import javax.inject.Named;

public final class IRCProfile{
    public final String nick;
    public final String username;
    public final String realname;

    @Inject
    private IRCProfile(@Named("nick") String nick, @Named("username") String username, @Named("realname") String realname){
        this.nick = nick;
        this.username = username;
        this.realname = realname;
    }
}