package dev.java10x.CadastroDeNinjas.Ninjas;

import dev.java10x.CadastroDeNinjas.Missoes.MissoesRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class NinjaServiceTest {

    @Mock
    private NinjaRepository ninjaRepository;

    @Mock
    private MissoesRepository missoesRepository;

    private NinjaService ninjaService;

    @BeforeEach
    public void setup() throws Exception {
        ninjaService = new NinjaService();
        // inject mocks into private fields using reflection
        Field ninjaRepoField = NinjaService.class.getDeclaredField("ninjaRepository");
        ninjaRepoField.setAccessible(true);
        ninjaRepoField.set(ninjaService, ninjaRepository);

        Field missoesRepoField = NinjaService.class.getDeclaredField("missoesRepository");
        missoesRepoField.setAccessible(true);
        missoesRepoField.set(ninjaService, missoesRepository);
    }

    @Test
    public void testPatchNinja_updateNameAndAge() {
        NinjaModel existing = new NinjaModel(1L, "Naruto", "naruto@konoha", 17, null);
        when(ninjaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(ninjaRepository.save(any(NinjaModel.class))).thenAnswer(inv -> inv.getArgument(0));

        Map<String, Object> patch = Map.of("nome", "Kakashi", "idade", 35);

        NinjaModel result = ninjaService.patchNinja(1L, patch);

        assertEquals("Kakashi", result.getNome());
        assertEquals(35, result.getIdade());
        verify(ninjaRepository).save(result);
    }

    @Test
    public void testPatchNinja_unknownField_shouldThrow() {
        NinjaModel existing = new NinjaModel(1L, "Naruto", "naruto@konoha", 17, null);
        when(ninjaRepository.findById(1L)).thenReturn(Optional.of(existing));

        Map<String, Object> patch = Map.of("apelido", "Nino");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ninjaService.patchNinja(1L, patch));
        assertTrue(ex.getMessage().contains("Campo(s) desconhecido(s)"));
    }

    @Test
    public void testPatchNinja_missaoNotFound_shouldThrow() {
        NinjaModel existing = new NinjaModel(1L, "Naruto", "naruto@konoha", 17, null);
        when(ninjaRepository.findById(1L)).thenReturn(Optional.of(existing));
        when(missoesRepository.findById(99L)).thenReturn(Optional.empty());

        Map<String, Object> patch = Map.of("missoes", 99);

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ninjaService.patchNinja(1L, patch));
        assertTrue(ex.getMessage().contains("Missão com id"));
    }

    @Test
    public void testPatchNinja_ninjaNotFound_shouldThrow() {
        when(ninjaRepository.findById(1L)).thenReturn(Optional.empty());

        Map<String, Object> patch = Map.of("nome", "Kakashi");

        NoSuchElementException ex = assertThrows(NoSuchElementException.class, () -> ninjaService.patchNinja(1L, patch));
        assertTrue(ex.getMessage().contains("Ninja não encontrado"));
    }

    @Test
    public void testPatchNinja_invalidIdadeType_shouldThrow() {
        NinjaModel existing = new NinjaModel(1L, "Naruto", "naruto@konoha", 17, null);
        when(ninjaRepository.findById(1L)).thenReturn(Optional.of(existing));

        Map<String, Object> patch = Map.of("idade", "abc");

        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () -> ninjaService.patchNinja(1L, patch));
        assertTrue(ex.getMessage().contains("Campo 'idade' deve ser um número inteiro"));
    }
}
