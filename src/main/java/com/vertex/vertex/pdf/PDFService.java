package com.vertex.vertex.pdf;

import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.io.source.ByteArrayOutputStream;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.*;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.vertex.vertex.task.model.entity.Task;
import com.vertex.vertex.task.relations.review.model.DTO.ReviewHoursDTO;
import com.vertex.vertex.task.relations.review.model.entity.Review;
import com.vertex.vertex.task.relations.review.service.ReviewService;
import com.vertex.vertex.task.service.TaskService;
import lombok.AllArgsConstructor;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

import javax.swing.border.Border;
import java.util.List;

@Service
@AllArgsConstructor
public class PDFService {

    private final ReviewService reviewService;
    private final TaskService taskService;

    public byte[] generatePdf(Long taskID) {
        Task task = taskService.findById(taskID);
        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            PdfWriter writer = new PdfWriter(outputStream);
            PdfDocument pdfDocument = new PdfDocument(writer);
            Document document = new Document(pdfDocument);
            ClassPathResource resource = new ClassPathResource("vertex-logo.png");

            document.add(new Image(ImageDataFactory.create(resource.getFile().getAbsolutePath()))
                    .scale(0.1F, 0.1F)
                    .setHorizontalAlignment(HorizontalAlignment.RIGHT));

            document.add(
                    new Paragraph(
                            task.getProject().getTeam().getName() + " | " + task.getProject().getName())
                            .setFontSize(24F));

            document.add(new Paragraph(task.getName()).setFontSize(16F));

            document.add(createTable(taskID).setMarginTop(5F));


            document.add(new Paragraph("Revisões")
                    .setFontSize(16)
                    .setMarginTop(15F)
                    .setBold());

            if (!task.getReviews().isEmpty()) {
                for (int i = 0; i < 3; i++) {

                    for (Review review : task.getReviews()) {
                        Paragraph p = new Paragraph().setPaddingLeft(40F);
                        Div div1 = new Div().setWidth(200f);
                        div1.add(new Paragraph("Enviado por: ").setBold());
                        div1.add(new Paragraph(review.getUserThatSentReview().getUserTeam().getUser().getFullName()).setFontSize(14));
                        div1.add(new Paragraph("Descrição de envio: ").setBold());
                        div1.add(new Paragraph(review.getDescription()).setFontSize(12));
                        p.add(div1);
                        p.addTabStops(new TabStop(150f));
                        p.add(new Tab());
                        Div div2 = new Div().setWidth(200f);
                        if (review.getReviewer() != null) {
                            div2.add(new Paragraph("Revisado por: ").setBold());
                            div2.add(new Paragraph(review.getReviewer().getUserTeam().getUser().getFullName()).setFontSize(14));
                            div2.add(new Paragraph("Descrição de devolutiva: ").setBold());
                            div2.add(new Paragraph(review.getFinalDescription()).setFontSize(12));
                            p.add(div2);
                            p.add(new Paragraph("Status: " + review.getApproveStatus().getName() + "   ")
                                    .add(new Paragraph("   |   "))
                                    .add(new Paragraph("Nota: " + review.getGrade())));
                        }
                        p.setBorderBottom(new SolidBorder(DeviceRgb.BLACK, 4F));
                        document.add(p);
                    }
                }
            } else {
                document.add(
                        new Paragraph("Nenhuma Revisão até o momento")
                                .setFontSize(14)
                                .setHorizontalAlignment(HorizontalAlignment.CENTER));
            }


            document.close();

            return outputStream.toByteArray();
        } catch (
                Exception e) {
            throw new RuntimeException("Error generating PDF", e);
        }
    }

    private Table createTable(Long taskId) {
        // Create a table with two columns
        Table table = new Table(2).setHorizontalAlignment(HorizontalAlignment.CENTER);

        List<ReviewHoursDTO> hours = reviewService.getPerformanceInTask(taskId);
        // Add rows to the table (you can add as many rows as needed)
        table.addHeaderCell(new Cell().add(new Paragraph("Responsável")));
        table.addHeaderCell(new Cell().add(new Paragraph("Tempo Trabalhado")));
        for (ReviewHoursDTO hourFor : hours) {
            table.addCell(new Cell().add(new Paragraph(hourFor.getUsername())));
            table.addCell(new Cell().add(new Paragraph(String.valueOf(hourFor.getTime()))));
        }

        return table;
    }
}
