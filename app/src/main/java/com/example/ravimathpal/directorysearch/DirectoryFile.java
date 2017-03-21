package com.example.ravimathpal.directorysearch;

/**
 * Created by Ravi Mathpal on 21-03-2017.
 */

public class DirectoryFile {

    private String key;
    private String path;
    private String lastModified;
    private double size;
    private String thumb;
    private String name;

    public String getName() {
        return name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
        String[] thumbs = key.split("\\.");
        thumb = thumbs[0] + "_thumb." + thumbs[1];
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public double getSize() {
        return size;
    }

    public void setSize(double size) {
        this.size = size;
    }

    public String getThumb() {
        return thumb;
    }

}
