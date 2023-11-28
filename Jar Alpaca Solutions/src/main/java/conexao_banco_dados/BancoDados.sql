-- My Sql
create database AlpacaDB;
use AlpacaDB;

drop database alpacadb;


INSERT INTO Endereco (cep, rua, numero, bairro, cidade, estado, ativo) VALUES
    ('07957020', 'Rua da Empresa 123', '123', 'Centro', 'CidadeA', 'EstadoA', true);


-- Inserir dados na tabela Empresa
INSERT INTO Empresa (nomeFantasia, razaoSocial, email, senha, cnpj, ativo, fk_endereco) VALUES
    ('Empresa1', 'Razao1', 'ewerton@gmail.com', '12345', '12345678901234', true, 1);

-- Inserir dados na tabela Telefone
INSERT INTO Telefone (numero, tipo, ativo, fkEmpresa) VALUES
    ('123456789', 'Comercial', true, 1),
    ('987654321', 'Suporte', true, 2);
create table Endereco(
idEndereco int primary key auto_increment,
cep varchar(8),
rua varchar(50),
numero varchar(50),
bairro varchar(50),
cidade varchar(50),
estado varchar(50),
ativo boolean
);

create table Empresa(
idEmpresa int primary key auto_increment,
nomeFantasia varchar(45),
razaoSocial varchar(45),
email varchar(50),
senha varchar(50),
cnpj varchar(14),
ativo boolean,
fk_endereco int,
constraint fk_endereco foreign key (fk_endereco)
references Endereco(idEndereco)
);
select * from empresa;

create table Telefone(
idTelefone int primary key auto_increment,
numero char(11),
tipo varchar(45),
ativo boolean,
fkEmpresa int,
constraint fkEmpresa foreign key (fkEmpresa)
references Empresa(idEmpresa)
);


Create table Usuario(
idUsuario int primary key auto_increment,
nome varchar(50),
email varchar(50),
senha varchar(50),
tipoAcesso varchar(20),
nivelAcesso varchar(20),
ativo boolean,
fkEmpresa int,
foreign key (fkEmpresa)
references Empresa(idEmpresa)
);

create table Unidade(
idUnidade int primary key auto_increment,
nomeInstituicao varchar(45),
ativo boolean,
fkEndereco int,
constraint fkEndereco foreign key (fkEndereco) references Endereco(idEndereco)
);

create table Maquina(
idMaquina int primary key auto_increment,
NomeMaquina varchar(50) not null,
ipMaquina varchar(45),
sistemaOperacional varchar(45),
statusMaquina boolean,
ativo boolean,
fkEmpresa int,
fKUnidade int,
foreign key (fkEmpresa)
references Empresa(idEmpresa),
foreign key (fKUnidade)
references Unidade(idUnidade)
);

CREATE TABLE TipoComponente (
  idTipoComponente INT PRIMARY KEY auto_increment,
  nomeTipo VARCHAR(60),
  tipoComponente VARCHAR(60)
);

SELECT M.*, U.Tipo AS TipoUnidadeMedida, TC.nomeTipo AS NomeTipoComponente
FROM Medicoes M
JOIN UnidadeMedida U ON M.fkUnidadeMedidaID = U.idParametros
JOIN TipoComponente TC ON M.fkTipoComponenteID = TC.idTipoComponente
WHERE TC.nomeTipo = 'memoria em uso';

select * from MetricasAlertas;

select * from tipoComponente;

select * from TipoComponente;
insert into unidade values (null ,"SPTECH" , true , 1);

CREATE TABLE Config (
  idComponentes INT PRIMARY KEY auto_increment,
  ValorConfiguracao VARCHAR(45),
  fkMaquina INT,
  fkTipoComponenteID INT,
  FOREIGN KEY (fkMaquina) REFERENCES Maquina(idMaquina),
  FOREIGN KEY (fkTipoComponenteID) REFERENCES TipoComponente(idTipoComponente)
);

SELECT M.*, U.Tipo AS TipoUnidadeMedida
FROM Medicoes M
JOIN UnidadeMedida U ON M.fkUnidadeMedidaID = U.idParametros
WHERE Tc.nomeTipo = 'memoria em uso';


select * from tipoComponente;




CREATE TABLE UnidadeMedida (
  idParametros INT PRIMARY KEY auto_increment,
  Tipo char(10),
  fkMaquina INT,
  FOREIGN KEY (fkMaquina) REFERENCES Maquina(idMaquina)
);
insert into UnidadeMedida(Tipo) values
("GB"),
("MB"),
("KB");





update UnidadeMedida set fkMaquina = ?  where idParamento = 1;

CREATE TABLE Medicoes (
  idMedicoes INT PRIMARY KEY auto_increment,
  valor DECIMAL(10,2),
  data_hora_leitura DATETIME,
  id_computador INT,
  fkTipoComponenteID INT,
  fkUnidadeMedidaID INT,
  FOREIGN KEY (fkTipoComponenteID) REFERENCES TipoComponente(idTipoComponente),
  FOREIGN KEY (fkUnidadeMedidaID) REFERENCES UnidadeMedida(idParametros)
);


CREATE TABLE MetricasAlertas (
  idMetricasAlertas INT PRIMARY KEY auto_increment,
  TipoComponente VARCHAR(45),
  maximo VARCHAR(45),
  mensagemAlerta VARCHAR(45),
  minimo VARCHAR(45),
  dhHoraAlerta DATETIME,
  fkUnidadeMedida INT,
  fkTipoComponente INT,
  fkConfiguracao INT,
  FOREIGN KEY (fkUnidadeMedida) REFERENCES UnidadeMedida(idParametros),
  FOREIGN KEY (fkTipoComponente) REFERENCES TipoComponente(idTipoComponente),
  FOREIGN KEY (fkConfiguracao) REFERENCES Config(idComponentes)
);


select * from MetricasAlertas;
ALTER TABLE MetricasAlertas MODIFY mensagemAlerta varchar(150);


SELECT DISTINCT MA.idMetricasAlertas, MA.TipoComponente, MA.maximo, MA.mensagemAlerta, MA.minimo, MA.dhHoraAlerta
FROM MetricasAlertas MA
JOIN UnidadeMedida UM ON MA.fkUnidadeMedida = UM.idParametros
JOIN Maquina M ON UM.fkMaquina = M.idMaquina
WHERE M.idMaquina = 1
GROUP BY MA.idMetricasAlertas;  -- Substitua ? pelo ID da máquina desejada



SELECT MA.idMetricasAlertas, MA.TipoComponente, MA.maximo, MA.mensagemAlerta, MA.minimo, MA.dhHoraAlerta
FROM MetricasAlertas MA
JOIN UnidadeMedida UM ON MA.fkUnidadeMedida = UM.idParametros
JOIN Maquina M ON UM.fkMaquina = M.idMaquina
WHERE M.idMaquina = 1 and TipoComponente = 'Memória'
GROUP BY MA.idMetricasAlertas limit 1;

SELECT MA.idMetricasAlertas, MA.TipoComponente, MA.maximo, MA.mensagemAlerta, MA.minimo, MA.dhHoraAlerta
                FROM MetricasAlertas MA
                JOIN UnidadeMedida UM ON MA.fkUnidadeMedida = UM.idParametros
                JOIN Maquina M ON UM.fkMaquina = M.idMaquina
                WHERE M.idMaquina = 1;


                select * from MetricasAlertas;
                ORDER BY MA.dhHoraAlerta DESC
                LIMIT 1;

-- Sql Server

USE master;
GO

-- Criar o banco de dados
CREATE DATABASE AlpacaDB;
GO

-- Usar o banco de dados
USE AlpacaDB;
GO

-- Dropar o banco de dados (caso necessário)
-- DROP DATABASE AlpacaDB;
-- GO

-- Criar tabela Endereco
CREATE TABLE Endereco (
    idEndereco INT PRIMARY KEY IDENTITY(1,1),
    cep VARCHAR(8),
    rua VARCHAR(50),
    numero VARCHAR(50),
    bairro VARCHAR(50),
    cidade VARCHAR(50),
    estado VARCHAR(50),
    ativo BIT
);
GO

-- Criar tabela Empresa
CREATE TABLE Empresa (
    idEmpresa INT PRIMARY KEY IDENTITY(1,1),
    nomeFantasia VARCHAR(45),
    razaoSocial VARCHAR(45),
    email VARCHAR(50),
    senha VARCHAR(50),
    cnpj VARCHAR(14),
    ativo BIT,
    fk_endereco INT,
    CONSTRAINT fk_endereco FOREIGN KEY (fk_endereco) REFERENCES Endereco(idEndereco)
);
GO

-- Criar tabela Telefone
CREATE TABLE Telefone (
    idTelefone INT PRIMARY KEY IDENTITY(1,1),
    numero CHAR(11),
    tipo VARCHAR(45),
    ativo BIT,
    fkEmpresa INT,
    CONSTRAINT fkEmpresa FOREIGN KEY (fkEmpresa) REFERENCES Empresa(idEmpresa)
);
GO

-- Criar tabela Usuario
CREATE TABLE Usuario (
    idUsuario INT PRIMARY KEY IDENTITY(1,1),
    nome VARCHAR(50),
    email VARCHAR(50),
    senha VARCHAR(50),
    tipoAcesso VARCHAR(20),
    nivelAcesso VARCHAR(20),
    ativo BIT,
    fkEmpresa INT,
    CONSTRAINT fkEmpresa FOREIGN KEY (fkEmpresa) REFERENCES Empresa(idEmpresa)
);
GO

-- Criar tabela Unidade
CREATE TABLE Unidade (
    idUnidade INT PRIMARY KEY IDENTITY(1,1),
    nomeInstituicao VARCHAR(45),
    ativo BIT,
    fkEndereco INT,
    CONSTRAINT fkEndereco FOREIGN KEY (fkEndereco) REFERENCES Endereco(idEndereco)
);
GO

-- Criar tabela Maquina
CREATE TABLE Maquina (
    idMaquina INT PRIMARY KEY IDENTITY(1,1),
    NomeMaquina VARCHAR(50) NOT NULL,
    ipMaquina VARCHAR(45),
    sistemaOperacional VARCHAR(45),
    statusMaquina BIT,
    ativo BIT,
    fkEmpresa INT,
    fKUnidade INT,
    CONSTRAINT fkEmpresa FOREIGN KEY (fkEmpresa) REFERENCES Empresa(idEmpresa),
    CONSTRAINT fKUnidade FOREIGN KEY (fKUnidade) REFERENCES Unidade(idUnidade)
);
GO

-- Criar tabela TipoComponente
CREATE TABLE TipoComponente (
    idTipoComponente INT PRIMARY KEY IDENTITY(1,1),
    nomeTipo VARCHAR(60),
    tipoComponente VARCHAR(60)
);
GO

-- Criar tabela UnidadeMedida
CREATE TABLE UnidadeMedida (
    idParametros INT PRIMARY KEY IDENTITY(1,1),
    Tipo CHAR(10),
    fkMaquina INT,
    CONSTRAINT fkMaquina FOREIGN KEY (fkMaquina) REFERENCES Maquina(idMaquina)
);
GO

-- Criar tabela Config
CREATE TABLE Config (
    idComponentes INT PRIMARY KEY IDENTITY(1,1),
    ValorConfiguracao VARCHAR(80),
    fkMaquina INT,
    fkTipoComponenteID INT,
    CONSTRAINT fkMaquina FOREIGN KEY (fkMaquina) REFERENCES Maquina(idMaquina),
    CONSTRAINT fkTipoComponenteID FOREIGN KEY (fkTipoComponenteID) REFERENCES TipoComponente(idTipoComponente)
);
GO

-- Criar tabela Medicoes
CREATE TABLE Medicoes (
    idMedicoes INT PRIMARY KEY IDENTITY(1,1),
    valor DECIMAL(10,2),
    data_hora_leitura DATETIME,
    id_computador INT,
    fkTipoComponenteID INT,
    fkUnidadeMedidaID INT,
    CONSTRAINT fkTipoComponenteID FOREIGN KEY (fkTipoComponenteID) REFERENCES TipoComponente(idTipoComponente),
    CONSTRAINT fkUnidadeMedidaID FOREIGN KEY (fkUnidadeMedidaID) REFERENCES UnidadeMedida(idParametros)
);
GO

-- Criar tabela MetricasAlertas
CREATE TABLE MetricasAlertas (
    idMetricasAlertas INT PRIMARY KEY IDENTITY(1,1),
    TipoComponente VARCHAR(45),
    maximo VARCHAR(45),
    mensagemAlerta VARCHAR(150),
    minimo VARCHAR(45),
    dhHoraAlerta DATETIME,
    fkUnidadeMedida INT,
    fkTipoComponente INT,
    fkConfiguracao INT,
    CONSTRAINT fkUnidadeMedida FOREIGN KEY (fkUnidadeMedida) REFERENCES UnidadeMedida(idParametros),
    CONSTRAINT fkTipoComponente FOREIGN KEY (fkTipoComponente) REFERENCES TipoComponente(idTipoComponente),
    CONSTRAINT fkConfiguracao FOREIGN KEY (fkConfiguracao) REFERENCES Config(idComponentes)
);
GO
