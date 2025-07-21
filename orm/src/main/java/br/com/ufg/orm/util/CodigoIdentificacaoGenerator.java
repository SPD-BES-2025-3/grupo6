package br.com.ufg.orm.util;

import br.com.ufg.orm.repository.ExemplarRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Component
@RequiredArgsConstructor
public class CodigoIdentificacaoGenerator {

    private final ExemplarRepository exemplarRepository;
    private static final String PREFIXO = "SPD";
    private static final Random random = new Random();

    /**
     * Gera um código de identificação único para exemplar
     * Formato: EX-YYYYMMDDHHMM-NNNN
     * Onde:
     * - SPD: prefixo fixo
     * - YYYYMMDDHHMM: timestamp (ano, mês, dia, hora, minuto)
     * - NNNN: número aleatório de 4 dígitos
     *
     * @return código de identificação único
     */
    public String gerarCodigo() {
        String codigo;
        int tentativas = 0;
        final int maxTentativas = 100;

        do {
            codigo = gerarCodigoBase();
            tentativas++;

            if (tentativas >= maxTentativas) {
                throw new RuntimeException("Não foi possível gerar um código único após " + maxTentativas + " tentativas");
            }
        } while (exemplarRepository.existsByCodigoIdentificacao(codigo));

        return codigo;
    }

    private String gerarCodigoBase() {
        // Gera timestamp atual
        LocalDateTime agora = LocalDateTime.now();
        String timestamp = agora.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));

        // Gera número aleatório de 4 dígitos
        int numeroAleatorio = random.nextInt(9999) + 1;
        String numeroFormatado = String.format("%04d", numeroAleatorio);

        return String.format("%s-%s-%s", PREFIXO, timestamp, numeroFormatado);
    }
}
