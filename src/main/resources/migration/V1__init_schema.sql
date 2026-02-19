-- Users
CREATE TABLE IF NOT EXISTS tb_users (
    id UUID PRIMARY KEY,
    nome VARCHAR(255),
    nome_da_mae VARCHAR(255),
    cpf VARCHAR(20) UNIQUE NOT NULL,
    data_nascimento DATE,
    senha VARCHAR(255),
    wallet_id UUID UNIQUE,
    ativo BOOLEAN DEFAULT TRUE
);

-- Wallets
CREATE TABLE IF NOT EXISTS tb_wallets (
    id UUID PRIMARY KEY,
    nome VARCHAR(255),
    balance NUMERIC(19,2) DEFAULT 0.00
);

ALTER TABLE tb_users
    ADD CONSTRAINT fk_users_wallet
    FOREIGN KEY (wallet_id) REFERENCES tb_wallets (id);

-- Savings
CREATE TABLE IF NOT EXISTS tb_savings (
    id UUID PRIMARY KEY,
    wallet_id UUID NOT NULL,
    balance NUMERIC(19,2) DEFAULT 0.00
);

ALTER TABLE tb_savings
    ADD CONSTRAINT fk_savings_wallet
    FOREIGN KEY (wallet_id) REFERENCES tb_wallets (id);

-- Contas (auth)
CREATE TABLE IF NOT EXISTS contas (
    id UUID PRIMARY KEY,
    usuario_id UUID UNIQUE,
    login VARCHAR(255) UNIQUE NOT NULL,
    senha VARCHAR(255) NOT NULL,
    role VARCHAR(20) NOT NULL
);

ALTER TABLE contas
    ADD CONSTRAINT fk_contas_usuario
    FOREIGN KEY (usuario_id) REFERENCES tb_users (id);

-- Índices úteis
CREATE INDEX IF NOT EXISTS idx_users_cpf ON tb_users (cpf);
CREATE INDEX IF NOT EXISTS idx_contas_login ON contas (login);