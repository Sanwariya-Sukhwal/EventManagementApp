// ===== Registration API Base URL =====
const REG_BASE_URL = "http://localhost:8080/registrations";

// ===== Helpers to unwrap ResponseStructure =====
function unwrapList(json) {
  if (Array.isArray(json)) return json;
  if (json && Array.isArray(json.data)) return json.data;
  return [];
}
function unwrapItem(json) {
  return (json && json.data !== undefined) ? json.data : json;
}

// ===== Create / Save Registration =====
async function saveRegistration() {
  const registrationDate = document.getElementById("registrationDate").value;
  const attendeeId = document.getElementById("regAttendeeIdSelect").value;
  const eventId = document.getElementById("regEventIdSelect").value;

  if (!registrationDate || !attendeeId || !eventId) {
    alert("⚠️ कृपया सभी फ़ील्ड भरें!");
    return;
  }

  const payload = {
    registrationDate,
    attendee: { id: Number(attendeeId) },
    event: { id: Number(eventId) }
  };

  try {
    const res = await fetch(REG_BASE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(payload)
    });

    if (!res.ok) throw new Error("Failed to save registration");
    alert("✅ Registration saved successfully!");

    // सूची सेक्शन खोलकर रीलोड करें
    showSection("list-registrations-section");
    await getAllRegistrations();
  } catch (err) {
    console.error(err);
    alert("⚠️ Error while saving registration!");
  }
}

// ===== Get All Registrations =====
async function getAllRegistrations() {
  const container = document.getElementById("registrationListContainer");
  container.innerHTML = `<div class="loading">Loading...</div>`;

  try {
    const res = await fetch(REG_BASE_URL);
    if (!res.ok) throw new Error("Failed to fetch registrations");

    const json = await res.json();
    const list = unwrapList(json);

    if (list.length === 0) {
      container.innerHTML = `<p>कोई registrations नहीं मिले।</p>`;
      return;
    }

    container.innerHTML = list.map(reg => formatRegistrationCard(reg)).join("");

  } catch (err) {
    console.error(err);
    container.innerHTML = `<p style="color:red;">⚠️ Error: ${err.message}</p>`;
  }
}

// ===== Get Registration By ID (form submit) =====
// ==========================
// 🔍 Get Registration by ID
// ==========================
document.getElementById("getRegistrationByIdForm")
  ?.addEventListener("submit", async (e) => {
    e.preventDefault();

    const id = document.getElementById("registrationIdInput").value.trim();
    const resultDiv = document.getElementById("registrationByIdResult");

    if (!id) {
      resultDiv.innerHTML = `<p class="error">⚠️ Please enter Registration ID</p>`;
      return;
    }

    resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

    try {
      const res = await fetch(`${REG_BASE_URL}/${id}`);
      if (!res.ok) {
        resultDiv.innerHTML = `<p class="error">❌ No Registration Found with ID: ${id}</p>`;
        return;
      }

      const json = await res.json();
      const reg = unwrapItem(json); // assuming your helper to unwrap data
      resultDiv.innerHTML = formatRegistrationCard(reg); // your custom formatter
    } catch (err) {
      console.error("Error fetching registration:", err);
      resultDiv.innerHTML = `<p class="error">⚠️ Failed to load Registration</p>`;
    }
  });


// ===== Delete Registration =====
document.getElementById("deleteRegistrationForm")
  .addEventListener("submit", async (e) => {
    e.preventDefault();
    const id = document.getElementById("deleteRegistrationId").value.trim();
    const resultDiv = document.getElementById("deleteRegistrationResult");
    resultDiv.innerHTML = `<div class="loading">Deleting...</div>`;

    try {
      const res = await fetch(`${REG_BASE_URL}/${id}`, { method: "DELETE" });
      let msg = "Deleted successfully";
      try {
        const json = await res.json();
        msg = json?.message ?? msg;
      } catch (_) {}

      if (!res.ok) throw new Error("Failed to delete");
      resultDiv.innerHTML = `<p style="color:green;">✅ ${msg} (ID ${id})</p>`;
    } catch (err) {
      resultDiv.innerHTML = `<p style="color:red;">❌ Error: ${err.message}</p>`;
    }
  });

// ===== Get Registrations by Attendee ID =====
document.getElementById("getRegistrationByAttendeeForm")
  .addEventListener("submit", async (e) => {
    e.preventDefault();
    const attendeeId = document.getElementById("searchAttendeeIdForReg").value.trim();
    const resultDiv = document.getElementById("registrationByAttendeeResult");
    resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

    try {
      const res = await fetch(`${REG_BASE_URL}/attendee/${attendeeId}`);
      if (!res.ok) throw new Error("No registrations found");
      const json = await res.json();
      const list = unwrapList(json);

      if (list.length === 0) {
        resultDiv.innerHTML = `<p>No registrations found for Attendee ID ${attendeeId}</p>`;
        return;
      }

      resultDiv.innerHTML = list.map(reg => formatRegistrationCard(reg)).join("");
    } catch (err) {
      resultDiv.innerHTML = `<p style="color:red;">⚠️ Error: ${err.message}</p>`;
    }
  });

// ===== Get Registrations by Event ID =====
document.getElementById("getRegistrationByEventForm")
  .addEventListener("submit", async (e) => {
    e.preventDefault();
    const eventId = document.getElementById("searchEventIdForReg").value.trim();
    const resultDiv = document.getElementById("registrationByEventResult");
    resultDiv.innerHTML = `<div class="loading">Loading...</div>`;

    try {
      const res = await fetch(`${REG_BASE_URL}/event/${eventId}`);
      if (!res.ok) throw new Error("No registrations found");
      const json = await res.json();
      const list = unwrapList(json);

      if (list.length === 0) {
        resultDiv.innerHTML = `<p>No registrations found for Event ID ${eventId}</p>`;
        return;
      }

      resultDiv.innerHTML = list.map(reg => formatRegistrationCard(reg)).join("");
    } catch (err) {
      resultDiv.innerHTML = `<p style="color:red;">⚠️ Error: ${err.message}</p>`;
    }
  });

// ===== Helper: Format Registration Card with Full Details =====
function formatRegistrationCard(reg) {
  return `
    <div class="card">
      <h3>📌 Registration ID: ${reg.id}</h3>
      <p><b>Date:</b> ${reg.registrationDate}</p>

      <h4>🎟 Event Details</h4>
      <p><b>Title:</b> ${reg.event?.title ?? "N/A"}</p>
      <p><b>Date:</b> ${reg.event?.eventDate ?? "N/A"}</p>
      <p><b>Description:</b> ${reg.event?.description ?? "N/A"}</p>

      <h4>📍 Venue</h4>
      <p><b>Name:</b> ${reg.event?.venue?.name ?? "N/A"}</p>
      <p><b>Location:</b> ${reg.event?.venue?.location ?? "N/A"}</p>
      <p><b>Capacity:</b> ${reg.event?.venue?.capacity ?? "N/A"}</p>

      <h4>👤 Organizer</h4>
      <p><b>Name:</b> ${reg.event?.organizer?.name ?? "N/A"}</p>
      <p><b>Email:</b> ${reg.event?.organizer?.email ?? "N/A"}</p>
      <p><b>Organization:</b> ${reg.event?.organizer?.organization ?? "N/A"}</p>

      <h4>🙋 Attendee</h4>
      <p><b>Name:</b> ${reg.attendee?.name ?? "N/A"}</p>
      <p><b>Email:</b> ${reg.attendee?.email ?? "N/A"}</p>
      <p><b>Contact:</b> ${reg.attendee?.contact ?? "N/A"}</p>
    </div>
  `;
}
