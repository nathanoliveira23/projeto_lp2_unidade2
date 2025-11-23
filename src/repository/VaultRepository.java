package repository;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import model.VaultEntry;
import util.CSVUtil;

public class VaultRepository implements IVaultRepository {

    public List<VaultEntry> loadVault(String username) throws IOException {
        Path file = Paths.get("data/vaults", username + ".csv");

        List<String[]> csvRows = CSVUtil.readCSV(file);
        List<VaultEntry> entries = new ArrayList<>();

        for (String[] p : csvRows) {
            if (p.length >= 6) {
                VaultEntry entry = new VaultEntry(p[1], p[2], p[3], p[4], p[5]);
                try {
                    var f = VaultEntry.class.getDeclaredField("id");
                    f.setAccessible(true);
                    f.set(entry, p[0]);
                } 
                catch (Exception e) {}

                entries.add(entry);
            }
        }
        return entries;
    }

    public void saveVault(String username, List<VaultEntry> entries) throws IOException {
        Path file = Paths.get("data/vaults", username + ".csv");
        List<String[]> rows = new ArrayList<>();

        for (VaultEntry e : entries) {
            rows.add(new String[]{
                e.getId(), e.getTitle(), e.getUsername(),
                e.getEncryptedPassword(), e.getUrl(), e.getNotes()
            });
        }

        CSVUtil.writeCsv(file, rows);
    }
}
