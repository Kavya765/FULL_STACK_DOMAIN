import { useState } from "react";
import { useNavigate } from "react-router-dom";

function Login() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");
const navigate = useNavigate();

const handleLogin = () => {
  localStorage.setItem("token", "mock-jwt-token");
  navigate("/dashboard");
};
  return (
    <div style={{ padding: "20px" }}>
      <h1>Login</h1>

      <div>
        <label>Email</label>
        <br />
        <input
          type="email"
          placeholder="Enter Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>

      <br />

      <div>
        <label>Password</label>
        <br />
        <input
          type="password"
          placeholder="Enter Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
      </div>

      <br />

      <button onClick={handleLogin}>
  Login
</button>
    </div>
  );
}

export default Login;