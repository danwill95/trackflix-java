package com.trackflix.repository;

import com.trackflix.model.Movie;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Repository;
import org.springframework.core.io.ClassPathResource;

import java.io.*;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class MovieRepository {
    private List<Movie> movies = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final String DATA_FILE = "movies.json";

    public MovieRepository() {
        loadMoviesFromFile();
    }

    private void loadMoviesFromFile() {
        try {
            ClassPathResource resource = new ClassPathResource(DATA_FILE);
            File file = resource.getFile();
            if (file.exists()) {
                movies = objectMapper.readValue(file, new TypeReference<List<Movie>>() {});
                // Atualiza o gerador de IDs
                if (!movies.isEmpty()) {
                    long maxId = movies.stream().mapToLong(Movie::getId).max().orElse(0);
                    idGenerator.set(maxId + 1);
                }
            }
        } catch (IOException e) {
            System.err.println("Erro ao carregar dados: " + e.getMessage());
            movies = new ArrayList<>();
        }
    }

    private void saveMoviesToFile() {
        try {
            ClassPathResource resource = new ClassPathResource(DATA_FILE);
            File file = resource.getFile();
            objectMapper.writerWithDefaultPrettyPrinter().writeValue(file, movies);
        } catch (IOException e) {
            System.err.println("Erro ao salvar dados: " + e.getMessage());
        }
    }

    public List<Movie> findAll() {
        return new ArrayList<>(movies);
    }

    public Optional<Movie> findById(Long id) {
        return movies.stream().filter(movie -> movie.getId().equals(id)).findFirst();
    }

    public Movie save(Movie movie) {
        if (movie.getId() == null) {
            movie.setId(idGenerator.getAndIncrement());
            movies.add(movie);
        } else {
            deleteById(movie.getId());
            movies.add(movie);
        }
        saveMoviesToFile();
        return movie;
    }

    public void deleteById(Long id) {
        movies.removeIf(movie -> movie.getId().equals(id));
        saveMoviesToFile();
    }

    public List<Movie> search(String title, String genre, Integer rating, String status) {
        return movies.stream()
                .filter(movie -> title == null || movie.getTitle().toLowerCase().contains(title.toLowerCase()))
                .filter(movie -> genre == null || movie.getGenre().toLowerCase().contains(genre.toLowerCase()))
                .filter(movie -> rating == null || movie.getRating().equals(rating))
                .filter(movie -> status == null || movie.getStatus().equals(status))
                .collect(Collectors.toList());
    }
}