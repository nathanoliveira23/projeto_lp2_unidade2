package view;

import service.VaultService;

import java.util.Scanner;

import app.ScreenManager;

public abstract class Screen {
    protected final ScreenManager screenManager;
    protected final VaultService vaultService;
    protected final Scanner sc;

    public Screen(ScreenManager screenManager, VaultService vaultService, Scanner sc) {
        this.screenManager = screenManager;
        this.vaultService = vaultService;
        this.sc = sc;
    }

    public abstract Screen show();
}
