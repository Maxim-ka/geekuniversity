package cloud_storage.server;

import cloud_storage.common.Rule;

public class Main {

    public static void main(String[] args) {
        try {
            new Server(Rule.PORT).run();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
