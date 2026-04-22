package cs489.asd.lab.controller;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(long patientId) {
        super("Patient with id " + patientId + " not found");
    }
}

