package it.exercises.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import it.exercises.business.CsvImportBusiness;
import it.exercises.model.io.CsvImportError;
import it.exercises.model.io.CsvImportResult;



@RestController
@RequestMapping("/api/users")
@CrossOrigin
public class CsvController {
    
    @Autowired
    private CsvImportBusiness csvImportService;
    
    /**
     * OPZIONE 1: Import transazionale - tutto o niente
     * POST /api/users/import/transactional
     */
    @PostMapping(value = "/import/transactional", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import CSV utenti - modalità transazionale", 
               description = "Importa utenti da file CSV. Se anche un solo record fallisce, viene fatto rollback di tutto.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Import completato con successo"),
        @ApiResponse(responseCode = "422", description = "Errori di validazione nei dati"),
        @ApiResponse(responseCode = "400", description = "File non valido"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<CsvImportResult> importUsersTransactional(
            @Parameter(description = "File CSV contenente i dati degli utenti", 
                      content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Validazione del file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResult("File non può essere vuoto"));
            }
            
            if (!isValidCsvFile(file)) {
                return ResponseEntity.badRequest()
                    .body(createErrorResult("File deve essere in formato CSV"));
            }
            
            // Esecuzione dell'import transazionale
            CsvImportResult result = csvImportService.importUsersTransactional(file);
            
            if (result.isAllSuccess()) {
                return ResponseEntity.ok(result);
            } else {
                return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(result);
            }
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResult("Errore durante l'elaborazione: " + e.getMessage()));
        }
    }
    
    /**
     * OPZIONE 2: Import parziale - ritorna successi e fallimenti
     * POST /api/users/import/partial
     */
    @PostMapping(value = "/import/partial", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(summary = "Import CSV utenti - modalità parziale", 
               description = "Importa utenti da file CSV. Processa tutti i record e ritorna successi e fallimenti.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Import processato (anche con errori parziali)"),
        @ApiResponse(responseCode = "400", description = "File non valido"),
        @ApiResponse(responseCode = "500", description = "Errore interno del server")
    })
    public ResponseEntity<CsvImportResult> importUsersPartial(
            @Parameter(description = "File CSV contenente i dati degli utenti",
                      content = @Content(mediaType = MediaType.MULTIPART_FORM_DATA_VALUE))
            @RequestParam("file") MultipartFile file) {
        
        try {
            // Validazione del file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest()
                    .body(createErrorResult("File non può essere vuoto"));
            }
            
            if (!isValidCsvFile(file)) {
                return ResponseEntity.badRequest()
                    .body(createErrorResult("File deve essere in formato CSV"));
            }
            
            // Esecuzione dell'import parziale
            CsvImportResult result = csvImportService.importUsersPartial(file);
            
            // Ritorna sempre 200 OK anche se ci sono errori parziali
            return ResponseEntity.ok(result);
            
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(createErrorResult("Errore durante l'elaborazione: " + e.getMessage()));
        }
    }
    
    /**
     * Endpoint per ottenere il template CSV
     * GET /api/users/csv-template
     */
    @GetMapping("/csv-template")
    @Operation(summary = "Download template CSV", 
               description = "Scarica un file template CSV per l'import degli utenti")
    @ApiResponse(responseCode = "200", description = "Template CSV",
                content = @Content(mediaType = "text/csv"))
    public ResponseEntity<String> getCsvTemplate() {
        String template = "name,surname,mail,address\n" +
                         "Mario,Rossi,mario.rossi@email.com,Via Roma 1\n" +
                         "Luigi,Bianchi,luigi.bianchi@email.com,Via Milano 2";
        
        return ResponseEntity.ok()
            .header("Content-Type", "text/csv")
            .header("Content-Disposition", "attachment; filename=\"user_template.csv\"")
            .body(template);
    }
    
    /**
     * Validazione del file CSV
     */
    private boolean isValidCsvFile(MultipartFile file) {
        String contentType = file.getContentType();
        String filename = file.getOriginalFilename();
        
        return (contentType != null && 
                (contentType.equals("text/csv") || 
                 contentType.equals("application/csv") ||
                 contentType.equals("text/plain"))) ||
               (filename != null && filename.toLowerCase().endsWith(".csv"));
    }
    
    /**
     * Crea un risultato di errore
     */
    private CsvImportResult createErrorResult(String errorMessage) {
        CsvImportResult result = new CsvImportResult();
        result.setTotalRecords(0);
        result.setSuccessfulRecords(0);
        result.setFailedRecords(0);
        result.setAllSuccess(false);
        
        CsvImportError error = new CsvImportError(0, errorMessage, null);
        result.getErrors().add(error);
        
        return result;
    }
}