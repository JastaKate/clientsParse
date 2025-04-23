CREATE TABLE clients (
                         id SERIAL PRIMARY KEY,
                         name VARCHAR(255) NOT NULL,
                         personal_number VARCHAR(50) NOT NULL UNIQUE,
                         address_id INT NOT NULL,
                         CONSTRAINT fk_address FOREIGN KEY (address_id) REFERENCES clients(id)
);

INSERT INTO clients (id, name, personal_number, address_id) VALUES
                                                                (1, 'Lakeshia Jorgensen Denise Seely', 'AS2JF22O5MD8SZ6Q', 5),
                                                                (2, 'Evia Rousseau Helga Adame', 'DVQZQP2X748M3D5U', 2),
                                                                (3, 'Sharita Thiel Vida Hatley', 'ALLDQFBVME5U08KO', 3),
                                                                (4, 'Katy Mccants Hugh Elizondo', '2CZ5VDZ15YYVV7QD', 4),
                                                                (5, 'Claretha Batiste Letty Mckeown', '6UNQOR44C2HU94V5', 9),
                                                                (6, 'Lon Nielsen Cassondra Singh', 'DVEG34AESUGEZEFQ', 15),
                                                                (7, 'Del Clemente Caleb Harden', 'QYE1RJO1CNXO18Y4', 7),
                                                                (8, 'Dia Cohen Fred Jankowski-Kimbrell', 'UGKFB50K6P3I0RO2', 8),
                                                                (9, 'Granville Noel Kathyrn Mcmillian', 'D5GRMGL89Z8O9OPL', 9),
                                                                (10, 'Yvonne Shannon Kenda Dabney', 'UM8MG7KMZY3Z2954', 10),
                                                                (11, 'Iona Speer Karlyn Chiu', 'PUI5EH1VZ3AO6L1B', 11),
                                                                (12, 'Rolland Luciano Alla Allard', '2JSZFLXITJ4YR7R8', 14),
                                                                (13, 'Chrystal Sherry Maud Shafer', 'HQE44F6YUAA3I2E3', 15),
                                                                (14, 'Agustina Mahon Sheron Simpkins', 'MQPBU9OJBXBNVRH4', 12),
                                                                (15, 'Kena Bisson Danille Guest', 'A07MTRZILMEY81XP', 12),
                                                                (16, 'Sandi Bauer Yasuko Freeland', '81SKMKQNTN26U7PN', 13),
                                                                (17, 'Gussie Stout Lola Gillen', '055E8TP0KUJUZ5BM', 3),
                                                                (18, 'Greg Bolling Tressa Ashford', 'NYA3DANY2Q0NHI40', 1),
                                                                (19, 'Alonso Steffen Maybell Jacobson', 'B39MXZYUFMZ2F92Z', 2),
                                                                (20, 'Cindy Hailey Aimee Grady', 'H0IQKG27X1L46LTZ', 8);