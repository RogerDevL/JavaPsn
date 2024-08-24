package com.sabado.jogos.controller;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import com.sabado.jogos.model.Jogos;
import com.sabado.jogos.repo.JogosRepository;

@Controller
@RequestMapping("/jogos")
public class JogosController {

	@Autowired
	private JogosRepository jogosRepo;

	private final String JOGOS_DIR = System.getProperty("user.dir") + "/src/main/resources/static/";

	@GetMapping("/")
	public String inicio(Model model) { // model -> org.springframe..
		model.addAttribute("jogos", jogosRepo.findAll());
		return "index"; // SELECT * FROM jogos;

	}

	@GetMapping("/form")
	public String form(Model model) {
		model.addAttribute("jogo", new Jogos());
		return "form";
	}
	
	@GetMapping("/form/{id}")
	public String form(@PathVariable("id") Long id, Model model) {
		Optional<Jogos> jogos = jogosRepo.findById(id);
		if (jogos.isPresent()) { // vendo se o filme existe
			model.addAttribute("jogo", jogos.get()); // se existe, get nele, mostra suas informacoes
		}else {
			model.addAttribute("jogo", new Jogos()); // caso nao existe, crie um novo filme e volta para o formulario
		}
		return "form"; //se existe o filme, retorna para o formulario para editar
	}
	
	
	
	
	@PostMapping("/add")
	public String addJogo(@RequestParam("id") Optional<Long> id,
			
			@RequestParam("nome") String nome, 
			@RequestParam("tema") String tema,
			@RequestParam("preco") String preco,
			@RequestParam("imagem") MultipartFile imagem) {
		
		Jogos jogos;
		
		
		if(id.isPresent()) { // verificando se existe o filme com o ID passado
			jogos = jogosRepo.findById(id.get()).orElse(new Jogos()); // se ele existe, busca o filme pelo id e atualiza suas informaçoes, caso ao contrario crie um filme
		} else {
			jogos = new Jogos(); // caso ao contrario novamente, criando filme em vazio
		}
		
		jogos.setNome(nome);
		jogos.setTema(tema);
		jogos.setPreco(preco);
		
		jogosRepo.save(jogos);
		
		if(!imagem.isEmpty()) {
			try {
				
				String fileName = "jogo" + jogos.getId() + "_" + imagem.getOriginalFilename(); 
				Path path = Paths.get(JOGOS_DIR + fileName); // capturando caminho completo
				Files.write(path, imagem.getBytes()); // escrevendo a imagem
				jogos.setImagem("/" +  fileName); // Adicionar o caminho para acessar a imagem
				
				jogosRepo.save(jogos); // salvar a imagem
				
				
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		
		return "redirect:/jogos/";
		
	}
			
			
			
			
	// /login
	@GetMapping("/login")
	public String login(Model model) {
		model.addAttribute("jogos", new Jogos());
		return "admin";
	}
	
	// /logar
	@PostMapping("/admin")
	public String admin(@RequestParam("user") String user, @RequestParam("senha") String senha, Model model) {

		Jogos jogos = new Jogos();
		jogos.setUser(user);
		jogos.setSenha(senha);

		System.out.println(user);
		System.out.println(senha);
		
		try {
			
			if (user.equals("admin") || senha.equals("admin")) {
				System.out.println("entrou");
				
				model.addAttribute("jogo", new Jogos());
				model.addAttribute("jogos", jogosRepo.findAll());
				return "form";	

			}
			else {
				System.err.println("SENHA INCORRETA!");
				return "admin";
			}

			
		} catch (Exception e) {
			e.printStackTrace();
		}

		return "form";

	}
	
	@GetMapping("/delete/{id}")
	public String deleteJogo(@PathVariable("id")Long id) {
		Optional<Jogos> jogos = jogosRepo.findById(id); // pegando o filme no banco de dadis pelo id
		
		if(jogos.isPresent()) { // verifica se existe o filme com o id passado
			Jogos jogoParaDeletar = jogos.get(); // criando uma variavel de deletar em filme
			String imagePath = JOGOS_DIR + jogoParaDeletar.getImagem(); // passando caminho da imagem
			try {
				Files.deleteIfExists(Paths.get(imagePath)); // se existir, deletar imagem
			} catch (Exception e) {
				e.printStackTrace();
			}
			
			jogosRepo.deleteById(id); // deletando filme do banco de dados
		}
		return "redirect:/jogos/";
	}

	
	

}
