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