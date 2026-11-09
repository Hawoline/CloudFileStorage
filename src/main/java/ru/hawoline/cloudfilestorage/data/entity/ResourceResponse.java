package ru.hawoline.cloudfilestorage.data.entity;

public class ResourceResponse {
    private String path;
    private String name;
    private String type;

    public ResourceResponse(String path, String name, String type) {
        this.path = path;
        this.name = name;
        this.type = type;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
