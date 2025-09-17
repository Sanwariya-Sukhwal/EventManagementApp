// ==========================
// 📌 Venue Module JS
// ==========================

// Utility: Show messages
function showMessage(msg, type = "info") {
  alert(msg); // Later replace with UI toast/snackbar
}

// venue.js

// Load all venues and display them in cards
async function loadVenues() {
  const container = document.getElementById("venueListContainer");
  if (!container) return console.error("❌ venueListContainer not found");

  container.innerHTML = `<div class="loading">Loading venues...</div>`;

  try {
    const res = await fetch("http://localhost:8080/venues");
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

    const result = await res.json();
    const venues = result.data || [];

    if (venues.length === 0) {
      container.innerHTML = `<p>No venues found.</p>`;
      return;
    }

    container.innerHTML = venues.map(v => `
      <div class="card">
        <h3 class="card-title">${v.name}</h3>
        <p><strong>ID:</strong> ${v.id}</p>
        <p><strong>Location:</strong> ${v.location}</p>
        <p><strong>Capacity:</strong> ${v.capacity}</p>
        <div class="card-actions" style="margin-top: 10px;">
          <button class="card-action-btn" onclick="editVenue(${v.id})">✏️ Edit</button>
          <button class="card-action-btn" onclick="deleteVenue(${v.id})">🗑️ Delete</button>
        </div>
      </div>
    `).join("");

  } catch (err) {
    console.error("API Error:", err);
    container.innerHTML = `<p>❌ Could not load venues</p>`;
  }
}


// ==========================
// ➕ Create Venue
// ==========================
document.getElementById("venueForm")?.addEventListener("submit", async function (e) {
  e.preventDefault();

  const newVenue = {
    name: document.getElementById("venueName").value.trim(),
    location: document.getElementById("venueLocation").value.trim(),
    capacity: Number(document.getElementById("venueCapacity").value)
  };

  try {
    const res = await fetch("http://localhost:8080/venues", {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(newVenue)
    });

    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    const result = await res.json();

    console.log("Created Venue:", result);
    showMessage("✅ Venue created successfully!");
    loadVenues();

  } catch (err) {
    console.error("API Error:", err);
    showMessage("❌ Error creating venue");
  }
});
// ==========================
// 📌 Venue Module JS
// ==========================

// ==========================
// 📦 Common API request wrapper
// ==========================
async function apiRequest(url, options = {}) {
  try {
    const res = await fetch(`http://localhost:8080${url}`, {
      headers: { "Content-Type": "application/json" },
      ...options
    });

    if (!res.ok) {
      throw new Error(`HTTP error! status: ${res.status}`);
    }

    return await res.json();
  } catch (err) {
    console.error("API Error:", err);
    throw err;
  }
}

// ==========================
// 🌐 Global variable to store loaded venue ID
// ==========================
let loadedVenueId = null;


// ==========================
// 📌 Simple Toast Function
// ==========================
function showToast(msg, type = "success") {
  const container = document.getElementById("toastContainer");
  const toast = document.createElement("div");

  toast.textContent = msg;
  toast.style.cssText = `
    margin-top:10px;padding:10px 16px;border-radius:6px;
    color:#fff;font-weight:bold;box-shadow:0 2px 6px rgba(0,0,0,0.2);
    background:${type === "error" ? "red" : "green"};
  `;

  container.appendChild(toast);

  setTimeout(() => toast.remove(), 3000); // 3s में गायब
}


// ==========================
// 📌 Load Venue Handler
// ==========================
document.getElementById("loadVenueForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const venueId = document.getElementById("loadVenueId").value;

  try {
    const result = await apiRequest(`/venues/${venueId}`);
    console.log("Loaded Venue Response:", result);

    const venue = result.data;

    document.getElementById("updateVenueId").value = venue.id;
    loadedVenueId = venue.id;

    document.getElementById("updateVenueName").value = venue.name;
    document.getElementById("updateVenueLocation").value = venue.location;
    document.getElementById("updateVenueCapacity").value = venue.capacity;

    document.getElementById("venueUpdateForm").classList.remove("hidden");

    showToast("✅ Venue loaded successfully!");
  } catch (error) {
    console.error("Error loading venue:", error);
    showToast("❌ Failed to load venue!", "error");
  }
});

// ==========================
// 📌 Update Venue Handler
// ==========================
document.getElementById("venueUpdateForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const id = document.getElementById("updateVenueId").value || loadedVenueId;

  if (!id) {
    showToast("❌ Venue ID missing, please load venue first!", "error");
    return;
  }

  const updatedVenue = {
    name: document.getElementById("updateVenueName").value,
    location: document.getElementById("updateVenueLocation").value,
    capacity: document.getElementById("updateVenueCapacity").value
  };

  try {
    const result = await apiRequest(`/venues/${id}`, {
      method: "PUT",
      body: JSON.stringify(updatedVenue)
    });

    console.log("Updated Venue:", result);
    showToast("✅ Venue updated successfully!");
  } catch (error) {
    console.error("Error updating venue:", error);
    showToast("❌ Failed to update venue!", "error");
  }
});



// ==========================
// 🗑️ Delete Venue
// ==========================
  document.getElementById("deleteVenueForm")?.addEventListener("submit", async function (e) {
    e.preventDefault();

    const venueId = document.getElementById("deleteVenueId").value;
    const resultDiv = document.getElementById("deleteVenueResult");
    resultDiv.innerHTML = `<div class="loading">Deleting...</div>`;

    try {
      const res = await fetch(`http://localhost:8080/venues/${venueId}`, {
        method: "DELETE"
      });

      let result = {};
      try { result = await res.json(); } catch {}

      if (res.status === 404) {
        resultDiv.innerHTML = `<p style="color:red;">❌ Venue ID ${venueId} not found</p>`;
      } else if (res.ok) {
        resultDiv.innerHTML = `<p style="color:green;">✅ Venue with ID ${venueId} deleted successfully!</p>`;
        // Optional: reload venues after deletion
        // loadVenues();
      } else {
        resultDiv.innerHTML = `<p style="color:red;">❌ Error deleting venue</p>`;
        console.error("Delete error:", result);
      }

    } catch (err) {
      console.error("API Error:", err);
      resultDiv.innerHTML = `<p style="color:red;">❌ Error deleting venue</p>`;
    }
  });

// ==========================
// 📌 Load all venues on page load
// ==========================
document.addEventListener("DOMContentLoaded", loadVenues);

// venue.js

// Fetch venue by ID and display in card format
document.getElementById("getVenueByIdForm").addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("searchVenueId").value;
  const resultContainer = document.getElementById("venueByIdResult");

  if (!id) return;

  resultContainer.innerHTML = `<div class="loading">Loading venue...</div>`;

  try {
    const res = await fetch(`http://localhost:8080/venues/${id}`);
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

    const result = await res.json();
    const venue = result.data;

    if (!venue) {
      resultContainer.innerHTML = `<p>❌ Venue not found.</p>`;
      return;
    }

    // Show venue in a card
    resultContainer.innerHTML = `
      <div class="card">
        <h3 class="card-title">${venue.name}</h3>
        <p><strong>ID:</strong> ${venue.id}</p>
        <p><strong>Location:</strong> ${venue.location}</p>
        <p><strong>Capacity:</strong> ${venue.capacity}</p>
        <div class="card-actions" style="margin-top: 10px;">
          <button class="card-action-btn" onclick="editVenue(${venue.id})">✏️ Edit</button>
          <button class="card-action-btn" onclick="deleteVenue(${venue.id})">🗑️ Delete</button>
        </div>
      </div>
    `;

  } catch (err) {
    console.error("API Error:", err);
    resultContainer.innerHTML = `<p>❌ Could not fetch venue</p>`;
  }
});

// venue.js (or events.js)

// Fetch events by Venue ID
const form = document.getElementById("getEventsByVenueForm");

if (form) {
  form.addEventListener("submit", async (e) => {
    e.preventDefault();

    const venueId = document.getElementById("searchEventsVenueId").value;
    const resultContainer = document.getElementById("eventsByVenueResult");

    if (!venueId) return;

    resultContainer.innerHTML = `<div class="loading">Loading events...</div>`;

    try {
      const res = await fetch(`http://localhost:8080/venues/${venueId}/events`);
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

      const result = await res.json();
      const events = result.data || [];

      if (events.length === 0) {
        resultContainer.innerHTML = `<p>❌ No events found for this venue.</p>`;
        return;
      }

      // Render event cards
      resultContainer.innerHTML = events.map(e => `
        <div class="card">
          <h3 class="card-title">${e.title}</h3>
          <p><strong>Date:</strong> ${e.eventDate}</p>
          <p><strong>Venue:</strong> ${e.venue?.name || "N/A"}</p>
          <p><strong>Organizer:</strong> ${e.organizer?.name || "N/A"} (${e.organizer?.organization || ""})</p>
          <p><strong>Description:</strong> ${e.description}</p>
          <div class="card-actions" style="margin-top: 10px;">
            <button class="card-action-btn" onclick="editEvent(${e.id})">✏️ Edit</button>
            <button class="card-action-btn" onclick="deleteEvent(${e.id})">🗑️ Delete</button>
          </div>
        </div>
      `).join("");

    } catch (err) {
      console.error("API Error:", err);
      resultContainer.innerHTML = `<p>❌ Could not load events</p>`;
    }
  });
}

// Fetch events by Location
const locationForm = document.getElementById("getEventsByLocationForm");

if (locationForm) {
  locationForm.addEventListener("submit", async (e) => {
    e.preventDefault();

    const location = document.getElementById("searchLocation").value;
    const resultContainer = document.getElementById("eventsByLocationResult");

    if (!location) return;

    resultContainer.innerHTML = `<div class="loading">Loading events...</div>`;

    try {
      const res = await fetch(`http://localhost:8080/venues/location/${location}/events`);
      if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);

      const result = await res.json();
      const events = result.data || [];

      if (events.length === 0) {
        resultContainer.innerHTML = `<p>❌ No events found in this location.</p>`;
        return;
      }

      // Render event cards
      resultContainer.innerHTML = events.map(e => `
        <div class="card">
          <h3 class="card-title">${e.title}</h3>
          <p><strong>Date:</strong> ${e.eventDate}</p>
          <p><strong>Venue:</strong> ${e.venue?.name || "N/A"}</p>
          <p><strong>Organizer:</strong> ${e.organizer?.name || "N/A"} (${e.organizer?.organization || ""})</p>
          <p><strong>Description:</strong> ${e.description}</p>
          <div class="card-actions" style="margin-top: 10px;">
            <button class="card-action-btn" onclick="editEvent(${e.id})">✏️ Edit</button>
            <button class="card-action-btn" onclick="deleteEvent(${e.id})">🗑️ Delete</button>
          </div>
        </div>
      `).join("");

    } catch (err) {
      console.error("API Error:", err);
      resultContainer.innerHTML = `<p>❌ Could not load events</p>`;
    }
  });
}

// ✅ Venue Pagination + Sorting
let venueCurrentPage = 0;
let venuePageSize = 5;
let venueSortBy = "id";       // default sort field
let venueSortOrder = "asc";   // default sort order

async function fetchVenues(page = venueCurrentPage) {
  const resultDiv = document.getElementById("venuePaginationResult");
  resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

  try {
    // ✅ Convert field to lowercase before sending
    const field = venueSortBy.toLowerCase();

    // ✅ API call with field & sort order
    const res = await apiRequest(
      `/venues/pagesort/${page}/${venuePageSize}/${field}/${venueSortOrder}`
    );

    if (!res?.data?.content || res.data.content.length === 0) {
      resultDiv.innerHTML = `<p>No venues found.</p>`;
      return;
    }

    // Render venue cards
    resultDiv.innerHTML = res.data.content.map(v => `
      <div class="card">
        <h3 class="card-title">${v.name}</h3>
        <p><strong>ID:</strong> ${v.id}</p>
        <p><strong>Location:</strong> ${v.location}</p>
        <p><strong>Capacity:</strong> ${v.capacity}</p>
        <div class="card-actions">
          <button class="card-action-btn" onclick="loadVenueForUpdate(${v.id})">✏️ Edit</button>
          <button class="card-action-btn danger" onclick="deleteVenue(${v.id})">🗑️ Delete</button>
        </div>
      </div>
    `).join("");

    // Update current page
    venueCurrentPage = page;
    document.getElementById("venuePageNumber").value = venueCurrentPage;

    // Enable/disable pagination buttons
    document.getElementById("venuePrevBtn").disabled = res.data.first === true;
    document.getElementById("venueNextBtn").disabled = res.data.last === true;

  } catch (err) {
    resultDiv.innerHTML = `<p class="error">⚠️ Error fetching venues</p>`;
    console.error("API Error:", err);
  }
}

// ✅ Form submission
document.getElementById("venuePaginationForm")?.addEventListener("submit", (e) => {
  e.preventDefault();

  venueCurrentPage = parseInt(document.getElementById("venuePageNumber").value) || 0;
  venuePageSize = parseInt(document.getElementById("venuePageSize").value) || 5;
  venueSortBy = document.getElementById("venueSortField").value || "id";
  venueSortOrder = document.getElementById("venueSortOrder").value || "asc";

  fetchVenues(venueCurrentPage);
});

// ✅ Pagination buttons
document.getElementById("venuePrevBtn")?.addEventListener("click", () => {
  if (venueCurrentPage > 0) fetchVenues(venueCurrentPage - 1);
});
document.getElementById("venueNextBtn")?.addEventListener("click", () => {
  fetchVenues(venueCurrentPage + 1);
});

