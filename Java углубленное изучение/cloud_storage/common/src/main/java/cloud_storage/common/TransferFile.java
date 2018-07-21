package cloud_storage.common;

import java.io.File;
import java.io.Serializable;

public class TransferFile implements Serializable{

    private String string;
    private File file;
    private int portion;
    private int total;
    private byte[] bytes;

    public String getString() {
        return string;
    }

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

    public TransferFile(String string, File file, int portion, int total, byte[] bytes) {
        this.string = string;
        this.file = file;
        this.portion = portion;
        this.total = total;
        this.bytes = bytes;
    }
}
