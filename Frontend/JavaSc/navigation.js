// =======================
// 🔄 Show/Hide Sections
// =======================
function showSection(sectionId) {
  document.querySelectorAll(".section").forEach(section => {
    section.classList.add("hidden");
  });

  const target = document.getElementById(sectionId);
  if (target) {
    target.classList.remove("hidden");
  }

  // ✅ Close mobile menu after selecting
  closeMobileMenu();
}

// =======================
// ✅ Set Active Nav Link
// =======================
function setActiveNav(selectedLink) {
  document.querySelectorAll(".nav-link").forEach(link => {
    link.classList.remove("active");
  });
  selectedLink.classList.add("active");
}

// =======================
// 📱 Mobile Nav Toggle
// =======================
function toggleMobileMenu() {
  document.getElementById("nav-menu")?.classList.toggle("active");
  document.getElementById("nav-toggle")?.classList.toggle("active");
}

// ✅ Close mobile menu (used after click)
function closeMobileMenu() {
  document.getElementById("nav-menu")?.classList.remove("active");
  document.getElementById("nav-toggle")?.classList.remove("active");
}

// =======================
// 🎯 Keyboard Shortcuts
// =======================
document.addEventListener("keydown", (e) => {
  if (e.altKey) {
    switch (e.key) {
      case "1":
        showSection("home-section");
        break;
      case "2":
        showSection("create-event-section");
        break;
      case "3":
        showSection("register-attendee-section");
        break;
      case "4":
        showSection("attendee-section");
        loadAttendees();
        break;
      case "5":
        showSection("add-organizer-section");
        break;
      case "6":
        showSection("add-venue-section");
        break;
    }
  }
});

// =======================
// 🖱️ Navbar Click Events
// =======================
document.querySelectorAll(".nav-link").forEach(link => {
  link.addEventListener("click", (e) => {
    e.preventDefault();

    const sectionId = link.getAttribute("data-section");
    if (sectionId) {
      showSection(sectionId);
      setActiveNav(link);
    }

    // Special case: Attendee section needs data loading
    if (sectionId === "attendee-section" && typeof loadAttendees === "function") {
      loadAttendees();
    }
  });
});
