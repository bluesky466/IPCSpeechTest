package me.linjw.test.ipc.speech.server.channel;

import android.util.Log;



import java.io.Closeable;
import java.io.IOException;
import java.io.InputStream;

import me.linjw.test.ipc.speech.server.DataSource;
import me.linjw.test.ipc.speech.server.socket.IServerSocket;
import me.linjw.test.ipc.speech.server.socket.ISocket;

public class SocketChannelServer extends Thread {
    private final byte[] mBuffer = new byte[DataSource.DATA_LENGTH];
    private IServerSocket mServer;

    private String mTag;
    private boolean mIsRunning = false;

    private long mTotalRead = 0;
    private long mTotalWrite = 0;

    public void start(String tag, IServerSocket serverSocket) {
        mTag = tag;
        mServer = serverSocket;
        mIsRunning = true;
        super.start();
    }

    private WorkMode getWorkMode(ISocket clientSocket) {
        int mode;
        try {
            mode = clientSocket.getInputStream().read();
        } catch (IOException e) {
            Log.e(mTag, "getWorkMode failed", e);
            safeClose(clientSocket);
            return WorkMode.UNKNOWN;
        }

        if (mode == WorkMode.READ.ordinal()) {
            return WorkMode.READ;
        } else if (mode == WorkMode.WRITE.ordinal()) {
            return WorkMode.WRITE;
        } else {
            return WorkMode.UNKNOWN;
        }
    }

    private void onReadRequest(ISocket clientSocket) {
        byte[] data = DataSource.getInstance().readData();
        try {
            clientSocket.getOutputStream().write(data);
            clientSocket.getInputStream().read();
            mTotalRead += data.length;
            Log.e(mTag, "client total read: " + mTotalRead);
        } catch (IOException e) {
            Log.e(mTag, "onReadRequest failed", e);
        } finally {
            safeClose(clientSocket);
        }
    }

    public static int readInt(InputStream is) throws IOException {
        int value = 0;
        for (int i = 0; i < 4; i++) {
            int shift = (4 - 1 - i) * 8;
            int read = is.read();
            if (read < 0) {
                return -1;
            }
            value += (read & 0x000000FF) << shift;// 往高位游
        }
        return value;
    }

    private void safeClose(Closeable closeable) {
        if (closeable == null) {
            return;
        }
        try {
            closeable.close();
        } catch (IOException e) {
            //ignore
        }
    }

    private void onWriteRequest(ISocket clientSocket) {
        try {
            InputStream is = clientSocket.getInputStream();

            int length = readInt(is);
            if (length <= 0) {
                return;
            }

            int read = 0;
            int totalRead = 0;
            while (read >= 0 && totalRead < length) {
                read = is.read(mBuffer);
                if (read > 0) {
                    totalRead += read;
                }
            }
            clientSocket.getOutputStream().write(0);
            clientSocket.close();
            mTotalWrite += totalRead;
            Log.e(mTag, "client total write: " + mTotalWrite);
        } catch (IOException e) {
            Log.e(mTag, "onWriteRequest failed", e);
        } finally {
            safeClose(clientSocket);
        }
    }

    @Override
    public void run() {
        super.run();
        Log.d(mTag, "server run");

        try {
            workLooper();
        } catch (IOException e) {
            Log.e(mTag, "accept failed", e);
            mIsRunning = false;
            safeClose(mServer);
        }
    }

    private void workLooper() throws IOException {
        while (mIsRunning) {
            ISocket clientSocket = mServer.accept();
            switch (getWorkMode(clientSocket)) {
                case READ:
                    onReadRequest(clientSocket);
                    break;
                case WRITE:
                    onWriteRequest(clientSocket);
                    break;
                default:
                    break;
            }
        }
    }

    private enum WorkMode {
        READ,
        WRITE,
        UNKNOWN
    }
}
