import { useParams } from "react-router-dom";
import { movies } from "../data/movies";
import Navbar from "../components/Navbar";

function MovieDetails() {
  const { id } = useParams();

  const movie = movies.find(
    (movie) => movie.id === Number(id)
  );

  if (!movie) {
    return <h2>Movie Not Found</h2>;
  }

  return (
    <>
      <Navbar />

      <h1>{movie.title}</h1>

      <p>Genre: {movie.genre}</p>

      <p>Rating: {movie.rating}</p>

      <p>Year: {movie.year}</p>
    </>
  );
}

export default MovieDetails;