// AidlLink.aidl
package me.linjw.test.ipc.speech.aidl;

// Declare any non-default types here with import statements

interface AidlLink {
    byte[] read();

    void write(in byte[] data);
}