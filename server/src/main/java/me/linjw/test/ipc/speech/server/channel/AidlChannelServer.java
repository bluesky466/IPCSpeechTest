package me.linjw.test.ipc.speech.server.channel;

import android.util.Log;

import me.linjw.test.ipc.speech.aidl.AidlLink;
import me.linjw.test.ipc.speech.server.DataSource;


public class AidlChannelServer extends AidlLink.Stub {
    private static final String TAG = "AidlChannelServer";

    private long mTotalRead = 0;
    private long mTotalWrite = 0;

    @Override
    public byte[] read() {
        byte[] data = DataSource.getInstance().readData();
        mTotalRead += data.length;
        Log.e(TAG, "client total read: " + mTotalRead);
        return data;
    }

    @Override
    public void write(byte[] data) {
        mTotalWrite += data.length;
        Log.e(TAG, "client total write: " + mTotalWrite);
    }
}
