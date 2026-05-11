package org.example.model;

import java.util.List;

public interface SearchStrategy {
    public List<Folder> search(Folder parent, String args);
}
