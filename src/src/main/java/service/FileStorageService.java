package com.realestate.search.service;

import com.realestate.search.model.SearchCriteria;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class FileStorageService {
    private static final String FILE_PATH = "favorites.txt";

    public void saveFavorite(String propertyId, String title) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            writer.write("FAV|" + propertyId + "|" + title);
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void savePreference(SearchCriteria criteria) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            String id = UUID.randomUUID().toString();
            writer.write("PREF|" + id + "|" + criteria.toString());
            writer.newLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<String[]> getFavorites() {
        return readLinesByPrefix("FAV|");
    }

    public List<String[]> getPreferences() {
        return readLinesByPrefix("PREF|");
    }

    private List<String[]> readLinesByPrefix(String prefix) {
        List<String[]> list = new ArrayList<>();
        File file = new File(FILE_PATH);
        if (!file.exists()) return list;

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(prefix)) {
                    list.add(line.substring(prefix.length()).split("\\|", -1));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void deleteEntry(String type, String id) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        List<String> lines = new ArrayList<>();
        String targetPrefix = type + "|" + id;
        
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.startsWith(targetPrefix + "|") && !line.equals(targetPrefix)) {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void updatePreference(String id, SearchCriteria criteria) {
        File file = new File(FILE_PATH);
        if (!file.exists()) return;

        List<String> lines = new ArrayList<>();
        String targetPrefix = "PREF|" + id + "|";

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(targetPrefix)) {
                    lines.add("PREF|" + id + "|" + criteria.toString());
                } else {
                    lines.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (String line : lines) {
                writer.write(line);
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
