package cloud_storage.common;

import java.io.File;
import java.io.Serializable;

public class RequestCatalog implements Serializable {

    private String string;
    private File[] catalog;

    public void setString(String string) {
        this.string = string;
    }

    public String getString() {
        return string;
    }

    public File[] getCatalog() {
        return catalog;
    }

    public RequestCatalog(String string, File[] catalog) {
        this.string = string;
        this.catalog = catalog;
    }
}
