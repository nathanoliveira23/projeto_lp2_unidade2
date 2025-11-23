package repository;

import java.util.List;

import model.VaultEntry;

public interface IVaultRepository {

    List<VaultEntry> loadVault(String username) throws Exception;
    void saveVault(String username, List<VaultEntry> entries) throws Exception;
}
