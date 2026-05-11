// package org.example;

import java.util.*;
import java.util.stream.Collectors;

public class FileSearch {

    Folder root = new Folder("", new HashMap<>(), false, 0, "/");

    public FileSearch() {

    }

    public void putFile(String path, int sizeMb) {
        String[] paths = path.split("/");
        StringBuilder absolutePath = new StringBuilder("/");
        Folder curr = root;
        for (int i = 1; i < paths.length; i++) {
            String pathName = paths[i];
            absolutePath.append(pathName);
            if (i != paths.length - 1) {
                absolutePath.append("/");
            }
            if (Objects.isNull(curr.search(pathName, sizeMb))) {
                curr = curr.add(pathName, sizeMb, String.valueOf(absolutePath), i == paths.length-1);
            } else {
                curr = curr.search(pathName, sizeMb);
            }
        }
    }

    private Folder searchFile(String dirPath) {
        String[] paths = dirPath.split("/");
        Folder curr = root;
        for (int i = 1; i < paths.length; i++) {
            String pathName = paths[i];
            curr = curr.search(pathName);
        }
        return curr;
    }

    public List<String> fullTraversal() {
        List<Folder> searchList = root.fullTraversal(root);
        List<String> result = new ArrayList<>();
        for (Folder folder: searchList) {
            result.add(folder.getAbsolutePath());
        }
        return result;
    }

    public List<String> search(int ruleId, String dirPath, String args) {
        Folder dir = searchFile(dirPath);
        if (dir == null || dir.isFile()) return new ArrayList<>();
        SearchStrategy searchStrategy = SearchStrategyFactory.getSearchStrategyInstance(ruleId);
        List<Folder> searchList = searchStrategy.search(dir, args);
        List<String> result = new ArrayList<>();
        for (Folder folder: searchList) {
            result.add(folder.getAbsolutePath());
        }
        return result;
    }
}

class SearchByFileType implements SearchStrategy {
    @Override
    public List<Folder> search(Folder parent, String args) {
        List<Folder> results = parent.fullTraversal(parent);
        return results.stream().filter(folder -> args.equals(folder.getFileType(folder))).sorted(Comparator.comparing(Folder::getAbsolutePath)).collect(Collectors.toList());
    }
}

class SearchBySize implements SearchStrategy {
    @Override
    public List<Folder> search(Folder parent, String args) {
        List<Folder> results = parent.fullTraversal(parent);
        return results.stream().filter(folder -> folder.getSize() > Integer.parseInt(args)).sorted(Comparator.comparing(Folder::getAbsolutePath)).collect(Collectors.toList());
    }
}

class SearchStrategyFactory {
    public static SearchStrategy getSearchStrategyInstance(int ruleId) {
        switch (ruleId) {
            case 1: return new SearchBySize();
            case 2: return new SearchByFileType();
            default: throw new IllegalArgumentException("No valid ruleId " + ruleId);
        }
    }
}

interface SearchStrategy {
    List<Folder> search(Folder parent, String args);
}

class Folder {

    private String name;
    private String absolutePath;
    private HashMap<String, Folder> childs;
    private boolean isFile;
    private int size;

    public boolean isFile() {
        return isFile;
    }

    public Folder(String name, HashMap<String, Folder> childs, boolean isFile, int size, String absolutePath) {
        this.name = name;
        this.childs = childs;
        this.isFile = isFile;
        this.size = size;
        this.absolutePath = absolutePath;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
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

    public Folder add(String file, int size, String absolutePath, boolean isFile) {
//        if (this.isFile) throw new IllegalArgumentException("Not a directory");
        Folder childFolder = new Folder(file, new HashMap<>(), isFile, size, absolutePath);
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
        for (String path : parent.childs.keySet()) {
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
        System.out.println(file);
        int dotIndex = file.name.lastIndexOf('.');
        if (dotIndex == -1) return "";
        return file.name.substring(dotIndex);
    }
}
