CREATE TABLE pedido (
    id VARCHAR(36) PRIMARY KEY,
    status VARCHAR(255) NOT NULL,
    total DECIMAL(19, 2) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE itens_pedido (
    id VARCHAR(36) PRIMARY KEY,
    product_sku VARCHAR(255) NOT NULL,
    qtd INT NOT NULL,
    preco DECIMAL(19, 2) NOT NULL,
    pedido_id VARCHAR(36) NOT NULL,
    FOREIGN KEY(pedido_id) REFERENCES pedido(id)
);