package me.linjw.test.ipc.speech.client.channel;

import android.net.LocalSocket;
import android.net.LocalSocketAddress;

import java.io.InputStream;
import java.io.OutputStream;

public class LocalSocketChannel implements IIpcChannel {
    private final String mAddress;
    private byte[] mBuffer = new byte[1024];

    public LocalSocketChannel(String address) {
        this.mAddress = address;
    }

    @Override
    public int read() throws Exception {
        LocalSocket localSocket = new LocalSocket();
        localSocket.connect(new LocalSocketAddress(mAddress));
        localSocket.getOutputStream().write(0);
        InputStream is = localSocket.getInputStream();
        int read = is.read(mBuffer, 0, mBuffer.length);
        localSocket.getOutputStream().write(0);
        localSocket.close();
        return read;
    }

    public static byte[] intToByteArray(int i) {
        byte[] result = new byte[4];
        result[0] = (byte) ((i >> 24) & 0xFF);
        result[1] = (byte) ((i >> 16) & 0xFF);
        result[2] = (byte) ((i >> 8) & 0xFF);
        result[3] = (byte) (i & 0xFF);
        return result;
    }

    @Override
    public void write(byte[] data) throws Exception {
        LocalSocket localSocket = new LocalSocket();
        localSocket.connect(new LocalSocketAddress(mAddress));
        OutputStream os = localSocket.getOutputStream();
        os.write(1);
        os.write(intToByteArray(data.length));
        os.write(data);
        localSocket.getInputStream().read();
        localSocket.close();
    }

    @Override
    public String getTag() {
        return "LocalSocketChannelClient";
    }
}
