package io.github.s0cks.kat.thread;

import io.github.s0cks.kat.event.IRCEvent;
import io.github.s0cks.kat.io.StringReader;
import io.github.s0cks.kat.irc.IRCConnection;

import java.net.Socket;

public final class ReadThread
        implements Runnable{
    private final Socket socket;
    private final IRCConnection connection;

    public ReadThread(IRCConnection connection, Socket socket){
        this.connection = connection;
        this.socket = socket;
    }

    @Override
    public void run(){
        try(StringReader reader = new StringReader(socket.getInputStream())){
            String line;
            while((line = reader.readLine()) != null){
                System.out.println(">> " + line);
                this.parseLine(line);
            }
        } catch(Exception e){
            e.printStackTrace(System.err);
        }
    }

    private void parseLine(String line){
        if(line.startsWith("PING ")){
            this.connection.EVENT_BUS.post(new IRCEvent.PingEvent(this.connection, line.substring(5)));
            this.connection.writeRaw(line.replace("PING", "PONG"));
            return;
        }

        String[] tokens = line.split(" ");
        String nick = "";
        String info = tokens[0];
        String command = tokens[1];
        String target = null;
        int excl = info.indexOf('!');
        int at = info.indexOf('@');

        if(info.startsWith(":")){
            if(excl > 0 && at > 0 && excl < at){
                nick = info.substring(1, excl);
            } else{
                int code = -1;

                try{
                    code = Integer.parseInt(command);
                } catch(Exception e){
                    // Fallthrough
                }

                if(code == -1){
                    nick = info;
                    target = command;
                }
            }
        }

        command = command.toUpperCase();
        if(nick.startsWith(":")){
            nick = nick.substring(1);
        }

        if(target == null){
            target = tokens[2];
        }

        if(target.startsWith(":")){
            target = target.substring(1);
        }

        if(command.equals("PRIVMSG") && target.charAt(0) == '#'){
            this.connection.EVENT_BUS.post(new IRCEvent.ChannelMessageEvent(this.connection, target, nick, line.substring(line.indexOf(" :") + 2)));
        } else if(command.equals("PRIVMSG")){
            this.connection.EVENT_BUS.post(new IRCEvent.PrivateMessageEvent(this.connection, nick, line.substring(line.indexOf(" :") + 2)));
        } else if(command.equals("JOIN")){
            this.connection.EVENT_BUS.post(new IRCEvent.JoinChannelEvent(this.connection, nick, target));
        } else if(command.equals("PART")){
            this.connection.EVENT_BUS.post(new IRCEvent.PartChannelEvent(this.connection, nick, target));
        }
    }
}