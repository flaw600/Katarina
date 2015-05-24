package io.github.s0cks.kat.ext.scripting;

import io.github.s0cks.kat.cmd.Command;
import io.github.s0cks.kat.event.IRCEvent;
import io.github.s0cks.kat.util.Utils;

import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.FileChannel;
import java.nio.channels.ReadableByteChannel;

public final class InstallCommand
implements Command{
    @Override
    public String getName() {
        return "script-install";
    }

    @Override
    public void execute(IRCEvent.ChannelMessageEvent event, String... args) {
        try{
            String name = args[0].trim();
            String url = args[1].trim();
            URL u = new URL(url);

            try(FileChannel fc = FileChannel.open(Scripts.scriptsFolder.resolve(name + ".scm"), Utils.WRITE);
                ReadableByteChannel rbc = Channels.newChannel(u.openStream())){

                fc.transferFrom(rbc, 0, Long.MAX_VALUE);
            }
        } catch(Exception e){
            event.connection().message(event.channel(), "Error Installing Script");
            e.printStackTrace(System.err);
        }
    }
}