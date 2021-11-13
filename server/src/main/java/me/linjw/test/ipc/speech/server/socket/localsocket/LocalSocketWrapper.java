package me.linjw.test.ipc.speech.server.socket.localsocket;

import android.net.LocalSocket;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import me.linjw.test.ipc.speech.server.socket.ISocket;

public class LocalSocketWrapper implements ISocket {
    private final LocalSocket mSocket;

    public LocalSocketWrapper(LocalSocket socket) {
        mSocket = socket;
    }

    @Override
    public InputStream getInputStream() throws IOException {
        return mSocket.getInputStream();
    }

    @Override
    public OutputStream getOutputStream() throws IOException {
        return mSocket.getOutputStream();
    }

    @Override
    public void close() throws IOException {
        mSocket.close();
    }
}
