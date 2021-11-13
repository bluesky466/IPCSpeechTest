package me.linjw.test.ipc.speech.server.socket.tcp;


import java.io.IOException;
import java.net.ServerSocket;

import me.linjw.test.ipc.speech.server.socket.IServerSocket;
import me.linjw.test.ipc.speech.server.socket.ISocket;

public class TcpServerSocketWrapper implements IServerSocket {
    private final int mPort;
    private ServerSocket mServerSocket;

    public TcpServerSocketWrapper(int port) {
        mPort = port;
    }

    @Override
    public ISocket accept() throws IOException {
        return new TcpSocketWrapper(mServerSocket.accept());
    }

    @Override
    public void bind() throws IOException {
        if (mServerSocket != null) {
            return;
        }
        mServerSocket = new ServerSocket(mPort);
    }

    @Override
    public void close() throws IOException {
        mServerSocket.close();
    }
}
