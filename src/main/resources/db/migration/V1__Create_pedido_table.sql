CREATE TABLE pedido (
    id VARCHAR(36) PRIMARY KEY,
    status VARCHAR(255) NOT NULL,
    user_id VARCHAR(255) NOT NULL,
    payment_info VARCHAR(255),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE itens_pedido (
    id VARCHAR(36) PRIMARY KEY,
    product_sku VARCHAR(255) NOT NULL,
    qtd INT NOT NULL,
    pedido_id VARCHAR(36) NOT NULL,
    FOREIGN KEY(pedido_id) REFERENCES pedido(id)
);