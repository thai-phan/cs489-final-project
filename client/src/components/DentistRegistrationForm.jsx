import {useState} from "react";
import {
  Alert,
  Box,
  Button,
  Card,
  CardContent,
  MenuItem,
  Stack,
  TextField,
  Typography
} from "@mui/material";
import axios from "axios";
import {buildApiUrl} from "../api/http.js";
import {useAuth} from "../auth/AuthContext.jsx";

const SPECIALIZATIONS = [
  "General Dentistry",
  "Orthodontics",
  "Pediatric Dentistry",
  "Periodontics",
  "Endodontics",
  "Oral and Maxillofacial Surgery",
  "Prosthodontics"
];

const INITIAL_FORM = {
  dentistId: "",
  firstName: "",
  lastName: "",
  phone: "",
  email: "",
  specialization: "",
  password: ""
};

export default function DentistRegistrationForm({
                                                  dentists = [],
                                                }) {
  const {token} = useAuth();
  const [formData, setFormData] = useState(INITIAL_FORM);
  const [formError, setFormError] = useState("");
  const [formSuccess, setFormSuccess] = useState("");

  const handleChange = (field) => (event) => {
    setFormData((current) => ({...current, [field]: event.target.value}));
  };

  const handleClear = () => {
    setFormData(INITIAL_FORM);
    setFormError("");
    setFormSuccess("");
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    const trimmed = Object.fromEntries(
        Object.entries(formData).map(([key, value]) => [key, value.trim()])
    );

    if (!/^\d+$/.test(trimmed.dentistId)) {
      setFormError("Dentist ID must be a numeric value.");
      setFormSuccess("");
      return;
    }

    if (!/^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(trimmed.email)) {
      setFormError("Enter a valid email address.");
      setFormSuccess("");
      return;
    }

    if (dentists.some((dentist) => String(dentist.dentistId) === trimmed.dentistId)) {
      setFormError("Dentist ID already exists in the current list.");
      setFormSuccess("");
      return;
    }
    //    String firstName,
    //         String lastName,
    //         String email,
    //         String phoneNumber,
    //         String password,
    //         String dentistIdNumber,
    //         String specialization
    const requestData = {
      dentistIdNumber: Number(trimmed.dentistId),
      firstName: trimmed.firstName,
      lastName: trimmed.lastName,
      phoneNumber: trimmed.phone,
      email: trimmed.email,
      password: trimmed.password,
      specialization: trimmed.specialization
    };

    try {
      const url = buildApiUrl("/adsweb/api/v1/dentists");
      // {
      //   "firstName": "string",
      //     "lastName": "string",
      //     "email": "string",
      //     "password": "string",
      //     "phoneNumber": "string"
      // }
      await axios.post(url, requestData, {
        headers: {Authorization: `Bearer ${token}`}
      });

      setFormData(INITIAL_FORM);
      setFormError("");
      setFormSuccess("Dentist registration has been captured in the UI.");
    } catch (err) {
      setFormSuccess("");
      setFormError(err.message || "Unable to register dentist right now.");
    }
  };

  return (
      <Card className="card form-card" variant="outlined">
        <CardContent>
          <Stack spacing={1} sx={{mb: 3}}>
            <Typography variant="h5" component="h2">
              Register Dentist
            </Typography>
            <Typography variant="body2" color="text.secondary">
              Office Managers can record each dentist&apos;s ID, contact details, and specialization.
            </Typography>
          </Stack>

          <Box component="form" onSubmit={handleSubmit}>
            <Box
                sx={{
                  display: "grid",
                  gap: 2,
                  gridTemplateColumns: {
                    xs: "1fr",
                    sm: "repeat(2, minmax(0, 1fr))"
                  }
                }}
            >
              <TextField
                  id="dentist-id"
                  label="Dentist ID"
                  value={formData.dentistId}
                  onChange={handleChange("dentistId")}
                  placeholder="1006"
                  required
                  fullWidth
              />

              <TextField
                  id="specialization"
                  label="Specialization"
                  select
                  value={formData.specialization}
                  onChange={handleChange("specialization")}
                  required
                  fullWidth
              >
                <MenuItem value="" disabled>
                  Select specialization
                </MenuItem>
                {SPECIALIZATIONS.map((specialization) => (
                    <MenuItem key={specialization} value={specialization}>
                      {specialization}
                    </MenuItem>
                ))}
              </TextField>

              <TextField
                  id="first-name"
                  label="First Name"
                  value={formData.firstName}
                  onChange={handleChange("firstName")}
                  required
                  fullWidth
              />

              <TextField
                  id="last-name"
                  label="Last Name"
                  value={formData.lastName}
                  onChange={handleChange("lastName")}
                  required
                  fullWidth
              />

              <TextField
                  id="phone"
                  label="Contact Phone Number"
                  type="tel"
                  value={formData.phone}
                  onChange={handleChange("phone")}
                  placeholder="(515) 555-0199"
                  required
                  fullWidth
              />

              <TextField
                  id="email"
                  label="Email"
                  type="email"
                  value={formData.email}
                  onChange={handleChange("email")}
                  placeholder="dentist@advantis.example"
                  required
                  fullWidth
              />

              <TextField
                  id="password"
                  label="Password"
                  type="password"
                  value={formData.password}
                  onChange={handleChange("password")}
                  required
                  fullWidth
              />
            </Box>

            <Stack direction="row" spacing={1.5} sx={{mt: 2.5, mb: 2}}>
              <Button variant="contained" type="submit">
                Register Dentist
              </Button>
              <Button variant="outlined" type="button" onClick={handleClear}>
                Clear Form
              </Button>
            </Stack>

            <Stack spacing={1}>
              {formError && <Alert severity="error">{formError}</Alert>}
              {formSuccess && <Alert severity="success">{formSuccess}</Alert>}
            </Stack>
          </Box>
        </CardContent>
      </Card>
  );
}
