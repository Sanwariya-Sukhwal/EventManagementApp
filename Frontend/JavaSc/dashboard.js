// dashboard.js

async function loadDashboardStats() {
  try {
    const [eventsRes, attendeesRes, venuesRes] = await Promise.all([
      apiRequest("/events"),
      apiRequest("/attendees"),
      apiRequest("/venues")
    ]);

    const events = eventsRes?.data || [];
    const attendees = attendeesRes?.data || [];
    const venues = venuesRes?.data || [];

    // Set counts to elements
    document.getElementById("totalEvents").innerText = events.length;
    document.getElementById("totalAttendees").innerText = attendees.length;
    document.getElementById("activeVenues").innerText = venues.length;

    // Calculate upcoming events
    const today = new Date().toISOString().split("T")[0];
    const upcomingCount = events.filter(e => e.eventDate >= today).length;
    document.getElementById("upcomingEvents").innerText = upcomingCount;

  } catch (err) {
    console.error("Dashboard stats error:", err);
    document.getElementById("totalEvents").innerText = "Err";
    document.getElementById("totalAttendees").innerText = "Err";
    document.getElementById("upcomingEvents").innerText = "Err";
    document.getElementById("activeVenues").innerText = "Err";
  }
}

// Run on load
window.onload = () => {
  showSection("home-section");
  loadDashboardStats();
};
