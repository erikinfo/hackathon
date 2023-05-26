package io.swagger.api;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

public class IGLoader {

    public static void main(String[] args) {
        IGLoader ig = new IGLoader();
    
        try {
            List<String> x = ig.loadFhirResources("/home/erik/Downloads/test-ig");
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


    public List<String> loadFhirResources(String directoryPath) throws IOException {
        File folder = new File(directoryPath);
        File[] listOfFiles = folder.listFiles();
        List<String> fhirResources = new ArrayList<>();
    
        for (File file : listOfFiles) {
            if (file.isFile() && file.getName().endsWith(".json")) {
                String content = new String(Files.readAllBytes(Paths.get(file.getPath())), StandardCharsets.UTF_8);
                fhirResources.add(content);
            }
        }
    
        return fhirResources;
    }
    
    
}
