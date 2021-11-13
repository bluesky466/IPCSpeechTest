package me.linjw.test.ipc.speech.client.channel;

public interface IIpcChannel {
    int read() throws Exception;

    void write(byte[] data) throws Exception;

    String getTag();
}
