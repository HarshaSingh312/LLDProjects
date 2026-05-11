package org.example.model;

import java.util.*;

public class Folder {

    private String name;

    public String getAbsolutePath() {
        return absolutePath;
    }

    private String absolutePath;
    private HashMap<String, Folder> childs;
    private boolean isFile;

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }

    private int size;


    public Folder(String name, HashMap<String, Folder> childs, boolean isFile, int size, String absolutePath) {
        this.name = name;
        this.childs = childs;
        this.isFile = isFile;
        this.size = size;
        this.absolutePath = absolutePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public HashMap<String, Folder> getChilds() {
        return childs;
    }

    public void setChilds(HashMap<String, Folder> childs) {
        this.childs = childs;
    }

    private boolean isFile(String fileName) {
        String[] paths = fileName.split("\\.");
        return paths.length > 1;
    }

    public Folder add(String file, int size, String absolutePath) {
        if (this.isFile) throw new IllegalArgumentException("Not a directory");
        Folder childFolder = new Folder(file, new HashMap<>(), isFile(file), size,absolutePath);
        this.childs.put(file, childFolder);
        return childFolder;
    }

    public Folder search(String file, int size) {
        Folder child = childs.getOrDefault(file, null);
        if (Objects.isNull(child)) return child;
        child.setSize(size);
        return child;
    }

    public Folder search(String file) {
        return childs.getOrDefault(file, null);
    }

    private void dfs(Folder parent, List<Folder> result) {
        if (parent.isFile) {
            result.add(parent);
        }

        for (String path: parent.childs.keySet()) {
            dfs(parent.childs.get(path), result);
        }
    }

    public List<Folder> fullTraversal(Folder parent) {
        List<Folder> folders = new ArrayList<>();
        dfs(parent, folders);
        return folders;
    }

    public String getFileType(Folder file) {
        if (!file.isFile) throw new IllegalArgumentException();
        String[] paths = this.name.split("\\.");
        return "." + paths[1];
    }
}
