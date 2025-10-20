package util;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.Base64;

/**

* @class CryptoUtil
* @brief Classe utilitária para criptografia e descriptografia usando AES-GCM.
*
* Esta classe fornece métodos estáticos para:
* * Geração de salt aleatório.
* * Derivação de chave a partir de senha com PBKDF2.
* * Criptografia e descriptografia com AES no modo GCM.
*
* O AES-GCM oferece confidencialidade e autenticação (integridade dos dados).
  */
public class CryptoUtil {

  /** @brief Gerador de números aleatórios criptograficamente seguro. */
  private static final SecureRandom RAND = new SecureRandom();

  /** @brief Comprimento do salt em bytes. */
  private static final int SALT_LEN = 16;

  /** @brief Comprimento do vetor de inicialização (IV) em bytes. */
  private static final int IV_LEN = 12;

  /** @brief Tamanho da chave em bits (AES-256). */
  private static final int KEY_LEN = 256;

  /** @brief Número de iterações para a função PBKDF2 (quanto maior, mais seguro). */
  private static final int ITERATIONS = 65536;

  /**

  * @brief Gera um novo salt aleatório para derivação de chave.
  *
  * Cada senha deve usar um salt único para evitar ataques de rainbow tables.
  *
  * @return Array de bytes contendo o salt gerado.
    */
    public static byte[] newSalt() {
        byte[] s = new byte[SALT_LEN];   /**< Buffer para armazenar o salt. */
        RAND.nextBytes(s);               /**< Preenche o salt com bytes aleatórios. */

        return s;                        /**< Retorna o salt gerado. */
    }

    public static String SHA256(String password, byte[] salt) throws Exception {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");

            digest.update(salt);

            byte[] hashBytes = digest.digest(password.getBytes("UTF-8"));

            StringBuilder hexHash = new StringBuilder();

            for (byte b : hashBytes)
                hexHash.append(String.format("%02x", b));

            return hexHash.toString();
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException();
        }
    }

    public static Boolean SHA256Match(String plainPasswd, String hashedPasswd, byte[] salt) throws Exception {
        String candidateHash = SHA256(plainPasswd, salt);

        byte[] candidateHashBytes = Base64.getDecoder().decode(candidateHash);
        byte[] hashedPasswdBytes = Base64.getDecoder().decode(hashedPasswd);

        return MessageDigest.isEqual(candidateHashBytes, hashedPasswdBytes);
    }

  /**

  * @brief Deriva uma chave AES a partir de uma senha e de um salt.
  *
  * Usa PBKDF2 com HMAC-SHA256 para gerar uma chave de 256 bits a partir da senha.
  *
  * @param password Senha em formato de array de caracteres.
  * @param salt Array de bytes contendo o salt.
  * @return Objeto SecretKey utilizável em operações AES.
  * @throws Exception Se ocorrer erro durante a derivação da chave.
  *
  * @see PBEKeySpec
  * @see SecretKeyFactory
    */
    public static SecretKey deriveKey(char[] password, byte[] salt) throws Exception {
        PBEKeySpec spec = new PBEKeySpec(password, salt, ITERATIONS, KEY_LEN); /**< Define os parâmetros da derivação. */
        SecretKeyFactory skf = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256"); /**< Fábrica de chaves PBKDF2. */
        byte[] keyBytes = skf.generateSecret(spec).getEncoded(); /**< Gera os bytes da chave derivada. */

        return new SecretKeySpec(keyBytes, "AES"); /**< Retorna a chave formatada para uso em AES. */
    }

  /**

  * @brief Criptografa um texto em claro usando AES-GCM.
  *
  * Gera um IV aleatório, realiza a criptografia e concatena o IV com o texto cifrado.
  * O resultado é retornado como uma string Base64 para facilitar o armazenamento.
  *
  * @param plaintext Texto simples a ser criptografado.
  * @param key Chave AES usada para criptografia.
  * @return String codificada em Base64 contendo IV + texto cifrado.
  * @throws Exception Se ocorrer falha na operação de criptografia.
  *
  * @see Cipher
  * @see GCMParameterSpec
    */
    public static String encrypt(String plaintext, SecretKey key) throws Exception {
        byte[] iv = new byte[IV_LEN];           /**< Cria o vetor de inicialização (IV). */
        RAND.nextBytes(iv);                     /**< Preenche o IV com bytes aleatórios. */

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding"); /**< Configura o algoritmo AES em modo GCM. */
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);   /**< Define o tamanho da tag e o IV. */

        cipher.init(Cipher.ENCRYPT_MODE, key, spec); /**< Inicializa o Cipher em modo de encriptação. */
        byte[] cipherText = cipher.doFinal(plaintext.getBytes("UTF-8")); /**< Executa a criptografia. */

        byte[] combined = new byte[iv.length + cipherText.length]; /**< Array para armazenar IV + dados criptografados. */
        System.arraycopy(iv, 0, combined, 0, iv.length);
        System.arraycopy(cipherText, 0, combined, iv.length, cipherText.length);

        return Base64.getEncoder().encodeToString(combined); /**< Retorna o resultado em Base64. */
    }

  /**

  * @brief Descriptografa uma string cifrada em Base64 (IV + texto cifrado).
  *
  * Decodifica o Base64, separa o IV do texto cifrado e executa a operação inversa da criptografia.
  *
  * @param b64combined String Base64 contendo IV + texto cifrado.
  * @param key Chave AES usada para descriptografia.
  * @return Texto original em formato UTF-8.
  * @throws Exception Se ocorrer falha na operação de descriptografia.
  *
  * @see Cipher
  * @see GCMParameterSpec
    */
    public static String decrypt(String b64combined, SecretKey key) throws Exception {
        byte[] combined = Base64.getDecoder().decode(b64combined); /**< Decodifica Base64 para bytes. */
        byte[] iv = Arrays.copyOfRange(combined, 0, IV_LEN);       /**< Extrai o IV do início do array. */
        byte[] cipherText = Arrays.copyOfRange(combined, IV_LEN, combined.length); /**< Extrai o texto cifrado. */

        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");  /**< Configura o modo AES-GCM. */
        GCMParameterSpec spec = new GCMParameterSpec(128, iv);    /**< Recria o parâmetro GCM. */

        cipher.init(Cipher.DECRYPT_MODE, key, spec); /**< Inicializa o Cipher em modo de descriptografia. */
        byte[] plain = cipher.doFinal(cipherText);   /**< Executa a descriptografia. */

        return new String(plain, "UTF-8"); /**< Retorna o texto original. */
    }
}
