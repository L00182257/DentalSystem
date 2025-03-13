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

-- Inserting Patients
INSERT INTO Patient (LastName, FirstName, DateOfBirth, Email, Street, Town, County, Eircode, MedicalCard)
VALUES
    ('Doe', 'John', '1985-06-15', 'johndoe@example.com', '123 Main St', 'Dublin', 'Dublin', 'D01 ABC', TRUE),
    ('Smith', 'Emily', '1990-11-30', 'emilysmith@example.com', '456 Oak St', 'Cork', 'Cork', 'T12 XYZ', FALSE),
    ('Brown', 'Michael', '1975-02-20', 'michaelbrown@example.com', '789 Pine Rd', 'Limerick', 'Limerick', 'V94 123', TRUE);

-- Inserting Dentists
INSERT INTO Dentist (LastName, FirstName, AwardingBody, Specialty, DateOfBirth, PhoneNo)
VALUES
    ('Taylor', 'Sarah', 'Irish Dental Council', 'Orthodontics', '1982-03-10', '0897897788'),
    ('Johnson', 'Mark', 'Irish Dental Council', 'General Dentistry', '1978-08-22', '0897897732'),
    ('Williams', 'Olivia', 'Royal College of Surgeons in Ireland', 'Periodontics', '1990-07-14', '0877567788');


-- Inserting Treatments
INSERT INTO Treatment (TreatmentType, Price, Length)
VALUES
    ('Teeth Whitening', 250.00, 45),
    ('Dental Checkup', 100.00, 30),
    ('Root Canal', 500.00, 90),
    ('Braces Consultation', 150.00, 60),
    ('Cavity Filling', 200.00, 40);