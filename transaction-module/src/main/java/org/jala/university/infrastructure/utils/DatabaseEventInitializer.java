package org.jala.university.infrastructure.utils;

import jakarta.persistence.EntityManager;
import org.jala.university.infrastructure.config.JPAConfig;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Utilitário para inicializar eventos do banco de dados.
 */
public class DatabaseEventInitializer {
    
    private static final Logger LOGGER = Logger.getLogger(DatabaseEventInitializer.class.getName());
    
    /**
     * Inicializa o evento de limpeza de códigos expirados.
     */
    public static void initializeCleanupEvent() {
        LOGGER.info("Inicializando evento de limpeza de códigos expirados");
        
        EntityManager entityManager = null;
        
        try {
            entityManager = JPAConfig.getEntityManagerFactory().createEntityManager();
            
            // Executar cada instrução SQL separadamente
            executeSQL(entityManager, "SET GLOBAL event_scheduler = ON");
            executeSQL(entityManager, "DROP EVENT IF EXISTS cleanup_expired_two_factor_codes");
            
            String createEventSQL = 
                "CREATE EVENT cleanup_expired_two_factor_codes " +
                "ON SCHEDULE EVERY 30 MINUTE " +
                "DO " +
                "BEGIN " +
                "    DELETE FROM two_factor_codes WHERE expires_at < NOW(); " +
                "END";
            
            executeSQL(entityManager, createEventSQL);
            
            LOGGER.info("Evento de limpeza de códigos expirados inicializado com sucesso");
            
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Erro ao inicializar evento de limpeza", e);
        } finally {
            if (entityManager != null && entityManager.isOpen()) {
                entityManager.close();
            }
        }
    }
    
    /**
     * Executa uma instrução SQL com tratamento de transação.
     * 
     * @param entityManager gerenciador de entidades
     * @param sql instrução SQL a ser executada
     */
    private static void executeSQL(EntityManager entityManager, String sql) {
        try {
            entityManager.getTransaction().begin();
            entityManager.createNativeQuery(sql).executeUpdate();
            entityManager.getTransaction().commit();
        } catch (Exception e) {
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
            throw e; // Relança a exceção para ser tratada pelo método chamador
        }
    }
}