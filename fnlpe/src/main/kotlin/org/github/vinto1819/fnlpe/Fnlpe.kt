package org.github.vinto1819.fnlpe

import org.github.vinto1819.fnlpe.expression.ExpressToken
import org.github.vinto1819.fnlpe.expression.ExpressTokenType
import org.github.vinto1819.fnlpe.expression.ExpressionParser
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL
import kr.co.shineware.nlp.komoran.core.Komoran
import kr.co.shineware.nlp.komoran.model.Token
import java.util.*
import kotlin.collections.ArrayList

public class Fnlpe(private val exp: String, private val komoranModel: DEFAULT_MODEL = DEFAULT_MODEL.LIGHT) {
    private val nlpEngine = Komoran(komoranModel) // Komoran NLP Engine
    private val expEngine = ExpressionParser(exp) // FNLPE Parser

    private val expTokens = getExpTokens() // org.github.vinto1819.fnlpe.expression Tokens

    private fun getExpTokens(): Array<ExpressToken> {
        var currentToken = expEngine.nextToken()
        val expTokens: ArrayList<ExpressToken> = arrayListOf()
        while(currentToken != null){
            expTokens.add(currentToken)

            currentToken = expEngine.nextToken()
        }

        return expTokens.toTypedArray()
    }

    // 두 품사가 같은가?
    private fun posEquals(pos: String, requirePos: String) : Boolean {
        if(pos.length != requirePos.length) return false

        for(i in pos.indices){
            if(pos.get(i) != requirePos.get(i) && requirePos.get(i) != '?') return false
        }
        return true
    }

    // 식에 일치하는 결과 반환
    public fun getMatches(text: String): Array<Array<Token>> {
        val processedTxt = text // 개행문자 전처리
            .replace("\r\n", " ")
            .replace("\n", " ")
            .replace("  ", " ")

        val nlpTokens = nlpEngine.analyze(processedTxt).tokenList // 파싱 된 자연어 토큰
        val result: MutableList<Array<Token>> = mutableListOf() // 결과값 저장 리스트

        val formatQueue: Queue<Token> = LinkedList() // 일치하는 토큰 임시 저장용 Queue
        var expPos = 0 // 표현식 토큰 포인터

        for(i in nlpTokens.indices){ // 자연어 토큰 탐색
            if(expPos == expTokens.size){ // 일치 토큰들 발견 시
                val tokens: MutableList<Token> = mutableListOf()
                var expCnt = 0
                while(formatQueue.isNotEmpty()){ // Queue 전부 탐색
                    val tok = formatQueue.poll()
                    if(expTokens[expCnt].type == ExpressTokenType.FDATA) // $ 구문만 반환
                        tokens.add(tok)

                    expCnt++
                }

                result.add(tokens.toTypedArray()) // 결과에 토큰들 추가

                expPos = 0 // 토큰 포인터 초기화 후 건너뜀
                continue
            }

            if(posEquals(nlpTokens[i].pos, expTokens[expPos].value)){ // 두 품사가 일치한다면
                formatQueue.add(nlpTokens[i])
                expPos++
            }else{ // 일치하지 않다면, 큐 비우고 토큰 포인터 초기화
                expPos = 0
                formatQueue.clear()
            }
        }

        // 결과값 배열로 반환
        return result.toTypedArray()
    }


}