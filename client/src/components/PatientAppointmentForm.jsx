import {useEffect, useState} from "react";
import axios from "axios";
import { TextField, Button, FormControl, InputLabel, Select, MenuItem, Box, Typography } from '@mui/material';
import {buildApiUrl} from "../api/http.js";
import { useAuth} from '../auth/AuthContext';  // Adjust path as needed

const DENTIST_OPTIONS = [
  "No Preference",
  "Dr. Smith"
]

export default function PatientAppointmentForm() {
  const { token, email, roles } = useAuth();  // Destructure what you need

  const [dentist, setDentist] = useState([])

  useEffect(() => {

    axios.get(buildApiUrl("/adsweb/api/v1/dentist-names"), {
      headers: { Authorization: `Bearer ${token}` }
    }).then(response => {
      setDentist(response.data)
    })

  }, [token])


  const [formData, setFormData] = useState({
    firstName: "Thai",
    lastName: "Phan",
    phone: "(515) 555-0142",
    email: email,
    dentist: "No Preference",
    contactMethod: "Phone Call",
    appointmentDate: new Date().toISOString().split("T")[0],
    appointmentTime: "09:00",
    reason: "Cleaning"
  });

  const [result, setResult] = useState(null);
  const [isPending, setIsPending] = useState(false);

  const handleSubmit = async (e) => {
    e.preventDefault();
    setIsPending(true);
    setResult(null);

    const { firstName, lastName, phone, email, dentist, contactMethod, appointmentDate, appointmentTime, reason } = formData;

    if (!firstName || !lastName || !email) {
      setResult({
        type: "error",
        message: "Please fill in your first name, last name, and email.",
      });
      setIsPending(false);
      return;
    }

    const requestData = {
      firstName,
      lastName,
      phone,
      email,
      dentist,
      contactMethod,
      appointmentDate,
      appointmentTime,
      reason
    };
    const token = localStorage.getItem("ads_auth_token");

    try {

      console.log(requestData)
      console.log(token)
      const url = buildApiUrl("/adsweb/api/v1/appointments/request");

      await axios.post(url, requestData, {
        headers: { Authorization: `Bearer ${token}` }
      });

      setResult({
        type: "success",
        message: "You have successfully submitted your appointment request!",
      });

    } catch (error) {
      setResult({
        type: "error",
        message: error.response.data.message,
      });
    } finally {
      setIsPending(false);
    }
  };

  const handleChange = (field) => (event) => {
    console.log(event.target.value)
    setFormData({ ...formData, [field]: event.target.value });
  };

  return (
    <Box sx={{ maxWidth: 600, mx: 'auto', p: 3, boxShadow: 3, borderRadius: 2, bgcolor: 'background.paper' }}>
      <Typography variant="h5" component="h2" gutterBottom>
        Request an Appointment
      </Typography>
      {result && (
        <Typography
          variant="body1"
          sx={{
            mb: 2,
            color: result.type === 'error' ? 'error.main' : 'success.main'
          }}
        >
          {result.message}
        </Typography>
      )}
      <Box component="form" onSubmit={handleSubmit} sx={{ display: 'flex', flexDirection: 'column', gap: 2 }}>
        <Box sx={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: 2 }}>
          <TextField
            label="First Name"
            value={formData.firstName}
            onChange={handleChange('firstName')}
            required
          />
          <TextField
            label="Last Name"
            value={formData.lastName}
            onChange={handleChange('lastName')}
            required
          />
          <TextField
            label="Contact Phone Number"
            type="tel"
            value={formData.phone}
            onChange={handleChange('phone')}
            placeholder="(515) 555-0142"
            required
          />
          <TextField
            label="Email"
            type="email"
            value={formData.email}
            InputProps={{ readOnly: true }}
            required
          />
          <FormControl fullWidth required>
            <InputLabel>Preferred Dentist</InputLabel>
            <Select
              value={formData.dentist}
              onChange={handleChange('dentist')}
              label="Preferred Dentist"
              variant="outlined"
            >
              {dentist.map((dentist) => (
                <MenuItem key={dentist} value={dentist}>
                  {dentist}
                </MenuItem>
              ))}
            </Select>
          </FormControl>
          <FormControl fullWidth required>
            <InputLabel>Preferred Follow-Up</InputLabel>
            <Select
              value={formData.contactMethod}
              onChange={handleChange('contactMethod')}
              label="Preferred Follow-Up"
              variant="outlined"
            >
              <MenuItem value="Phone Call">Phone Call</MenuItem>
              <MenuItem value="Email">Email</MenuItem>
            </Select>
          </FormControl>
          <TextField
            label="Preferred Date"
            type="date"
            value={formData.appointmentDate}
            onChange={handleChange('appointmentDate')}
            InputLabelProps={{ shrink: true }}
            required
          />
          <TextField
            label="Preferred Time"
            type="time"
            value={formData.appointmentTime}
            onChange={handleChange('appointmentTime')}
            InputLabelProps={{ shrink: true }}
            required
          />
        </Box>
        <TextField
          label="Reason for Visit"
          multiline
          rows={4}
          value={formData.reason}
          onChange={handleChange('reason')}
          placeholder="Cleaning, tooth pain, consultation, follow-up, or another reason."
          required
        />
        <Button
          type="submit"
          variant="contained"
          color="primary"
          disabled={isPending}
          sx={{ mt: 2 }}
        >
          {isPending ? 'Submitting...' : 'Submit Request'}
        </Button>
      </Box>
    </Box>
  );
}
