
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
    unidade_medida_id int not null references unidade_medida(id)
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
    quantidade decimal(10,3) not null,
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
    id int primary key auto_increment,
    pedido_id int not null references pedidos(id),
    prato_id int not null references pratos(id),
    quantidade int not null default 1 check (quantidade > 0),
    preco_unitario decimal(10,2) not null,
    observacao varchar(200)
);      