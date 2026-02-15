INSERT INTO shipments (id) VALUES (1);
INSERT INTO cargos (id, shipment_id, income, cost, additional_cost, total_cost, profit) VALUES (1, 1, 2500, 600, 30, 630, 1870);
INSERT INTO cargos (id, shipment_id, income, cost, additional_cost, total_cost, profit) VALUES (2, 1, 2000, 500, 20, 520, 1480);

INSERT INTO shipments (id) VALUES (2);
INSERT INTO cargos (id, shipment_id, income, cost, additional_cost, total_cost, profit) VALUES (3, 2, 13000, 2800, 500, 3300, 9700);

-- Ensure the identity generator now starts AFTER 2
ALTER TABLE shipments ALTER COLUMN id RESTART WITH 3;
ALTER TABLE cargos ALTER COLUMN id RESTART WITH 4;