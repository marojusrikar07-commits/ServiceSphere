import { useState } from "react";
import api from "../services/api";

function AuthModal({ mode, onClose, onSuccess }) {
  const [isLogin, setIsLogin] = useState(mode === "login");
  const [authMode, setAuthMode] = useState("auth");
  const [role, setRole] = useState("CUSTOMER");

  const [formData, setFormData] = useState({
    name: "",
    email: "",
    password: "",
    phone: "",
  });

  const [resetData, setResetData] = useState({
    email: "",
    token: "",
    newPassword: "",
    confirmPassword: "",
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

  const handleResetChange = (event) => {
    setResetData({
      ...resetData,
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
          email: formData.email.trim(),
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

  const handleForgotPassword = async (event) => {
    event.preventDefault();

    setError("");
    setMessage("");
    setLoading(true);

    try {
      const response = await api.post("/users/forgot-password", {
        email: resetData.email.trim(),
      });

      setMessage(
        response.data ||
          "If an account exists for this email, reset instructions are available."
      );
    } catch (err) {
      const responseData = err.response?.data;

      setError(
        responseData?.message ||
          "Unable to process your request. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleResetPassword = async (event) => {
    event.preventDefault();

    setError("");
    setMessage("");

    if (resetData.newPassword.length < 8) {
      setError("Password must contain at least 8 characters.");
      return;
    }

    if (resetData.newPassword !== resetData.confirmPassword) {
      setError("Passwords do not match.");
      return;
    }

    setLoading(true);

    try {
      const response = await api.post("/users/reset-password", {
        token: resetData.token.trim(),
        newPassword: resetData.newPassword,
      });

      setAuthMode("auth");
      setIsLogin(true);

      setFormData({
        name: "",
        email: resetData.email || formData.email,
        password: "",
        phone: "",
      });

      setResetData({
        email: "",
        token: "",
        newPassword: "",
        confirmPassword: "",
      });

      setMessage(
        response.data ||
          "Password reset successfully. You can now log in."
      );
    } catch (err) {
      const responseData = err.response?.data;

      setError(
        responseData?.message ||
          "Password reset failed. Check your token or request a new reset link."
      );
    } finally {
      setLoading(false);
    }
  };

  const handleBackToLogin = () => {
    setAuthMode("auth");
    setIsLogin(true);
    setError("");
    setMessage("");
  };

  const showError = error && (
    <div className="auth-message auth-error" role="alert">
      {error}
    </div>
  );

  const showMessage = message && (
    <div className="auth-message auth-success" role="status">
      {message}
    </div>
  );

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

        <h2>
          {authMode === "forgot"
            ? "Forgot your password?"
            : authMode === "reset"
              ? "Reset your password."
              : isLogin
                ? "Welcome back."
                : "Join our community."}
        </h2>

        <p className="auth-description">
          {authMode === "forgot"
            ? "Enter your registered email to request a reset token."
            : authMode === "reset"
              ? "Enter your reset token and choose a new password."
              : isLogin
                ? "Log in to manage your bookings and services."
                : "Create an account to get started with local services."}
        </p>

        {authMode === "forgot" && (
          <form
            onSubmit={handleForgotPassword}
            className="auth-form"
          >
            <label>
              Email address
              <input
                type="email"
                name="email"
                placeholder="you@example.com"
                value={resetData.email}
                onChange={handleResetChange}
                autoComplete="email"
                required
              />
            </label>

            {showError}
            {showMessage}

            <button
              type="submit"
              className="btn btn-primary auth-submit"
              disabled={loading}
            >
              {loading ? "Please wait..." : "Send Reset Link"}
            </button>

            <button
              type="button"
              className="auth-back-button"
              onClick={() => {
                setAuthMode("reset");
                setError("");
                setMessage("");
              }}
            >
              I already have a reset token
            </button>

            <button
              type="button"
              className="auth-back-button"
              onClick={handleBackToLogin}
            >
              ← Back to Login
            </button>
          </form>
        )}

        {authMode === "reset" && (
          <form
            onSubmit={handleResetPassword}
            className="auth-form"
          >
            <label>
              Email address
              <input
                type="email"
                name="email"
                placeholder="you@example.com"
                value={resetData.email}
                onChange={handleResetChange}
                autoComplete="email"
                required
              />
            </label>

            <label>
              Reset token
              <input
                type="text"
                name="token"
                placeholder="Paste your reset token"
                value={resetData.token}
                onChange={handleResetChange}
                required
              />
            </label>

            <label>
              New password
              <input
                type="password"
                name="newPassword"
                placeholder="Enter a new password"
                value={resetData.newPassword}
                onChange={handleResetChange}
                minLength={8}
                maxLength={100}
                autoComplete="new-password"
                required
              />
            </label>

            <label>
              Confirm new password
              <input
                type="password"
                name="confirmPassword"
                placeholder="Confirm your new password"
                value={resetData.confirmPassword}
                onChange={handleResetChange}
                minLength={8}
                maxLength={100}
                autoComplete="new-password"
                required
              />
            </label>

            {showError}
            {showMessage}

            <button
              type="submit"
              className="btn btn-primary auth-submit"
              disabled={loading}
            >
              {loading ? "Updating..." : "Update Password"}
            </button>

            <button
              type="button"
              className="auth-back-button"
              onClick={() => {
                setAuthMode("forgot");
                setError("");
                setMessage("");
              }}
            >
              Request another token
            </button>

            <button
              type="button"
              className="auth-back-button"
              onClick={handleBackToLogin}
            >
              ← Back to Login
            </button>
          </form>
        )}

        {authMode === "auth" && (
          <>
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
                    isLogin
                      ? "Enter your password"
                      : "Create a password"
                  }
                  value={formData.password}
                  onChange={handleChange}
                  minLength={isLogin ? undefined : 8}
                  maxLength={100}
                  autoComplete={
                    isLogin ? "current-password" : "new-password"
                  }
                  required
                />
              </label>

              {isLogin && (
                <div className="auth-forgot-row">
                  <button
                    type="button"
                    className="auth-forgot-button"
                    onClick={() => {
                      setAuthMode("forgot");

                      setResetData({
                        email: formData.email,
                        token: "",
                        newPassword: "",
                        confirmPassword: "",
                      });

                      setError("");
                      setMessage("");
                    }}
                  >
                    Forgot Password?
                  </button>
                </div>
              )}

              {showError}
              {showMessage}

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
              {isLogin
                ? "New to ServiceSphere?"
                : "Already have an account?"}

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
          </>
        )}
      </div>
    </div>
  );
}

export default AuthModal;