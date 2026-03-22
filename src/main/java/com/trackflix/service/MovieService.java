package com.trackflix.service;

import com.trackflix.dto.MovieDTO;
import com.trackflix.model.Movie;
import com.trackflix.repository.MovieRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class MovieService {

    @Autowired
    private MovieRepository movieRepository;

    public List<MovieDTO> getAllMovies() {
        return movieRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public MovieDTO getMovieById(Long id) {
        return movieRepository.findById(id)
                .map(this::convertToDTO)
                .orElse(null);
    }

    public MovieDTO createMovie(MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        movie.setId(null);
        Movie savedMovie = movieRepository.save(movie);
        return convertToDTO(savedMovie);
    }

    public MovieDTO updateMovie(Long id, MovieDTO movieDTO) {
        Movie movie = convertToEntity(movieDTO);
        movie.setId(id);
        Movie updatedMovie = movieRepository.save(movie);
        return convertToDTO(updatedMovie);
    }

    public void deleteMovie(Long id) {
        movieRepository.deleteById(id);
    }

    public List<MovieDTO> searchMovies(String title, String genre, Integer rating, String status) {
        return movieRepository.search(title, genre, rating, status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private MovieDTO convertToDTO(Movie movie) {
        return new MovieDTO(
                movie.getId(),
                movie.getTitle(),
                movie.getGenre(),
                movie.getStatus(),
                movie.getRating(),
                movie.getNotes(),
                movie.getImageUrl()
        );
    }

    private Movie convertToEntity(MovieDTO movieDTO) {
        return new Movie(
                movieDTO.getId(),
                movieDTO.getTitle(),
                movieDTO.getGenre(),
                movieDTO.getStatus(),
                movieDTO.getRating(),
                movieDTO.getNotes(),
                movieDTO.getImageUrl()
        );
    }
}