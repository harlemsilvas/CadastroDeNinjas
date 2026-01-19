package dev.java10x.CadastroDeNinjas.Ninjas;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.Set;
import java.util.Objects;

import dev.java10x.CadastroDeNinjas.Missoes.MissoesRepository;
import dev.java10x.CadastroDeNinjas.Missoes.MissoesModel;

@Service
public class NinjaService {

    @Autowired
    private NinjaRepository ninjaRepository;

    @Autowired
    private MissoesRepository missoesRepository;

    // Criar ninja
    public NinjaModel criarNinja(NinjaModel ninja) {
        return ninjaRepository.save(ninja);
    }

    // Listar todos os ninjas
    public List<NinjaModel> listarNinjas() {
        return ninjaRepository.findAll();
    }

    // Buscar ninja por ID
    public Optional<NinjaModel> buscarNinjaPorId(Long id) {
        return ninjaRepository.findById(id);
    }

    // Atualizar ninja
    public NinjaModel atualizarNinja(Long id, NinjaModel ninjaAtualizado) {
        Optional<NinjaModel> ninjaExistente = ninjaRepository.findById(id);
        if (ninjaExistente.isPresent()) {
            NinjaModel ninja = ninjaExistente.get();
            ninja.setNome(ninjaAtualizado.getNome());
            ninja.setEmail(ninjaAtualizado.getEmail());
            ninja.setIdade(ninjaAtualizado.getIdade());
            ninja.setMissoes(ninjaAtualizado.getMissoes());
            return ninjaRepository.save(ninja);
        }
        return null; // Ou lançar exceção
    }

    // Novo: PATCH - atualizações parciais
    public NinjaModel patchNinja(Long id, Map<String, Object> patch) {
        Optional<NinjaModel> ninjaExistente = ninjaRepository.findById(id);
        if (ninjaExistente.isEmpty()) {
            throw new NoSuchElementException("Ninja não encontrado");
        }
        NinjaModel ninja = ninjaExistente.get();

        // Campos permitidos
        Set<String> permitidos = Set.of("nome", "email", "idade", "missoes");

        // Verifica campos desconhecidos (modo estrito)
        for (String chave : patch.keySet()) {
            if (!permitidos.contains(chave)) {
                throw new IllegalArgumentException("Campo(s) desconhecido(s): " + chave);
            }
        }

        // Aplicar campos presentes
        if (patch.containsKey("nome")) {
            Object v = patch.get("nome");
            if (v == null) {
                ninja.setNome(null);
            } else if (v instanceof String s) {
                ninja.setNome(s);
            } else {
                throw new IllegalArgumentException("Campo 'nome' deve ser string");
            }
        }

        if (patch.containsKey("email")) {
            Object v = patch.get("email");
            if (v == null) {
                ninja.setEmail(null);
            } else if (v instanceof String s) {
                ninja.setEmail(s);
            } else {
                throw new IllegalArgumentException("Campo 'email' deve ser string");
            }
        }

        if (patch.containsKey("idade")) {
            Object v = patch.get("idade");
            int idadeVal;
            Objects.requireNonNull(v, "Campo 'idade' não pode ser nulo");
            if (v instanceof Number n) {
                idadeVal = n.intValue();
            } else if (v instanceof String s) {
                try {
                    idadeVal = Integer.parseInt(s);
                } catch (NumberFormatException e) {
                    throw new IllegalArgumentException("Campo 'idade' deve ser um número inteiro");
                }
            } else {
                throw new IllegalArgumentException("Campo 'idade' deve ser um número inteiro");
            }
            if (idadeVal < 0) throw new IllegalArgumentException("Campo 'idade' deve ser >= 0");
            ninja.setIdade(idadeVal);
        }

        if (patch.containsKey("missoes")) {
            Object v = patch.get("missoes");
            if (v == null) {
                ninja.setMissoes(null);
            } else {
                Long missaoId;
                if (v instanceof Number n) {
                    missaoId = n.longValue();
                } else if (v instanceof Map<?, ?> map) {
                    Object idObj = map.get("id");
                    if (idObj instanceof Number num) {
                        missaoId = num.longValue();
                    } else if (idObj instanceof String s) {
                        try {
                            missaoId = Long.parseLong(s);
                        } catch (NumberFormatException e) {
                            throw new IllegalArgumentException("Campo 'missoes.id' deve ser numérico");
                        }
                    } else {
                        throw new IllegalArgumentException("Campo 'missoes' inválido - fornecer id da missão");
                    }
                } else {
                    throw new IllegalArgumentException("Campo 'missoes' inválido - fornecer id da missão");
                }

                Optional<MissoesModel> missaoOpt = missoesRepository.findById(missaoId);
                if (missaoOpt.isEmpty()) {
                    throw new IllegalArgumentException("Missão com id " + missaoId + " não encontrada");
                }
                ninja.setMissoes(missaoOpt.get());
            }
        }

        return ninjaRepository.save(ninja);
    }

    // Remover ninja
    public boolean deletarNinja(Long id) {
        if (ninjaRepository.existsById(id)) {
            ninjaRepository.deleteById(id);
            return true;
        }
        return false;
    }
}

