package dev.java10x.CadastroDeNinjas.Config;

import dev.java10x.CadastroDeNinjas.Missoes.MissoesModel;
import dev.java10x.CadastroDeNinjas.Missoes.MissoesRepository;
import dev.java10x.CadastroDeNinjas.Ninjas.NinjaModel;
import dev.java10x.CadastroDeNinjas.Ninjas.NinjaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

@Component
public class DataInitializer implements CommandLineRunner {

    private final NinjaRepository ninjaRepository;
    private final MissoesRepository missoesRepository;
    private final Random random = new Random();

    public DataInitializer(NinjaRepository ninjaRepository, MissoesRepository missoesRepository) {
        this.ninjaRepository = ninjaRepository;
        this.missoesRepository = missoesRepository;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        // Limpa dados antigos (útil em DB em memória)
        ninjaRepository.deleteAll();
        missoesRepository.deleteAll();

        // Criar algumas missões
        MissoesModel m1 = new MissoesModel(null, "Praticar Taijutsu", "Facil", null);
        MissoesModel m2 = new MissoesModel(null, "Recuperar Pergaminho", "Media", null);
        MissoesModel m3 = new MissoesModel(null, "Proteger a Vila", "Dificil", null);

        List<MissoesModel> missoes = missoesRepository.saveAll(Arrays.asList(m1, m2, m3));

        // Nomes inspirados em Naruto
        List<String> nomes = Arrays.asList(
                "Naruto Uzumaki",
                "Sasuke Uchiha",
                "Sakura Haruno",
                "Kakashi Hatake",
                "Shikamaru Nara",
                "Hinata Hyuga",
                "Rock Lee",
                "Neji Hyuga",
                "Gaara",
                "Itachi Uchiha"
        );

        for (int i = 0; i < nomes.size(); i++) {
            String nome = nomes.get(i);
            String email = nome.toLowerCase().replaceAll("[^a-z]","") + "@naruto.test";
            int idade = 13 + random.nextInt(15); // idade entre 13 e 27
            // distribuir missões ciclicamente
            MissoesModel missao = missoes.get(i % missoes.size());

            NinjaModel ninja = new NinjaModel(null, nome, email, idade, missao);
            ninjaRepository.save(ninja);
        }

        System.out.println("DataInitializer: inserted " + ninjaRepository.count() + " ninjas and " + missoes.size() + " missoes.");
    }
}
