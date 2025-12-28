package org.jala.university.infrastructure.config;

import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaInitializer {
    private static EntityManagerFactory emf;

    public static synchronized EntityManagerFactory getEntityManagerFactory() {
        if (emf == null) {
            try {
                emf = Persistence.createEntityManagerFactory("default");
                // Testa a conexão
                emf.createEntityManager().close();
                System.out.println("JPA configurado com sucesso!");
            } catch (Exception e) {
                System.err.println("Falha ao configurar JPA:");
                e.printStackTrace();
                throw new RuntimeException("Erro na configuração do JPA", e);
            }
        }
        return emf;
    }

    public static void shutdown() {
        if (emf != null && emf.isOpen()) {
            emf.close();
        }
    }
}
