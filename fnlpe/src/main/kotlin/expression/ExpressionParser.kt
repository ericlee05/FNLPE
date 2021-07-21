package expression

import java.lang.StringBuilder

class ExpressionParser(private val expression: String) {
    private var curPos: Int = 0
    private var curChar: Char? = null

    private fun nextChar(): Char? {
        if(expression.length <= curPos) return null

        val newChar = expression[curPos++]
        curChar = newChar
        return newChar
    }

    fun nextToken(): ExpressToken? {
        val builder: StringBuilder = StringBuilder()
        nextChar() // first char

        while(curChar != null){
            if(curChar == '$' || curChar == '%'){
                val identify = if(curChar == '$'){ ExpressTokenType.FDATA } else { ExpressTokenType.MATCHER }
                while(nextChar() != null){
                    if(curChar in 'A'..'Z' || curChar in 'a'..'z' || curChar == '?'){
                        builder.append(curChar)
                    }else {
                        curPos--
                        break
                    }
                }
                return ExpressToken(identify, builder.toString())
            }

            if(nextChar() == null) break
        }

        return null
    }


}