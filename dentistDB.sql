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
    Email VARCHAR(255) UNIQUE,
    Street VARCHAR(255),
    Town VARCHAR(255),
    County VARCHAR(255),
    Eircode VARCHAR(10),
    MedicalCard BOOLEAN
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

INSERT INTO Patient (LastName, FirstName, DateOfBirth, Email, Street, Town, County, Eircode, MedicalCard)
VALUES
    ('Doe', 'John', '1985-06-15', 'johndoe@example.com', '123 Main St', 'Dublin', 'Dublin', 'D01 ABC', TRUE),
    ('Smith', 'Emily', '1990-11-30', 'emilysmith@example.com', '456 Oak St', 'Cork', 'Cork', 'T12 XYZ', FALSE),
    ('Brown', 'Michael', '1975-02-20', 'michaelbrown@example.com', '789 Pine Rd', 'Limerick', 'Limerick', 'V94 123', TRUE),
    ('McLaughlin', 'Noah', '2005-10-10', 'BigMan123@gmail.com', '16 Ohiovile', 'Bunc', 'Donegal', 'F32 JH33', TRUE),
    ('Friel', 'Darren', '1899-04-20', 'HungDaddy8@Hotmail.com', '1 SwagSt', 'Novigrad', 'Redania', 'F69 HH88', FALSE),
    ('Olana', 'Rox', '2013-07-14', 'DealCollege4Life@outlook.com', '18 WomanSt', 'Killybegs', 'Odessa', 'F12 RT21', FALSE);

-- Inserting Dentists
INSERT INTO Dentist (LastName, FirstName, AwardingBody, Specialty, DateOfBirth, PhoneNo)
VALUES
    ('Taylor', 'Sarah', 'Irish Dental Council', 'Orthodontics', '1982-03-10', '35312345678'),
    ('Johnson', 'Mark', 'Irish Dental Council', 'General Dentistry', '1978-08-22', '35318765432'),
    ('Williams', 'Olivia', 'Royal College of Surgeons in Ireland', 'Periodontics', '1990-07-14', '35311122334'),
    ('Kako', 'Ibrahim', 'GoatSelecters', 'CodeMaster', '2090-11-11', '087 1 2345543');


-- Inserting Treatments
INSERT INTO Treatment (TreatmentType, Price, Length)
VALUES
    ('Teeth Whitening', 250.00, 45),
    ('Dental Checkup', 100.00, 30),
    ('Root Canal', 500.00, 90),
    ('Braces Consultation', 150.00, 60),
    ('Cavity Filling', 200.00, 40);
