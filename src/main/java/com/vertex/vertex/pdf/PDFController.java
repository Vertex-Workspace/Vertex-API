package com.vertex.vertex.pdf;


import lombok.AllArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pdf")
@AllArgsConstructor
public class PDFController {

    private PDFService pdfGenerationService;

    @GetMapping("/generate/{taskID}")
    public ResponseEntity<?> generatePdf(@PathVariable Long taskID) {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePdf(taskID);

            // Set content type as application/pdf
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);

            // Set content disposition as attachment
            headers.setContentDispositionFormData("filename", "custom.pdf");
            return new ResponseEntity<>(pdfBytes, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.OK);
        }
    }
}
