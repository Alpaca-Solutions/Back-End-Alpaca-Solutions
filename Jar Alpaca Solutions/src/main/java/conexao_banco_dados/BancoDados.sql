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
