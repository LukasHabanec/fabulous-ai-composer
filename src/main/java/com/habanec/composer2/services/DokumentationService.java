package com.habanec.composer2.services;

import com.habanec.composer2.models.Composition;

import java.util.List;

public interface DokumentationService {

    String save(Composition composition, String filename, String hash);

    List<String> readFile(String filename);

    List<String> listFilesInDirectory(String filename);

}
