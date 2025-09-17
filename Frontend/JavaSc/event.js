// event.js

// Load all events and display them in cards
async function loadEvents() {
  const container = document.getElementById("eventListContainer");
  if (!container) return console.error("❌ eventListContainer not found");

  container.innerHTML = `<div class="loading">Loading events...</div>`;

  const res = await apiRequest("/events");
  if (!res) return;

  const events = res.data || [];
  if (events.length === 0) {
    container.innerHTML = `<p>No events found.</p>`;
    return;
  }

  container.innerHTML = events.map(e => `
    <div class="card">
      <h3 class="card-title">${e.title}</h3>
      <p><strong>Date:</strong> ${e.eventDate}</p>
      <p><strong>Venue:</strong> ${e.venue?.name || "N/A"}</p>
      <p><strong>Organizer:</strong> ${e.organizer?.name || "N/A"}</p>
      <p><strong>Description:</strong> ${e.description}</p>
      <div class="card-actions" style="margin-top: 10px;">
        <button class="card-action-btn" onclick="editEvent(${e.id})">✏️ Edit</button>
        <button class="card-action-btn" onclick="deleteEvent(${e.id})">🗑️ Delete</button>
      </div>
    </div>
  `).join("");
}

// Populate venue & organizer dropdowns
async function populateDropdowns() {
  const venueSelect = document.getElementById("venueId");
  const organizerSelect = document.getElementById("organizerId");

  const [venuesRes, organizersRes] = await Promise.all([
    apiRequest("/venues"),
    apiRequest("/organizers")
  ]);

  if (venuesRes?.data) {
    venueSelect.innerHTML = `<option value="">Select Venue</option>` +
      venuesRes.data.map(v => `<option value="${v.id}">${v.name}</option>`).join("");
  }

  if (organizersRes?.data) {
    organizerSelect.innerHTML = `<option value="">Select Organizer</option>` +
      organizersRes.data.map(o => `<option value="${o.id}">${o.name}</option>`).join("");
  }
}

// Handle event form submit (create or update)
document.getElementById("eventForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;

  const data = {
    title: form.title.value,
    eventDate: form.eventDate.value,
    description: form.description.value,
    venue: { id: form.venueId.value },
    organizer: { id: form.organizerId.value }
  };

  const isEdit = form.hasAttribute("data-edit-id");
  const url = isEdit ? `/events/${form.getAttribute("data-edit-id")}` : "/events";
  const method = isEdit ? "PUT" : "POST";

  const res = await apiRequest(url, {
    method,
    body: JSON.stringify(data)
  });

  if (res) {
    showMessage(`Event ${isEdit ? "updated" : "created"} successfully!`, "success");
    form.reset();
    form.removeAttribute("data-edit-id");
    showSection("home-section");
    loadEvents();
    loadDashboardStats?.();
  }
});

// Edit event (load data into form)
async function editEvent(id) {
  const res = await apiRequest(`/events/${id}`);
  if (!res?.data) return;

  const event = res.data;
  const form = document.getElementById("eventForm");
  if (!form) return console.error("❌ eventForm not found");

  await populateDropdowns();

  form.title.value = event.title || "";
  form.eventDate.value = event.eventDate || "";
  form.description.value = event.description || "";
  form.venueId.value = event.venue?.id || "";
  form.organizerId.value = event.organizer?.id || "";
  form.setAttribute("data-edit-id", id);

  showSection("create-event-section");
}

// Delete event
async function deleteEvent(id) {
  if (!confirm("Are you sure you want to delete this event?")) return;

  const res = await apiRequest(`/events/${id}`, { method: "DELETE" });
  if (res) {
    showMessage("Event deleted successfully!", "success");
    loadEvents();
    loadDashboardStats?.();
  }
}

// Utility to open event list and preload
function openEventList() {
  loadEvents();
  showSection("list-events-section"); // Make sure this section exists
}

// Initial setup when needed
populateDropdowns();

document.getElementById("getEventByIdForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("eventIdInput").value;
  const container = document.getElementById("eventByIdResult");
  container.innerHTML = `<div class="loading">Loading...</div>`;

  const res = await apiRequest(`/events/${id}`);
  if (!res?.data) {
    container.innerHTML = `<p>No event found.</p>`;
    return;
  }

  const eData = res.data;
  container.innerHTML = `
    <div class="card">
      <h3 class="card-title">${eData.title}</h3>
      <p><strong>Date:</strong> ${eData.eventDate}</p>
      <p><strong>Venue:</strong> ${eData.venue?.name || "N/A"}</p>
      <p><strong>Organizer:</strong> ${eData.organizer?.name || "N/A"}</p>
      <p><strong>Description:</strong> ${eData.description}</p>
    </div>
  `;
});



//   update event

// Load dropdowns
async function loadUpdateDropdowns() {
  const [venuesRes, organizersRes] = await Promise.all([
    apiRequest("/venues"),
    apiRequest("/organizers")
  ]);

  const venueSelect = document.getElementById("updateVenueId");
  const organizerSelect = document.getElementById("updateOrganizerId");

  if (venuesRes?.data) {
    venueSelect.innerHTML = `<option value="">Select Venue</option>` +
      venuesRes.data.map(v => `<option value="${v.id}">${v.name}</option>`).join("");
  }

  if (organizersRes?.data) {
    organizerSelect.innerHTML = `<option value="">Select Organizer</option>` +
      organizersRes.data.map(o => `<option value="${o.id}">${o.name}</option>`).join("");
  }
}

// Fetch event and fill update form
document.getElementById("updateEventByIdForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("updateEventId").value;
  const res = await apiRequest(`/events/${id}`);
  if (!res?.data) return showMessage("Event not found", "error");

  await loadUpdateDropdowns();

  const eData = res.data;
  document.getElementById("eventUpdateForm").classList.remove("hidden");
  document.getElementById("eventUpdateForm").setAttribute("data-id", id);

  document.getElementById("updateTitle").value = eData.title;
  document.getElementById("updateEventDate").value = eData.eventDate;
  document.getElementById("updateDescription").value = eData.description;
  document.getElementById("updateVenueId").value = eData.venue?.id || "";
  document.getElementById("updateOrganizerId").value = eData.organizer?.id || "";
});

// Submit updated data
document.getElementById("eventUpdateForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = e.target.getAttribute("data-id");

  const data = {
    title: document.getElementById("updateTitle").value,
    eventDate: document.getElementById("updateEventDate").value,
    description: document.getElementById("updateDescription").value,
    venue: { id: document.getElementById("updateVenueId").value },
    organizer: { id: document.getElementById("updateOrganizerId").value }
  };

  const res = await apiRequest(`/events/${id}`, {
    method: "PUT",
    body: JSON.stringify(data)
  });

  if (res) {
    showMessage("Event updated successfully!", "success");
    showSection("home-section");
    loadEvents();
    loadDashboardStats?.();
  }
});


// Delete Event
document.getElementById("deleteEventForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("deleteEventId").value;
  const resultDiv = document.getElementById("deleteEventResult");

  if (!confirm(`Are you sure you want to delete event ID ${id}?`)) {
    return;
  }

  resultDiv.innerHTML = `<div class="loading">Deleting...</div>`;

  try {
    const res = await apiRequest(`/events/${id}`, "DELETE");
    if (res?.status === 200 || res?.status === 204) {
      resultDiv.innerHTML = `<p class="success">✅ Event deleted successfully!</p>`;
    } else {
      resultDiv.innerHTML = `<p class="error">❌ Failed to delete event.</p>`;
    }
  } catch (err) {
    console.error("Delete Event Error:", err);
    resultDiv.innerHTML = `<p class="error">⚠️ Error deleting event.</p>`;
  }
});


// Get Attendee by ID
// Get Attendee by ID
document.getElementById("getAttendeeByIdForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("attendeeId").value;
  const resultDiv = document.getElementById("attendeeResult");

  resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

  try {
    const res = await apiRequest(`/attendees/${id}`, "GET");

    if (!res?.data) {
      resultDiv.innerHTML = `<p class="error">⚠️ Attendee not found</p>`;
      return;
    }

    const attendee = res.data;

    // Show in card format
    resultDiv.innerHTML = `
      <div class="card">
        <h3 class="card-title">${attendee.name}</h3>
        <p><strong>Email:</strong> ${attendee.email}</p>
        <p><strong>Contact:</strong> ${attendee.contact}</p>
      </div>
    `;
  } catch (err) {
    console.error(err);
    resultDiv.innerHTML = `<p class="error">⚠️ Attendee not found</p>`;
  }
});


// Get Organizer by ID
document.getElementById("getOrganizerByIdForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const id = document.getElementById("organizerId").value;
  const resultDiv = document.getElementById("organizerResult");

  resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

  try {
    const res = await apiRequest(`/organizers/${id}`, "GET");

    if (!res?.data) {
      resultDiv.innerHTML = `<p class="error">⚠️ Organizer not found</p>`;
      return;
    }

    const organizer = res.data;

    resultDiv.innerHTML = `
      <div class="card">
        <h3 class="card-title">${organizer.name}</h3>
        <p><strong>Email:</strong> ${organizer.email || "N/A"}</p>
        <p><strong>Contact:</strong> ${organizer.contact || "N/A"}</p>
        <p><strong>Organization:</strong> ${organizer.organizationName || "N/A"}</p>
      </div>
    `;
  } catch (err) {
    console.error(err);
    resultDiv.innerHTML = `<p class="error">⚠️ Organizer not found</p>`;
  }
});


// Get Events by Pagination + Sorting
document.getElementById("getEventsPaginationForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const page = document.getElementById("page").value;
  const size = document.getElementById("size").value;
  const sortBy = document.getElementById("sortBy").value;
  const sortDir = document.getElementById("sortDir").value;

  const resultDiv = document.getElementById("eventsPaginationResult");
  resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

  try {
    const res = await apiRequest(`/events?page=${page}&size=${size}&sortBy=${sortBy}&sortDir=${sortDir}`, "GET");

    if (!res?.data || res.data.length === 0) {
      resultDiv.innerHTML = `<p>No events found.</p>`;
      return;
    }

    const events = res.data;

    resultDiv.innerHTML = events.map(e => `
      <div class="card">
        <h3 class="card-title">${e.title}</h3>
        <p><strong>Date:</strong> ${e.eventDate}</p>
        <p><strong>Venue:</strong> ${e.venue?.name || "N/A"}</p>
        <p><strong>Organizer:</strong> ${e.organizer?.name || "N/A"}</p>
        <p><strong>Description:</strong> ${e.description}</p>
        <div class="card-actions" style="margin-top: 10px;">
          <button class="card-action-btn" onclick="editEvent(${e.id})">✏️ Edit</button>
          <button class="card-action-btn" onclick="deleteEvent(${e.id})">🗑️ Delete</button>
        </div>
      </div>
    `).join("");
  } catch (err) {
    console.error(err);
    resultDiv.innerHTML = `<p class="error">⚠️ Failed to load events</p>`;
  }
});
