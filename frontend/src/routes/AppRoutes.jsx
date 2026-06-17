import { Routes, Route } from "react-router-dom";
import ProtectedRoute from "../components/ProtectedRoute";
import Login from "../pages/Login";
import Dashboard from "../pages/Dashboard";
import Movies from "../pages/Movies";
import MovieDetails from "../pages/MovieDetails";


function AppRoutes() {
  return (
    <Routes>
      <Route path="/" element={<Login />} />
     <Route
  path="/dashboard"
  element={
    <ProtectedRoute>
      <Dashboard />
    </ProtectedRoute>
  }
/>
<Route
  path="/movies"
  element={
    <ProtectedRoute>
      <Movies />
    </ProtectedRoute>
  }
/>
      <Route
  path="/movies/:id"
  element={
    <ProtectedRoute>
      <MovieDetails />
    </ProtectedRoute>
  }
/>
    </Routes>
  );
}

export default AppRoutes;