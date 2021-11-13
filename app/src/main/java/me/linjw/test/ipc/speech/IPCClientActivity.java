package me.linjw.test.ipc.speech;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.system.Os;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import me.linjw.test.ipc.speech.aidl.AidlLink;
import me.linjw.test.ipc.speech.client.IpcUtils;
import me.linjw.test.ipc.speech.client.channel.AidlChannel;
import me.linjw.test.ipc.speech.client.channel.IIpcChannel;
import me.linjw.test.ipc.speech.client.channel.LocalSocketChannel;
import me.linjw.test.ipc.speech.client.channel.TcpSocketChannel;


public class IPCClientActivity extends AppCompatActivity {
    private static final String TAG = "IPCClientActivity";
    private static final int READ_WRITE_TIME = 3000;
    private static final byte[] DATA_TO_WRITE = new byte[1024];

    private final List<IIpcChannel> mTaskQueue = new LinkedList<>();
    private final List<IIpcChannel> mChannels = new ArrayList<>();
    private ArrayAdapter mArrayAdapter;

    private ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            Log.d(TAG, "onServiceConnected");
            AidlLink aidl = AidlLink.Stub.asInterface(service);

            mChannels.add(new AidlChannel(aidl));
            mChannels.add(new LocalSocketChannel("test.speech.localsocket"));
            mChannels.add(new TcpSocketChannel(8888));
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {

        }
    };

    private void execReadTask() {
        if (mTaskQueue.isEmpty()) {
            Log.d(TAG, "taskQueue is empty");
            return;
        }
        IpcUtils.read(mTaskQueue.get(0), READ_WRITE_TIME, (time, dataLength, exception) -> {
            IIpcChannel channel = mTaskQueue.remove(0);
            String log = "read " + dataLength + " bytes from " + channel.getTag() + " cost " + time;
            if (exception == null) {
                addLog(log);
            } else {
                Log.d(TAG, "read from " + channel.getTag() + "failed", exception);
            }
            execReadTask();
        });
    }

    private void execWriteTask() {
        if (mTaskQueue.isEmpty()) {
            Log.d(TAG, "taskQueue is empty");
            return;
        }
        IpcUtils.write(mTaskQueue.get(0), DATA_TO_WRITE, READ_WRITE_TIME, (time, dataLength, exception) -> {
            IIpcChannel channel = mTaskQueue.remove(0);
            execWriteTask();
            String log = "write " + dataLength + " bytes to " + channel.getTag() + " cost " + time;
            if (exception == null) {
                addLog(log);
            } else {
                Log.d(TAG, "write to " + channel.getTag() + "failed", exception);
            }
        });
    }

    private void addLog(String log){
        runOnUiThread(() -> {
            Log.d(TAG, log);
            mArrayAdapter.add(log);
        });
    }

    public void onClick(View view) {
        if (!mTaskQueue.isEmpty()) {
            Log.d(TAG, "taskQueue is not empty");
            return;
        }
        if (view.getId() == R.id.read) {
            mTaskQueue.addAll(mChannels);
            execReadTask();
        } else if (view.getId() == R.id.write) {
            mTaskQueue.addAll(mChannels);
            execWriteTask();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ListView listView = findViewById(R.id.log);

        mArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1);
        listView.setAdapter(mArrayAdapter);

        Log.d(TAG, "client pic = " + Os.getpid());

        for (int i = 0; i < DATA_TO_WRITE.length; i++) {
            DATA_TO_WRITE[i] = (byte) i;
        }

        Intent intent = new Intent("test.ipc.speech.aidl");
        intent.setPackage("me.linjw.test.ipc.speech");
        bindService(intent, mServiceConnection, Context.BIND_AUTO_CREATE);
    }
}