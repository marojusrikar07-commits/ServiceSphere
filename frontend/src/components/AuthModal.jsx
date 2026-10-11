import { useState } from "react";
import api from "../services/api";

function AuthModal({ mode, onClose, onSuccess }) {
  const [isLogin, setIsLogin] = useState(mode === "login");
  const [role, setRole] = useState("CUSTOMER");

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
  });

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");
  const [message, setMessage] = useState("");

  const handleChange = (event) => {
    setFormData({
      ...formData,
      [event.target.name]: event.target.value,
    });
  };

  const handleSubmit = async (event) => {
    event.preventDefault();

    setError("");
    setMessage("");
    setLoading(true);

    try {
      let response;

      if (isLogin) {
        response = await api.post("/users/login", {
          email: formData.email,
          password: formData.password,
        });
      } else {
        const endpoint =
          role === "PROVIDER"
            ? "/users/provider/register"
            : "/users/register";

        const registrationData = {
          name: formData.name.trim(),
          email: formData.email.trim(),
          password: formData.password,
          phone: formData.phone.trim(),
        };

        response = await api.post(endpoint, registrationData);

        setMessage("Registration successful! You can now log in.");
        setIsLogin(true);
        setFormData({
          name: "",
          email: registrationData.email,
          password: "",
          phone: "",
        });

        return;
      }

      const user = response.data;

      localStorage.setItem("user", JSON.stringify(user));

      setMessage(`Welcome back, ${user.name}!`);

      if (onSuccess) {
        onSuccess(user);
      }
    } catch (err) {
      const responseData = err.response?.data;

      setError(
        responseData?.message ||
          responseData?.error ||
          "Something went wrong. Please check your details and try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="modal-backdrop" onClick={onClose}>
      <div
        className="modal-card auth-modal"
        onClick={(event) => event.stopPropagation()}
      >
        <button
          className="modal-close"
          onClick={onClose}
          aria-label="Close dialog"
          type="button"
        >
          ×
        </button>

        <span className="section-label">WELCOME TO SERVICESPHERE</span>

        <h2>{isLogin ? "Welcome back." : "Join our community."}</h2>

        <p className="auth-description">
          {isLogin
            ? "Log in to manage your bookings and services."
            : "Create an account to get started with local services."}
        </p>

        {!isLogin && (
          <div className="auth-role-selector">
            <button
              type="button"
              className={`auth-role ${
                role === "CUSTOMER" ? "selected" : ""
              }`}
              onClick={() => setRole("CUSTOMER")}
            >
              👤 Customer
            </button>

            <button
              type="button"
              className={`auth-role ${
                role === "PROVIDER" ? "selected" : ""
              }`}
              onClick={() => setRole("PROVIDER")}
            >
              🛠️ Provider
            </button>
          </div>
        )}

        <form onSubmit={handleSubmit} className="auth-form">
          {!isLogin && (
            <label>
              Full name
              <input
                type="text"
                name="name"
                placeholder="Enter your full name"
                value={formData.name}
                onChange={handleChange}
                minLength={2}
                maxLength={100}
                autoComplete="name"
                required
              />
            </label>
          )}

          <label>
            Email address
            <input
              type="email"
              name="email"
              placeholder="you@example.com"
              value={formData.email}
              onChange={handleChange}
              autoComplete="email"
              required
            />
          </label>

          {!isLogin && (
            <label>
              Mobile number
              <input
                type="tel"
                name="phone"
                placeholder="10-digit mobile number"
                value={formData.phone}
                onChange={handleChange}
                pattern="[6-9][0-9]{9}"
                title="Enter a valid 10-digit Indian mobile number"
                autoComplete="tel"
                required
              />
            </label>
          )}

          <label>
            Password
            <input
              type="password"
              name="password"
              placeholder={
                isLogin ? "Enter your password" : "Create a password"
              }
              value={formData.password}
              onChange={handleChange}
              minLength={isLogin ? undefined : 8}
              maxLength={100}
              autoComplete={isLogin ? "current-password" : "new-password"}
              required
            />
          </label>

          {error && (
            <div className="auth-message auth-error" role="alert">
              {error}
            </div>
          )}

          {message && (
            <div className="auth-message auth-success" role="status">
              {message}
            </div>
          )}

          <button
            type="submit"
            className="btn btn-primary auth-submit"
            disabled={loading}
          >
            {loading
              ? "Please wait..."
              : isLogin
                ? "Log In"
                : "Create Account"}
          </button>
        </form>

        <div className="auth-switch">
          {isLogin ? "New to ServiceSphere?" : "Already have an account?"}

          <button
            type="button"
            onClick={() => {
              setIsLogin(!isLogin);
              setError("");
              setMessage("");
            }}
          >
            {isLogin ? "Create an account" : "Log in"}
          </button>
        </div>
      </div>
    </div>
  );
}

export default AuthModal;