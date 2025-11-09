
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

delimiter //

create procedure atualizar_estoque(
    in p_prato_id int,
    in p_quantidade int,
    in p_operacao TINYINT(1)
)
begin
    -- aqui eu declaro as variaveis que o procedimento ira usar
    declare v_ingrediente_id int;
    declare v_qde_necessaria float;
    declare done boolean default false;

    -- aqui eu vou criar o cursor  para percorrer a composicao em busca dos ingredientes
    declare c cursor for 
        select ingrediente_id, quantidade 
        from composicao
        where prato_id = p_prato_id;

    -- aqui eu crio um handler (tratador) pra quando o cursor chegar no fim dos dados (not found)
    declare continue handler for not found set done = true;

    -- abrindo o cursor
    open c;

    -- aqui começa o loop que vai percorrer cada ingrediente do prato
    read_loop: loop
        -- carrega os dados da linha atual do cursor
        fetch c into v_ingrediente_id, v_qde_necessaria;

        -- se não houver mais linhas, sai do loop
        if done then
            leave read_loop;
        end if;

        -- saida, no caso quando o cliente compra e vai sair do estoque
        if p_operacao = 0 then     
            update ingredientes
            set qt_estoque = qt_estoque - (v_qde_necessaria * p_quantidade)
            where id = v_ingrediente_id;

        -- entrada, quando tem uma devolutiva dos ingredientes
        else
            update ingredientes
            set qt_estoque = qt_estoque + (v_qde_necessaria * p_quantidade)
            where id = v_ingrediente_id;
        end if;

        -- aqui já mandamos o ponteiro para a próxima linha (caso exista)
    end loop;

    -- fechando o cursor
    close c;
end //

delimiter ;

    
-- trigger para insert
create trigger tr_ins_itens_pedido
after insert on itens_pedido
/*o mysql so suporta o modo linha por linha, por isso esse for each row
  ele executa esse trigger para cada linha afetada, se eu fizer um insert de 10 linhas
  ele vai rodar 1x para cada uma das linhas e nao em apenas 1, no caso a primeira
*/
for each row 
begin
   call atualizar_estoque(NEW.prato_id, NEW.quantidade, 0);
end
delimiter ;

-- as siglas NEW e OLD sao para o banco pegar o novo (new) valor q ta sendo adicionado
--e o OLD (antigo) o que acabou de ser deletado

--estou claramente fazendo essas anotacoes um pouco embregado professor


-- trigger para delete
create trigger tr_del_itens_pedido
after delete on itens_pedido
for each row 
begin
 call atualizar_estoque(OLD.prato_id, OLD_quantidade,1);
end
delimiter ;

--trigger para update
create trigger tr_update_itens_pedido
after update on itens_pedido
for each row
begin
  --verifica se a quantidade mudou
 if NEW.quantidade != OLD.quantidade then
  --devolve o que foi tirado 
 call atualizar_estoque(OLD.prato_id, OLD.quantidade, 0);
  --desconta a nova quantidade
 call atualizar_estoque(NEW.prato_id, NEW.quantidade, 1);
 end if;
end
delimiter ;
