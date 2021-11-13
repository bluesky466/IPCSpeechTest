package me.linjw.test.ipc.speech.client.channel;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class TcpSocketChannel implements IIpcChannel {
    private final int mPort;
    private byte[] mBuffer = new byte[1024];
    private static final String IP = "127.0.0.1";

    public TcpSocketChannel(int port) {
        this.mPort = port;
    }

    @Override
    public int read() throws Exception {
        Socket localSocket = new Socket(IP, mPort);
        localSocket.getOutputStream().write(0);
        InputStream is = localSocket.getInputStream();
        int read = is.read(mBuffer, 0, mBuffer.length);
        localSocket.getOutputStream().write(1);
        localSocket.close();
        return read;
    }

    @Override
    public void write(byte[] data) throws Exception {
        Socket localSocket = new Socket(IP, mPort);
        OutputStream os = localSocket.getOutputStream();
        os.write(1);
        os.write(data.length);
        os.write(data);
        localSocket.close();
    }

    @Override
    public String getTag() {
        return "TcpSocketChannelClient";
    }
}
