package com.sabado.jogos.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.sabado.jogos.model.Jogos;

public interface JogosRepository extends JpaRepository <Jogos, Long>{ 

}
