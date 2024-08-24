package com.sabado.jogos.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
public class Jogos {

	@Id
	@GeneratedValue
	private Long id;
	private String nome;
	private String tema;
	private String preco;
	private String imagem;
	private String senha;
	private String user;

}
