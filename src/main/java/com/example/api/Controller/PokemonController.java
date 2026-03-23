package com.example.api.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.api.Model.Pokemon;
import com.example.api.Repository.PokemonRepository;

@RestController
@RequestMapping("/api/pokemons")
@CrossOrigin(origins = "*", allowedHeaders = "*", methods = {RequestMethod.GET, RequestMethod.POST, RequestMethod.PATCH, RequestMethod.DELETE, RequestMethod.PUT, RequestMethod.OPTIONS})
public class PokemonController {

    @Autowired
    private PokemonRepository repository;

    @GetMapping
    public List<Pokemon> getPokemons(@RequestParam(required = false, defaultValue = "votes") String sort) {

        // Según lo que llegue en la URL, llamamos a un método u otro
        switch (sort) {
            case "pokedex":
                return repository.findAllByOrderByPokedexNumberAsc();
            case "region":
                return repository.findAllByOrderByRegionAsc();
            case "votes":
            default:
                return repository.findAllByOrderByVotesDesc();
        }
    }

    // 1. Guardar un nuevo Pokemon (cuando lo traes de la PokeAPI)
    @PostMapping
    public Pokemon savePokemon(@RequestBody Pokemon pokemon) {
        return repository.save(pokemon);
    }

    // 2. Obtener todos para ver el ranking
    // @GetMapping
    // public List<Pokemon> getAll() {
    //     return repository.findAll();
    // }
    // 3. ¡EL MÁS IMPORTANTE! Sumar un voto
    @PatchMapping("/{id}/vote")
    public ResponseEntity<Pokemon> addVote(@PathVariable Integer id) {
        return repository.findById(id)
                .map(pokemon -> {
                    pokemon.setVotes(pokemon.getVotes() + 1); // Sumamos el voto
                    return ResponseEntity.ok(repository.save(pokemon)); // Guardamos el cambio
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 4. Actualizar datos de un Pokemon (PATCH general)
    @PatchMapping("/{id}")
    public ResponseEntity<Pokemon> updatePokemon(@PathVariable Integer id, @RequestBody Pokemon details) {
        return repository.findById(id)
                .map(pokemon -> {
                    if (details.getName() != null) {
                        pokemon.setName(details.getName());
                    }
                    if (details.getRegion() != null) {
                        pokemon.setRegion(details.getRegion());
                    }
                    if (details.getPokedexNumber() != null) {
                        pokemon.setPokedexNumber(details.getPokedexNumber());
                    }
                    if (details.getVotes() != null) {
                        pokemon.setVotes(details.getVotes());
                    }

                    return ResponseEntity.ok(repository.save(pokemon));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    // 5. Borrar un Pokemon
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePokemon(@PathVariable Integer id) {
        return repository.findById(id)
                .map(pokemon -> {
                    repository.delete(pokemon);
                    return ResponseEntity.noContent().<Void>build(); // 204 No Content
                })
                .orElse(ResponseEntity.notFound().build());
    }
    // Nuevo endpoint para la gráfica de regiones
    @GetMapping("/rankingByRegion")
    public ResponseEntity<List<PokemonRepository.RegionVotosDTO>> getRankingByRegion() {
        // Llamamos al método que creamos en el Repository
        List<PokemonRepository.RegionVotosDTO> ranking = repository.findRankingByRegion();
        return ResponseEntity.ok(ranking);
    }
}
