package org.applicationn.pesquisa.pivot;

import java.io.Serializable;

public class ImportTaskResult implements Serializable {

    private static final long serialVersionUID = 1L;

    private String status;
    private String message;

    public ImportTaskResult(String status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}