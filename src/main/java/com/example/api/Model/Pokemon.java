package com.example.api.Model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "pokemons")
public class Pokemon {

    @Id
    private Integer pokedexNumber; // El número de la pokedex como ID único

    private String name;
    private String region;

    @Column(columnDefinition = "int default 0")
    private Integer votes;

    public Integer getPokedexNumber() {
        return pokedexNumber;
    }

    public void setPokedexNumber(Integer pokedexNumber) {
        this.pokedexNumber = pokedexNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRegion() {
        return region;
    }

    public void setRegion(String region) {
        this.region = region;
    }

    public Integer getVotes() {
        return votes;
    }

    public void setVotes(Integer votes) {
        this.votes = votes;
    }

    // Constructores
    public Pokemon() {
    }

    public Pokemon(Integer pokedexNumber, String name, String region) {
        this.pokedexNumber = pokedexNumber;
        this.name = name;
        this.region = region;
        this.votes = 0;
    }

    // Getters y Setters...
}
