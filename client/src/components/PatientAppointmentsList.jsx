import {
  Alert,
  Box, Button,
  Paper,
  Table,
  TableBody,
  TableCell,
  TableContainer,
  TableHead,
  TableRow,
  Typography
} from "@mui/material";

function formatDateTime(value) {
  if (!value) {
    return "N/A";
  }

  const parsed = new Date(value);
  if (Number.isNaN(parsed.getTime())) {
    return value;
  }

  return parsed.toLocaleString();
}

export default function PatientAppointmentsList({ appointments, loading, error }) {


  return (
    <Box sx={{ width: "100%" }}>
      {error && <Alert severity="error" sx={{ mb: 2 }}>{error}</Alert>}
      {!error && loading && (
        <Typography variant="body2" color="text.secondary" sx={{ mb: 2 }}>
          Loading appointment details...
        </Typography>
      )}

      {!loading && !error && appointments.length > 0 ? (
        <TableContainer component={Paper} variant="outlined">
          <Table size="small" aria-label="appointments table">
            <TableHead>
              <TableRow>
                <TableCell>Appointment ID</TableCell>
                <TableCell>Patient Name</TableCell>
                <TableCell>Date &amp; Time</TableCell>
                <TableCell>Status</TableCell>
                <TableCell>Dentist</TableCell>
                <TableCell>Specialization</TableCell>
                <TableCell>Email</TableCell>
                <TableCell>Surgery</TableCell>
              </TableRow>
            </TableHead>
            <TableBody>
              {appointments.map((appointment) => (
                <TableRow key={appointment.appointmentId} hover>
                  <TableCell>{appointment.appointmentId}</TableCell>
                  <TableCell>{appointment.patient.firstName + " " + appointment.patient.lastName}</TableCell>
                  <TableCell>{formatDateTime(appointment.appointmentDateTime)}</TableCell>
                  <TableCell>{appointment.status}</TableCell>
                  <TableCell>{appointment.dentistName}</TableCell>
                  <TableCell>{appointment.dentistSpecialization}</TableCell>
                  <TableCell>{appointment.dentistEmail}</TableCell>
                  <TableCell>{appointment.surgeryLocation}</TableCell>
                </TableRow>
              ))}
            </TableBody>
          </Table>
        </TableContainer>
      ) : null}

      {!loading && !error && appointments.length === 0 ? (
        <Typography variant="body2" color="text.secondary">
          No appointments found.
        </Typography>
      ) : null}
    </Box>
  );
}
