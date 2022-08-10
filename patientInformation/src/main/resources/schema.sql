CREATE TABLE patient_information (
                patient_id INT AUTO_INCREMENT NOT NULL,
                family VARCHAR(50) NOT NULL,
                given VARCHAR(50) NOT NULL,
                sex VARCHAR(1) NOT NULL,
                phone VARCHAR(12),
                address VARCHAR(256),
                dob DATE NOT NULL,
                PRIMARY KEY (patient_id)
);

