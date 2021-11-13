package me.linjw.test.ipc.speech;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.system.Os;
import android.util.Log;

import androidx.annotation.Nullable;

import java.io.IOException;

import me.linjw.test.ipc.speech.aidl.AidlLink;
import me.linjw.test.ipc.speech.server.channel.AidlChannelServer;
import me.linjw.test.ipc.speech.server.channel.SocketChannelServer;
import me.linjw.test.ipc.speech.server.socket.IServerSocket;
import me.linjw.test.ipc.speech.server.socket.localsocket.LocalServerSocketWrapper;
import me.linjw.test.ipc.speech.server.socket.tcp.TcpServerSocketWrapper;

public class IPCServerService extends Service {
    private static final String TAG = "IPCServerService";
    private final AidlLink.Stub mServerStub = new AidlChannelServer();

    private SocketChannelServer mLocalSocketChannel;
    private SocketChannelServer mTcpSocketChannel;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "server pic = " + Os.getpid());

        mLocalSocketChannel = startSocketChannel("LocalSocketChannelServer", new LocalServerSocketWrapper("test.speech.localsocket"));
        mLocalSocketChannel = startSocketChannel("TcpSocketChannelServer", new TcpServerSocketWrapper(8888));
    }

    private SocketChannelServer startSocketChannel(String tag, IServerSocket socketServer) {
        try {
            socketServer.bind();
        } catch (IOException e) {
            Log.e(TAG, "startSocketChannel " + tag + " failed", e);
            return null;
        }
        SocketChannelServer channel = new SocketChannelServer();
        channel.start(tag, socketServer);
        return channel;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return mServerStub;
    }
}
