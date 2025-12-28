package org.jala.university.presentation.util;

import org.jala.university.application.dto.TransactionHistoryDTO;
import org.jala.university.application.dto.UserDTO;
import org.jala.university.domain.entity.enums.TransactionStatus;
import org.jala.university.infrastructure.utils.SessionManager;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Classe responsável por exportar transações para um arquivo CSV.
 * Inclui cabeçalho com informações do banco e da conta, além de resumo financeiro.
 */
public class CSVExporter {

    /**
     * Exporta uma lista de transações para um arquivo CSV formatado.
     * 
     * @param transactions Lista de transações a serem exportadas
     * @param file Arquivo de destino
     */
    public static void exportToCsv(List<TransactionHistoryDTO> transactions, File file) {
        try (FileWriter writer = new FileWriter(file)) {
            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm");
            
            // Adicionar cabeçalho com informações do banco
            writer.append("BANCO DIGITAL S.A.\n");
            writer.append("CNPJ: 00.000.000/0001-00\n");
            writer.append("www.bancodigital.com.br\n");
            writer.append("Central de Atendimento: 0800 123 4567\n\n");
            
            // Adicionar informações da conta
            UserDTO currentUser = SessionManager.getCurrentUser();
            Long accountId = SessionManager.getCurrentAccountId();
            
            writer.append("EXTRATO DE TRANSAÇÕES\n\n");
            
            if (currentUser != null) {
                writer.append("Cliente: ").append(currentUser.getName()).append("\n");
                writer.append("CPF: ").append(formatCpf(currentUser.getCpf())).append("\n");
            } else {
                writer.append("Cliente: Informação não disponível\n");
            }
            
            writer.append("Conta: ").append(accountId != null ? accountId.toString() : "N/A").append("\n");
            writer.append("Tipo: Conta Corrente\n\n");
            
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
                
                DateTimeFormatter periodFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                writer.append("Período: ")
                      .append(firstDate.format(periodFormatter))
                      .append(" a ")
                      .append(lastDate.format(periodFormatter))
                      .append("\n\n");
            }
            
            // Adicionar resumo financeiro
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
            
            writer.append("RESUMO FINANCEIRO\n");
            writer.append("Créditos: R$ ").append(String.format("%.2f", totalCredits)).append("\n");
            writer.append("Débitos: R$ ").append(String.format("%.2f", totalDebits)).append("\n");
            writer.append("Saldo: R$ ").append(String.format("%.2f", balance)).append("\n\n");
            
            // Verificar se há transações concluídas
            if (transactions.stream().noneMatch(tx -> tx.getStatus() == TransactionStatus.COMPLETED)) {
                writer.append("Não há transações concluídas no período selecionado.\n\n");
                return;
            }
            
            // Cabeçalho da tabela de transações
            writer.append("DETALHAMENTO DE TRANSAÇÕES\n");
            writer.append("Data,Descrição,Destinatário/Origem,Tipo,Valor\n");
            
            // Adicionar as transações no arquivo
            for (TransactionHistoryDTO tx : transactions) {
                // Garantir que apenas transações COMPLETED sejam exportadas
                if (tx.getStatus() == TransactionStatus.COMPLETED) {
                    String tipo = tx.isDebit() ? "Débito" : "Crédito";
                    writer.append(String.format("%s,\"%s\",\"%s\",%s,R$ %.2f\n",
                            tx.getDate().format(dateFormatter),
                            escapeCsvField(tx.getDescription()),
                            escapeCsvField(tx.getCounterpartyName()),
                            tipo,
                            tx.getAmount()));
                }
            }
            
            // Adicionar notas e informações legais
            writer.append("\nNOTAS IMPORTANTES:\n");
            writer.append("• Este extrato apresenta apenas as transações concluídas com sucesso.\n");
            writer.append("• Em caso de dúvidas, entre em contato com nossa central de atendimento.\n");
            writer.append("• Documento emitido eletronicamente em: ")
                  .append(LocalDateTime.now().format(dateFormatter))
                  .append("\n");
            
        } catch (IOException e) {
            e.printStackTrace(); // Em caso de erro
        }
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
    
    /**
     * Escapa campos que podem conter vírgulas para evitar problemas no CSV
     */
    private static String escapeCsvField(String field) {
        if (field == null) {
            return "";
        }
        // Remover aspas duplas e substituir por aspas simples para evitar problemas no CSV
        return field.replace("\"", "'");
    }
}
