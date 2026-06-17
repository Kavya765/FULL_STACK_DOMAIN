import { Link } from "react-router-dom";

function MovieCard({ movie }) {
  return (
    <div style={{ border: "1px solid black", padding: "10px", margin: "10px" }}>
      <h2>{movie.title}</h2>

      <p>Genre: {movie.genre}</p>

      <p>Rating: {movie.rating}</p>

      <p>Year: {movie.year}</p>

      <Link to={`/movies/${movie.id}`}>
        View Details
      </Link>
    </div>
  );
}

export default MovieCard;