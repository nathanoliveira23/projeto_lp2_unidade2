package util;

import java.util.List;
import java.util.Arrays;
import java.util.Map;
import java.util.HashMap;
import java.security.SecureRandom;

public class PasswordGenerator {
    // Lista de caracteres possíveis para gerar senha
    private static final List<Character> CHARS = Arrays.asList(
            '0','1','2','3','4','5','6','7','8','9',
            'A','B','C','D','E','F','G','H','I','J',
            'K','L','M','N','O','P','Q','R','S','T',
            'U','V','W','X','Y','Z',
            'a','b','c','d','e','f','g','h','i','j',
            'k','l','m','n','o','p','q','r','s','t',
            'u','v','w','x','y','z',
            '@','#','$','%','&'
    );

    // Mapa de correspondência para embaralhar caracteres
    private static final Map<Character, List<Character>> CORRESPONDENCE = new HashMap<>();
    static {
        CORRESPONDENCE.put('a', Arrays.asList('A', 'a', '@', '4'));
        CORRESPONDENCE.put('b', Arrays.asList('B', 'b', '6'));
        CORRESPONDENCE.put('c', Arrays.asList('C', 'c'));
        CORRESPONDENCE.put('d', Arrays.asList('D', 'd'));
        CORRESPONDENCE.put('e', Arrays.asList('E', 'e', '&', '3'));
        CORRESPONDENCE.put('f', Arrays.asList('F', 'f'));
        CORRESPONDENCE.put('g', Arrays.asList('G', 'g', '&', '9'));
        CORRESPONDENCE.put('h', Arrays.asList('H', 'h', '#'));
        CORRESPONDENCE.put('i', Arrays.asList('I', 'i', '1'));
        CORRESPONDENCE.put('j', Arrays.asList('J', 'j'));
        CORRESPONDENCE.put('k', Arrays.asList('K', 'k'));
        CORRESPONDENCE.put('l', Arrays.asList('L', 'l', '1'));
        CORRESPONDENCE.put('m', Arrays.asList('M', 'm'));
        CORRESPONDENCE.put('n', Arrays.asList('N', 'n'));
        CORRESPONDENCE.put('o', Arrays.asList('O', 'o', '0'));
        CORRESPONDENCE.put('p', Arrays.asList('P', 'p'));
        CORRESPONDENCE.put('q', Arrays.asList('Q', 'q', '9'));
        CORRESPONDENCE.put('r', Arrays.asList('R', 'r'));
        CORRESPONDENCE.put('s', Arrays.asList('S', 's', '$', '5'));
        CORRESPONDENCE.put('t', Arrays.asList('T', 't', '7'));
        CORRESPONDENCE.put('u', Arrays.asList('U', 'u'));
        CORRESPONDENCE.put('v', Arrays.asList('V', 'v'));
        CORRESPONDENCE.put('w', Arrays.asList('W', 'w'));
        CORRESPONDENCE.put('x', Arrays.asList('X', 'x'));
        CORRESPONDENCE.put('y', Arrays.asList('Y', 'y', '7'));
        CORRESPONDENCE.put('z', Arrays.asList('Z', 'z', '2'));
    }
    private SecureRandom rnd = new SecureRandom();

    public String generate(int length) {
        if (length <= 0)
            throw new IllegalArgumentException(Color.RED.apply("O tamanho da senha dever ser um valor positivo"));

        StringBuilder sb = new StringBuilder(length);

        for (int i = 0; i < length; i++) {
            int idx = rnd.nextInt(CHARS.size());
            sb.append(CHARS.get(idx));
        }

        return sb.toString();
    }

    public String scramblePassword(String input) {
        if (input == null)
            return "";

        StringBuilder scrambledPassword = new StringBuilder(input.length());

        for (char c : input.toCharArray()) {
            List<Character> charsCandidate = CORRESPONDENCE.get(c);

            if (charsCandidate != null && charsCandidate.size() != 0) {
                int idx = rnd.nextInt(charsCandidate.size());
                scrambledPassword.append(charsCandidate.get(idx));
            }
            else {
                scrambledPassword.append(c);
            }
        }

        return scrambledPassword.toString();
    }
}
