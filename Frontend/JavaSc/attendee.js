// ==========================
// 📌 Attendee Module JS
// ==========================

// Common API request wrapper
async function apiRequest(url, options = {}) {
  try {
    const res = await fetch(`http://localhost:8080${url}`, {
      headers: { "Content-Type": "application/json" },
      ...options
    });
    if (!res.ok) throw new Error("API Error");
    return await res.json();
  } catch (err) {
    console.error("API Error:", err);
    showMessage("Something went wrong. Please try again.", "error");
    return null;
  }
}

// Message utility
function showMessage(msg, type = "info") {
  const box = document.getElementById("messageBox");
  if (!box) return;
  box.textContent = msg;
  box.className = `message ${type}`;
  setTimeout(() => (box.textContent = ""), 3000);
}

// ==========================
// 1️⃣ Load All Attendees (Card Grid)
// ==========================
async function loadAllAttendees() {
  const container = document.getElementById("attendeeListContainer");
  if (!container) return console.error("❌ attendeeListContainer not found");

  container.innerHTML = `<div class="loading">Loading attendees...</div>`;

  const res = await apiRequest("/attendees");
  if (!res) return;

  const attendees = res.data || [];
  if (attendees.length === 0) {
    container.innerHTML = `<p>No attendees found.</p>`;
    return;
  }

  container.innerHTML = attendees
    .map(
      (a) => `
    <div class="card">
      <h3 class="card-title">👤 ${a.name}</h3>
      <p><strong>Email:</strong> ${a.email}</p>
      <p><strong>Contact:</strong> ${a.contact}</p>
      <div class="actions">
        <button class="card-action-btn" onclick="editAttendee(${a.id})">✏️ Edit</button>
        <button class="card-action-btn" onclick="deleteAttendee(${a.id})">🗑️ Delete</button>
      </div>
    </div>
  `
    )
    .join("");
}

// ==========================
// 2️⃣ Create / Update Attendee (Add Form)
// ==========================
document.getElementById("attendeeForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;

  const data = {
    name: form.name.value,
    email: form.email.value,
    contact: form.contact.value,
  };

  const isEdit = form.hasAttribute("data-edit-id");
  const url = isEdit ? `/attendees/${form.getAttribute("data-edit-id")}` : "/attendees";
  const method = isEdit ? "PUT" : "POST";

  const res = await apiRequest(url, {
    method,
    body: JSON.stringify(data),
  });

  if (res) {
    showMessage(`Attendee ${isEdit ? "updated" : "created"} successfully!`, "success");
    form.reset();
    form.removeAttribute("data-edit-id");
    form.querySelector(".submit-btn").textContent = "Save Attendee";
    loadAllAttendees();
    loadDashboardStats?.();
  }
});

// ==========================
// 3️⃣ Edit Attendee (Prefill Form by ID)
// ==========================
let loadedAttendeeId = null;

document.getElementById("loadAttendeeForm")?.addEventListener("submit", async function (e) {
  e.preventDefault();

  const attendeeId = document.getElementById("loadAttendeeId").value;

  try {
    const result = await apiRequest(`/attendees/${attendeeId}`);
    console.log("Loaded Attendee Response:", result);

    const a = result.data;
    loadedAttendeeId = a.id;

    // Fill update form
    document.getElementById("updateAttendeeId").value = a.id;
    document.getElementById("updateAttendeeName").value = a.name || "";
    document.getElementById("updateAttendeeEmail").value = a.email || "";
    document.getElementById("updateAttendeeContact").value = a.contact || "";

    // Show update form
    document.getElementById("attendeeUpdateForm").classList.remove("hidden");

    showToast("✅ Attendee loaded successfully!");
  } catch (error) {
    console.error("Error loading attendee:", error);
    showToast("❌ Failed to load attendee!", "error");
  }
});

// ==========================
// 4️⃣ Update Attendee (Update Form)
// ==========================
document.getElementById("attendeeUpdateForm")?.addEventListener("submit", async function (e) {
  e.preventDefault();

  const id = document.getElementById("updateAttendeeId").value || loadedAttendeeId;

  if (!id) {
    showToast("❌ Attendee ID missing, please load attendee first!", "error");
    return;
  }

  const updatedAttendee = {
    name: document.getElementById("updateAttendeeName").value,
    email: document.getElementById("updateAttendeeEmail").value,
    contact: document.getElementById("updateAttendeeContact").value,
  };

  try {
    const result = await apiRequest(`/attendees/${id}`, {
      method: "PUT",
      body: JSON.stringify(updatedAttendee),
    });

    console.log("Updated Attendee:", result);
    showToast("✅ Attendee updated successfully!");
    showSection("list-attendees-section");
    loadAllAttendees();
  } catch (error) {
    console.error("Error updating attendee:", error);
    showToast("❌ Failed to update attendee!", "error");
  }
});

// ==========================
// 5️⃣ Delete Attendee
// ==========================
async function deleteAttendee(id) {
  if (!confirm(`Are you sure you want to delete attendee ID ${id}?`)) return;

  const res = await apiRequest(`/attendees/${id}`, { method: "DELETE" });
  if (res) {
    showMessage("🗑️ Attendee deleted successfully!", "success");
    loadAllAttendees();
    loadDashboardStats?.();
  }
}

// ==========================
// 6️⃣ Get Attendee by ID
// ==========================
// ==========================
// 6️⃣ Get Attendee by ID
// ==========================
document.getElementById("getAttendeeByIdForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("searchAttendeeId").value.trim();
  const resultDiv = document.getElementById("attendeeDetails");

  if (!id) {
    resultDiv.classList.remove("hidden");
    resultDiv.innerHTML = `<p class="error">⚠️ Please enter an Attendee ID</p>`;
    return;
  }

  resultDiv.classList.remove("hidden");
  resultDiv.innerHTML = `<div class="loading">Loading attendee...</div>`;

  const res = await apiRequest(`/attendees/${id}`);
  if (!res?.data) {
    resultDiv.innerHTML = `<p class="error">❌ No Registrations Found for Attendee ID: ${id}</p>`;
    return;
  }

  const a = res.data;
  resultDiv.innerHTML = `
    <div class="card">
      <h3 class="card-title">👤 ${a.name}</h3>
      <p><strong>Email:</strong> ${a.email}</p>
      <p><strong>Contact:</strong> ${a.contact}</p>
    </div>
  `;
});


// ==========================
// 7️⃣ Get Attendee by Contact
// ==========================
// ==========================
// 7️⃣ Get Attendee by Contact
// ==========================
document.getElementById("getAttendeeByContactForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const contact = document.getElementById("attendeeContactInput").value.trim();
  const container = document.getElementById("attendeeByContactContainer");

  container.innerHTML = `<div class="loading">Loading...</div>`;

  try {
    const res = await apiRequest(`/attendees/contact/${contact}`);
    const attendees = res?.data || [];

    if (attendees.length === 0) {
      container.innerHTML = `<p class="error">❌ No Attendees Found with Contact: ${contact}</p>`;
      return;
    }

    container.innerHTML = attendees
      .map(
        (a) => `
      <div class="card">
        <h3 class="card-title">👤 ${a.name}</h3>
        <p><strong>Email:</strong> ${a.email}</p>
        <p><strong>Contact:</strong> ${a.contact}</p>
      </div>
    `
      )
      .join("");
  } catch (err) {
    console.error("API Error:", err);
    container.innerHTML = `<p class="error">❌ No Attendees Found with Contact: ${contact}</p>`;
  }
});


// ==========================
// 9️⃣ Get Registrations by Attendee ID
// ==========================
document.getElementById("getRegistrationsByAttendeeForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const attendeeId = document.getElementById("registrationAttendeeId").value.trim();
  const container = document.getElementById("registrationsContainer");

  if (!attendeeId) {
    container.innerHTML = `<p class="error">⚠️ Please enter Attendee ID</p>`;
    return;
  }

  container.innerHTML = `<div class="loading">Loading registrations...</div>`;

  try {
    const res = await apiRequest(`/attendees/${attendeeId}/registrations`);
    const registrations = res?.data || [];

    if (registrations.length === 0) {
      container.innerHTML = `<p class="error">❌ No Registrations Found for Attendee ID: ${attendeeId}</p>`;
      return;
    }

    container.innerHTML = registrations
      .map(
        (r) => `
        <div class="card">
          <h3 class="card-title">📝 Registration ID: ${r.id}</h3>
          <p><strong>Date:</strong> ${r.registrationDate}</p>

          <h4>🎉 Event Info</h4>
          <p><strong>Title:</strong> ${r.event.title}</p>
          <p><strong>Date:</strong> ${r.event.eventDate}</p>
          <p><strong>Description:</strong> ${r.event.description}</p>

          <h4>🏛️ Venue Info</h4>
          <p><strong>Name:</strong> ${r.event.venue.name}</p>
          <p><strong>Location:</strong> ${r.event.venue.location}</p>
          <p><strong>Capacity:</strong> ${r.event.venue.capacity}</p>

          <h4>👤 Organizer Info</h4>
          <p><strong>Name:</strong> ${r.event.organizer.name}</p>
          <p><strong>Email:</strong> ${r.event.organizer.email}</p>
          <p><strong>Organization:</strong> ${r.event.organizer.organization}</p>

          <h4>🙋 Attendee Info</h4>
          <p><strong>Name:</strong> ${r.attendee.name}</p>
          <p><strong>Email:</strong> ${r.attendee.email}</p>
          <p><strong>Contact:</strong> ${r.attendee.contact}</p>
        </div>
      `
      )
      .join("");
  } catch (err) {
    console.error("Error fetching registrations:", err);
    container.innerHTML = `<p class="error">❌ No Registrations Found for Attendee ID: ${attendeeId}</p>`;
  }
});


// ==========================
// 8️⃣ Pagination + Sorting
// ==========================
let currentPage = 0;
let pageSize = 5;
let sortBy = "id";
let sortOrder = "asc";

const validSortFields = ["id", "name", "email", "contact"];

document.getElementById("paginationForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  currentPage = parseInt(document.getElementById("pageNumber").value, 10);
  pageSize = parseInt(document.getElementById("pageSize").value, 10);
  sortBy = document.getElementById("sortBy").value;
  sortOrder = document.getElementById("sortOrder").value;

  if (!validSortFields.includes(sortBy)) sortBy = "id";

  await loadPaginatedAttendees();
});

async function loadPaginatedAttendees() {
  const container = document.getElementById("paginationResults");
  container.innerHTML = `<div class="loading">Loading attendees...</div>`;

  try {
    const data = await apiRequest(
      `/attendees/page?page=${currentPage}&size=${pageSize}&field=${sortBy}&sortOrder=${sortOrder}`
    );

    if (!data || !data.data) {
      container.innerHTML = `<p class="error">⚠️ Failed to load data</p>`;
      return;
    }

    const attendees = data.data.content || [];
    container.innerHTML = "";

    if (attendees.length === 0) {
      container.innerHTML = `<p>No attendees found.</p>`;
      return;
    }

    attendees.forEach((att) => {
      const card = document.createElement("div");
      card.classList.add("card");
      card.innerHTML = `
        <h3>${att.name || "N/A"}</h3>
        <p><b>ID:</b> ${att.id}</p>
        <p><b>Email:</b> ${att.email || "N/A"}</p>
        <p><b>Contact:</b> ${att.contact || "N/A"}</p>
      `;
      container.appendChild(card);
    });

    const paginationInfo = document.createElement("p");
    paginationInfo.classList.add("pagination-info");
    paginationInfo.textContent = `Page ${data.data.number + 1} / ${data.data.totalPages}, Total Attendees: ${data.data.totalElements}`;
    container.appendChild(paginationInfo);

    togglePaginationButtons(data.data);
  } catch (err) {
    console.error("Pagination error:", err);
    container.innerHTML = `<p class="error">❌ Error loading attendees</p>`;
  }
}

function togglePaginationButtons(data) {
  document.getElementById("prevBtn").disabled = data.first;
  document.getElementById("nextBtn").disabled = data.last;
}

document.getElementById("prevBtn")?.addEventListener("click", async () => {
  if (currentPage > 0) {
    currentPage--;
    document.getElementById("pageNumber").value = currentPage;
    await loadPaginatedAttendees();
  }
});

document.getElementById("nextBtn")?.addEventListener("click", async () => {
  currentPage++;
  document.getElementById("pageNumber").value = currentPage;
  await loadPaginatedAttendees();
});
