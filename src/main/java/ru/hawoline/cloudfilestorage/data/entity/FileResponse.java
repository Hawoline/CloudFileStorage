package ru.hawoline.cloudfilestorage.data.entity;

public class FileResponse extends ResourceResponse {
    private long size;

    public FileResponse(String path, String name, String type, long size) {
        super(path, name, type);
        this.size = size;
    }

    public long getSize() {
        return size;
    }

    public void setSize(long size) {
        this.size = size;
    }
}
