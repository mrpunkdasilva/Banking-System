package org.jala.university.presentation.util;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.lowagie.text.Font;
import com.lowagie.text.Image;
import com.lowagie.text.Rectangle;

import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.infrastructure.utils.SessionManager;

import java.awt.Color;
import java.io.File;
import java.io.FileOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe responsável por exportar transações para um arquivo PDF estilizado.
 */
public class PDFExporter {

    // Cores do tema
    private static final Color PRIMARY_COLOR = new Color(0, 87, 146); // Azul escuro
    private static final Color SECONDARY_COLOR = new Color(240, 240, 240); // Cinza claro
    private static final Color ACCENT_COLOR = new Color(0, 150, 136); // Verde água
    private static final Color TEXT_COLOR = new Color(33, 33, 33); // Quase preto
    private static final Color DEBIT_COLOR = new Color(213, 0, 0); // Vermelho
    private static final Color CREDIT_COLOR = new Color(0, 150, 0); // Verde

    // Fontes
    private static final Font TITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 18, PRIMARY_COLOR);
    private static final Font SUBTITLE_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 14, PRIMARY_COLOR);
    private static final Font HEADER_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 12, Color.WHITE);
    private static final Font NORMAL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 10, TEXT_COLOR);
    private static final Font BOLD_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, TEXT_COLOR);
    private static final Font SMALL_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8, TEXT_COLOR);
    private static final Font FOOTER_FONT = FontFactory.getFont(FontFactory.HELVETICA, 8, Color.GRAY);
    private static final Font DEBIT_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, DEBIT_COLOR);
    private static final Font CREDIT_FONT = FontFactory.getFont(FontFactory.HELVETICA_BOLD, 10, CREDIT_COLOR);

    /**
     * Exporta uma lista de transações para um arquivo PDF estilizado.
     * 
     * @param transactions Lista de transações a serem exportadas
     * @param file Arquivo de destino
     */
    public static void exportToPdf(List<TransactionHistoryDTO> transactions, File file) {
        try {
            // Configurar o documento PDF
            Document document = new Document(PageSize.A4, 36, 36, 54, 36); // Margens
            PdfWriter writer = PdfWriter.getInstance(document, new FileOutputStream(file));
            
            // Adicionar eventos de página para cabeçalho e rodapé
            writer.setPageEvent(new PdfPageEventHelper() {
                public void onEndPage(PdfWriter writer, Document document) {
                    try {
                        // Adicionar rodapé
                        PdfContentByte cb = writer.getDirectContent();
                        
                        // Linha separadora
                        cb.setColorStroke(SECONDARY_COLOR);
                        cb.setLineWidth(1);
                        cb.moveTo(document.left(), document.bottom() - 10);
                        cb.lineTo(document.right(), document.bottom() - 10);
                        cb.stroke();
                        
                        // Texto do rodapé
                        ColumnText.showTextAligned(cb, Element.ALIGN_CENTER,
                                new Phrase("Este documento é um extrato digital das transações concluídas. Página " + 
                                           writer.getPageNumber(), FOOTER_FONT),
                                (document.left() + document.right()) / 2,
                                document.bottom() - 20, 0);
                        
                        // Data e hora de geração
                        ColumnText.showTextAligned(cb, Element.ALIGN_RIGHT,
                                new Phrase("Gerado em: " + 
                                           LocalDateTime.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss")), FOOTER_FONT),
                                document.right(),
                                document.bottom() - 20, 0);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            
            document.open();
            
            // Adicionar cabeçalho com informações do banco
            addBankHeader(document);
            
            // Adicionar informações da conta
            addAccountInfo(document);
            
            // Adicionar título do extrato
            Paragraph title = new Paragraph("EXTRATO DE TRANSAÇÕES", TITLE_FONT);
            title.setAlignment(Element.ALIGN_CENTER);
            title.setSpacingBefore(10);
            title.setSpacingAfter(5);
            document.add(title);
            
            // Adicionar período do extrato
            if (!transactions.isEmpty()) {
                LocalDateTime firstDate = transactions.stream()
                        .map(TransactionHistoryDTO::getDate)
                        .min(LocalDateTime::compareTo)
                        .orElse(LocalDateTime.now());
                
                LocalDateTime lastDate = transactions.stream()
                        .map(TransactionHistoryDTO::getDate)
                        .max(LocalDateTime::compareTo)
                        .orElse(LocalDateTime.now());
                
                DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                
                Paragraph period = new Paragraph(
                        "Período: " + firstDate.format(dateFormatter) + " a " + lastDate.format(dateFormatter),
                        SUBTITLE_FONT);
                period.setAlignment(Element.ALIGN_CENTER);
                period.setSpacingAfter(15);
                document.add(period);
            }
            
            // Adicionar resumo financeiro
            addFinancialSummary(document, transactions);
            
            // Adicionar tabela de transações
            addTransactionsTable(document, transactions);
            
            // Adicionar notas e informações legais
            addLegalNotes(document);
            
            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Adiciona o cabeçalho com informações do banco
     */
    private static void addBankHeader(Document document) throws Exception {
        // Criar tabela para o cabeçalho
        PdfPTable headerTable = new PdfPTable(2);
        headerTable.setWidthPercentage(100);
        headerTable.setWidths(new float[]{1, 2});
        headerTable.setSpacingAfter(10);
        
        // Célula para o logo (simulado com texto estilizado)
        PdfPCell logoCell = new PdfPCell();
        Paragraph logoParagraph = new Paragraph("BANCO DIGITAL", FontFactory.getFont(FontFactory.HELVETICA_BOLD, 20, PRIMARY_COLOR));
        logoCell.addElement(logoParagraph);
        logoCell.setBorder(Rectangle.NO_BORDER);
        logoCell.setPaddingBottom(10);
        headerTable.addCell(logoCell);
        
        // Célula para informações do banco
        PdfPCell infoCell = new PdfPCell();
        Paragraph infoParagraph = new Paragraph();
        infoParagraph.add(new Chunk("Banco Digital S.A.\n", BOLD_FONT));
        infoParagraph.add(new Chunk("CNPJ: 00.000.000/0001-00\n", NORMAL_FONT));
        infoParagraph.add(new Chunk("www.bancodigital.com.br\n", NORMAL_FONT));
        infoParagraph.add(new Chunk("Central de Atendimento: 0800 123 4567", NORMAL_FONT));
        infoCell.addElement(infoParagraph);
        infoCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        infoCell.setBorder(Rectangle.NO_BORDER);
        infoCell.setPaddingBottom(10);
        headerTable.addCell(infoCell);
        
        document.add(headerTable);
        
        // Linha separadora
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBackgroundColor(PRIMARY_COLOR);
        lineCell.setFixedHeight(2f);
        lineCell.setBorder(Rectangle.NO_BORDER);
        lineTable.addCell(lineCell);
        document.add(lineTable);
    }
    
    /**
     * Adiciona informações da conta do usuário
     */
    private static void addAccountInfo(Document document) throws Exception {
        // Obter informações do usuário atual
        UserDTO currentUser = SessionManager.getCurrentUser();
        Long accountId = SessionManager.getCurrentAccountId();
        
        // Criar tabela para informações da conta
        PdfPTable accountTable = new PdfPTable(2);
        accountTable.setWidthPercentage(100);
        accountTable.setSpacingBefore(10);
        accountTable.setSpacingAfter(15);
        
        // Informações do cliente
        PdfPCell clientCell = new PdfPCell();
        Paragraph clientInfo = new Paragraph();
        
        if (currentUser != null) {
            clientInfo.add(new Chunk("Cliente: ", BOLD_FONT));
            clientInfo.add(new Chunk(currentUser.getName() + "\n", NORMAL_FONT));
            
            clientInfo.add(new Chunk("CPF: ", BOLD_FONT));
            clientInfo.add(new Chunk(formatCpf(currentUser.getCpf()) + "\n", NORMAL_FONT));
        } else {
            clientInfo.add(new Chunk("Cliente: ", BOLD_FONT));
            clientInfo.add(new Chunk("Informação não disponível\n", NORMAL_FONT));
        }
        
        clientCell.addElement(clientInfo);
        clientCell.setBorder(Rectangle.NO_BORDER);
        accountTable.addCell(clientCell);
        
        // Informações da conta
        PdfPCell accountCell = new PdfPCell();
        Paragraph accountInfo = new Paragraph();
        
        accountInfo.add(new Chunk("Conta: ", BOLD_FONT));
        accountInfo.add(new Chunk(accountId != null ? accountId.toString() : "N/A" + "\n", NORMAL_FONT));
        
        accountInfo.add(new Chunk("Tipo: ", BOLD_FONT));
        accountInfo.add(new Chunk("Conta Corrente\n", NORMAL_FONT));
        
        accountCell.addElement(accountInfo);
        accountCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
        accountCell.setBorder(Rectangle.NO_BORDER);
        accountTable.addCell(accountCell);
        
        document.add(accountTable);
    }
    
    /**
     * Adiciona um resumo financeiro das transações
     */
    private static void addFinancialSummary(Document document, List<TransactionHistoryDTO> transactions) throws Exception {
        // Calcular totais
        BigDecimal totalCredits = BigDecimal.ZERO;
        BigDecimal totalDebits = BigDecimal.ZERO;
        
        for (TransactionHistoryDTO tx : transactions) {
            if (tx.getStatus() == TransactionStatus.COMPLETED) {
                if (tx.isDebit()) {
                    totalDebits = totalDebits.add(tx.getAmount());
                } else {
                    totalCredits = totalCredits.add(tx.getAmount());
                }
            }
        }
        
        BigDecimal balance = totalCredits.subtract(totalDebits);
        
        // Criar tabela para o resumo
        PdfPTable summaryTable = new PdfPTable(3);
        summaryTable.setWidthPercentage(100);
        summaryTable.setWidths(new float[]{1, 1, 1});
        summaryTable.setSpacingAfter(15);
        
        // Estilo para as células do resumo
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(SECONDARY_COLOR);
        headerCell.setPadding(5);
        
        // Célula de créditos
        headerCell = new PdfPCell(new Phrase("CRÉDITOS", BOLD_FONT));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(SECONDARY_COLOR);
        summaryTable.addCell(headerCell);
        
        // Célula de débitos
        headerCell = new PdfPCell(new Phrase("DÉBITOS", BOLD_FONT));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(SECONDARY_COLOR);
        summaryTable.addCell(headerCell);
        
        // Célula de saldo
        headerCell = new PdfPCell(new Phrase("SALDO", BOLD_FONT));
        headerCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        headerCell.setBackgroundColor(SECONDARY_COLOR);
        summaryTable.addCell(headerCell);
        
        // Valores
        PdfPCell valueCell = new PdfPCell(new Phrase("R$ " + String.format("%.2f", totalCredits), CREDIT_FONT));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueCell.setPadding(5);
        summaryTable.addCell(valueCell);
        
        valueCell = new PdfPCell(new Phrase("R$ " + String.format("%.2f", totalDebits), DEBIT_FONT));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueCell.setPadding(5);
        summaryTable.addCell(valueCell);
        
        Font balanceFont = balance.compareTo(BigDecimal.ZERO) >= 0 ? CREDIT_FONT : DEBIT_FONT;
        valueCell = new PdfPCell(new Phrase("R$ " + String.format("%.2f", balance), balanceFont));
        valueCell.setHorizontalAlignment(Element.ALIGN_CENTER);
        valueCell.setPadding(5);
        summaryTable.addCell(valueCell);
        
        document.add(summaryTable);
    }
    
    /**
     * Adiciona a tabela de transações
     */
    private static void addTransactionsTable(Document document, List<TransactionHistoryDTO> transactions) throws Exception {
        // Título da seção
        Paragraph tableTitle = new Paragraph("Detalhamento de Transações", SUBTITLE_FONT);
        tableTitle.setSpacingBefore(10);
        tableTitle.setSpacingAfter(10);
        document.add(tableTitle);
        
        if (transactions.isEmpty() || transactions.stream().noneMatch(tx -> tx.getStatus() == TransactionStatus.COMPLETED)) {
            Paragraph noTransactions = new Paragraph("Não há transações concluídas no período selecionado.", NORMAL_FONT);
            noTransactions.setAlignment(Element.ALIGN_CENTER);
            noTransactions.setSpacingBefore(10);
            noTransactions.setSpacingAfter(10);
            document.add(noTransactions);
            return;
        }
        
        // Criar a tabela com 5 colunas
        PdfPTable table = new PdfPTable(5);
        table.setWidthPercentage(100);
        table.setWidths(new float[]{2, 3, 3, 1.5f, 1.5f});
        table.setSpacingAfter(10);
        
        // Estilo para o cabeçalho da tabela
        PdfPCell headerCell = new PdfPCell();
        headerCell.setBackgroundColor(PRIMARY_COLOR);
        headerCell.setPadding(5);
        
        // Cabeçalhos da tabela
        headerCell.setPhrase(new Phrase("Data", HEADER_FONT));
        table.addCell(headerCell);
        
        headerCell.setPhrase(new Phrase("Descrição", HEADER_FONT));
        table.addCell(headerCell);
        
        headerCell.setPhrase(new Phrase("Destinatário/Origem", HEADER_FONT));
        table.addCell(headerCell);
        
        headerCell.setPhrase(new Phrase("Tipo", HEADER_FONT));
        table.addCell(headerCell);
        
        headerCell.setPhrase(new Phrase("Valor", HEADER_FONT));
        table.addCell(headerCell);
        
        // Formatador de data
        DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
        
        // Estilo para as células de dados
        PdfPCell dataCell = new PdfPCell();
        dataCell.setPadding(5);
        
        // Contador para alternar cores das linhas
        int rowCount = 0;
        
        // Adicionar as transações (apenas COMPLETED)
        for (TransactionHistoryDTO tx : transactions) {
            if (tx.getStatus() == TransactionStatus.COMPLETED) {
                // Alternar cores das linhas para melhor legibilidade
                if (rowCount % 2 == 0) {
                    dataCell.setBackgroundColor(Color.WHITE);
                } else {
                    dataCell.setBackgroundColor(new Color(245, 245, 245));
                }
                
                // Data
                dataCell.setPhrase(new Phrase(tx.getDate().format(dateFormatter), NORMAL_FONT));
                table.addCell(dataCell);
                
                // Descrição
                dataCell.setPhrase(new Phrase(tx.getDescription(), NORMAL_FONT));
                table.addCell(dataCell);
                
                // Destinatário/Origem
                dataCell.setPhrase(new Phrase(tx.getCounterpartyName(), NORMAL_FONT));
                table.addCell(dataCell);
                
                // Tipo (Débito/Crédito)
                String tipoText = tx.isDebit() ? "Débito" : "Crédito";
                Font tipoFont = tx.isDebit() ? DEBIT_FONT : CREDIT_FONT;
                dataCell.setPhrase(new Phrase(tipoText, tipoFont));
                table.addCell(dataCell);
                
                // Valor
                String valorFormatado = String.format("R$ %.2f", tx.getAmount());
                Font valorFont = tx.isDebit() ? DEBIT_FONT : CREDIT_FONT;
                dataCell.setPhrase(new Phrase(valorFormatado, valorFont));
                dataCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
                table.addCell(dataCell);
                
                // Resetar alinhamento para as próximas células
                dataCell.setHorizontalAlignment(Element.ALIGN_LEFT);
                
                rowCount++;
            }
        }
        
        document.add(table);
    }
    
    /**
     * Adiciona notas e informações legais
     */
    private static void addLegalNotes(Document document) throws Exception {
        // Linha separadora
        PdfPTable lineTable = new PdfPTable(1);
        lineTable.setWidthPercentage(100);
        lineTable.setSpacingBefore(10);
        PdfPCell lineCell = new PdfPCell();
        lineCell.setBackgroundColor(SECONDARY_COLOR);
        lineCell.setFixedHeight(1f);
        lineCell.setBorder(Rectangle.NO_BORDER);
        lineTable.addCell(lineCell);
        document.add(lineTable);
        
        // Notas legais
        Paragraph notes = new Paragraph();
        notes.setAlignment(Element.ALIGN_JUSTIFIED);
        notes.setSpacingBefore(10);
        notes.add(new Chunk("Notas importantes:\n", BOLD_FONT));
        notes.add(new Chunk("• Este extrato apresenta apenas as transações concluídas com sucesso.\n", SMALL_FONT));
        notes.add(new Chunk("• Em caso de dúvidas, entre em contato com nossa central de atendimento.\n", SMALL_FONT));
        notes.add(new Chunk("• Documento emitido eletronicamente, dispensada assinatura.\n", SMALL_FONT));
        notes.add(new Chunk("• Conforme Lei Geral de Proteção de Dados (LGPD), seus dados são protegidos e utilizados apenas para os fins específicos do serviço contratado.", SMALL_FONT));
        
        document.add(notes);
    }
    
    /**
     * Formata um CPF com máscara
     */
    private static String formatCpf(String cpf) {
        if (cpf == null || cpf.length() != 11) {
            return cpf;
        }
        return cpf.substring(0, 3) + "." + cpf.substring(3, 6) + "." + 
               cpf.substring(6, 9) + "-" + cpf.substring(9);
    }
}
