$(document).ready(function() {
    loadMovies();

    // Adicionar filme
    $('#movieForm').submit(function(e) {
        e.preventDefault();
        const movie = {
            title: $('#title').val(),
            genre: $('#genre').val(),
            status: $('#status').val(),
            rating: parseInt($('#rating').val()),
            notes: $('#notes').val(),
            imageUrl: ''
        };

        $.ajax({
            url: '/api/movies',
            type: 'POST',
            contentType: 'application/json',
            data: JSON.stringify(movie),
            success: function() {
                resetForm();
                loadMovies();
            }
        });
    });

    // Aplicar filtros
    $('#filterTitle, #filterGenre, #filterStatus, #filterRating').on('input change', function() {
        applyFilters();
    });

    function loadMovies() {
        $.get('/api/movies', function(movies) {
            displayMovies(movies);
        });
    }

    function applyFilters() {
        const title = $('#filterTitle').val();
        const genre = $('#filterGenre').val();
        const status = $('#filterStatus').val();
        const rating = $('#filterRating').val();

        let url = '/api/movies/search?';
        if(title) url += `title=${title}&`;
        if(genre) url += `genre=${genre}&`;
        if(status) url += `status=${status}&`;
        if(rating) url += `rating=${rating}&`;

        $.get(url, function(movies) {
            displayMovies(movies);
        });
    }

    function displayMovies(movies) {
        const moviesList = $('#moviesList');
        moviesList.empty();

        if(movies.length === 0) {
            moviesList.html('<div class="col-12 text-center"><p>Nenhum filme encontrado</p></div>');
            return;
        }

        movies.forEach(movie => {
            const stars = '⭐'.repeat(movie.rating);
            const statusClass = movie.status === 'Assistido' ? 'success' :
                               (movie.status === 'Assistindo' ? 'warning' : 'secondary');

            const card = `
                <div class="col-md-4 mb-4">
                    <div class="card h-100">
                        <div class="card-body">
                            <h5 class="card-title">${movie.title}</h5>
                            <h6 class="card-subtitle mb-2 text-muted">${movie.genre}</h6>
                            <p class="card-text">${movie.notes || 'Sem notas'}</p>
                            <div class="mb-2">
                                <span class="badge bg-${statusClass}">${movie.status}</span>
                                ${movie.rating > 0 ? `<span class="badge bg-info ms-2">${stars}</span>` : ''}
                            </div>
                            <div class="btn-group" role="group">
                                <button class="btn btn-sm btn-outline-primary" onclick="updateStatus(${movie.id}, 'Assistindo')">Assistindo</button>
                                <button class="btn btn-sm btn-outline-success" onclick="updateStatus(${movie.id}, 'Assistido')">Assistido</button>
                                <button class="btn btn-sm btn-outline-danger" onclick="deleteMovie(${movie.id})">Excluir</button>
                            </div>
                        </div>
                    </div>
                </div>
            `;
            moviesList.append(card);
        });
    }

    function updateStatus(id, newStatus) {
        $.get(`/api/movies/${id}`, function(movie) {
            movie.status = newStatus;
            $.ajax({
                url: `/api/movies/${id}`,
                type: 'PUT',
                contentType: 'application/json',
                data: JSON.stringify(movie),
                success: function() {
                    loadMovies();
                }
            });
        });
    }

    function deleteMovie(id) {
        if(confirm('Tem certeza que deseja excluir este filme?')) {
            $.ajax({
                url: `/api/movies/${id}`,
                type: 'DELETE',
                success: function() {
                    loadMovies();
                }
            });
        }
    }

    function resetForm() {
        $('#title').val('');
        $('#genre').val('');
        $('#status').val('Pendente');
        $('#rating').val('0');
        $('#notes').val('');
    }

    window.updateStatus = updateStatus;
    window.deleteMovie = deleteMovie;
});