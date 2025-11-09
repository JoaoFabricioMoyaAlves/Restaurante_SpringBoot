-- =======================================================
-- SCRIPT DE INSERTS DE DADOS (DML)
-- Execute APÓS a criação do banco de dados (DDL)
-- =======================================================

-- 1. UNIDADE DE MEDIDA (unidade_medida)
INSERT INTO unidade_medida (sigla, descricao) VALUES
('UN', 'Unidade'),
('KG', 'Kilograma'),
('G', 'Grama'),
('L', 'Litro'),
('ML', 'Mililitro');

-- 2. CLIENTES (clientes)
INSERT INTO clientes (nome, telefone, email, data_nasc) VALUES
('Alice Silva', '(11) 98765-4321', 'alice.silva@teste.com', '1990-05-15'),
('Bruno Costa', '(21) 91234-5678', 'bruno.costa@teste.com', '1985-11-20'),
('Carla Oliveira', '(31) 99876-1234', 'carla.o@teste.com', '2001-08-01'),
('Daniel Santos', '(41) 95678-9012', 'daniel.s@teste.com', '1976-03-25'),
('Eduarda Pereira', '(51) 93456-7890', 'eduarda.p@teste.com', '1999-12-10');

-- 3. INGREDIENTES (ingredientes)
INSERT INTO ingredientes (descricao, valor_unt, unidade_medida_id, qt_estoque) VALUES
-- UNIDADES (ID 1)
('Pão Brioche', 1.50, 1, 50.0),      -- ID 1
('Ovo', 0.80, 1, 100.0),             -- ID 2
-- GRAMAS/KILOS (ID 3, 2)
('Carne Bovina (Patty)', 8.00, 3, 5000.0), -- 5kg -> ID 3
('Queijo Cheddar (Fatia)', 0.50, 3, 2000.0), -- 2kg -> ID 4
('Tomate', 0.30, 3, 1000.0),         -- 1kg -> ID 5
('Alface Americana', 0.40, 3, 500.0),-- ID 6
('Farinha de Trigo', 3.50, 2, 25.0), -- ID 7
('Frango (Filé)', 12.00, 2, 10.0),   -- ID 8
('Arroz', 4.00, 2, 50.0),            -- ID 9
-- LITROS/ML (ID 4, 5)
('Óleo de Soja', 0.01, 5, 2000.0),   -- 2L -> ID 10
('Água Mineral', 1.00, 4, 10.0);     -- ID 11

-- 4. PRATOS (pratos)
INSERT INTO pratos (nome, descricao, preco_venda, tempo_preparo_min, ativo) VALUES
('Burger Clássico', 'Hambúrguer de 180g, queijo, salada e molho especial.', 25.90, 15, TRUE), -- ID 1
('Prato Executivo Frango', 'Filé de frango grelhado, arroz, feijão e batata frita.', 35.50, 20, TRUE), -- ID 2
('Salada Caesar', 'Mix de folhas, frango em cubos, croutons e molho Caesar.', 28.00, 10, TRUE), -- ID 3
('Lasanha Bolonhesa', 'Lasanha tradicional com molho bolonhesa.', 42.00, 30, TRUE), -- ID 4
('Pudim de Leite', 'Sobremesa clássica de pudim.', 10.00, 5, TRUE); -- ID 5

-- 5. COMPOSIÇÃO (composicao)
INSERT INTO composicao (prato_id, ingrediente_id, quantidade) VALUES
-- Receita: Burger Clássico (ID 1)
(1, 1, 2),    -- 2x Pão Brioche (UN)
(1, 3, 180),  -- 180g Carne Bovina (G)
(1, 4, 2),    -- 2x Queijo Cheddar (G)
(1, 5, 20),   -- 20g Tomate (G)
(1, 6, 10),   -- 10g Alface (G)

-- Receita: Prato Executivo Frango (ID 2)
(2, 8, 0.200),  -- 200g Frango (KG)
(2, 9, 0.150),  -- 150g Arroz (KG)
(2, 10, 50),    -- 50ml Óleo de Soja (ML)

-- Receita: Salada Caesar (ID 3)
(3, 8, 0.100),  -- 100g Frango (KG)
(3, 6, 100);    -- 100g Alface Americana (G)

-- 6. PEDIDOS (pedidos)
INSERT INTO pedidos (cliente_id, data_pedido, valor_total, status) VALUES
(1, NOW() - INTERVAL 2 HOUR, 51.80, FALSE),  -- Pedido Fechado (ID 1)
(3, NOW() - INTERVAL 1 HOUR, 71.40, TRUE),   -- Pedido Aberto (ID 2)
(2, NOW(), NULL, TRUE);                       -- Pedido Aberto (ID 3)

-- 7. ITENS DE PEDIDO (itens_pedido)
INSERT INTO itens_pedido (pedido_id, prato_id, quantidade, preco_unitario, observacao) VALUES
-- Pedido 1 (Fechado)
(1, 1, 2, 25.90, 'Bem passado, sem tomate'), 

-- Pedido 2 (Aberto)
(2, 2, 1, 35.50, 'Arroz integral no lugar do branco'),  
(2, 3, 1, 28.00, 'Molho extra'),                        

-- Pedido 3 (Aberto)
(3, 1, 1, 25.90, 'Adicionar bacon (não mapeado)'), 
(3, 2, 1, 35.50, 'Sem feijão');