package dev.java10x.CadastroDeNinjas.Missoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/missoes")
public class MissoesController {

    @Autowired
    private MissoesService missoesService;

    // Criar missão
    @PostMapping
    public ResponseEntity<MissoesModel> criarMissao(@RequestBody MissoesModel missao) {
        MissoesModel novaMissao = missoesService.criarMissao(missao);
        return new ResponseEntity<>(novaMissao, HttpStatus.CREATED);
    }

    // Listar missões
    @GetMapping
    public ResponseEntity<List<MissoesModel>> listarMissoes() {
        List<MissoesModel> missoes = missoesService.listarMissoes();
        return new ResponseEntity<>(missoes, HttpStatus.OK);
    }

    // Buscar missão por ID
    @GetMapping("/{id}")
    public ResponseEntity<MissoesModel> buscarMissaoPorId(@PathVariable Long id) {
        Optional<MissoesModel> missao = missoesService.buscarMissaoPorId(id);
        if (missao.isPresent()) {
            return new ResponseEntity<>(missao.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Atualizar missão
    @PutMapping("/{id}")
    public ResponseEntity<MissoesModel> atualizarMissao(@PathVariable Long id, @RequestBody MissoesModel missao) {
        MissoesModel missaoAtualizada = missoesService.atualizarMissao(id, missao);
        if (missaoAtualizada != null) {
            return new ResponseEntity<>(missaoAtualizada, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Remover missão
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarMissao(@PathVariable Long id) {
        boolean deletado = missoesService.deletarMissao(id);
        if (deletado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}