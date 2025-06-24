package it.exercises.model.io;

import java.util.ArrayList;
import java.util.List;

public class CsvImportResult {
    private int totalRecords;
    private int successfulRecords;
    private int failedRecords;
    private List<CsvImportError> errors;
    private boolean allSuccess;
    
    public CsvImportResult() {
        this.errors = new ArrayList<>();
    }
    
    // Getters e Setters
    public int getTotalRecords() {
        return totalRecords;
    }
    
    public void setTotalRecords(int totalRecords) {
        this.totalRecords = totalRecords;
    }
    
    public int getSuccessfulRecords() {
        return successfulRecords;
    }
    
    public void setSuccessfulRecords(int successfulRecords) {
        this.successfulRecords = successfulRecords;
    }
    
    public int getFailedRecords() {
        return failedRecords;
    }
    
    public void setFailedRecords(int failedRecords) {
        this.failedRecords = failedRecords;
    }
    
    public List<CsvImportError> getErrors() {
        return errors;
    }
    
    public void setErrors(List<CsvImportError> errors) {
        this.errors = errors;
    }
    
    public boolean isAllSuccess() {
        return allSuccess;
    }
    
    public void setAllSuccess(boolean allSuccess) {
        this.allSuccess = allSuccess;
    }
}