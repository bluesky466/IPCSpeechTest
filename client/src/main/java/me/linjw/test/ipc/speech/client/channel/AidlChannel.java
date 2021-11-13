package me.linjw.test.ipc.speech.client.channel;


import me.linjw.test.ipc.speech.aidl.AidlLink;

public class AidlChannel implements IIpcChannel {
    private final AidlLink mAidl;

    public AidlChannel(AidlLink aidl) {
        this.mAidl = aidl;
    }

    @Override
    public int read() throws Exception {
        return mAidl.read().length;
    }

    @Override
    public void write(byte[] data) throws Exception {
        mAidl.write(data);
    }

    @Override
    public String getTag() {
        return "AidlChannelClient";
    }
}
