package com.trackflix.controller;

import com.trackflix.dto.MovieDTO;
import com.trackflix.service.MovieService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class MovieController {

    @Autowired
    private MovieService movieService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("movies", movieService.getAllMovies());
        return "index";
    }

    @GetMapping("/api/movies")
    @ResponseBody
    public List<MovieDTO> getAllMovies() {
        return movieService.getAllMovies();
    }

    @GetMapping("/api/movies/{id}")
    @ResponseBody
    public MovieDTO getMovie(@PathVariable Long id) {
        return movieService.getMovieById(id);
    }

    @PostMapping("/api/movies")
    @ResponseBody
    public MovieDTO createMovie(@RequestBody MovieDTO movieDTO) {
        return movieService.createMovie(movieDTO);
    }

    @PutMapping("/api/movies/{id}")
    @ResponseBody
    public MovieDTO updateMovie(@PathVariable Long id, @RequestBody MovieDTO movieDTO) {
        return movieService.updateMovie(id, movieDTO);
    }

    @DeleteMapping("/api/movies/{id}")
    @ResponseBody
    public void deleteMovie(@PathVariable Long id) {
        movieService.deleteMovie(id);
    }

    @GetMapping("/api/movies/search")
    @ResponseBody
    public List<MovieDTO> searchMovies(
            @RequestParam(required = false) String title,
            @RequestParam(required = false) String genre,
            @RequestParam(required = false) Integer rating,
            @RequestParam(required = false) String status) {
        return movieService.searchMovies(title, genre, rating, status);
    }
}