package it.exercises.validator;

import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import java.util.regex.Matcher;
@Component
public class MailValidator {
    
    // Pattern regex per validazione email (RFC 5322 compliant)
    private final String EMAIL_PATTERN = 
        "^[a-zA-Z0-9_+&*-]+(?:\\.[a-zA-Z0-9_+&*-]+)*@" +
        "(?:[a-zA-Z0-9-]+\\.)+[a-zA-Z]{2,7}$";
    
    private final Pattern pattern = Pattern.compile(EMAIL_PATTERN);
    
    /**
     * Valida un indirizzo email usando regex
     * @param email l'indirizzo email da validare
     * @return true se l'email è valida, false altrimenti
     */
    public boolean isValidEmail(String email) {
        if (email == null || email.trim().isEmpty()) {
            return false;
        }
        
        Matcher matcher = pattern.matcher(email.trim());
        return matcher.matches();
    }
    
    /**
     * Valida un indirizzo email con controlli aggiuntivi
     * @param email l'indirizzo email da validare
     * @return true se l'email è valida, false altrimenti
     */
    public boolean isValidEmailStrict(String email) {
        if (!isValidEmail(email)) {
            return false;
        }
        
        email = email.trim();
        
        // Controlli aggiuntivi
        if (email.length() > 254) return false; // RFC 5321 limit
        if (email.startsWith(".") || email.endsWith(".")) return false;
        if (email.contains("..")) return false; // punti consecutivi
        
        String[] parts = email.split("@");
        if (parts.length != 2) return false;
        
        String localPart = parts[0];
        String domainPart = parts[1];
        
        // Validazione parte locale (prima di @)
        if (localPart.length() > 64) return false; // RFC 5321 limit
        if (localPart.startsWith(".") || localPart.endsWith(".")) return false;
        
        // Validazione dominio
        if (domainPart.length() > 253) return false;
        if (domainPart.startsWith("-") || domainPart.endsWith("-")) return false;
        
        return true;
    }
    
    /**
     * Restituisce un messaggio di errore dettagliato per email non valide
     * @param email l'indirizzo email da validare
     * @return messaggio di errore o null se l'email è valida
     */
    public String validateEmailWithMessage(String email) {
        if (email == null) {
            return "L'email non può essere null";
        }
        
        if (email.trim().isEmpty()) {
            return "L'email non può essere vuota";
        }
        
        email = email.trim();
        
        if (email.length() > 254) {
            return "L'email è troppo lunga (massimo 254 caratteri)";
        }
        
        if (!email.contains("@")) {
            return "L'email deve contenere il simbolo @";
        }
        
        String[] parts = email.split("@");
        if (parts.length != 2) {
            return "L'email deve contenere esattamente un simbolo @";
        }
        
        String localPart = parts[0];
        String domainPart = parts[1];
        
        if (localPart.isEmpty()) {
            return "La parte locale dell'email (prima di @) non può essere vuota";
        }
        
        if (domainPart.isEmpty()) {
            return "Il dominio dell'email (dopo @) non può essere vuoto";
        }
        
        if (localPart.length() > 64) {
            return "La parte locale dell'email è troppo lunga (massimo 64 caratteri)";
        }
        
        if (!pattern.matcher(email).matches()) {
            return "Il formato dell'email non è valido";
        }
        
        return null; // Email valida
    }
    
 
}
