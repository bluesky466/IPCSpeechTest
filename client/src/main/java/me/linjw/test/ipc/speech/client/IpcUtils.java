package me.linjw.test.ipc.speech.client;

import android.util.Log;

import me.linjw.test.ipc.speech.client.channel.IIpcChannel;

public class IpcUtils {
    public static void write(IIpcChannel ipcChannel, byte[] data, int writeTime, ICallback callback) {
        new Thread(() -> {
            long time = System.currentTimeMillis();
            long writeSize = 0;
            try {
                for (int i = 0; i < writeTime; i++) {
                    ipcChannel.write(data);
                    writeSize += data.length;
                }
                callback.onFinish(getDuration(time), writeSize, null);
            } catch (Exception e) {
                callback.onFinish(getDuration(time), writeSize, e);
            }
        }).start();
    }

    public static void read(IIpcChannel ipcChannel, int readTime, ICallback callback) {
        new Thread(() -> {
            long time = System.currentTimeMillis();
            long readSize = 0;

            try {
                for (int i = 0; i < readTime; i++) {
                    int read = ipcChannel.read();
                    if (read > 0) {
                        readSize += read;
                    }
                }
                callback.onFinish(getDuration(time), readSize, null);
            } catch (Exception e) {
                callback.onFinish(getDuration(time), readSize, e);
            }
        }).start();
    }

    private static float getDuration(long startTime) {
        return (System.currentTimeMillis() - startTime) / 1000.0f;
    }


    public interface ICallback {
        void onFinish(float time, long dataLength, Exception exception);
    }
}
