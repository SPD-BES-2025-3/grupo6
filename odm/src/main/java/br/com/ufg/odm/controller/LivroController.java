package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.LivroDTO;
import br.com.ufg.odm.model.Livro;
import br.com.ufg.odm.repository.LivroRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/livro")
public class LivroController {

    @Autowired
    private LivroRepository livroRepository;

    @GetMapping
    public ResponseEntity<List<LivroDTO>> listarLivros() {
        List<Livro> livros = livroRepository.findAll();

        List<LivroDTO> livrosDTO = livros.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(livrosDTO);
    }

    private LivroDTO convertToDTO(Livro livro) {
        LivroDTO dto = new LivroDTO();
        dto.setId(livro.getId());
        dto.setIdOrm(livro.getIdOrm());
        dto.setNome(livro.getNome());
        dto.setAnoLancamento(livro.getAnoLancamento());
        dto.setAutor(livro.getAutor());
        dto.setEditora(livro.getEditora());
        return dto;
    }
}
