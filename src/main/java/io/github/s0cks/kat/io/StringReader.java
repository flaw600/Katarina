package io.github.s0cks.kat.io;


import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;


public final class StringReader
implements Closeable {
    private final InputStream is;

    public StringReader(InputStream is){
        if(is == null) throw new NullPointerException("InputStream == null");
        this.is = is;
    }

    public String readLine()
    throws IOException {
        String ret = "";

        int i;
        it: while((i = this.is.read()) != -1){
            char c = (char) i;
            switch(c)
            {
                case '\n':
                    break it;
                default:
                    ret += c;
            }
        }

        return ret.isEmpty() ? null : ret;
    }

    @Override
    public void close()
    throws IOException{

    }
}