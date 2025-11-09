
-- criando o data base né rs

create database restaurante_springboot;

use restaurante_springboot;

create table clientes (
    id int primary key auto_increment,
    nome varchar(50) not null,
    telefone varchar(30),
    email varchar(60),
    data_nasc date not null
);

create table unidade_medida (
    id int primary key auto_increment,
    sigla char(5) not null unique,
    descricao varchar(30) not null
);

create table ingredientes (
    id int primary key auto_increment,
    descricao varchar(50) not null,
    valor_unt float not null,
    unidade_medida_id int not null references unidade_medida(id),
    qt_estoque float default 0
);

create table pratos (
    id int primary key auto_increment,
    nome varchar(60) not null,
    descricao text,
    preco_venda decimal(10,2) not null,
    tempo_preparo_min int,
    ativo bool default true  -- true = ativo, false = inativo
);

create table composicao (
    id int primary key auto_increment,
    prato_id int not null references pratos(id),
    ingrediente_id int not null references ingredientes(id),
    quantidade int not null,
    unique (prato_id, ingrediente_id)
);

create table pedidos (
    id int primary key auto_increment,
    cliente_id int references clientes(id),
    data_pedido datetime default current_timestamp,
    valor_total decimal(10,2),
    status bool not null default true  -- true = aberto, false = fechado
);

create table itens_pedido (
    pedido_id    int not null references pedidos(id),
    prato_id     int not null references pratos(id),
    quantidade    int not null default 1 check (quantidade > 0),
    preco_unitario decimal(10,2) not null,
    observacao    varchar(200),
    constraint pk_itens_pedido primary key (pedido_id, prato_id)
);

/*essa procedure aqui, vai ser fundamental para os tiggers e para as outras coisa
  ela vai permitar com que eu atualize o estoque de ingredientes inclementando e 
  declementando recebbendo o id do prato, a quantidade que vai ser comprada e a
  operacao em uma boolean, de forma resumida, eu pegoe o id do prato, faco um select 
  em composicao para pegar todos os ingredientes do prato referenciado e atualizar 
  a quantidade do estoque de um por um com base na quantidade deque o cliente comprou,
  ai falo o (saida) ou 1(entrada) no boolean para falar se a funcao vai 
  inclementar ou declementar
    
                                                                                                                                                
                                                                                                        ##                                    
                                                                                                        ########                                
                                                                                                    ##@@@@MM##                                
                                                                                                    ####@@@@##                                
                                                                                                        ########                                
                                                                                                        ######                                  
                                                                                                    ##@@@@####                                
                                                                                                    ####@@@@MM##                                
                                                                                                    ##@@@@@@@@@@##                              
        ##                                                                                      ##@@@@@@@@@@@@##                              
        ##@@####                                                                                ####@@@@@@@@@@@@@@##                            
    ##@@@@@@##                                                                                ##@@@@@@@@@@@@@@@@####                          
    ##@@@@@@########                                                                        ##@@@@@@@@@@@@@@@@@@@@##                          
        ########@@@@@@##########################                                              ##@@MM@@@@@@@@@@@@@@@@@@##                        
            ####MM@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@####                        ############################@@@@@@@@@@@@##                        
                ##@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@##                ######@@@@@@@@@@@@@@@@        ..########@@@@@@@@##                      
                ##@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@##            ####@@@@@@@@@@@@@@@@@@--                @@######@@@@##                      
                ####@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@##      ####@@@@@@@@@@@@@@@@@@@@@@                  --@@@@####@@@@##                    
                    ##@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@##    ##@@@@@@@@@@@@@@@@@@@@@@@@--  MMMM        @@..  @@@@@@######                      
                    ##@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@####@@@@@@@@@@@@@@@@@@@@@@@@@@::  @@@@        @@MM..@@@@@@@@@@##                      
                    ####@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@####@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@  ..  ..          @@@@@@@@@@@@@@##                    
                        ##@@@@@@@@@@@@@@@@@@@@@@@@@@@@####@@@@@@@@@@@@@@@@@@@@##########@@@@::    --++  MM@@@@####@@@@@@@@@@##                  
                        ##@@@@@@@@@@@@@@@@@@@@@@@@####@@@@@@@@@@@@@@@@@@######--  --######@@@@--mm@@mm@@@@##########@@@@@@####                
                        ####@@@@@@@@@@@@@@@@@@@@@@##@@@@@@@@@@@@@@@@####@@              ######@@@@MM@@####mm      ####@@@@@@##                
                            ##@@@@@@@@@@@@@@@@@@@@####@@@@@@@@@@@@@@####                      ############            ####@@@@@@##              
                            ##@@@@@@@@@@@@@@@@@@##@@@@@@@@@@@@@@####                            mm##::              MM####@@@@##              
                                ##@@@@@@@@@@@@@@####@@@@@@@@@@@@####  mm++##                                      ..##..  ####@@@@##            
                                ##@@@@@@@@@@@@@@##@@@@@@@@@@MM####      ########                                MM######    ##@@@@##            
                                ##@@@@@@@@######@@@@@@@@@@@@##        ############                          ##########@@  MM##@@##            
                                    ########    ##@@@@@@@@@@####        ############                          ############    ##@@####          
                                                ##@@@@@@@@@@##          ############                            ########--    ########          
                                                ##@@@@@@@@@@##          ##########..                ####MM        ####mm      ::##@@##          
                                                ##@@@@@@@@####            ######--                  --  ##                      ##@@##          
                                                ##@@@@@@@@####                                        ::                        ######          
                                                ##@@@@@@@@####                                              ::                  ####            
                                                ##@@@@@@@@####                              ##..            ##                  ####            
                                                ##@@@@@@@@@@##                              ##++############                  ++####            
                                                ####@@@@@@@@##                              MM            MM                  ####              
                                                ######@@@@####                              --        ##                    ####              
                                        ####::##++##  ##@@@@##--                            ##        ##                  ..##                
                                        ++  mm::######  ##@@@@@@##                            ::mm    @@                    ####                
                                        MM######::##mm##  ##@@@@####                            ++####::                  ####                  
                                    ##++##mm######mmMM########@@####                                                  ####                    
                                    mm####@@mmMM++####++##..++##MM####@@                                            ####                      
                                    ##++##..######++++########..##@@@@@@####::                                    mm##                          
                                    MMmm######....######mm@@..####@@@@@@@@@@######                            ::####                            
                                    ##mm##    ##--####  ##@@  ##mm######@@@@@@##################################                                
                                    mmmm########@@..  ##mmMM..####################@@@@@@@@##@@@@@@@@##@@##@@######                            
                                        ######....######++##..##@@@@@@@@@@MM@@@@@@@@@@@@######@@----MM..++@@@@@@########    ####              
                                        ##++++######..--@@##########@@MM@@@@@@@@@@@@@@@@##mm####....----##@@@@##..--########..--##            
                                        ##@@####  ..######        @@##########@@@@@@@@##    ######..--..##@@MM@@::..####@@######              
                                            ##..++####                    ..##@@@@@@####..    mm##@@######@@..  ######@@@@@@@@####              
                                                ####          --##    ##@@##########          ##@@@@@@##########MM--##@@@@@@@@@@##            
                                                ########..    --##########--##@@                  ##@@@@@@@@@@@@##MM..##@@@@@@@@@@@@##          
                                            ##################@@@@@@########                    ++##@@@@@@@@@@MM####@@@@@@@@@@@@@@@@##        
                                            ####@@##mmmmmm####@@@@@@@@@@####                        @@##@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@MM##      
                                        ####@@@@##mmMMMMmm####@@@@@@@@####                          @@##@@@@@@@@@@@@@@@@@@@@########@@@@##    
                                        ####@@@@@@##MMmmmm####@@@@@@@@@@##                              ::##@@@@@@############################  
                                        ##@@@@@@@@####MMMM##@@@@@@@@@@@@##                                  ########      ##@@@@@@@@@@@@@@@@##  
                                    ####@@@@@@@@##@@##mmmm##@@@@@@@@@@##                                        ##      ##@@@@@@@@@@@@@@@@##  
                                    ##@@@@@@@@@@@@@@##MMmm##@@@@@@@@@@##                                        ++  ######@@@@@@@@@@@@@@####  
                                    ##@@@@@@@@@@@@@@@@##mmmm####MM@@@@##                                        ++####MM@@@@@@@@@@@@@@@@##    
                                    ##############@@@@@@@@##mmmm######MM@@##                                      ####@@@@@@@@@@@@@@@@@@####    
                                    ######################@@##@@mmmm##########                                    ##@@@@@@@@@@@@@@@@@@####      
                                                        ############mmmmmm####                                  ####@@@@@@@@@@@@@@MM####        
                                                            ####@@############                              --####@@@@@@@@@@@@@@@@####          
                                                            ######@@@@@@##                                  ##@@@@@@@@@@@@########            
                                                                ##########                    ######        ##@@@@@@##########                
                                                                    ######                  ##++          MM##########                        
                                                                ####                      @@##            mm####                              
                                                                ##mm                        ##                    ##                            
                                                                ##                          ##                      ##                          
                                                                ..                          ##                      ##                          
                                                                ##                        mm@@                    --                            
                                                                ##--                      ##                    ++##                            
                                                                ##############################################      

    MERECIA OS 2 PONTOS SO PELO ASCII DA KUROMI EMMM !                         

*/
-- ========================================
-- PROCEDURE: atualizar_estoque
-- ========================================

-- Essa procedure atualiza o estoque dos ingredientes de acordo com os pratos inseridos, deletados ou alterados
-- Ela recebe:
--   p_prato_id   -> o id do prato
--   p_quantidade -> a quantidade pedida
--   p_operacao   -> 0 = saída (pedido), 1 = entrada (remoção/cancelamento)
-- O MySQL não tem tipo BOOLEAN, por isso usamos TINYINT(1) para representar verdadeiro/falso

DELIMITER //

CREATE PROCEDURE atualizar_estoque(
    IN p_prato_id INT,
    IN p_quantidade INT,
    IN p_operacao TINYINT(1)
)
BEGIN
    -- Declarações de variáveis
    DECLARE v_ingrediente_id INT;
    DECLARE v_qde_necessaria FLOAT;
    DECLARE done BOOLEAN DEFAULT FALSE;

    -- Cursor
    DECLARE c CURSOR FOR 
        SELECT ingrediente_id, quantidade
        FROM composicao
        WHERE prato_id = p_prato_id;

    -- Handler
    DECLARE CONTINUE HANDLER FOR NOT FOUND SET done = TRUE;

    -- Abre o cursor
    OPEN c;

    read_loop: LOOP
        FETCH c INTO v_ingrediente_id, v_qde_necessaria;
        IF done THEN
            LEAVE read_loop;
        END IF;

        -- 0 = saída, 1 = entrada
        IF p_operacao = 0 THEN
            UPDATE ingredientes
            SET qt_estoque = qt_estoque - (v_qde_necessaria * p_quantidade)
            WHERE id = v_ingrediente_id;
        ELSE
            UPDATE ingredientes
            SET qt_estoque = qt_estoque + (v_qde_necessaria * p_quantidade)
            WHERE id = v_ingrediente_id;
        END IF;
    END LOOP;

    CLOSE c;
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER tr_ins_itens_pedido
AFTER INSERT ON itens_pedido
FOR EACH ROW
BEGIN
    /* 
      o mysql so suporta o modo linha por linha, por isso esse for each row
      ele executa esse trigger para cada linha afetada, se eu fizer um insert de 10 linhas
      ele vai rodar 1x para cada uma das linhas e nao em apenas 1, no caso a primeira
    */
    CALL atualizar_estoque(NEW.prato_id, NEW.quantidade, 0);
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER tr_del_itens_pedido
AFTER DELETE ON itens_pedido
FOR EACH ROW
BEGIN
    -- aqui é quando o pedido é deletado, ou seja, o estoque volta (entrada)
    CALL atualizar_estoque(OLD.prato_id, OLD.quantidade, 1);
END //

DELIMITER ;

DELIMITER //

CREATE TRIGGER tr_update_itens_pedido
AFTER UPDATE ON itens_pedido
FOR EACH ROW
BEGIN
    -- verifica se a quantidade mudou
    IF NEW.quantidade != OLD.quantidade THEN
        -- devolve o que foi tirado antes
        CALL atualizar_estoque(OLD.prato_id, OLD.quantidade, 1);
        -- desconta a nova quantidade
        CALL atualizar_estoque(NEW.prato_id, NEW.quantidade, 0);
    END IF;
END //

DELIMITER ;
