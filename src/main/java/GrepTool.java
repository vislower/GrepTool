package main.java;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GrepTool {

    String grep(String pattern, List<String> flags, List<String> files) {
        Set<String> grepFlags = new HashSet<>(flags);
        StringBuilder sb = new StringBuilder();

        for (String file : files) {

            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
                String line;
                while ((line = reader.readLine()) != null) {
                    if (grepFlags.contains("-i")) {
                        line.toLowerCase();
                    }
                }


                reader.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            sb.append("\n");
        }

        return sb.toString();
    }

}