package com.lijie.jiancang.ext

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

fun Document.getContentEle(): Element {
    val element1 = this.body().excavateContent(false)
    val parent = element1?.parent()
    element1?.remove()
    val element2 = parent?.excavateContent(true)
    element2?.remove()
    return Jsoup.parse((element1?.toString() ?: "") + (element2?.toString() ?: "")).body()
}

private fun Element.excavateContent(img: Boolean): Element? {
    this.doScoreToElement(img)
    return if (img) {
        this.getMaxScoreChild(img)
    } else {
        val maxScoreElement: Element? = this.getMaxScoreChild(img)
        maxScoreElement?.let {
            val pathBuffer: StringBuffer = checkPath(maxScoreElement, this)
            var path = pathBuffer.toString()
            if (path.contains(">p>")) {
                path = path.split(">p>").toTypedArray()[0]
            }
            if (path.endsWith(">")) {
                path = path.substring(0, path.length - 1)
            }
            if (path.endsWith(">p")) {
                path = path.substring(0, path.length - 2)
            }
            this.select(path).first()
        } ?: run {
            null
        }
    }
}

private fun Element.doScoreToElement(img: Boolean): Int {
    val children: Elements = this.children()
    return if (children.size == 0) {
        Rating.doRate(this, img)
    } else {
        var accum: Int = Rating.doOwnTextRate(this)
        for (child in children) {
            accum += child.doScoreToElement(img)
        }
        this.attr("score", accum.toString())
        accum
    }
}

private fun Element.getMaxScoreChild(img: Boolean): Element? {
    if (this.childNodeSize() == 0) {
        return this
    }
    val children = this.children()
    if (children.size == 0) {
        return this
    }
    var maxScoreElement = children.first()!!
    var score = 0
    if (img) {
        for (e in children) {
            val strScore = e.attr("score") ?: continue
            if (Integer.valueOf(strScore) > score) {
                maxScoreElement = e
                score = Integer.valueOf(strScore)
            }
        }
        return if (score > 0 && maxScoreElement.getElementsByTag("img").size > 0) maxScoreElement else null
    } else {
        for (e in children) {
            val strScore = e.attr("score") ?: continue
            if (Integer.valueOf(strScore) > score) {
                maxScoreElement = e
                score = Integer.valueOf(strScore)
            }
        }
        return maxScoreElement.getMaxScoreChild(img)
    }
}

private fun checkPath(element: Element, document: Element): StringBuffer {
    var accum = StringBuffer()
    if (element.parent() == null) {
        return accum
    }
    if (element.parent() != null) {
        val parentElement = element.parent()
        val tagStr = parentElement!!.tagName()
        if (parentElement.hasAttr("id")) {
            accum.insert(0, tagStr + "#" + parentElement.attr("id") + ">")
        } else if (parentElement.hasAttr("class")) {
            var classStr = parentElement.attr("class").trim { it <= ' ' }
                .replace(" ", ".")
            if ("p" == tagStr) {
                classStr = ""
            }
            if ("" != classStr) {
                accum.insert(0, "$tagStr.$classStr>")
            } else {
                accum.insert(0, "$tagStr>")
            }
            accum = if ("p" == tagStr || document.getElementsByClass(classStr).size > 1) {
                checkPath(element.parent()!!, document)
            } else {
                return accum
            }
        } else {
            accum.insert(0, "$tagStr>")
            if ("body" != tagStr) {
                accum = checkPath(element.parent()!!, document)
            }
        }
    }
    return accum
}

private object Rating {
    fun doRate(element: Element, img: Boolean): Int {
        val map: Map<String, Int>
        val s: Int
        val brSize = element.getElementsByTag("br").size
        val pSize = element.getElementsByTag("p").size
        val imgSize = element.getElementsByTag("img").size
        val htmlString = element.html()
        map = GetCharsNum.getNum(htmlString)
        val chCharacter = map["chCharacter"]!!
        val chPunctuationCharacter = map["chPunctuationCharacter"]!!
        val otherCharacter = map["otherCharacter"]!!
        s = if (img) {
            imgSize * Properties.ImgSecore - brSize * Properties.BRSecore - pSize * Properties.PSecore - chCharacter * Properties.CNCHARSCORE - chPunctuationCharacter * Properties.CNPNCHARSCORE - otherCharacter / 5
        } else {
            brSize * Properties.BRSecore + pSize * Properties.PSecore + imgSize * Properties.ImgSecore + chCharacter * Properties.CNCHARSCORE + chPunctuationCharacter * Properties.CNPNCHARSCORE + otherCharacter / 5
        }
        element.attr("score", s.toString())
        return s
    }

    fun doOwnTextRate(element: Element): Int {
        val map: Map<String, Int>
        val s: Int
        val textString = element.ownText()
        map = GetCharsNum.getNum(textString)
        val chCharacter = map["chCharacter"]!!
        val chPunctuationCharacter = map["chPunctuationCharacter"]!!
        val otherCharacter = map["otherCharacter"]!!
        s =
            chCharacter * Properties.CNCHARSCORE + chPunctuationCharacter * Properties.CNPNCHARSCORE + otherCharacter / 5
        element.attr("score", s.toString())
        return s
    }
}


private object GetCharsNum {

    fun getNum(str: String): Map<String, Int> {
        var chCharacter = 0
        var chPunctuationCharacter = 0
        var otherCharacter = 0
        val map: MutableMap<String, Int> = HashMap()
        str.trim { it <= ' ' }
        val chars = str.toCharArray()
        for (i in chars.indices) {
            when {
                isChinese(chars[i]) -> {
                    chCharacter++
                }
                isChinesePunctuation(chars[i]) -> {
                    chPunctuationCharacter++
                }
                else -> {
                    otherCharacter++
                }
            }
        }
        map["chCharacter"] = chCharacter
        map["chPunctuationCharacter"] = chPunctuationCharacter
        map["otherCharacter"] = otherCharacter
        return map
    }

    private fun isChinese(ch: Char): Boolean {
        val ub = Character.UnicodeBlock.of(ch)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A || ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_B
    }

    private fun isChinesePunctuation(ch: Char): Boolean {
        val ub = Character.UnicodeBlock.of(ch)
        return ub === Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS || ub === Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION || ub === Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS || ub === Character.UnicodeBlock.GENERAL_PUNCTUATION
    }
}

object Properties {
    const val CNCHARSCORE = 1
    const val CNPNCHARSCORE = 5
    const val ImgSecore = 10
    const val BRSecore = 20
    const val PSecore = 20
}