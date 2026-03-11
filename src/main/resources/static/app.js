document.addEventListener('DOMContentLoaded', () => {

    // ==========================================
    // 1. LOGIN LOGIC
    // ==========================================
    const loginForm = document.getElementById('loginForm');

    if (loginForm) {
        loginForm.addEventListener('submit', async (e) => {
            e.preventDefault();

            const usernameInput = document.getElementById('username').value;
            const passwordInput = document.getElementById('password').value;
            const errorDiv = document.getElementById('errorMessage');

            errorDiv.style.display = 'none';

            try {
                const response = await fetch('/login', {
                    method: 'POST',
                    headers: {
                        'Content-Type': 'application/json'
                    },
                    body: JSON.stringify({
                        username: usernameInput,
                        password: passwordInput
                    })
                });

                if (response.ok) {
                    const data = await response.json();
                    localStorage.setItem('access-token', data['access-token']);
                    if (data['refresh-token']) {
                        localStorage.setItem('refresh-token', data['refresh-token']);
                    }
                    window.location.href = '/dashboard';
                } else {
                    const errorData = await response.json();
                    errorDiv.textContent = 'Login failed: ' + (errorData.message || 'Invalid credentials');
                    errorDiv.style.display = 'block';
                }
            } catch (error) {
                console.error('Error during login:', error);
                errorDiv.textContent = 'Cannot connect to the server. Please try again.';
                errorDiv.style.display = 'block';
            }
        });
    }

    // ==========================================
    // 2. DASHBOARD ENTRY POINT
    // ==========================================
    const patientTableBody = document.getElementById('patientTableBody');
    if (patientTableBody) {
        initDashboard();
    }
});

// ==========================================
// 3. DASHBOARD FUNCTIONS
// ==========================================

function initDashboard() {
    const token = localStorage.getItem('access-token');

    if (!token) {
        window.location.href = '/login-page';
        return;
    }

    setupLogout();
    setupModal();
    fetchPatients(token);
}

function setupLogout() {
    const logoutBtn = document.getElementById('logoutBtn');
    if (logoutBtn) {
        logoutBtn.addEventListener('click', () => {
            localStorage.removeItem('access-token');
            localStorage.removeItem('refresh-token');
            window.location.href = '/login-page';
        });
    }
}

async function fetchPatients(token) {
    const loadingMsg = document.getElementById('loadingMessage');
    const errorMsg = document.getElementById('errorMessageDashboard');

    try {
        if (loadingMsg) loadingMsg.style.display = 'block';
        if (errorMsg) errorMsg.style.display = 'none';

        const response = await fetch('/api/patients', {
            method: 'GET',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            }
        });

        if (response.ok) {
            const patients = await response.json();
            renderPatientTable(patients);
        } else if (response.status === 401 || response.status === 403) {
            console.warn("Unauthorized access. Token might be expired.");
            document.getElementById('logoutBtn').click();
        } else {
            throw new Error(`Server returned status: ${response.status}`);
        }
    } catch (error) {
        console.error('Error fetching patients:', error);
        if (errorMsg) {
            errorMsg.textContent = 'Could not load patient data. Check console for details.';
            errorMsg.style.display = 'block';
        }
    } finally {
        if (loadingMsg) loadingMsg.style.display = 'none';
    }
}

function renderPatientTable(patients) {
    const tableBody = document.getElementById('patientTableBody');
    if (!tableBody) return;

    tableBody.innerHTML = '';

    if (!patients || patients.length === 0) {
        tableBody.innerHTML = `
            <tr>
                <td colspan="5" style="text-align:center; padding: 20px; color: var(--text-muted);">
                    No patients found. Click '+ New Patient' to add one.
                </td>
            </tr>`;
        return;
    }

    patients.forEach(patient => {
        const row = document.createElement('tr');
        row.innerHTML = `
            <td>${patient.firstName}</td>
            <td>${patient.lastName}</td>
            <td>${patient.gender}</td>
            <td>${patient.phoneNumber || 'N/A'}</td>
            <td>${patient.insuranceNumber}</td>
        `;
        tableBody.appendChild(row);
    });
}

function setupModal() {
    const modal = document.getElementById('patientModal');
    const openBtn = document.getElementById('addPatientBtn');
    const closeBtn = document.getElementById('closeModalBtn');
    const cancelBtn = document.getElementById('cancelBtn');
    const form = document.getElementById('newPatientForm');

    if (!modal || !openBtn) return;

    openBtn.addEventListener('click', () => {
        modal.style.display = 'flex';
    });

    const closeModal = () => {
        modal.style.display = 'none';
        form.reset();
        document.getElementById('modalError').style.display = 'none';
    };

    closeBtn.addEventListener('click', closeModal);
    cancelBtn.addEventListener('click', closeModal);

    window.addEventListener('click', (e) => {
        if (e.target === modal) {
            closeModal();
        }
    });

    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        await createNewPatient();
    });
}

async function createNewPatient() {
    const token = localStorage.getItem('access-token');
    const errorMsg = document.getElementById('modalError');
    const modal = document.getElementById('patientModal');
    const form = document.getElementById('newPatientForm');

    const patientData = {
        firstName: document.getElementById('firstName').value,
        lastName: document.getElementById('lastName').value,
        dateOfBirth: document.getElementById('dateOfBirth').value,
        gender: document.getElementById('gender').value,
        phoneNumber: document.getElementById('phoneNumber').value,
        insuranceNumber: document.getElementById('insuranceNumber').value,
        address: document.getElementById('address').value,
        city: document.getElementById('city').value,
        state: document.getElementById('state').value,
        country: document.getElementById('country').value
    };

    try {
        const response = await fetch('/api/patients', {
            method: 'POST',
            headers: {
                'Authorization': `Bearer ${token}`,
                'Content-Type': 'application/json'
            },
            body: JSON.stringify(patientData)
        });

        if (response.ok) {
            modal.style.display = 'none';
            form.reset();
            errorMsg.style.display = 'none';
            fetchPatients(token);
        } else {
            const errorData = await response.json();
            errorMsg.textContent = 'Failed to create patient: ' + (errorData.message || 'Check inputs.');
            errorMsg.style.display = 'block';
        }
    } catch (error) {
        console.error('Error creating patient:', error);
        errorMsg.textContent = 'Server error. Please try again.';
        errorMsg.style.display = 'block';
    }
}