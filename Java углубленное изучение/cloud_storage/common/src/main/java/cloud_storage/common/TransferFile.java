package cloud_storage.common;

import java.io.File;
import java.io.Serializable;

public class TransferFile implements Serializable{

    private File file;
    private int portion;
    private int total;
    private byte[] bytes;

    public File getFile() {
        return file;
    }

    public int getPortion() {
        return portion;
    }

    public int getTotal() {
        return total;
    }

    public byte[] getBytes() {
        return bytes;
    }

    TransferFile(File file, int portion, int total, byte[] bytes) {
        this.file = file;
        this.portion = portion;
        this.total = total;
        this.bytes = bytes;
    }
}
