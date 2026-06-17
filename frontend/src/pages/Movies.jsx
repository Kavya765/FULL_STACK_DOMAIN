import { movies } from "../data/movies";
import MovieCard from "../components/MovieCard";
import Navbar from "../components/Navbar";

function Movies() {
  return (
    <>
      <Navbar />

      <div>
        <h1>Movie Catalog</h1>

        {movies.map((movie) => (
          <MovieCard
            key={movie.id}
            movie={movie}
          />
        ))}
      </div>
    </>
  );
}

export default Movies;