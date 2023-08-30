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
                BufferedReader reader = new BufferedReader(new FileReader(new File("C:\\Users\\paolo\\Code\\GrepTool\\src\\main\\java\\" + file)));
                String line;
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    boolean match = matchPattern(pattern, sb, grepFlags, line, lineNumber, file, files.size() > 1);
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

        return sb.length() == 0 ? "" : sb.toString();
    }

    boolean matchPattern(String patternToMatch, StringBuilder sb, Set<String> grepFlags, String line, int lineNumber, String fileName, boolean multipleFiles) {
        String lineBackup = line; // keep backup of original line
        if (grepFlags.contains("-i")) {
            line = line.toLowerCase();
            patternToMatch = patternToMatch.toLowerCase();
        }

        if (grepFlags.contains("-x")) {
            if (line.equals(patternToMatch) && !grepFlags.contains("-v")) {
                if (!sb.isEmpty()) {
                    sb.append("\n");
                }
                if (grepFlags.contains("-l")) {
                    sb.append(fileName);
                    return true;
                }
                
                if (multipleFiles) {
                    sb.append(fileName + ":");
                }
                if (grepFlags.contains("-n")) {
                    sb.append(lineNumber + ":" + lineBackup);
                } else {
                    sb.append(lineBackup);
                }
                return true;
            } else if (grepFlags.contains("-v") && !line.equals(patternToMatch)) {
                if (!sb.isEmpty()) {
                    sb.append("\n");
                }
                if (multipleFiles) {
                    sb.append(fileName + ":");
                }
                if (grepFlags.contains("-n")) {
                    sb.append(lineNumber + ":" + lineBackup);
                } else {
                    sb.append(lineBackup);
                }
                return true;
            } else {
                return false;
            }
        }

        if (line.contains(patternToMatch)) {
            if (grepFlags.contains("-v")) {
                return false;
            }
            if (!sb.isEmpty()) {
                sb.append("\n");
            }
            if (grepFlags.contains("-l")) {
                sb.append(fileName);
                return true;
            }
            if (multipleFiles) {
                sb.append(fileName + ":");
            }
            if (grepFlags.contains("-n")) {
                sb.append(lineNumber + ":" + lineBackup);
            } else {
                sb.append(lineBackup);
            }
            return true;
        } else {
            if (grepFlags.contains("-v")) {
                if (!sb.isEmpty()) {
                    sb.append("\n");
                }
                if (multipleFiles) {
                    sb.append(fileName + ":");
                }
                if (grepFlags.contains("-n")) {
                    sb.append(lineNumber + ":" + lineBackup);
                } else {
                    sb.append(lineBackup);
                }
                return true;
            }
            return false;
        }
    
    }

}