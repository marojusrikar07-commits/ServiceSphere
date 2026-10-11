import { useState } from "react";
import AuthModal from "./components/AuthModal";
import Dashboard from "./pages/Dashboard";
import "./index.css";

function App() {
  const [activeTab, setActiveTab] = useState("customer");

  const [currentUser, setCurrentUser] = useState(() => {
    try {
      return JSON.parse(localStorage.getItem("user") || "null");
    } catch {
      return null;
    }
  });

  const [showDashboard, setShowDashboard] = useState(() => {
    try {
      return Boolean(JSON.parse(localStorage.getItem("user") || "null"));
    } catch {
      return false;
    }
  });

  const openLogin = () => setActiveTab("login");

  const openRegister = () => setActiveTab("register");

  const handleLogout = () => {
    localStorage.removeItem("user");
    setCurrentUser(null);
    setShowDashboard(false);
    setActiveTab("customer");
  };

  const handleAuthSuccess = (user) => {
    localStorage.setItem("user", JSON.stringify(user));
    setCurrentUser(user);
    setShowDashboard(true);
    setActiveTab("customer");
  };

  // Display the appropriate dashboard after login.
  if (showDashboard && currentUser) {
    return (
      <Dashboard
        user={currentUser}
        onLogout={handleLogout}
      />
    );
  }

  return (
    <div className="app">
      {/* NAVIGATION */}
      <header className="navbar">
        <a className="brand" href="#home">
          <span className="brand-icon">S</span>

          <span>
            Service<span className="brand-highlight">Sphere</span>
          </span>
        </a>

        <nav className="nav-links">
          <a href="#services">Explore Services</a>
          <a href="#how-it-works">How It Works</a>
        </nav>

        <div className="nav-actions">
          {currentUser ? (
            <>
              <span className="user-greeting">
                Hi, {currentUser.name}
              </span>

              <button
                className="btn btn-outline"
                onClick={handleLogout}
              >
                Log Out
              </button>
            </>
          ) : (
            <>
              <button
                className="btn btn-outline"
                onClick={openLogin}
              >
                Log In
              </button>

              <button
                className="btn btn-primary"
                onClick={openRegister}
              >
                Get Started
              </button>
            </>
          )}
        </div>
      </header>

      <main>
        {/* HERO SECTION */}
        <section className="hero" id="home">
          <div className="hero-content">
            <div className="eyebrow">
              <span className="status-dot"></span>
              YOUR NEIGHBOURHOOD, CONNECTED
            </div>

            <h1>
              Local experts.
              <br />
              <span>Reliable service.</span>
              <br />
              Right at your door.
            </h1>

            <p className="hero-description">
              Find trusted professionals for home repairs, vehicle
              maintenance, appliance servicing and more. Book with
              confidence, right in your neighbourhood.
            </p>

            <div className="hero-actions">
              <a
                href="#services"
                className="btn btn-primary btn-large"
              >
                Explore Services <span>↗</span>
              </a>

              <button
                className="btn btn-outline btn-large"
                onClick={openRegister}
              >
                Become a Provider
              </button>
            </div>

            <div className="trust-row">
              <div className="trust-avatars">
                <span>S</span>
                <span>R</span>
                <span>A</span>
              </div>

              <div>
                <strong>One platform, many solutions</strong>
                <p>Simple bookings. Transparent estimates.</p>
              </div>
            </div>
          </div>

          {/* HERO VISUAL */}
          <div className="hero-visual">
            <div className="visual-orbit orbit-one"></div>
            <div className="visual-orbit orbit-two"></div>

            <div className="service-illustration">
              <div className="illustration-top">
                <span className="mini-logo">S</span>

                <span className="online-badge">
                  <span className="status-dot"></span>
                  Available nearby
                </span>
              </div>

              <div className="illustration-icon">🛠️</div>

              <h3>Help is closer than you think.</h3>

              <p>
                Home · Vehicle · Appliances · Electronics
              </p>

              <div className="illustration-footer">
                <span>Verified professionals</span>
                <span>✓</span>
              </div>
            </div>

            <div className="floating-card floating-rating">
              <span className="floating-icon">★</span>

              <div>
                <strong>Quality first</strong>
                <p>Customer reviews</p>
              </div>
            </div>

            <div className="floating-card floating-booking">
              <span className="floating-icon">✓</span>

              <div>
                <strong>Easy booking</strong>
                <p>Track every request</p>
              </div>
            </div>
          </div>
        </section>

        {/* WORKFLOW STRIP */}
        <section className="stats-strip">
          <div>
            <strong>01</strong>
            <span>Describe your problem</span>
          </div>

          <div>
            <strong>02</strong>
            <span>Choose your professional</span>
          </div>

          <div>
            <strong>03</strong>
            <span>Book and track your service</span>
          </div>

          <div>
            <strong>04</strong>
            <span>Review your experience</span>
          </div>
        </section>

        {/* SERVICES */}
        <section className="services-section" id="services">
          <div className="section-heading">
            <div>
              <span className="section-label">
                SERVICES FOR EVERYDAY LIFE
              </span>

              <h2>What can we help you with?</h2>
            </div>

            <p>
              From quick fixes to regular maintenance, find the right
              service for your needs.
            </p>
          </div>

          <div className="service-grid">
            <article className="service-card">
              <div className="service-icon icon-plumbing">🔧</div>
              <span className="card-label">HOME SERVICES</span>
              <h3>Home Repairs</h3>
              <p>
                Electrical, plumbing, carpentry and everyday fixes.
              </p>
              <span className="card-arrow">↗</span>
            </article>

            <article className="service-card">
              <div className="service-icon icon-vehicle">🚗</div>
              <span className="card-label">VEHICLE CARE</span>
              <h3>Vehicle Services</h3>
              <p>
                Car and bike servicing, batteries and inspections.
              </p>
              <span className="card-arrow">↗</span>
            </article>

            <article className="service-card">
              <div className="service-icon icon-appliance">❄️</div>
              <span className="card-label">APPLIANCE CARE</span>
              <h3>Appliance Repair</h3>
              <p>
                AC, refrigerator, washing machine and more.
              </p>
              <span className="card-arrow">↗</span>
            </article>

            <article className="service-card">
              <div className="service-icon icon-electronics">💻</div>
              <span className="card-label">TECH SUPPORT</span>
              <h3>Electronics</h3>
              <p>
                Laptop, computer, printer and device assistance.
              </p>
              <span className="card-arrow">↗</span>
            </article>
          </div>
        </section>

        {/* HOW IT WORKS */}
        <section className="how-section" id="how-it-works">
          <span className="section-label">
            SIMPLE FROM START TO FINISH
          </span>

          <h2>Service made straightforward.</h2>

          <div className="how-grid">
            <article>
              <span>01</span>
              <h3>Find your service</h3>
              <p>
                Browse services and describe what needs fixing.
              </p>
            </article>

            <article>
              <span>02</span>
              <h3>Compare professionals</h3>
              <p>
                Explore providers, ratings and estimated prices.
              </p>
            </article>

            <article>
              <span>03</span>
              <h3>Book with confidence</h3>
              <p>
                Choose your time, address and urgency level.
              </p>
            </article>

            <article>
              <span>04</span>
              <h3>Track and review</h3>
              <p>
                Follow progress and review completed services.
              </p>
            </article>
          </div>
        </section>

        {/* PROVIDER BANNER */}
        <section className="provider-banner">
          <div>
            <span className="section-label">
              FOR LOCAL PROFESSIONALS
            </span>

            <h2>
              Grow your business, one booking at a time.
            </h2>

            <p>
              Showcase your services, manage requests and build trust
              through customer reviews.
            </p>
          </div>

          <button
            className="btn btn-light btn-large"
            onClick={openRegister}
          >
            Join as a Provider ↗
          </button>
        </section>
      </main>

      {/* FOOTER */}
      <footer className="footer">
        <a className="brand" href="#home">
          <span className="brand-icon">S</span>

          <span>
            Service<span className="brand-highlight">Sphere</span>
          </span>
        </a>

        <p>
          Connecting neighbourhoods with reliable local services.
        </p>

        <span>Built for better everyday living.</span>
      </footer>

      {/* LOGIN AND REGISTRATION */}
      {(activeTab === "login" || activeTab === "register") && (
        <AuthModal
          key={activeTab}
          mode={activeTab}
          onClose={() => setActiveTab("customer")}
          onSuccess={handleAuthSuccess}
        />
      )}
    </div>
  );
}

export default App;