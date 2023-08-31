import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayDeque;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

class GrepTool {

    String grep(String pattern, List<String> flags, List<String> files) {
        Set<String> grepFlags = new HashSet<>(flags);
        Collection<CharSequence> matches = new ArrayDeque<>();

        for (String file : files) {

            try {
                BufferedReader reader = new BufferedReader(new FileReader(new File(file)));
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    boolean match = matchPattern(pattern, matches, grepFlags, line, lineNumber, file, files.size() > 1);
                    if (match && grepFlags.contains("-l")) { // don't print multiple times same file name
                        break;
                    }
                    lineNumber++;
                }

                reader.close();
            } catch (IOException ioe) {
                ioe.printStackTrace();
            }

        }

        return String.join("\n", matches);
    }

    boolean matchPattern(String patternToMatch, Collection<CharSequence> matches, Set<String> grepFlags, String line, int lineNumber, String fileName, boolean multipleFiles) {
        String lineBackup = line; // keep backup of original line
        if (grepFlags.contains("-i")) { // if search is case insensitive
            line = line.toLowerCase();
            patternToMatch = patternToMatch.toLowerCase();
        }


        if (grepFlags.contains("-x")) { // match entire line
            if (line.equals(patternToMatch) && !grepFlags.contains("-v")) {
                if (grepFlags.contains("-l")) { // only print file name
                    matches.add(fileName);
                    return true;
                }
                formatLine(multipleFiles, fileName, lineNumber, lineBackup, grepFlags, matches);
                return true;
            } else if (!line.equals(patternToMatch) && grepFlags.contains("-v")) {
                formatLine(multipleFiles, fileName, lineNumber, lineBackup, grepFlags, matches);
                return true;
            } else {
                return false;
            }
        }

        if (line.contains(patternToMatch)) { // search a match in the line
            if (grepFlags.contains("-v")) {
                return false;
            }
            if (grepFlags.contains("-l")) {
                matches.add(fileName);
                return true;
            }
            formatLine(multipleFiles, fileName, lineNumber, lineBackup, grepFlags, matches);
            return true;
        } else if (grepFlags.contains("-v")) {
            formatLine(multipleFiles, fileName, lineNumber, lineBackup, grepFlags, matches);
            return true;
        }
        
        return false;
    
    }

    private void formatLine(boolean multipleFiles, String fileName, int lineNumber, String lineBackup, Set<String> grepFlags, Collection<CharSequence> matches) {
        StringBuilder formatedLine = new StringBuilder();

        if (multipleFiles) {
            formatedLine.append(fileName + ":");
        }
        if (grepFlags.contains("-n")) {
            formatedLine.append(lineNumber + ":" + lineBackup);
        } else {
            formatedLine.append(lineBackup);
        }

        matches.add(formatedLine);
    }

}