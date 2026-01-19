package dev.java10x.CadastroDeNinjas.Missoes;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class MissoesService {

    @Autowired
    private MissoesRepository missoesRepository;

    // Criar missão
    public MissoesModel criarMissao(MissoesModel missao) {
        return missoesRepository.save(missao);
    }

    // Listar todas as missões
    public List<MissoesModel> listarMissoes() {
        return missoesRepository.findAll();
    }

    // Buscar missão por ID
    public Optional<MissoesModel> buscarMissaoPorId(Long id) {
        return missoesRepository.findById(id);
    }

    // Atualizar missão
    public MissoesModel atualizarMissao(Long id, MissoesModel missaoAtualizada) {
        Optional<MissoesModel> missaoExistente = missoesRepository.findById(id);
        if (missaoExistente.isPresent()) {
            MissoesModel missao = missaoExistente.get();
            missao.setNome(missaoAtualizada.getNome());
            missao.setDificuldade(missaoAtualizada.getDificuldade());
            return missoesRepository.save(missao);
        }
        return null; // Ou lançar exceção
    }

    // Remover missão
    public boolean deletarMissao(Long id) {
        if (missoesRepository.existsById(id)) {
            missoesRepository.deleteById(id);
            return true;
        }
        return false;
    }
}