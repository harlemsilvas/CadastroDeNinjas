-- data.sql: popula H2 in-memory com missões e 10 ninjas a cada inicialização
-- Usar MERGE INTO (H2) para tornar idempotente e evitar duplicate key em ambientes de teste

-- Insere/atualiza missões
MERGE INTO tb_missoes (id, nome, dificuldade) KEY(id) VALUES (1, 'Praticar Taijutsu', 'Facil');
MERGE INTO tb_missoes (id, nome, dificuldade) KEY(id) VALUES (2, 'Recuperar Pergaminho', 'Media');
MERGE INTO tb_missoes (id, nome, dificuldade) KEY(id) VALUES (3, 'Proteger a Vila', 'Dificil');

-- Ajustar sequence/identity para evitar colisões (próximo id = max(id)+1)
ALTER TABLE tb_missoes ALTER COLUMN id RESTART WITH 4;

-- Insere/atualiza 10 ninjas (ids fixos para reprodução); distribui missoes_id ciclicamente
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (1, 'Naruto Uzumaki', 'narutouzumaki@naruto.test', 16, 1);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (2, 'Sasuke Uchiha', 'sasukeuchiha@naruto.test', 17, 2);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (3, 'Sakura Haruno', 'sakuraharuno@naruto.test', 16, 3);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (4, 'Kakashi Hatake', 'kakashihatake@naruto.test', 29, 1);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (5, 'Shikamaru Nara', 'shikamarunara@naruto.test', 21, 2);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (6, 'Hinata Hyuga', 'hinatahyuga@naruto.test', 17, 3);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (7, 'Rock Lee', 'rocklee@naruto.test', 19, 1);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (8, 'Neji Hyuga', 'nejihyuga@naruto.test', 18, 2);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (9, 'Gaara', 'gaara@naruto.test', 20, 3);
MERGE INTO tb_cadastro (id, nome, email, idade, missoes_id) KEY(id) VALUES (10, 'Itachi Uchiha', 'itachiuchiha@naruto.test', 21, 1);

-- Ajustar sequence/identity para tb_cadastro
ALTER TABLE tb_cadastro ALTER COLUMN id RESTART WITH 11;
