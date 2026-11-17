import spock.lang.Specification
import spock.lang.Unroll

class BoletimSpec extends Specification {

    def boletim = new Boletim()

    @Unroll
    def "Bug 1: deve calcular a média de #aluno considerando todas as notas incluindo a primeira (#notas)"() {
        when: "calculamos a média das notas"
        def media = boletim.calcularMedia(notas)
        def mediaFormatada = String.format(Locale.US, "%.2f", media)

        then: "a primeira nota é incluída no cálculo e a média formatada corresponde ao esperado"
        Math.abs(media - mediaEsperada) < 0.01
        mediaFormatada == mediaFormatadaEsperada

        where:
        aluno              | notas             | mediaEsperada | mediaFormatadaEsperada
        "Ana"              | [8.0, 7.5, 9.0]   | 8.17          | "8.17"
        "Bruno"            | [5.0, 6.0, 7.0]   | 6.00          | "6.00"
        "Carla"            | [10.0, 4.0, 7.0]  | 7.00          | "7.00"
        "Diego"            | [3.0, 9.0, 9.0]   | 7.00          | "7.00"
        "Apenas 1ª nota"   | [9.0, 0.0, 0.0]   | 3.00          | "3.00"
        "Notas zeradas"    | [0.0, 0.0, 0.0]   | 0.00          | "0.00"
        "Notas máximas"    | [10.0, 10.0, 10.0]| 10.00         | "10.00"
        "Notas decimais"   | [6.5, 7.5, 8.5]   | 7.50          | "7.50"
    }

    @Unroll
    def "Bug 2: deve classificar média #media (#cenario) como '#situacaoEsperada' com corte de recuperação em 5.0"() {
        expect: "a situação segue a regra de recuperação >= 5.0 e reprovação < 5.0"
        boletim.situacao(media, 7.0, 5.0) == situacaoEsperada

        where:
        cenario                  | media | situacaoEsperada
        "Bruno (problema)"       | 6.00  | "Recuperação"
        "Fronteira exata (5.0)"  | 5.00  | "Recuperação"
        "Entre 5.0 e 6.5"        | 5.50  | "Recuperação"
        "Abaixo do antigo 6.5"   | 6.49  | "Recuperação"
        "Abaixo de 5.0"          | 4.99  | "Reprovado"
        "Nota zero"              | 0.00  | "Reprovado"
    }

    def "Bug 2: saída do boletim deve classificar Bruno (média 6.00) como Recuperação usando a regra do script"() {
        given: "um buffer para capturar a saída padrão do script"
        def originalOut = System.out
        def buffer = new ByteArrayOutputStream()
        System.setOut(new PrintStream(buffer, true, "UTF-8"))

        when: "executamos o script Boletim"
        try {
            boletim.run()
        } finally {
            System.setOut(originalOut)
        }
        def saida = buffer.toString("UTF-8")

        then: "Bruno é exibido com média 6.00 e situação Recuperação"
        saida.contains("Bruno: média 6.00 - Recuperação")
    }
}
