import java.io.File
import java.nio.charset.Charset

fun main(args:Array<String>){
    val text = File("test.txt").readText(Charset.forName("utf8"))

    val expression = "\$N??%JX" //"\$N??\$VV"
    val fnlpe = Fnlpe(expression)
    val result = fnlpe.getMatches(text)

    println("${result.size}개의 결과 :")
    result.forEach {
        println(" * ${it.map { tok -> tok.morph }.joinToString(", ")}")
    }
}