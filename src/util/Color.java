package util;

public enum Color {
    RESET("\u001B[0m"),
    BOLD("\u001B[1m"),
    UNDERLINE("\u001B[4m"),

    RED("\u001B[31m"),
    GREEN("\u001B[32m"),
    YELLOW("\u001B[33m"),
    BLUE("\u001B[34m"),
    PURPLE("\u001B[35m"),
    CYAN("\u001B[36m"),
    WHITE("\u001B[37m"),

    BRIGHT_RED("\u001B[91m"),
    BRIGHT_GREEN("\u001B[92m"),
    BRIGHT_YELLOW("\u001B[93m"),
    BRIGHT_BLUE("\u001B[94m"),
    BRIGHT_PURPLE("\u001B[95m"),
    BRIGHT_CYAN("\u001B[96m"),
    BRIGHT_WHITE("\u001B[97m");

    private final String code;

    Color(String code) {
        this.code = code;
    }

    @Override
    public String toString() {
        return code;
    }

    /** Aplica a cor diretamente ao texto */
    public String apply(String text) {
        return code + text + RESET;
    }

    /** Combina cor + negrito */
    public String bold(String text) {
        return BOLD.code + code + text + RESET;
    }
}
