package jumpfaster


fun caretAfter(text: String): Int {
    return text.length
}

fun jumpLeft(text: String, caret: Int): Int = when (caret) {
    0 -> 0
    else -> {
        val lines = calcLineOffsets(text)
        lines.lastOrNull { it.wordEnds.any { it < caret } }?.wordEnds?.lastOrNull { it < caret }
                ?: lines.firstOrNull()?.wordStarts?.firstOrNull()
                ?: caret
    }
}

fun jumpRight(text: String, caret: Int): Int = when (caret) {
    caretAfter(text) -> caret
    else -> {
        val lines = calcLineOffsets(text)
        lines.firstOrNull { it.wordStarts.any { it > caret } }?.wordStarts?.firstOrNull { it > caret }
                ?: lines.lastOrNull()?.wordEnds?.lastOrNull()
                ?: caret
    }
}

fun jumpUp(text: String, caret: Int): Int {
    val lines = calcLineOffsets(text)
    val currentLine = lines.find { it.containsCaret(caret)!! }!!
    val column = currentLine.column(caret)
    val validPrevLine = lines.filter { it.lineEnd!! < caret }
            .dropLastWhile { it.wordEnds.isEmpty() }
            .lastOrNull()
    return validPrevLine?.let { line -> line.wordEnds.lastOrNull { line.column(it) <= column } ?: line.wordEnds.first { line.column(it) > column } }
            ?: currentLine.wordStarts.firstOrNull()
            ?: caret
}

fun jumpDown(text: String, caret: Int): Int {
    val lines = calcLineOffsets(text)
    val currentLine = lines.find { it.containsCaret(caret)!! }!!
    val column = currentLine.column(caret)
    val validLaterLine = lines.filter { it.lineStart > caret }
            .dropWhile { it.wordStarts.isEmpty() }
            .firstOrNull()
    return validLaterLine?.let { line -> line.wordStarts.firstOrNull { line.column(it) >= column } ?: line.wordStarts.last { line.column(it) < column } }
            ?: currentLine.wordEnds.lastOrNull()
            ?: caret
}

private fun Char.isLineSeparator(): Boolean {
    return this == '\r' || this == '\n'
}

private val wordChars = ("$" + "_" +
        "abcdefghijklmnopqrstuvwxyz" +
        "ABCDEFGHIJKLMNOPQRSTVUWXYZ" +
        "0123456789").toSet()

private fun Char.isWordChar() = wordChars.contains(this)

data class LineOffsets(
        var line: Int,
        var lineStart: Int,
        var lineEnd: Int? = null,
        val wordStarts: MutableList<Int> = mutableListOf(),
        val wordEnds: MutableList<Int> = mutableListOf()
) {
    fun containsCaret(caret: Int): Boolean? = lineStart <= caret && caret <= lineEnd!!
    fun column(caret: Int): Int = caret - lineStart
}

fun calcLineOffsets(text: String): List<LineOffsets> {
    val result = mutableListOf<LineOffsets>()

    var lineOffsets: LineOffsets? = null

    var index = 0
    loop@ while (index <= text.length) {
        val prevChar = text.getOrNull(index - 1)
        val nextChar = text.getOrNull(index)
        if (atLineStart(prevChar, nextChar)) {
            lineOffsets = if (lineOffsets == null) {
                LineOffsets(0, lineStart = index)
            } else {
                LineOffsets(line = lineOffsets.line + 1, lineStart = index)
            }
        }
        if (atLineEnd(prevChar, nextChar)) {
            lineOffsets?.let {
                it.lineEnd = index
                result.add(it)
            }
        }
        if (atWordStart(prevChar, nextChar)) {
            lineOffsets?.wordStarts?.add(index)
        }
        if (atWordEnd(prevChar, nextChar)) {
            lineOffsets?.wordEnds?.add(index)
        }
        index += 1
    }
    return result
}

private fun atWordStart(prevChar: Char?, nextChar: Char?): Boolean {
    val prevIsWordChar = prevChar?.isWordChar() ?: false
    val nextIsWordChar = nextChar?.isWordChar() ?: false
    return !prevIsWordChar && nextIsWordChar
}

private fun atWordEnd(prevChar: Char?, nextChar: Char?): Boolean {
    val prevIsWordChar = prevChar?.isWordChar() ?: false
    val nextIsWordChar = nextChar?.isWordChar() ?: false
    return prevIsWordChar && !nextIsWordChar
}

private fun atLineStart(prevChar: Char?, nextChar: Char?): Boolean {
    if (prevChar == '\r' && nextChar == '\n') return false
    return prevChar?.isLineSeparator() ?: true
}

private fun atLineEnd(prevChar: Char?, nextChar: Char?): Boolean {
    if (prevChar == '\r' && nextChar == '\n') return false
    return nextChar?.isLineSeparator() ?: true
}