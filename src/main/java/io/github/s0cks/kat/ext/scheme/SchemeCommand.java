package io.github.s0cks.kat.ext.scheme;

import gnu.mapping.Procedure1;
import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.event.IRCEvent;
import io.github.s0cks.kat.irc.IRCConnection;
import kawa.standard.Scheme;

public final class SchemeCommand
implements Command{
    private static final Scheme scheme = Scheme.getR7rsInstance();
    static{
        scheme.getLangEnvironment().setCanRedefine(true);
        scheme.getLangEnvironment().setCanDefine(true);
        scheme.getEnvironment().setCanDefine(true);
        scheme.getEnvironment().setCanRedefine(true);
    }

    @Override
    public String getName() {
        return "scheme";
    }

    @Override
    public void execute(final IRCEvent.ChannelMessageEvent event, String... args) {
        try {
            StringBuilder str = new StringBuilder();
            for(String arg : args){
                str.append(arg.trim()).append(" ");
            }

            scheme.define("print", new PrintProcedure(event.connection(), event.channel()));
            Object resp = scheme.eval(str.toString().trim());
            if(resp != null){
                event.connection().message(event.channel(), "Scheme> " + resp);
            }
        } catch (Throwable throwable) {
            event.connection().message(event.channel(), "Error Executing Scheme: " + throwable.getLocalizedMessage());
            throwable.printStackTrace(System.err);
        }
    }

    private final class PrintProcedure
    extends Procedure1{
        private final IRCConnection connection;
        private final String channel;

        private PrintProcedure(IRCConnection connection, String channel) {
            this.connection = connection;
            this.channel = channel;
        }

        @Override
        public Object apply1(Object o)
        throws Throwable {
            this.connection.message(this.channel, o.toString());
            return null;
        }
    }
}