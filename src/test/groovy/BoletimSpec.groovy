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
}
