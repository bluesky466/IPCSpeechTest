package me.linjw.test.ipc.speech.server.socket.localsocket;

import android.net.LocalServerSocket;
import android.net.LocalSocket;


import java.io.IOException;

import me.linjw.test.ipc.speech.server.socket.IServerSocket;
import me.linjw.test.ipc.speech.server.socket.ISocket;

public class LocalServerSocketWrapper implements IServerSocket {
    private final String mAdress;
    private LocalServerSocket mServerSocket;

    public LocalServerSocketWrapper(String address) {
        mAdress = address;
    }

    @Override
    public ISocket accept() throws IOException {
        return new LocalSocketWrapper(mServerSocket.accept());
    }

    @Override
    public void bind() throws IOException {
        if (mServerSocket != null) {
            return;
        }
        this.mServerSocket = new LocalServerSocket(mAdress);
    }

    @Override
    public void close() throws IOException {
        mServerSocket.close();
    }
}
