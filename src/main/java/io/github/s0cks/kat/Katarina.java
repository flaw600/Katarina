package io.github.s0cks.kat;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import io.github.s0cks.kat.handler.CommandHandler;
import io.github.s0cks.kat.irc.IRCConnection;
import io.github.s0cks.kat.irc.IRCProfile;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.TimeUnit;

public final class Katarina{
    static{
        if(!Files.exists(Paths.get(System.getProperty("user.dir"), "katarina.db"))){
            throw new RuntimeException("Database doesn't exist");
        }

        try{
            Class.forName("org.sqlite.JDBC");
        } catch(Exception e){
            throw new RuntimeException("class org.sqlite.JDBC doesn't exist");
        }
    }

    public static final Injector injector;

    static{
        try{
            List<String> mods = new LinkedList<>();
            try(BufferedReader reader = new BufferedReader(new InputStreamReader(System.class.getResourceAsStream("/modules.ls")))){
                String line;
                StringBuilder str = new StringBuilder();
                while((line = reader.readLine()) != null){
                    str.append(line);
                }
                mods.addAll(Arrays.asList(str.toString().split(",")));
            }

            List<Module> modules = new LinkedList<>();
            Injector i = Guice.createInjector(new KatarinaModule());

            for(String str : mods){
                String classpath = "io.github.s0cks.kat.ext." + Character.toUpperCase(str.trim().charAt(0)) + str.trim().substring(1) + "Module";
                System.out.println("Loading Module: " + classpath);
                Class<?> c = Class.forName(classpath);
                modules.add((Module) i.getInstance(c));
            }

            injector = i.createChildInjector(modules);
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args){
    	boolean iWinConnected = false;
    	boolean speakEasyConnecte = false;
        IRCProfile profile = injector.getInstance(IRCProfile.class);
        try(IRCConnection connection = injector.getInstance(IRCConnection.class)){
            System.out.println("Connecting To Server");
            connection.connect(profile);
            System.out.println("Done Connecting");
            connection.EVENT_BUS.register(new CommandHandler());
            Thread.sleep(TimeUnit.SECONDS.toMillis(10));
            System.out.println("Joining Channels");
            connection.join("#iWin");
            if (iWinConnected = true) {
            	System.out.println("Joined #iWin"); //logging purposes
            } else {
            	System.out.println("Join to #iWin failed");
            }
            connection.join("#SpeakEasy");
            if(iWinConnected = true) {
            	System.out.println("Joined #SpeakEasy"); //will need to create booleans on demand
            } else {
            	System.out.println("Join to #SpeakEasy failed"); //or get error MSG from network/channel
            }
            while(connection.isConnected()){/* Fallthrough */}
        } catch(Exception e){
            throw new RuntimeException(e); 
        }
    }
}