package br.com.ufg.odm.controller;

import br.com.ufg.odm.dto.EmprestimoDTO;
import br.com.ufg.odm.model.Emprestimo;
import br.com.ufg.odm.repository.EmprestimoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/emprestimo")
public class EmprestimoController {

    @Autowired
    private EmprestimoRepository emprestimoRepository;

    @GetMapping
    public ResponseEntity<List<EmprestimoDTO>> listarEmprestimos() {
        List<Emprestimo> emprestimos = emprestimoRepository.findAll();

        List<EmprestimoDTO> emprestimosDTO = emprestimos.stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());

        return ResponseEntity.ok(emprestimosDTO);
    }

    private EmprestimoDTO convertToDTO(Emprestimo emprestimo) {
        EmprestimoDTO dto = new EmprestimoDTO();
        dto.setId(emprestimo.getId());
        dto.setIdOrm(emprestimo.getIdOrm());
        dto.setNomeUsuario(emprestimo.getNomeUsuario());
        dto.setIdOrmExemplar(emprestimo.getIdOrmExemplar());
        dto.setDataEmprestimo(emprestimo.getDataEmprestimo());
        dto.setDataDevolucaoPrevista(emprestimo.getDataDevolucaoPrevista());
        dto.setDataDevolucao(emprestimo.getDataDevolucao());
        dto.setStatus(emprestimo.getStatus());
        dto.setRenovacoes(emprestimo.getRenovacoes());
        return dto;
    }
}
