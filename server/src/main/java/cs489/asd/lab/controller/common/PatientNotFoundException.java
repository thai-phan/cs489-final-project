package cs489.asd.lab.controller.common;

public class PatientNotFoundException extends RuntimeException {
    public PatientNotFoundException(long patientId) {
        super("Patient with id " + patientId + " not found");
    }
}

