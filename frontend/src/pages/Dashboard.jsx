import { useEffect, useState } from "react";
import api from "../services/api";

function Dashboard({ user, onLogout }) {
  const isProvider = user?.role === "PROVIDER";

  const [categories, setCategories] = useState([]);
  const [services, setServices] = useState([]);
  const [bookings, setBookings] = useState([]);
  const [providerProfile, setProviderProfile] = useState(null);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [notice, setNotice] = useState("");

  const [search, setSearch] = useState("");
  const [selectedCategory, setSelectedCategory] = useState("ALL");

  const [bookingForm, setBookingForm] = useState({
    providerId: "",
    serviceId: "",
    bookingDate: "",
    bookingTime: "",
    urgencyLevel: "NORMAL",
    serviceAddress: "",
    serviceCity: "Hyderabad",
    serviceArea: "",
    problemDescription: "",
    customerNotes: "",
  });

  const [bookingLoading, setBookingLoading] = useState(false);

  const formatPrice = (amount) =>
    new Intl.NumberFormat("en-IN", {
      style: "currency",
      currency: "INR",
      maximumFractionDigits: 2,
    }).format(amount ?? 0);

  const formatStatus = (status) =>
    status
      ? status.replaceAll("_", " ").toLowerCase().replace(/\b\w/g, (c) => c.toUpperCase())
      : "Unknown";

  const loadData = async () => {
    setLoading(true);
    setError("");

    try {
      const [categoriesResult, servicesResult] = await Promise.allSettled([
        api.get("/categories"),
        api.get("/services"),
      ]);

      if (categoriesResult.status === "fulfilled") {
        setCategories(Array.isArray(categoriesResult.value.data) ? categoriesResult.value.data : []);
      }

      if (servicesResult.status === "fulfilled") {
        setServices(Array.isArray(servicesResult.value.data) ? servicesResult.value.data : []);
      }

      // Provider booking endpoints use the provider-profile ID, not the user ID.
      let bookingOwnerId = user.id;
      if (isProvider) {
        try {
          const profileResponse = await api.get(`/providers/user/${user.id}`);
          const profile = profileResponse.data;
          setProviderProfile(profile);
          bookingOwnerId = profile?.id;
        } catch {
          setProviderProfile(null);
          bookingOwnerId = null;
        }
      }

      if (isProvider && !bookingOwnerId) {
        setBookings([]);
        setError("Your provider business profile was not found. Create a provider profile before receiving bookings.");
      } else {
        try {
          const bookingsResponse = isProvider
            ? await api.get(`/bookings/provider/${bookingOwnerId}`)
            : await api.get(`/bookings/customer/${user.id}`);
          setBookings(Array.isArray(bookingsResponse.data) ? bookingsResponse.data : []);
        } catch {
          setBookings([]);
          setError("Bookings could not be loaded. Please check the backend and your account IDs.");
        }
      }
    } catch {
      setError("Unable to load dashboard information. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, [user.id, isProvider]);

  const visibleServices = services.filter((service) => {
    const matchesSearch =
      service.name?.toLowerCase().includes(search.toLowerCase()) ||
      service.description?.toLowerCase().includes(search.toLowerCase()) ||
      service.categoryName?.toLowerCase().includes(search.toLowerCase());

    const matchesCategory =
      selectedCategory === "ALL" ||
      String(service.categoryId) === selectedCategory;

    return matchesSearch && matchesCategory && service.active !== false;
  });

  const handleBookingChange = (event) => {
    setBookingForm({
      ...bookingForm,
      [event.target.name]: event.target.value,
    });
  };

  const createBooking = async (event) => {
    event.preventDefault();

    if (!bookingForm.providerId || !bookingForm.serviceId) {
      setNotice("Please select a provider and service.");
      return;
    }

    setBookingLoading(true);
    setNotice("");
    setError("");

    try {
      // BookingController expects request parameters, not a JSON request body.
      const params = new URLSearchParams({
        customerId: String(user.id),
        providerId: String(Number(bookingForm.providerId)),
        serviceId: String(Number(bookingForm.serviceId)),
        bookingDate: bookingForm.bookingDate,
        bookingTime: bookingForm.bookingTime,
        urgencyLevel: bookingForm.urgencyLevel,
        serviceAddress: bookingForm.serviceAddress.trim(),
        serviceCity: bookingForm.serviceCity.trim(),
        serviceArea: bookingForm.serviceArea.trim(),
        // Approximate Hyderabad coordinates until location selection is implemented.
        serviceLatitude: "17.3850",
        serviceLongitude: "78.4867",
      });

      if (bookingForm.problemDescription.trim()) {
        params.append("problemDescription", bookingForm.problemDescription.trim());
      }
      if (bookingForm.customerNotes.trim()) {
        params.append("customerNotes", bookingForm.customerNotes.trim());
      }

      await api.post("/bookings", params, {
        headers: { "Content-Type": "application/x-www-form-urlencoded" },
      });

      setNotice("Booking created successfully!");

      setBookingForm({
        providerId: "",
        serviceId: "",
        bookingDate: "",
        bookingTime: "",
        urgencyLevel: "NORMAL",
        serviceAddress: "",
        serviceCity: "Hyderabad",
        serviceArea: "",
        problemDescription: "",
        customerNotes: "",
      });

      await loadData();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Booking could not be created. Check the booking API request format."
      );
    } finally {
      setBookingLoading(false);
    }
  };

  const updateBookingStatus = async (bookingId, status) => {
    setError("");
    setNotice("");

    try {
      await api.patch(`/bookings/${bookingId}/status`, null, {
        params: { status },
      });

      setNotice(`Booking updated to ${formatStatus(status)}.`);
      await loadData();
    } catch (err) {
      setError(
        err.response?.data?.message ||
          "Could not update booking status. Please try again."
      );
    }
  };

  return (
    <div className="dashboard-page">
      <header className="dashboard-navbar">
        <a className="brand" href="#dashboard">
          <span className="brand-icon">S</span>
          <span>
            Service<span className="brand-highlight">Sphere</span>
          </span>
        </a>

        <div className="dashboard-user">
          <div className="dashboard-avatar">
            {user.name?.charAt(0)?.toUpperCase() || "U"}
          </div>

          <div className="dashboard-user-info">
            <strong>{user.name}</strong>
            <span>{isProvider ? "Service Provider" : "Customer"}</span>
          </div>

          <button className="btn btn-outline" onClick={onLogout}>
            Log Out
          </button>
        </div>
      </header>

      <main className="dashboard-main" id="dashboard">
        <section className="dashboard-welcome">
          <div>
            <span className="section-label">
              {isProvider ? "PROVIDER WORKSPACE" : "YOUR PERSONAL WORKSPACE"}
            </span>

            <h1>
              {isProvider
                ? `Welcome, ${user.name?.split(" ")[0] || "Provider"}.`
                : `Hello, ${user.name?.split(" ")[0] || "there"}.`}
            </h1>

            <p>
              {isProvider
                ? "Manage service requests, update bookings and grow your local business."
                : "Find reliable local professionals and manage your service bookings in one place."}
            </p>
          </div>

          <button className="btn btn-primary" onClick={loadData}>
            ↻ Refresh
          </button>
        </section>

        {error && (
          <div className="dashboard-alert dashboard-alert-error" role="alert">
            {error}
          </div>
        )}

        {notice && (
          <div className="dashboard-alert dashboard-alert-success" role="status">
            {notice}
          </div>
        )}

        <section className="dashboard-stats">
          <article className="dashboard-stat">
            <span>{isProvider ? "Booking requests" : "My bookings"}</span>
            <strong>{bookings.length}</strong>
            <small>Across all statuses</small>
          </article>

          <article className="dashboard-stat">
            <span>Pending</span>
            <strong>
              {bookings.filter((booking) => booking.status === "PENDING").length}
            </strong>
            <small>Awaiting action</small>
          </article>

          <article className="dashboard-stat">
            <span>Completed</span>
            <strong>
              {bookings.filter((booking) => booking.status === "COMPLETED").length}
            </strong>
            <small>Successfully completed</small>
          </article>

          <article className="dashboard-stat">
            <span>{isProvider ? "Your rating" : "Available services"}</span>
            <strong>
              {isProvider
                ? Number(providerProfile?.averageRating || 0).toFixed(1)
                : services.length}
            </strong>
            <small>
              {isProvider
                ? `${providerProfile?.totalReviews || 0} customer reviews`
                : "Across service categories"}
            </small>
          </article>
        </section>

        {!isProvider && (
          <>
            <section className="dashboard-section">
              <div className="dashboard-section-heading">
                <div>
                  <span className="section-label">DISCOVER LOCAL HELP</span>
                  <h2>Find the right service</h2>
                </div>
              </div>

              <div className="service-filters">
                <input
                  type="search"
                  placeholder="Search services, repairs or categories..."
                  value={search}
                  onChange={(event) => setSearch(event.target.value)}
                />

                <select
                  value={selectedCategory}
                  onChange={(event) => setSelectedCategory(event.target.value)}
                >
                  <option value="ALL">All categories</option>
                  {categories.map((category) => (
                    <option key={category.id} value={String(category.id)}>
                      {category.name}
                    </option>
                  ))}
                </select>
              </div>

              {loading ? (
                <p className="dashboard-empty">Loading services...</p>
              ) : visibleServices.length === 0 ? (
                <p className="dashboard-empty">
                  No matching services found. Try another search.
                </p>
              ) : (
                <div className="dashboard-service-grid">
                  {visibleServices.map((service) => (
                    <article className="dashboard-service-card" key={service.id}>
                      <span className="dashboard-service-icon">🔧</span>

                      <span className="section-label">
                        {service.categoryName || "LOCAL SERVICE"}
                      </span>

                      <h3>{service.name}</h3>

                      <p>{service.description || "Professional local service."}</p>

                      <div className="dashboard-service-price">
                        <strong>{formatPrice(service.basePrice)}</strong>
                        <span>Starting price</span>
                      </div>

                      <button
                        className="btn btn-primary"
                        onClick={() => {
                          setBookingForm({
                            ...bookingForm,
                            serviceId: String(service.id),
                          });

                          document
                            .getElementById("create-booking")
                            ?.scrollIntoView({ behavior: "smooth" });
                        }}
                      >
                        Book this service ↗
                      </button>
                    </article>
                  ))}
                </div>
              )}
            </section>

            <section className="dashboard-section" id="create-booking">
              <div className="dashboard-section-heading">
                <div>
                  <span className="section-label">SCHEDULE A SERVICE</span>
                  <h2>Create a booking</h2>
                </div>
              </div>

              <form className="booking-form" onSubmit={createBooking}>
                <label>
                  Service
                  <select
                    name="serviceId"
                    value={bookingForm.serviceId}
                    onChange={handleBookingChange}
                    required
                  >
                    <option value="">Select a service</option>
                    {services
                      .filter((service) => service.active !== false)
                      .map((service) => (
                        <option key={service.id} value={service.id}>
                          {service.name} — {service.categoryName}
                        </option>
                      ))}
                  </select>
                </label>

                <label>
                  Provider ID
                  <input
                    name="providerId"
                    type="number"
                    min="1"
                    value={bookingForm.providerId}
                    onChange={handleBookingChange}
                    placeholder="Enter provider ID"
                    required
                  />
                </label>

                <label>
                  Booking date
                  <input
                    name="bookingDate"
                    type="date"
                    min={new Date().toLocaleDateString("en-CA")}
                    value={bookingForm.bookingDate}
                    onChange={handleBookingChange}
                    required
                  />
                </label>

                <label>
                  Preferred time
                  <input
                    name="bookingTime"
                    type="time"
                    value={bookingForm.bookingTime}
                    onChange={handleBookingChange}
                    required
                  />
                </label>

                <label>
                  Urgency
                  <select
                    name="urgencyLevel"
                    value={bookingForm.urgencyLevel}
                    onChange={handleBookingChange}
                  >
                    <option value="NORMAL">Normal</option>
                    <option value="URGENT">Urgent</option>
                    <option value="EMERGENCY">Emergency</option>
                  </select>
                </label>

                <label>
                  City
                  <input
                    name="serviceCity"
                    value={bookingForm.serviceCity}
                    onChange={handleBookingChange}
                    required
                  />
                </label>

                <label>
                  Area
                  <input
                    name="serviceArea"
                    value={bookingForm.serviceArea}
                    onChange={handleBookingChange}
                    placeholder="e.g. LB Nagar"
                    required
                  />
                </label>

                <label>
                  Full service address
                  <input
                    name="serviceAddress"
                    value={bookingForm.serviceAddress}
                    onChange={handleBookingChange}
                    placeholder="House number, street and landmark"
                    required
                  />
                </label>

                <label className="booking-form-wide">
                  Describe the problem
                  <textarea
                    name="problemDescription"
                    value={bookingForm.problemDescription}
                    onChange={handleBookingChange}
                    placeholder="Explain what needs to be repaired or serviced..."
                    rows="3"
                    required
                  />
                </label>

                <label className="booking-form-wide">
                  Additional notes (optional)
                  <textarea
                    name="customerNotes"
                    value={bookingForm.customerNotes}
                    onChange={handleBookingChange}
                    placeholder="Any instructions for the service provider?"
                    rows="2"
                  />
                </label>

                <div className="booking-form-wide">
                  <button
                    type="submit"
                    className="btn btn-primary"
                    disabled={bookingLoading}
                  >
                    {bookingLoading ? "Creating booking..." : "Confirm Booking"}
                  </button>
                </div>
              </form>
            </section>
          </>
        )}

        {isProvider && (
          <section className="dashboard-section">
            <div className="dashboard-section-heading">
              <div>
                <span className="section-label">YOUR BUSINESS</span>
                <h2>Provider profile</h2>
              </div>
            </div>

            {providerProfile ? (
              <div className="provider-profile-card">
                <h3>{providerProfile.businessName}</h3>
                <p>{providerProfile.description}</p>

                <div className="provider-profile-details">
                  <span>📍 {providerProfile.area}, {providerProfile.city}</span>
                  <span>⭐ {Number(providerProfile.averageRating || 0).toFixed(1)}</span>
                  <span>Experience: {providerProfile.experienceYears} years</span>
                  <span>
                    Availability: {providerProfile.available ? "Available" : "Unavailable"}
                  </span>
                </div>
              </div>
            ) : (
              <div className="dashboard-empty">
                <h3>Provider profile not found</h3>
                <p>
                  Your account is registered, but a separate provider business
                  profile may still need to be created.
                </p>
                <p>
                  We'll add provider profile setup after confirming the main dashboard works.
                </p>
              </div>
            )}
          </section>
        )}

        <section className="dashboard-section">
          <div className="dashboard-section-heading">
            <div>
              <span className="section-label">BOOKING MANAGEMENT</span>
              <h2>{isProvider ? "Incoming bookings" : "My bookings"}</h2>
            </div>
          </div>

          {loading ? (
            <p className="dashboard-empty">Loading bookings...</p>
          ) : bookings.length === 0 ? (
            <div className="dashboard-empty">
              <span className="empty-icon">📅</span>
              <h3>No bookings yet</h3>
              <p>
                {isProvider
                  ? "New customer requests will appear here."
                  : "Your service bookings will appear here once you create one."}
              </p>
            </div>
          ) : (
            <div className="booking-list">
              {bookings.map((booking) => (
                <article className="booking-card" key={booking.id}>
                  <div className="booking-card-top">
                    <div>
                      <span className="section-label">
                        BOOKING #{booking.id}
                      </span>

                      <h3>{booking.serviceName}</h3>

                      <p>
                        {isProvider
                          ? `Customer: ${booking.customerName}`
                          : `Provider: ${booking.businessName || booking.providerName}`}
                      </p>
                    </div>

                    <span
                      className={`booking-status status-${booking.status?.toLowerCase()}`}
                    >
                      {formatStatus(booking.status)}
                    </span>
                  </div>

                  <div className="booking-details">
                    <span>
                      📅 {booking.bookingDate} · {booking.bookingTime}
                    </span>

                    <span>
                      📍 {booking.serviceArea}, {booking.serviceCity}
                    </span>

                    <span>
                      ⚡ {formatStatus(booking.urgencyLevel)}
                    </span>

                    <strong>{formatPrice(booking.estimatedTotal)}</strong>
                  </div>

                  {booking.problemDescription && (
                    <p className="booking-problem">
                      {booking.problemDescription}
                    </p>
                  )}

                  {isProvider && booking.status === "PENDING" && (
                    <div className="booking-actions">
                      <button
                        className="btn btn-primary"
                        onClick={() =>
                          updateBookingStatus(booking.id, "ACCEPTED")
                        }
                      >
                        Accept
                      </button>

                      <button
                        className="btn btn-outline"
                        onClick={() =>
                          updateBookingStatus(booking.id, "REJECTED")
                        }
                      >
                        Reject
                      </button>
                    </div>
                  )}

                  {isProvider && booking.status === "ACCEPTED" && (
                    <button
                      className="btn btn-primary"
                      onClick={() =>
                        updateBookingStatus(booking.id, "IN_PROGRESS")
                      }
                    >
                      Start Service
                    </button>
                  )}

                  {isProvider && booking.status === "IN_PROGRESS" && (
                    <button
                      className="btn btn-primary"
                      onClick={() =>
                        updateBookingStatus(booking.id, "COMPLETED")
                      }
                    >
                      Mark Completed
                    </button>
                  )}
                </article>
              ))}
            </div>
          )}
        </section>

        <p className="dashboard-footer">
          ServiceSphere · Local services, managed simply.
        </p>
      </main>
    </div>
  );
}

export default Dashboard;