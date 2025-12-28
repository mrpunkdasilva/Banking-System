-- Criar um evento agendado no MySQL para limpar códigos expirados

-- Verificar se o scheduler de eventos está habilitado
SET GLOBAL event_scheduler = ON;

-- Remover evento existente se houver
DROP EVENT IF EXISTS cleanup_expired_two_factor_codes;

-- Criar evento para executar a cada 30 minutos
CREATE EVENT cleanup_expired_two_factor_codes
ON SCHEDULE EVERY 30 MINUTE
DO
BEGIN
    -- Deletar códigos expirados
    DELETE FROM two_factor_codes WHERE expires_at < NOW();
END;
