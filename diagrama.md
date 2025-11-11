classDiagram

%% === MODELO PRINCIPAL ===
class Main {
  +main(String[] args)
}

class ScreenManager {
  -UserController userController
  -VaultController vaultController
  -VaultService vaultService
  +start()
  +stop()
}

%% === CONTROLLERS ===
class UserController {
  -UserRepository userRepo
  -VaultService vaultService
  +register()
  +login()
  +deleteAccount()
  +updateUsername()
  +updatePassword()
}

class VaultController {
  -VaultService vaultService
  +listEntries()
  +addEntry()
  +updateEntry()
  +removeEntry()
  +generatePassword()
}

%% === EXCEÇÕES ===
class AuthenticationException
class EntryNotFoundException
class PersistenceException

Exception <|-- AuthenticationException
Exception <|-- EntryNotFoundException
Exception <|-- PersistenceException

%% === MODELOS ===
class User {
  +String username
  +String verifierHash
  +getUsername()
  +getVerifierHash()
}

class VaultEntry {
  +String id
  +String title
  +String username
  +String encryptedPassword
  +String url
  +String notes
  +getId()
  +getTitle()
  +getUsername()
  +getEncryptedPassword()
  +getUrl()
  +getNotes()
}

%% === REPOSITÓRIOS ===
class UserRepository {
  +save(User user)
  +findByUsername(String username)
  +delete(User user)
}

class VaultRepository {
  +save(VaultEntry entry)
  +findAll()
  +deleteById(String id)
  +findById(String id)
}

%% === SERVICES ===
class VaultService {
  -VaultRepository vaultRepo
  +listEntries()
  +generatePassword()
  +saveEntry()
  +deleteEntry()
}

%% === UTILITÁRIOS ===
class Color {
  +toString()
  +apply()
  +bold()
}

class ConsoleUtil {
  +clear()
  +print()
  +readLine()
}

class CryptoUtil {
  +encrypt()
  +decrypt()
  +deriveKey()
}

class CSVUtil {
  +readCSV()
  +writeCSV()
}

class PasswordGenerator {
  +generate()
  +scramblePassword()
}

%% === TELAS / VIEWS ===
class Screen {
  +printMenuOption()
  +printMenuHeader()
  +printInputMessage()
  +systemMessage()
  +showInvalidOption()
  +tryParseOption()
}

class AccountSettingsScreen {
  +show()
}
class AuthenticationScreen {
  +show()
}
class DeleteAccountScreen {
  +show()
}
class LoginScreen {
  +show()
}
class RegisterScreen {
  +show()
}
class UpdatePasswordScreen {
  +show()
}
class UpdateUsernameScreen {
  +show()
}
class VaultAddEntryScreen {
  +show()
}
class VaultGeneratePasswordScreen {
  +show()
}
class VaultGenerateRandomPassword {
  +show()
}
class VaultGenerateScrambledPassword {
  +show()
}
class VaultListEntries {
  +show()
}
class VaultMenuScreen {
  +show()
}
class VaultRemoveEntryScreen {
  +show()
}
class VaultUpdateEntryScreen {
  +show()
}
class VaultViewDecryptedPassword {
  +show()
}

%% === HERANÇA DAS TELAS ===
Screen <|-- AccountSettingsScreen
Screen <|-- AuthenticationScreen
Screen <|-- DeleteAccountScreen
Screen <|-- LoginScreen
Screen <|-- RegisterScreen
Screen <|-- UpdatePasswordScreen
Screen <|-- UpdateUsernameScreen
Screen <|-- VaultAddEntryScreen
Screen <|-- VaultGeneratePasswordScreen
Screen <|-- VaultGenerateRandomPassword
Screen <|-- VaultGenerateScrambledPassword
Screen <|-- VaultListEntries
Screen <|-- VaultMenuScreen
Screen <|-- VaultRemoveEntryScreen
Screen <|-- VaultUpdateEntryScreen
Screen <|-- VaultViewDecryptedPassword

%% === RELACIONAMENTOS ===
Main --> ScreenManager : usa
ScreenManager --> UserController : gerencia
ScreenManager --> VaultController : gerencia
ScreenManager --> VaultService : injeta

UserController --> UserRepository : acessa
UserController --> VaultService : usa
VaultController --> VaultService : usa
VaultService --> VaultRepository : acessa
VaultRepository --> VaultEntry : manipula
UserRepository --> User : manipula

VaultService --> PasswordGenerator : utiliza
VaultService --> CryptoUtil : criptografa

CSVUtil --> UserRepository : manipula persistência
CSVUtil --> VaultRepository : manipula persistência

ConsoleUtil --> Screen : utilitário de interface
Color --> Screen : estiliza texto

ScreenManager --> Screen : controla navegação
Screen --> UserController : interage
Screen --> VaultController : interage
