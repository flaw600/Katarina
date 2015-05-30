package io.github.s0cks.kat.irc;

import com.google.common.eventbus.EventBus;
import io.github.s0cks.kat.thread.ReadThread;
import io.github.s0cks.kat.thread.WriteThread;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Singleton;
import java.io.Closeable;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.LinkedBlockingQueue;

@Singleton
public final class IRCConnection
implements Closeable {
    public final EventBus EVENT_BUS = new EventBus();

    private final BlockingQueue<String> outQueue = new LinkedBlockingQueue<>();
    private final ForkJoinPool taskPool = new ForkJoinPool();
    private final Socket socket;
    private final int port;
    private final String server;
    private boolean joined = false;

    @Inject
    private IRCConnection(@Named("server") String server, @Named("port") int port){
        this.socket = new Socket();
        this.server = server;
        this.port = port;
        this.EVENT_BUS.register(this);
    }

    public boolean isConnected(){
        return this.socket.isConnected();
    }
    
    /*
     * TODO make sure that isJoined is per channel than assume isJoined resets per channel
     * TODO verify correct usage of 'this'
     */
    public boolean isJoined() {
    	return this.joined;
    }

    public void connect(IRCProfile profile)
    throws IOException{
        this.socket.connect(new InetSocketAddress(this.server, this.port));
        this.taskPool.execute(new ReadThread(this, this.socket));
        this.taskPool.execute(new WriteThread(this.socket, this.outQueue));
        this.writeRaw("NICK " + profile.nick);
        this.writeRaw("USER " + profile.username + " 8 * : " + profile.realname);
    }

    public void join(String channel){
        this.writeRaw("JOIN " + channel);
    }

    public void part(String channel){
        this.writeRaw("PART " + channel);
    }

    public void message(String target, String message){
        this.writeRaw("PRIVMSG " + target + " :" + message);
    }

    public void writeRaw(String line){
        this.outQueue.add(line.endsWith("\r\n") ? line : line + "\r\n");
    }

    public void notice(String target, String message){
        this.writeRaw("NOTICE " + target + " :" + message);
    }

    public void mode(String channel, String mode){
        this.writeRaw("MODE " + channel + " " + mode);
    }
    
    public void setJoinedState(boolean joinedState) {
    	this.joined=joinedState;
    }

    @Override
    public void close()
    throws IOException{
        this.taskPool.shutdownNow();
        this.socket.close();
    }
}