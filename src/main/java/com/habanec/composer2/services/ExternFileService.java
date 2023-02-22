package com.habanec.composer2.services;

import com.habanec.composer2.models.Composition;
import com.habanec.composer2.utils.Utils;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ExternFileService implements DokumentationService {

    @Override
    public String save(Composition composition, String filename, String hash) {
        Path path = Paths.get(filename + "-" + hash + ".txt");
        List<String> newLines = new ArrayList<>();

        newLines.add(composition.getForm().name());
        newLines.add(composition.getModeIndex() + "_" + composition.getQuintCircleMainKeyIndex());
        newLines.add(composition.getMelody().getStartingGrade().index + "");

        newLines.add(Utils.arrayToString(composition.getMelody().getShuffledRhythmPatterns()));
        newLines.add(Utils.arrayToString(composition.getMelody().getShuffledTunePatterns()));
        newLines.add(Utils.arrayToString(composition.getMelody().getUserSpecialShifters()));
        newLines.add(Utils.arrayToString(composition.getAccompaniment().getShuffledFigurationEnums()));

        newLines.add(composition.getTempo() + "");
        newLines.add(hash);

        try {
            Files.write(path, newLines);
            return "Documentation successfully saved to " + path;
        } catch (IOException e) {
            e.printStackTrace();
            return "Error! No success writing to file " + path;
        }
    }


    @Override
    public List<String> readFile(String filename) {
        try {
            return Files.readAllLines(Paths.get(filename));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public List<String> listFilesInDirectory(String dirname) {
        List<String> list = new ArrayList();
        try {
            try (Stream<Path> stream = Files.list(Paths.get(dirname))) {
                list = stream
                        .filter(file -> !Files.isDirectory(file))
                        .filter(file -> file.toString().contains(".txt"))
                        .map(Path::getFileName)
                        .map(Path::toString)
                        .sorted(Comparator.reverseOrder())
                        .collect(Collectors.toList());
                if (list.isEmpty()) {
                    list.add("No files...");
                }
            }
        } catch (IOException e) {
            list.add("Reading file error");
        }
        return list;
    }
}
