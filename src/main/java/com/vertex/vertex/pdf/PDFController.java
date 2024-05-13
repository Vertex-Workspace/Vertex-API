package com.vertex.vertex.pdf;


import lombok.AllArgsConstructor;
import org.apache.tomcat.util.codec.binary.Base64;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.sql.rowset.serial.SerialBlob;
import java.sql.Blob;

@RestController
@AllArgsConstructor
public class PDFController {

    private final PDFService pdfGenerationService;

    @GetMapping("/task/{taskID}/pdf")
    public ResponseEntity<?> generatePdf(@PathVariable Long taskID) {
        try {
            byte[] pdfBytes = pdfGenerationService.generatePdf(taskID);

            String base64Pdf = Base64.encodeBase64String(pdfBytes);

            return new ResponseEntity<>(base64Pdf, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
