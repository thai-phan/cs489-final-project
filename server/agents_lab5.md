
Advantis Dental Surgeries, LLC (ADS) are a company that are in the business of managing a
growing network of dental surgeries which are located across cities in the South West
region. Assume you have been hired by the company, as a Lead Software Engineer and
tasked to lead the effort in designing and developing a web-based software solution (i.e. a
website) which the company will be using to manage their business. 

The system will be used by an Office Manager to register Dentists who apply to join their
network of dental surgeries. Each Dentist is given a unique ID number and their First Name,
Last Name, Contact Phone Number, Email and Specialization are recorded into the system.
The Office Manager also uses the system to enroll new Patients who require dental services,
including the Patient’s First Name, Last Name, Contact Phone Number, Email, Mailing
Address and Date of Birth. A Patient can call-in to request appointments to see a dentist. A
Patient can also request appointment by submitting an online form on the ADS website.
Upon receiving a request for an appointment, the Office Manager can then book the
appointment and the system will send a confirmation email notifying the Patient and the
appointment gets recorded accordingly.
Dentists should be able to sign-in to the system and view a listing of all their Appointments,
including details of the Patients who they have been scheduled to see. Each appointment is
normally made for a specific date and time and the dentist is expected to see/treat the
patient at one of ADS’s surgery locations. The system should provide information about
each Surgery, including its name, location address and telephone number. Patients should
be able to sign-in to the system and view their appointments, including the information of
the dentist who they have been booked to see. Patients should also be able to request to
cancel or change their appointments.
A dentist cannot be given more than 5 appointments in any given week. The system should
prevent a Patient from requesting a new appointment if they have an outstanding, unpaid
bill for dental service they have received.

1. HTTP GET request: http://localhost:8080/adsweb/api/v1/patients - Displays the list of all Patients, including their primaryAddresses, sorted in ascending order by their lastName, in JSON format.
2. HTTP GET request: http://localhost:8080/adsweb/api/v1/patients/1 - Displays the data for Patient whose PatientId is 1 including the primaryAddress, in JSON format. Also, make sure to implement appropriate exception handling, for where patientId is invalid and not found.
3. HTTP POST request: http://localhost:8080/adsweb/api/v1/patients - Register a new Patient into the system. Note: You supply the correct/appropriate Patient data in JSON format
4. HTTP PUT request: http://localhost:8080/adsweb/api/v1/patient/1 - Retrieves and updates Patient data for the patient whose patientId is 1 (or any other valid patientId). Also, make sure to implement appropriate exception handling, for where patientId is invalid and not found.
5. HTTP DELETE request: http://localhost:8080/adsweb/api/v1/patient/1 - Deletes the Patient data for the patient whose patientId is 1 (or any other valid patientId).
6. http://localhost:8080/adsweb/api/v1/patient/search/{searchString} - Queries all the Patient data for the patient(s) whose data matches the input searchString.
7. HTTP GET request: http://localhost:8080/adsweb/api/v1/addresses - Displays the list of all Addresses, including the Patient data, sorted in ascending order by their city, in JSON format.

ROLE_PATIENT