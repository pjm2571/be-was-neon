package webserver.handlers.writers;

import java.io.DataOutputStream;
import java.io.OutputStream;

public class ResponseWriter {
    private DataOutputStream responseWriter;

    public ResponseWriter(OutputStream outputStream){
        this.responseWriter = new DataOutputStream(outputStream);
    }


}

