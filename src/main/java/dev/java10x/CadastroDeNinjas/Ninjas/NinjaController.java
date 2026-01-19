package dev.java10x.CadastroDeNinjas.Ninjas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/ninjas")
public class NinjaController {

    @Autowired
    private NinjaService ninjaService;

    @GetMapping("/boasvindas")
    public String boasVindas() {
        return "Boas vindas ! Essa é a minha primeira mensagem nessa rota.";
    }

    // Criar ninja
    @PostMapping
    public ResponseEntity<NinjaModel> criarNinja(@RequestBody NinjaModel ninja) {
        NinjaModel novoNinja = ninjaService.criarNinja(ninja);
        return new ResponseEntity<>(novoNinja, HttpStatus.CREATED);
    }

    // Listar ninjas
    @GetMapping
    public ResponseEntity<List<NinjaModel>> listarNinjas() {
        List<NinjaModel> ninjas = ninjaService.listarNinjas();
        return new ResponseEntity<>(ninjas, HttpStatus.OK);
    }

    // Buscar ninja por ID
    @GetMapping("/{id}")
    public ResponseEntity<NinjaModel> buscarNinjaPorId(@PathVariable Long id) {
        Optional<NinjaModel> ninja = ninjaService.buscarNinjaPorId(id);
        if (ninja.isPresent()) {
            return new ResponseEntity<>(ninja.get(), HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Atualizar ninja
    @PutMapping("/{id}")
    public ResponseEntity<NinjaModel> atualizarNinja(@PathVariable Long id, @RequestBody NinjaModel ninja) {
        NinjaModel ninjaAtualizado = ninjaService.atualizarNinja(id, ninja);
        if (ninjaAtualizado != null) {
            return new ResponseEntity<>(ninjaAtualizado, HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }

    // Remover ninja
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarNinja(@PathVariable Long id) {
        boolean deletado = ninjaService.deletarNinja(id);
        if (deletado) {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
