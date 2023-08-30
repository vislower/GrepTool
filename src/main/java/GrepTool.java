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
                    boolean match = matchPattern(pattern, sb, grepFlags, line);

                }


                reader.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

            sb.append("\n");
        }

        return sb.toString();
    }

    boolean matchPattern(String patternToMatch, StringBuilder sb, Set<String> grepFlags, String line) {
        String lineBackup = line; // keep backup of original line
        if (grepFlags.contains("-i")) {
            line = line.toLowerCase();
            patternToMatch = patternToMatch.toLowerCase();
        }

        if (grepFlags.contains("-x")) {
            if (line.equals(patternToMatch) && !grepFlags.contains("-v")) {
                if (grepFlags.contains("-l")) {
                    return true;
                }
                sb.append(lineBackup);
                return true;
            } else if (grepFlags.contains("-v")) {
                sb.append(lineBackup);
                return true;
            } else {
                return false;
            }
        }

        if (line.contains(patternToMatch) && !grepFlags.contains("-v")) {
            if (grepFlags.contains("-l")) {
                return true;
            }
            int startIndex = line.indexOf(patternToMatch);
            sb.append(lineBackup.substring(startIndex, startIndex+patternToMatch.length()));
            return true;
        } else if (grepFlags.contains("-v")) {
            // capture what doesn't match
            return true;
        }
        
        return false;
    }

}