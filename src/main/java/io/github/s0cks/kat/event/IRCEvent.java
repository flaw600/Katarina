package io.github.s0cks.kat.event;

import io.github.s0cks.kat.irc.IRCConnection;

public class IRCEvent{
    public final IRCConnection connection;

    public IRCEvent(IRCConnection connection){
        this.connection = connection;
    }

    public static final class JoinChannelEvent
            extends IRCEvent{
        public final String user;
        public final String channel;

        public JoinChannelEvent(IRCConnection connection, String user, String channel){
            super(connection);
            this.user = user;
            this.channel = channel;
        }
    }
    
    public static final class JoinChannelFailedEvent extends IRCEvent {
    	public final String user;
        public final String channel;

        public JoinChannelFailedEvent(IRCConnection connection, String user, String channel){
            super(connection);
            this.user = user;
            this.channel = channel;
        }
    }

    public static final class PartChannelEvent
            extends IRCEvent{
        public final String user;
        public final String channel;

        public PartChannelEvent(IRCConnection connection, String user, String channel){
            super(connection);
            this.user = user;
            this.channel = channel;
        }
    }

    public static final class PingEvent
            extends IRCEvent{
        public final String payload;

        public PingEvent(IRCConnection connection, String payload){
            super(connection);
            this.payload = payload;
        }
    }

    public static interface MessageEvent{
        public String message();
        public String sender();
        public String channel();
        public IRCConnection connection();
    }

    public static final class ChannelMessageEvent
            extends IRCEvent
            implements MessageEvent{
        public final String channel;
        public final String sender;
        public final String message;

        public ChannelMessageEvent(IRCConnection connection, String channel, String sender, String message){
            super(connection);
            this.channel = channel;
            this.sender = sender;
            this.message = message;
        }

        @Override
        public String message(){
            return this.message;
        }

        @Override
        public String sender(){
            return this.sender;
        }

        @Override
        public String channel(){
            return this.channel;
        }

        @Override
        public IRCConnection connection(){
            return this.connection;
        }
    }

    public static final class PrivateMessageEvent
            extends IRCEvent
            implements MessageEvent{
        public final String sender;
        public final String message;

        public PrivateMessageEvent(IRCConnection connection, String sender, String message){
            super(connection);
            this.sender = sender;
            this.message = message;
        }

        @Override
        public String message(){
            return this.message;
        }

        @Override
        public String sender(){
            return this.sender;
        }

        @Override
        public String channel(){
            return null;
        }

        @Override
        public IRCConnection connection(){
            return this.connection;
        }
    }
}