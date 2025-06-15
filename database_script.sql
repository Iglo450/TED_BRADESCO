-- Script de criação do banco de dados para o Sistema Anti-Golpe TED
-- Microsoft SQL Server

-- Criação do banco de dados
IF NOT EXISTS (SELECT name FROM sys.databases WHERE name = 'AntiGolpeDB')
BEGIN
    CREATE DATABASE AntiGolpeDB;
END
GO

USE AntiGolpeDB;
GO

-- Tabela de Usuários
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[USUARIO]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[USUARIO] (
        [USR_ID_INT] INT IDENTITY(1,1) PRIMARY KEY,
        [USR_CONTA_STR] VARCHAR(14) NOT NULL UNIQUE,  -- CPF/CNPJ
        [USR_NOME_STR] NVARCHAR(255) NOT NULL,
        [USR_EMAIL_STR] NVARCHAR(255),
        [USR_SENHA_STR] NVARCHAR(255) NOT NULL,       -- Hash da senha
        [USR_AGENCIA_STR] VARCHAR(10) NOT NULL,
        [USR_TIPO_CONTA_STR] VARCHAR(20) NOT NULL,    -- 'Corrente' ou 'Poupança'
        [USR_CODIGO_BANCO_STR] VARCHAR(10) NOT NULL,
        [USR_TIPO_STR] VARCHAR(10) NOT NULL,          -- 'ADMIN' ou 'USUARIO'
        [USR_DATA_CRIACAO] DATETIME NOT NULL DEFAULT GETDATE()
    );
END
GO

-- Tabela de Destinatários
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DESTINATARIO]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[DESTINATARIO] (
        [DES_ID_INT] INT IDENTITY(1,1) PRIMARY KEY,
        [DES_CONTA_STR] VARCHAR(14) NOT NULL UNIQUE,  -- CPF/CNPJ
        [DES_NOME_STR] NVARCHAR(255) NOT NULL,
        [DES_AGENCIA_STR] VARCHAR(10) NOT NULL,
        [DES_TIPO_CONTA_STR] VARCHAR(20) NOT NULL,    -- 'Corrente' ou 'Poupança'
        [DES_BANCO_STR] VARCHAR(10) NOT NULL
    );
END
GO

-- Tabela de Transferências
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[TRANSFERENCIA]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[TRANSFERENCIA] (
        [TRF_ID_INT] INT IDENTITY(1,1) PRIMARY KEY,
        [USR_ID_INT] INT NOT NULL,
        [DES_ID_INT] INT NOT NULL,
        [TRF_VALOR_DEC] DECIMAL(15,2) NOT NULL,
        [TRF_DATAHR_DATE] DATETIME NOT NULL DEFAULT GETDATE(),
        [TRF_STATUS_STR] VARCHAR(20) NOT NULL,        -- 'PENDENTE', 'CONCLUIDA', 'BLOQUEADA_DENUNCIA', 'BLOQUEADA_QUARENTENA', 'CANCELADA', 'EM_ANALISE_ADMIN', 'APROVADA_ADMIN', 'REJEITADA_ADMIN'
        [TRF_TOKEN_BOOL] BIT NOT NULL DEFAULT 0,      -- Indica se o token foi validado
        FOREIGN KEY ([USR_ID_INT]) REFERENCES [dbo].[USUARIO] ([USR_ID_INT]),
        FOREIGN KEY ([DES_ID_INT]) REFERENCES [dbo].[DESTINATARIO] ([DES_ID_INT])
    );
END
GO

-- Tabela de Denúncias
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[DENUNCIA]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[DENUNCIA] (
        [DEN_ID_INT] INT IDENTITY(1,1) PRIMARY KEY,
        [USR_ID_INT] INT NOT NULL,                    -- Usuário que fez a denúncia
        [DEN_CPF_CNPJ_STR] VARCHAR(14) NOT NULL,      -- CPF/CNPJ denunciado
        [DEN_MOTIVO_STR] NVARCHAR(500) NOT NULL,
        [DEN_DATA_DATE] DATETIME NOT NULL DEFAULT GETDATE(),
        FOREIGN KEY ([USR_ID_INT]) REFERENCES [dbo].[USUARIO] ([USR_ID_INT])
    );
END
GO

-- Tabela de Quarentena
IF NOT EXISTS (SELECT * FROM sys.objects WHERE object_id = OBJECT_ID(N'[dbo].[QUARENTENA]') AND type in (N'U'))
BEGIN
    CREATE TABLE [dbo].[QUARENTENA] (
        [QRT_ID_INT] INT IDENTITY(1,1) PRIMARY KEY,
        [DES_CONTA_STR] VARCHAR(14) NOT NULL UNIQUE,  -- CPF/CNPJ em quarentena
        [QRT_QTD_DENUNCIA_INT] INT NOT NULL DEFAULT 0,
        [QRT_ULTIMA_DENUNCIA_DATE] DATETIME,
        [QRT_MOTIVO_STR] NVARCHAR(500),
        [QRT_STATUS_DENUNCIA_STR] VARCHAR(20) NOT NULL DEFAULT 'NORMAL'  -- 'NORMAL', 'ALERTA', 'BLOQUEADO', 'LIBERADO_ADMIN'
    );
END
GO

-- Inserção do usuário administrador padrão
-- Senha: 501515 (hash SHA-256)
IF NOT EXISTS (SELECT * FROM [dbo].[USUARIO] WHERE [USR_CONTA_STR] = 'admin')
BEGIN
    INSERT INTO [dbo].[USUARIO] (
        [USR_CONTA_STR], 
        [USR_NOME_STR], 
        [USR_EMAIL_STR], 
        [USR_SENHA_STR], 
        [USR_AGENCIA_STR], 
        [USR_TIPO_CONTA_STR], 
        [USR_CODIGO_BANCO_STR], 
        [USR_TIPO_STR], 
        [USR_DATA_CRIACAO]
    ) VALUES (
        '12345678909', 
        'Administrador do Sistema', 
        'admin@sistema.com', 
        '9d4e1e23bd5b727046a9e3b4b7db57bd8d6ee684dd6e4a4db68c1f7785eabf6b', -- Hash SHA-256 para '501515'
        '0000', 
        'Corrente', 
        '000', 
        'ADMIN', 
        GETDATE()
    );
END
GO
PRINT 'Banco de dados AntiGolpeDB criado com sucesso!';
GO
