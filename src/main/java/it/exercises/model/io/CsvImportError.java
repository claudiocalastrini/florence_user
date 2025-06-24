package it.exercises.model.io;


// Oggetto per rappresentare gli errori di import
public class CsvImportError {
    private int rowNumber;
    private String reason;
    private UserIn userData;
    
    public CsvImportError() {}
    
    public CsvImportError(int rowNumber, String reason, UserIn userData) {
        this.rowNumber = rowNumber;
        this.reason = reason;
        this.userData = userData;
    }
    
    // Getters e Setters
    public int getRowNumber() {
        return rowNumber;
    }
    
    public void setRowNumber(int rowNumber) {
        this.rowNumber = rowNumber;
    }
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
    
    public UserIn getUserData() {
        return userData;
    }
    
    public void setUserData(UserIn userData) {
        this.userData = userData;
    }
}