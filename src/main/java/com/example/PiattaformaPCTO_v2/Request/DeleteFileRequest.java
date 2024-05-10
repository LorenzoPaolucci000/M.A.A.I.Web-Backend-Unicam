package com.example.PiattaformaPCTO_v2.Request;

public class DeleteFileRequest {
    private String name;

    public DeleteFileRequest(String name) {
        this.name = name;
    }
    public DeleteFileRequest(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
