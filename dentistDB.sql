DROP DATABASE dentistdb;
CREATE DATABASE dentistdb;
USE dentistdb;

CREATE TABLE Treatment (
    TreatmentID INT AUTO_INCREMENT PRIMARY KEY,
    TreatmentType VARCHAR(50),
    Price DECIMAL(10,2),
    Length INT
);

CREATE TABLE Dentist (
    DentistID INT AUTO_INCREMENT PRIMARY KEY,
    LastName VARCHAR(255),
    FirstName VARCHAR(255),
    AwardingBody VARCHAR(255),
    Specialty VARCHAR(255),
    DateOfBirth DATE,
    PhoneNo VARCHAR(15)
);

CREATE TABLE Patient (
    PatientID INT AUTO_INCREMENT PRIMARY KEY,
    LastName VARCHAR(255),
    FirstName VARCHAR(255),
    DateOfBirth DATE,
    PhoneNo VARCHAR(15),
    Email VARCHAR(255) UNIQUE,
    Street VARCHAR(255),
    Town VARCHAR(255),
    County VARCHAR(255),
    Eircode VARCHAR(10),
    MedicalCard BOOLEAN,
    AmountOwed DOUBLE
);

CREATE TABLE Appointment (
    AppointmentID INT AUTO_INCREMENT PRIMARY KEY,
    DateOfAppointment DATE,
    TimeOfAppointment TIME,
    Attended BOOLEAN,
    TreatmentID INT,
    PatientID INT,
    DentistID INT,
    FOREIGN KEY (TreatmentID) REFERENCES Treatment(TreatmentID) ON DELETE CASCADE,
    FOREIGN KEY (PatientID) REFERENCES Patient(PatientID) ON DELETE CASCADE,
    FOREIGN KEY (DentistID) REFERENCES Dentist(DentistID) ON DELETE CASCADE
);

-- Inserting Patients
INSERT INTO Patient (LastName, FirstName, DateOfBirth,PhoneNo, Email, Street, Town, County, Eircode, MedicalCard, AmountOwed)
VALUES
    ('Doe', 'John', '1985-06-15', '+3530897890088' , 'johndoe@example.com', '123 Main St', 'Dublin', 'Dublin', 'D01 ABC', TRUE, 0.00),
    ('Smith', 'Emily', '1990-11-30', '+353089782222' , 'emilysmith@example.com', '456 Oak St', 'Cork', 'Cork', 'T12 XYZ', FALSE, 0.00),
    ('Brown', 'Michael', '1975-02-20', '+353089765646' ,'michaelbrown@example.com', '789 Pine Rd', 'Limerick', 'Limerick', 'V94 123', TRUE, 0.00),
    ('Smith', 'Alice', '1990-02-20', '+3530897854532' ,'alice.smith@example.com', '456 Elm St', 'Cork', 'Cork', 'T12 DEF', TRUE, 0.00),
    ('Johnson', 'Michael', '1982-11-05', '+3530897892233' ,'michael.johnson@example.com', '789 Oak St', 'Galway', 'Galway', 'H91 GHI', FALSE, 0.00),
    ('Williams', 'Emma', '1978-03-12', '+3530897891188' ,'emma.williams@example.com', '321 Pine St', 'Limerick', 'Limerick', 'V94 JKL', TRUE, 0.00),
    ('Brown', 'David', '1995-07-30', '+3530897894567' ,'david.brown@example.com', '654 Cedar St', 'Waterford', 'Waterford', 'X91 MNO', FALSE, 0.00),
    ('Jones', 'Sophia', '1988-09-25', '+3530897890033' ,'sophia.jones@example.com', '987 Birch St', 'Kilkenny', 'Kilkenny', 'R95 PQR', TRUE, 0.00),
    ('Garcia', 'James', '1980-12-15', '+353089832488' ,'james.garcia@example.com', '135 Maple St', 'Wexford', 'Wexford', 'Y35 STU', FALSE, 0.00),
    ('Martinez', 'Olivia', '1992-05-10', '+3530697890088' ,'olivia.martinez@example.com', '246 Spruce St', 'Sligo', 'Sligo', 'F91 VWX', TRUE, 0.00),
    ('Davis', 'Liam', '1984-08-22', '+3530897810088' ,'liam.davis@example.com', '357 Fir St', 'Donegal', 'Donegal', 'F94 YZA', FALSE, 0.00),
    ('Rodriguez', 'Isabella', '1991-01-18','+3530897430088' , 'isabella.rodriguez@example.com', '468 Willow St', 'Kerry', 'Kerry', 'V93 BCD', TRUE, 0.00),
    ('Martinez', 'Noah', '1986-04-28','+3530845890088' , 'noah.martinez@example.com', '579 Ash St', 'Tipperary', 'Tipperary', 'E34 EFG', FALSE, 0.00);

-- Inserting Dentists
INSERT INTO Dentist (LastName, FirstName, AwardingBody, Specialty, DateOfBirth, PhoneNo)
VALUES
    ('Taylor', 'Sarah', 'Irish Dental Council', 'Orthodontics', '1982-03-10', '+3530897897788'),
    ('Johnson', 'Mark', 'Irish Dental Council', 'General Dentistry', '1978-08-22', '+3530897897732'),
    ('Williams', 'Olivia', 'Royal College of Surgeons in Ireland', 'Periodontics', '1990-07-14', '+3530877567788'),
    ('Smith', 'John', 'American Dental Association', 'Pediatric Dentistry', '1975-06-15', '+3530876543210'),
    ('Johnson', 'Emily', 'British Dental Association', 'Periodontics', '1988-11-22', '+3530865432109'),
    ('Williams', 'Michael', 'Canadian Dental Association', 'Oral Surgery', '1980-01-30', '+3530854321098'),
    ('Brown', 'Jessica', 'Australian Dental Association', 'Cosmetic Dentistry', '1990-04-05', '+3530843210987'),
    ('Jones', 'David', 'New Zealand Dental Association', 'Endodontics', '1978-09-12', '+3530832109876'),
    ('Garcia', 'Laura', 'Mexican Dental Association', 'Prosthodontics', '1985-12-25', '+3530821098765'),
    ('Martinez', 'Daniel', 'South African Dental Association', 'General Dentistry', '1992-07-19', '+3530810987654'),
    ('Davis', 'Sophia', 'Indian Dental Association', 'Oral Medicine', '1983-10-28', '+3530809876543'),
    ('Rodriguez', 'James', 'European Dental Association', 'Public Health Dentistry', '1979-02-14', '+3530798765432'),
    ('Martinez', 'Olivia', 'World Dental Federation', 'Dental Anesthesiology', '1986-05-03', '+3530787654321');


-- Inserting Treatments
INSERT INTO Treatment (TreatmentType, Price, Length)
VALUES
    ('Teeth Whitening', 250.00, 30),
    ('Dental Checkup', 100.00, 30),
    ('Root Canal', 500.00, 30),
    ('Braces', 150.00, 30),
    ('Dental Cleaning',30,30),
    ('Crowning',30,30),
    ('Veneers',30,30),
    ('Dental Cleaning',30,30),
    ('Tooth Extractions',30,30),
    ('Cavity Filling', 200.00, 30);
    
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'password';
FLUSH PRIVILEGES;
