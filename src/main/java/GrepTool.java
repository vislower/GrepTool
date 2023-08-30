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
                int lineNumber = 1;
                while ((line = reader.readLine()) != null) {
                    boolean match = matchPattern(pattern, sb, grepFlags, line, lineNumber);
                    if (match) {
                        if (grepFlags.contains("-l")) {
                            sb.append(file);
                        }
                        sb.append("\n");
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

    boolean matchPattern(String patternToMatch, StringBuilder sb, Set<String> grepFlags, String line, int lineNumber) {
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

                if (grepFlags.contains("-n")) {
                    sb.append(lineNumber + ":" + lineBackup);
                } else {
                    sb.append(lineBackup);
                }
                return true;
            } else if (grepFlags.contains("-v")) {
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
            if (grepFlags.contains("-l")) {
                return true;
            }
            if (grepFlags.contains("-n")) {
                    sb.append(lineNumber + ":" + lineBackup);
            } else {
                sb.append(lineBackup);
            }
            return true;
        } else {
            if (grepFlags.contains("-v")) {
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