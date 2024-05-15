package com.example.PiattaformaPCTO_v2.Request;

public class DeleteFileRequest {
    private String name;
    private int anno;

    public DeleteFileRequest(String name,int anno) {
        this.name = name;
        this.anno=anno;
    }
    public DeleteFileRequest(){

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAnno() {
        return anno;
    }

    public void setAnno(int anno) {
        this.anno = anno;
    }
}
