package io.github.s0cks.kat.thread;

import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.BlockingQueue;

public final class WriteThread
implements Runnable{
    private final BlockingQueue<String> queue;
    private final Socket socket;

    public WriteThread(Socket socket, BlockingQueue<String> queue){
        this.socket = socket;
        this.queue = queue;
    }

    @Override
    public void run(){
        try{
            while(this.socket.isConnected()){
                String line = this.queue.take();
                if(line != null){
                    System.out.print("<< " + line);
                    this.socket.getOutputStream().write(line.getBytes(StandardCharsets.UTF_8));
                }
            }
        } catch(Exception e){
            e.printStackTrace(System.err);
        }
    }
}