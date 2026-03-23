package com.example.api.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.example.api.Model.Pokemon;

@Repository
public interface PokemonRepository extends JpaRepository<Pokemon, Integer> {

    // --- NUEVO: Interfaz para que Spring sepa cómo leer el ranking ---
    public interface RegionVotosDTO {

        String getRegion();

        Long getTotalVotos();
    }

    // --- NUEVO: Consulta para sumar votos por cada región ---
    @Query("SELECT p.region as region, SUM(p.votes) as totalVotos "
            + "FROM Pokemon p GROUP BY p.region ORDER BY SUM(p.votes) DESC")
    List<RegionVotosDTO> findRankingByRegion();

    // Método 1: Ordenar por votos (de mayor a menor)
    List<Pokemon> findAllByOrderByVotesDesc();

    // Método 2: Ordenar por número de Pokedex (de menor a mayor)
    List<Pokemon> findAllByOrderByPokedexNumberAsc();

    // Método 3: Ordenar por región (alfabéticamente)
    List<Pokemon> findAllByOrderByRegionAsc();
}
