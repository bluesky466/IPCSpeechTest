package me.linjw.test.ipc.speech.server.socket;

import java.io.Closeable;
import java.io.IOException;

public interface IServerSocket extends Closeable {
    ISocket accept() throws IOException;

    void bind() throws IOException;
}
