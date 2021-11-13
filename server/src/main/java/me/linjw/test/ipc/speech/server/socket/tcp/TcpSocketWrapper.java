package me.linjw.test.ipc.speech.server.socket.tcp;


import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import me.linjw.test.ipc.speech.server.socket.ISocket;

public class TcpSocketWrapper implements ISocket {
    private final Socket mSocket;

    public TcpSocketWrapper(Socket socket) {
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
