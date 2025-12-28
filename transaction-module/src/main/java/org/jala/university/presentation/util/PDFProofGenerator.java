package org.jala.university.presentation.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.PdfWriter;
import org.jala.university.application.dto.TransactionDetailsDTO;

import java.io.FileOutputStream;
import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

public class PDFProofGenerator {

    public static void generate(TransactionDetailsDTO dto, String filePath) throws Exception {
        Document document = new Document();
        PdfWriter.getInstance(document, new FileOutputStream(filePath));
        document.open();

        Font headerFont = new Font(Font.HELVETICA, 18, Font.BOLD);
        document.add(new Paragraph("Comprovante de Transação Bancária", headerFont));
        document.add(new Paragraph(" ")); // Espaço

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
        document.add(new Paragraph("Data: " + dto.getCreatedAt().format(formatter)));

        document.add(new Paragraph("ID da Transação: " + dto.getId()));
        document.add(new Paragraph("Tipo: " + (dto.isDebit() ? "Enviada" : "Recebida")));

        NumberFormat currencyFormatter = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        document.add(new Paragraph("Valor: " + currencyFormatter.format(dto.getAmount())));

         document.add(new Paragraph("Status: " + dto.getStatus().name()));

        document.add(new Paragraph("Descrição: " + (dto.getDescription() == null ? "N/A" : dto.getDescription())));

        document.add(new Paragraph("\n--- Remetente ---"));
        document.add(new Paragraph("Nome: " + dto.getSenderName()));
        document.add(new Paragraph("Conta: " + dto.getSenderAccountNumber()));

        document.add(new Paragraph("\n--- Destinatário ---"));
        document.add(new Paragraph("Nome: " + dto.getReceiverName()));
        document.add(new Paragraph("Conta: " + dto.getReceiverAccountNumber()));

        document.close();
    }
}
