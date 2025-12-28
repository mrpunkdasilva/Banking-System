package org.jala.university.infrastructure.utils;

import lombok.Getter;
import lombok.Setter;
import org.jala.university.application.dto.UserDTO;

import java.util.HashMap;
import java.util.Map;

/**
 * Gerencia a sessão do usuário logado no sistema.
 * Esta classe fornece métodos estáticos para armazenar e recuperar informações
 * da sessão atual, como o usuário logado e o ID da conta.
 */
public class SessionManager {

    @Getter
    @Setter
    private static UserDTO currentUser;

    @Getter
    @Setter
    private static Long currentAccountId;

    @Getter
    @Setter
    private static String authToken;
    private static final Map<String, Object> temporaryData = new HashMap<>();
    
    private SessionManager() {
        // Construtor privado para evitar instanciação
    }

    /**
     * Limpa todos os dados da sessão atual.
     * Este método deve ser chamado durante o logout.
     */
    public static void clearSession() {
        System.out.println("Limpando sessão...");
        currentUser = null;
        currentAccountId = null;
        authToken = null;
        temporaryData.clear();
        System.out.println("Sessão limpa com sucesso");
        
        // Também limpar a outra implementação do SessionManager
        try {
            org.jala.university.presentation.util.SessionManager.getInstance().clearSession();
            System.out.println("SessionManager da presentation também foi limpo");
        } catch (Exception e) {
            System.err.println("Erro ao limpar SessionManager da presentation: " + e.getMessage());
        }
    }
    
    /**
     * Verifica se há um usuário logado no sistema.
     * 
     * @return true se houver um usuário logado, false caso contrário
     */
    public static boolean isAuthenticated() {
        return currentUser != null && authToken != null;
    }
    
    /**
     * Armazena um dado temporário na sessão.
     * Útil para passar informações entre telas.
     * 
     * @param key A chave para identificar o dado
     * @param value O valor a ser armazenado
     */
    public static void setTemporaryData(String key, Object value) {
        temporaryData.put(key, value);
    }
    
    /**
     * Obtém um dado temporário da sessão.
     * 
     * @param key A chave do dado a ser recuperado
     * @return O valor armazenado ou null se a chave não existir
     */
    public static Object getTemporaryData(String key) {
        return temporaryData.get(key);
    }
    
    /**
     * Remove um dado temporário da sessão.
     * 
     * @param key A chave do dado a ser removido
     * @return O valor que foi removido ou null se a chave não existir
     */
    public static Object removeTemporaryData(String key) {
        return temporaryData.remove(key);
    }
}
