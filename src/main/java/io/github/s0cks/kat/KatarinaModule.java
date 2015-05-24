package io.github.s0cks.kat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.name.Names;
import io.github.s0cks.kat.anno.Commands;
import io.github.s0cks.kat.cmd.Command;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.LinkedList;
import java.util.List;

public final class KatarinaModule
extends AbstractModule{
    private final List<Command> commands = new LinkedList<>();
    private final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .create();

    @Override
    protected void configure() {
        this.bind(new TypeLiteral<List<Command>>(){})
                .annotatedWith(Commands.class)
                .toInstance(this.commands);
        this.bind(Gson.class)
                .toInstance(this.gson);

        Connection c;
        Statement s;

        try{
            c = DriverManager.getConnection("jdbc:sqlite:./katarina.db");
            c.setAutoCommit(true);

            s = c.createStatement();
            try(ResultSet serverResult = s.executeQuery("SELECT * FROM connection")){
                this.bind(String.class)
                    .annotatedWith(Names.named("server"))
                    .toInstance(serverResult.getString("address"));
                this.bind(Integer.class)
                    .annotatedWith(Names.named("port"))
                    .toInstance(serverResult.getInt("port"));
            }

            s.close();
            s = c.createStatement();

            try(ResultSet profileResult = s.executeQuery("SELECT * FROM profile")){
                this.bind(String.class)
                    .annotatedWith(Names.named("username"))
                    .toInstance(profileResult.getString("username"));
                this.bind(String.class)
                    .annotatedWith(Names.named("realname"))
                    .toInstance(profileResult.getString("realname"));
                this.bind(String.class)
                    .annotatedWith(Names.named("nick"))
                    .toInstance(profileResult.getString("nick"));
            }

            s.close();
            c.close();
        } catch(Exception e){
            throw new RuntimeException(e);
        }
    }
}