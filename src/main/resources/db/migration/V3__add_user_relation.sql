-- V3 - adiciona relacionamento de usuário em oficina e maquina
ALTER TABLE oficina ADD COLUMN user_id BIGINT;
ALTER TABLE maquina ADD COLUMN user_id BIGINT;
ALTER TABLE oficina ADD CONSTRAINT fk_oficina_user FOREIGN KEY (user_id) REFERENCES users(id);
ALTER TABLE maquina ADD CONSTRAINT fk_maquina_user FOREIGN KEY (user_id) REFERENCES users(id);