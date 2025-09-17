// organizer.js
// Base API request helper (reusable)
async function apiRequest(endpoint, options = {}) {
  try {
    const res = await fetch(`http://localhost:8080${endpoint}`, {
      headers: { "Content-Type": "application/json" },
      ...options,
    });
    if (!res.ok) throw new Error(`HTTP error! status: ${res.status}`);
    return await res.json();
  } catch (err) {
    console.error("API Error:", err);
    return null;
  }
}

// ✅ Load all organizers
async function getAllOrganizers() {
  const container = document.getElementById("organizerListContainer");
  if (!container) return;

  container.innerHTML = `<div class="loading">Loading organizers...</div>`;
  const res = await apiRequest("/organizers");

  if (!res?.data || res.data.length === 0) {
    container.innerHTML = `<p>No organizers found.</p>`;
    return;
  }

  container.innerHTML = res.data.map(o => `
    <div class="card">
      <h3 class="card-title">${o.name}</h3>
      <p><strong>Email:</strong> ${o.email || "N/A"}</p>
      <p><strong>Organization:</strong> ${o.organization || "N/A"}</p>
      <div class="card-actions">
        <button class="card-action-btn" onclick="loadOrganizerForUpdate(${o.id})">✏️ Edit</button>
        <button class="card-action-btn danger" onclick="deleteOrganizer(${o.id})">🗑️ Delete</button>
      </div>
    </div>
  `).join("");
}

// ✅ Add organizer
document.getElementById("organizerForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();
  const form = e.target;
  const resultDiv = document.getElementById("organizerAddResult");

  // Reset previous message
  resultDiv.innerHTML = "";

  const data = {
    name: form.organizerName.value,
    email: form.organizerEmail.value,
    organization: form.organizerOrganization.value
  };

  try {
    const res = await apiRequest("/organizers", {
      method: "POST",
      body: JSON.stringify(data)
    });

    if (res) {
      resultDiv.innerHTML = `<p style="color:green;">✅ Organizer added successfully!</p>`;
      form.reset();
      showSection("list-organizers-section");
      getAllOrganizers();
    } else {
      resultDiv.innerHTML = `<p style="color:red;">❌ Failed to add organizer.</p>`;
    }

  } catch (err) {
    console.error("API Error:", err);
    resultDiv.innerHTML = `<p style="color:red;">❌ Error adding organizer.</p>`;
  }
});

// ==========================
// 🌐 Global variable to store loaded organizer ID
// ==========================
let loadedOrganizerId = null;


// ==========================
// 📌 Load Organizer Handler
// ==========================
document.getElementById("getOrganizerForUpdateForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const organizerId = document.getElementById("loadOrganizerId").value;

  try {
    const result = await apiRequest(`/organizers/${organizerId}`);
    console.log("Loaded Organizer Response:", result);

    const o = result.data;

    // ID को global variable में save करो
    loadedOrganizerId = o.id;

    // Update form में values भरो
    document.getElementById("updateOrganizerId").value = o.id; // hidden field
    document.getElementById("updateOrganizerName").value = o.name;
    document.getElementById("updateOrganizerEmail").value = o.email;
    document.getElementById("updateOrganizerOrganization").value = o.organization;

    // Update form दिखाओ
    document.getElementById("organizerUpdateForm").classList.remove("hidden");

    showToast("✅ Organizer लोड हो गया!");
  } catch (error) {
    console.error("Error loading organizer:", error);
    showToast("❌ Organizer लोड नहीं हुआ!", "error");
  }
});


// ==========================
// 📌 Update Organizer Handler
// ==========================
document.getElementById("organizerUpdateForm").addEventListener("submit", async function (e) {
  e.preventDefault();

  const id = document.getElementById("updateOrganizerId").value || loadedOrganizerId;

  if (!id) {
    showToast("❌ Organizer ID missing, please load organizer first!", "error");
    return;
  }

  const updatedOrganizer = {
    name: document.getElementById("updateOrganizerName").value,
    email: document.getElementById("updateOrganizerEmail").value,
    organization: document.getElementById("updateOrganizerOrganization").value
  };

  try {
    const result = await apiRequest(`/organizers/${id}`, {
      method: "PUT",
      body: JSON.stringify(updatedOrganizer)
    });

    console.log("Updated Organizer:", result);
    showToast("✅ Organizer अपडेट सफल रहा!");
    showSection("list-organizers-section");
    getAllOrganizers();
  } catch (error) {
    console.error("Error updating organizer:", error);
    showToast("❌ Organizer अपडेट असफल रहा!", "error");
  }
});



// ✅ Delete organizer (inline button)
async function deleteOrganizer(id) {
  if (!confirm(`Are you sure you want to delete organizer ID ${id}?`)) return;
  const res = await apiRequest(`/organizers/${id}`, { method: "DELETE" });

  if (res) {
    alert("✅ Organizer deleted successfully!");
    getAllOrganizers();
  }
}

// ✅ Delete organizer by form
document.getElementById("deleteOrganizerForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const id = document.getElementById("deleteOrganizerId").value;
  const resultDiv = document.getElementById("deleteOrganizerResult");
  resultDiv.innerHTML = `<div class="loading">Deleting...</div>`;

  try {
    const res = await fetch(`http://localhost:8080/organizers/${id}`, { method: "DELETE" });
    let result = {};
    try { result = await res.json(); } catch {}

    if (res.status === 404) {
      resultDiv.innerHTML = `<p style="color:red;">❌ Organizer ID ${id} not found</p>`;
    } else if (res.ok) {
      resultDiv.innerHTML = `<p style="color:green;">✅ Organizer with ID ${id} deleted successfully!</p>`;
      // Optional: reload organizers list
      // loadOrganizers();
    } else {
      resultDiv.innerHTML = `<p style="color:red;">❌ Failed to delete organizer</p>`;
      console.error("Delete error:", result);
    }

  } catch (err) {
    console.error("API Error:", err);
    resultDiv.innerHTML = `<p style="color:red;">❌ Error deleting organizer</p>`;
  }
});

// ✅ Get organizer by ID
document.getElementById("getOrganizerByIdForm")?.addEventListener("submit", async (e) => {
  e.preventDefault();

  const container = document.getElementById("organizerByIdResult");
  let id = document.getElementById("organizerIdInput").value.trim();

  // अगर user ने कुछ नहीं डाला
  if (!id) {
    container.innerHTML = `<p class="error">❌ Please enter Organizer ID.</p>`;
    return;
  }

  // id को number में बदलो
  id = parseInt(id, 10);
  if (isNaN(id) || id <= 0) {
    container.innerHTML = `<p class="error">❌ Invalid Organizer ID.</p>`;
    return;
  }

  try {
    container.innerHTML = `<div class="loading">Loading...</div>`;
    console.log("📡 API call:", `/organizers/${id}`);

    const res = await apiRequest(`/organizers/${id}`);  

    if (!res?.data) {
      container.innerHTML = `<p>❌ Organizer not found.</p>`;
      return;
    }

    const o = res.data;
    container.innerHTML = `
      <div class="card">
        <h3 class="card-title">${o.name}</h3>
        <p><strong>Email:</strong> ${o.email || "N/A"}</p>
        <p><strong>Organization:</strong> ${o.organization || "N/A"}</p>
      </div>
    `;
  } catch (error) {
    console.error("Error fetching organizer:", error);
    container.innerHTML = `<p class="error">❌ Failed to fetch organizer.</p>`;
  }
});


// ✅ Pagination + Sorting
let orgCurrentPage = 0;
let orgPageSize = 5;
let orgSortBy = "id";       // default sort field
let orgSortOrder = "asc";   // default sort order

async function fetchOrganizers(page = orgCurrentPage) {
  const resultDiv = document.getElementById("organizerPaginationResult");
  resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

  try {
    // ✅ Correct API call for backend
    const res = await apiRequest(
      `/organizers/pagesort/${page}/${orgPageSize}/${orgSortBy}/${orgSortOrder}`
    );

    if (!res?.data?.content || res.data.content.length === 0) {
      resultDiv.innerHTML = `<p>No organizers found.</p>`;
      return;
    }

    // Render organizers
    resultDiv.innerHTML = res.data.content.map(o => `
      <div class="card">
        <h3 class="card-title">${o.name}</h3>
        <p><strong>Email:</strong> ${o.email || "N/A"}</p>
        <p><strong>Organization:</strong> ${o.organization || "N/A"}</p>
        <div class="card-actions">
          <button class="card-action-btn" onclick="loadOrganizerForUpdate(${o.id})">✏️ Edit</button>
          <button class="card-action-btn danger" onclick="deleteOrganizer(${o.id})">🗑️ Delete</button>
        </div>
      </div>
    `).join("");

    // update current page
    orgCurrentPage = page;
    document.getElementById("organizerPageNumber").value = orgCurrentPage;

    // ✅ enable/disable pagination buttons
    document.getElementById("orgPrevBtn").disabled = res.data.first === true;
    document.getElementById("orgNextBtn").disabled = res.data.last === true;

  } catch (err) {
    resultDiv.innerHTML = `<p class="error">⚠️ Error fetching organizers</p>`;
    console.error("API Error:", err);
  }
}

// handle form submit
document.getElementById("organizerPaginationForm")?.addEventListener("submit", (e) => {
  e.preventDefault();
  orgCurrentPage = parseInt(document.getElementById("organizerPageNumber").value, 10);
  orgPageSize = parseInt(document.getElementById("organizerPageSize").value, 10);
  orgSortBy = document.getElementById("organizerSortBy").value;
  orgSortOrder = document.getElementById("organizerSortOrder").value; // <-- add dropdown/select for sort order
  fetchOrganizers(orgCurrentPage);
});

// Prev button
document.getElementById("orgPrevBtn")?.addEventListener("click", () => {
  if (orgCurrentPage > 0) {
    fetchOrganizers(orgCurrentPage - 1);
  }
});

// Next button
document.getElementById("orgNextBtn")?.addEventListener("click", () => {
  fetchOrganizers(orgCurrentPage + 1);
});
