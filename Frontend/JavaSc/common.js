// common.js

// Base backend URL
const BASE_URL = "http://localhost:8080";

// Generic API request handler
async function apiRequest(endpoint, options = {}) {
  try {
    const res = await fetch(`${BASE_URL}${endpoint}`, {
      headers: {
        "Content-Type": "application/json",
        ...options.headers,
      },
      ...options,
    });
    if (!res.ok) throw new Error(`HTTP Error: ${res.status}`);
    return await res.json();
  } catch (err) {
    console.error("API Error:", err);
    showMessage("Connection error. Is the server running?", "error");
    return null;
  }
}

// Display success or error messages in the UI
function showMessage(message, type = "success") {
  const container = document.querySelector(".content-wrapper");
  if (!container) return;

  const msgDiv = document.createElement("div");
  msgDiv.className = type === "success" ? "success-message" : "error-message";
  msgDiv.innerText = message;

  container.prepend(msgDiv);
  setTimeout(() => msgDiv.remove(), 4000);
}

// Back button functionality
function goBack() {
  window.location.href = "index.html"; // Replace with your home page URL
}

// Dark mode toggle
const toggleBtn = document.getElementById("darkModeToggle");

// Check saved theme preference
const prefersDark = localStorage.getItem("theme") === "dark";

// Apply saved theme on load
if (prefersDark) {
  document.body.classList.add("dark-mode");
  toggleBtn.textContent = "☀️";
} else {
  toggleBtn.textContent = "🌙";
}

// Toggle dark mode on button click
toggleBtn.addEventListener("click", () => {
  const isDark = document.body.classList.toggle("dark-mode");
  toggleBtn.textContent = isDark ? "☀️" : "🌙";
  localStorage.setItem("theme", isDark ? "dark" : "light");
});