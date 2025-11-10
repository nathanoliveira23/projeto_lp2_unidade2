package util;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class CSVUtil {

    public static List<String[]> readCSV(Path path) throws IOException {
        List<String[]> rows = new ArrayList<>();

        if (!Files.exists(path))
            return rows;

        try (BufferedReader br = Files.newBufferedReader(path)) {
            String line;

            while ((line = br.readLine()) != null) {
                rows.add(line.split(",", -1)); // -1 mantém campos vazios
            }
        }
        return rows;
    }

    public static void writeCsv(Path path, List<String[]> rows) throws IOException {
        // Garante que o diretório pai existe
        Files.createDirectories(path.getParent());

        // Cria ou sobrescreve o mesmo arquivo
        try (BufferedWriter bw = Files.newBufferedWriter(
                path,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING,
                StandardOpenOption.WRITE)) {

            for (String[] row : rows) {
                bw.write(String.join(",", row));
                bw.newLine();
            }
        }
    }

    public static void appendCsv(Path path, String[] row) throws IOException {
        Files.createDirectories(path.getParent());
        try (BufferedWriter bw = Files.newBufferedWriter(
                path, StandardOpenOption.CREATE, StandardOpenOption.APPEND)) {
            bw.write(String.join(",", row));
            bw.newLine();
        }
    }
}
