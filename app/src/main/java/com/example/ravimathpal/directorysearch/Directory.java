package com.example.ravimathpal.directorysearch;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi Mathpal on 20-03-2017.
 */

public class Directory {

    private static final int TYPE = 0;
    private String directoryName;

    private Directory parentDirectory = null;

    List<Directory> listOfSubDirectories;
    List<DirectoryFile> listOfDirectoryFiles;

    public Directory(String directoryName) {
        this.directoryName = directoryName;
        listOfDirectoryFiles = new ArrayList<>();
        listOfSubDirectories = new ArrayList<>();
    }

    public String getDirectoryName() {
        return directoryName;
    }

    public Directory getParentDirectory() {
        return parentDirectory;
    }

    public void setParentDirectory(Directory parentDirectory) {
        this.parentDirectory = parentDirectory;
    }

    public void setDirectoryName(String directoryName) {
        this.directoryName = directoryName;
    }

    public List<Directory> getListOfSubDirectories() {
        return listOfSubDirectories;
    }

    public List<DirectoryFile> getListOfDirectoryFiles() {
        return listOfDirectoryFiles;
    }

    public void addDirectory(Directory directory) {
        directory.setParentDirectory(this);
        listOfSubDirectories.add(directory);
    }

    public void addFile(DirectoryFile file) {
        listOfDirectoryFiles.add(file);
    }

    public boolean isDirectoryName(String name) {
        return (name.equals(directoryName));
    }

    public boolean hasFile(String name) {
        for (DirectoryFile file : listOfDirectoryFiles) {
            if (file.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasDirectory(String name) {
        for (Directory directory : listOfSubDirectories) {
            if (directory.getDirectoryName().equals(name))
                return true;
        }
        return false;
    }

    public Directory getDirectory(String name) {
        for (Directory directory : listOfSubDirectories) {
            if (directory.getDirectoryName().equals(name)) {
                return directory;
            }
        }
        return null;
    }
}
