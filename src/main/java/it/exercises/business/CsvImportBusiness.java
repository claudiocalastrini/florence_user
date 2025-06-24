package it.exercises.business;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import it.exercises.model.io.CsvImportError;
import it.exercises.model.io.CsvImportResult;
import it.exercises.model.io.UserIn;
import it.exercises.service.UserService;

@Service
public class CsvImportBusiness {
    
    @Autowired
    private UserService userService;
    
    /**
     * OPZIONE 1: Import transazionale - tutto o niente
     * Se anche un solo record fallisce, viene fatto rollback di tutto
     */
    @Transactional
    public CsvImportResult importUsersTransactional(MultipartFile file) throws IOException {
        List<UserIn> users = parseCsvFile(file);
        CsvImportResult result = new CsvImportResult();
        result.setTotalRecords(users.size());
        
        try {
            // Validazione preliminare di tutti i record
            validateAllUsers(users, result);
            
            if (!result.getErrors().isEmpty()) {
                result.setAllSuccess(false);
                result.setSuccessfulRecords(0);
                result.setFailedRecords(users.size());
                // Il rollback avverrà automaticamente per l'eccezione
                throw new RuntimeException("Validazione fallita per alcuni record");
            }
            
            // Se arriviamo qui, tutti i record sono validi
            for (UserIn user : users) {
                userService.addUser(user);
            }
            
            result.setSuccessfulRecords(users.size());
            result.setFailedRecords(0);
            result.setAllSuccess(true);
            
        } catch (Exception e) {
            // In caso di errore, il rollback è automatico
            result.setAllSuccess(false);
            result.setSuccessfulRecords(0);
            result.setFailedRecords(users.size());
            throw e; // Rilancia l'eccezione per il rollback
        }
        
        return result;
    }
    
    /**
     * OPZIONE 2: Import parziale - processa tutti i record e ritorna successi/fallimenti
     */
    public CsvImportResult importUsersPartial(MultipartFile file) throws IOException {
        List<UserIn> users = parseCsvFile(file);
        CsvImportResult result = new CsvImportResult();
        result.setTotalRecords(users.size());
        
        int successCount = 0;
        int failureCount = 0;
        
        for (int i = 0; i < users.size(); i++) {
            UserIn user = users.get(i);
            try {
                // Validazione del singolo utente
                validateUser(user);
                
                // Tentativo di salvataggio
                userService.addUser(user);
                successCount++;
                
            } catch (Exception e) {
                failureCount++;
                CsvImportError error = new CsvImportError(i + 2, e.getMessage(), user); // +2 perché partiamo da riga 2 (header = riga 1)
                result.getErrors().add(error);
            }
        }
        
        result.setSuccessfulRecords(successCount);
        result.setFailedRecords(failureCount);
        result.setAllSuccess(failureCount == 0);
        
        return result;
    }
    
    /**
     * Parsing del file CSV
     */
    private List<UserIn> parseCsvFile(MultipartFile file) throws IOException {
        List<UserIn> users = new ArrayList<>();
        
        try (InputStream is = file.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(is))) {
            
            String line;
            boolean isFirstLine = true;
            
            while ((line = reader.readLine()) != null) {
                // Salta l'header
                if (isFirstLine) {
                    isFirstLine = false;
                    continue;
                }
                
                UserIn user = parseCsvLine(line);
                if (user != null) {
                    users.add(user);
                }
            }
        }
        
        return users;
    }
    
    /**
     * Parsing di una singola riga CSV
     * Formato atteso: name,surname,mail,address
     */
    private UserIn parseCsvLine(String line) {
        if (line == null || line.trim().isEmpty()) {
            return null;
        }
        
        String[] fields = line.split(",");
        if (fields.length < 4) {
            return null;
        }
        
        UserIn user = new UserIn();
        user.setName(fields[0].trim());
        user.setSurname(fields[1].trim());
        user.setMail(fields[2].trim());
        user.setAddress(fields[3].trim());
        
        return user;
    }
    
    /**
     * Validazione di tutti gli utenti (per modalità transazionale)
     */
    private void validateAllUsers(List<UserIn> users, CsvImportResult result) {
        for (int i = 0; i < users.size(); i++) {
            try {
                validateUser(users.get(i));
            } catch (Exception e) {
                CsvImportError error = new CsvImportError(i + 2, e.getMessage(), users.get(i));
                result.getErrors().add(error);
            }
        }
    }
    
    /**
     * Validazione di un singolo utente
     */
    private void validateUser(UserIn user) {
        if (user.getName() == null || user.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Nome è obbligatorio");
        }
        
        if (user.getSurname() == null || user.getSurname().trim().isEmpty()) {
            throw new IllegalArgumentException("Cognome è obbligatorio");
        }
        
        if (user.getMail() == null || user.getMail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email è obbligatoria");
        }
        
        // Validazione email base
        if (!user.getMail().contains("@")) {
            throw new IllegalArgumentException("Formato email non valido");
        }
        
        if (user.getAddress() == null || user.getAddress().trim().isEmpty()) {
            throw new IllegalArgumentException("Indirizzo è obbligatorio");
        }
    }
}