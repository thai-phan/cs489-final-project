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
import {useEffect, useState} from "react";
import {useAuth} from "../auth/AuthContext.jsx";
import {restRequest} from "../api/restApi.js";
import axios from "axios";
import {buildApiUrl} from "../api/http.js";

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

export default function ManagerAppointmentsList() {

  const {token, roles} = useAuth();
  const [appointments, setAppointments] = useState([]);


  async function loadAppointments() {
    console.log("Loading appointments with token:", token);
    try {
      const path = "/adsweb/api/v1/appointments/pending";
      const data = await restRequest(token, "GET", path);

      const mappedAppointments = (data ?? []).map((appointment) => ({
        ...appointment,
        dentistName: appointment.dentist
            ? `${appointment.dentist.firstName} ${appointment.dentist.lastName}`
            : `Dentist #${appointment.dentistId}`,
        dentistSpecialization: appointment.dentist?.specialization || "N/A",
        dentistEmail: appointment.dentist?.email || "N/A"
      }));
      console.log("Loaded appointments:", mappedAppointments);
      setAppointments(mappedAppointments);
    } catch (err) {
      console.log("Failed to load appointments:", err);
    }
  }

  useEffect(() => {
    loadAppointments();
  }, []);

  const updateStatus = (appointmentId) => async () => {
    try {
      const url = buildApiUrl(`/adsweb/api/v1/appointments/${appointmentId}/status`);
      const requestData = {
        status: "SCHEDULED"
      }
      await axios.put(url, requestData, {
        headers: {Authorization: `Bearer ${token}`}
      });
      loadAppointments();

      // window.location.reload();
    } catch (err) {
      console.error("Failed to update appointment status:", err);
    }
  }

  return (
      <>
        <Box sx={{width: "100%"}}>
          {appointments.length > 0 ? (
              <TableContainer component={Paper} variant="outlined">
                <Table size="small" aria-label="appointments table">
                  <TableHead>
                    <TableRow>
                      <TableCell>Appointment ID</TableCell>
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
                          <TableCell>{formatDateTime(appointment.appointmentDateTime)}</TableCell>
                          <TableCell>{appointment.status}</TableCell>
                          <TableCell>{appointment.dentistName}</TableCell>
                          <TableCell>{appointment.dentistSpecialization}</TableCell>
                          <TableCell>{appointment.dentistEmail}</TableCell>
                          <TableCell>{appointment.surgeryLocation}</TableCell>
                          <TableCell>
                            <Button disabled={appointment.status !== "PENDING"} variant="contained"
                                    onClick={updateStatus(appointment.appointmentId)}>SCHEDULE</Button>
                          </TableCell>
                        </TableRow>
                    ))}
                  </TableBody>
                </Table>
              </TableContainer>
          ) : null}


        </Box>
      </>
  );
}
