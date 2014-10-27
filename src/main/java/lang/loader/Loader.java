package lang.loader;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Loader {

    public static final String LINE_SEP = System.getProperty("line.separator");

    public static String load(String... resourcePaths) throws IOException {
        StringBuilder code = new StringBuilder();
        for (String name : resourcePaths) {
            InputStream stream = Loader.class.getClassLoader().getResourceAsStream(name);
            InputStreamReader streamReader = new InputStreamReader(stream);
            BufferedReader reader = new BufferedReader(streamReader);
            try {
                while (true) {
                    String line = reader.readLine();
                    if (line == null) break;
                    code.append(line);
                    code.append(LINE_SEP);
                }
            } finally {
                reader.close();
            }
        }
        return code.toString();
    }
}
