package me.linjw.test.ipc.speech.server;

public class DataSource {
    public static final int DATA_LENGTH = 1024;
    private static final DataSource sInstance = new DataSource();

    private byte[][] mDatas = new byte[10][DATA_LENGTH];
    private int mIndex = 0;

    public static DataSource getInstance() {
        return sInstance;
    }

    private DataSource() {
        for (int i = 0; i < mDatas.length; i++) {
            for (int j = 0; j < mDatas[0].length; j++) {
                mDatas[i][j] = (byte) (i + j);
            }
        }
    }

    public byte[] readData() {
        mIndex = (mIndex + 1) % mDatas.length;
        return mDatas[mIndex];
    }
}
