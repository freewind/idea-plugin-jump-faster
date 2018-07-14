package jumpfaster


fun caretAfter(text: String): Int {
    return text.length
}

fun jumpLeft(text: String, caret: Int): Int = when (caret) {
    0 -> 0
    else -> {
        text.take(caret)
                .dropLastWhile { !it.isWordChar() }
                .dropLastWhile { it.isWordChar() }
                .length
    }
}

fun jumpRight(text: String, caret: Int): Int = when (caret) {
    caretAfter(text) -> caret
    else -> {
        val textAfterCaret = text.drop(caret)
        val textAfterNewCaret = textAfterCaret.dropWhile { it.isWordChar() }.dropWhile { !it.isWordChar() }
        caret + (textAfterCaret.length - textAfterNewCaret.length)
    }
}


fun jumpUp(text: String, caret: Int): Int {
    val textBeforeCaret = text.take(caret)
    val lines = textBeforeCaret.reader().readLines()
    if (lines.size <= 1) return caret

    val goodLines = lines.dropLast(1).dropLastWhile { !it.containsWordChar() }
    if (goodLines.isEmpty()) return caret

    val newCaretInLine = minOf(lines.last().length, goodLines.last().length)

    val lineSeparator = text.findLineSeparator()!!
    val newCaret = goodLines.dropLast(1).joinToString("") { it + lineSeparator }.length + newCaretInLine
    return newCaret
}


private fun Char.isLineSeparator(): Boolean {
    return this == '\r' || this == '\n'
}

fun jumpDown(text: String, caret: Int): Int {
    val textAfterCaret = text.drop(caret)
    val lines = textAfterCaret.reader().readLines()
    if (lines.size <= 1) return caret

    val (skipLines, remain) = lines.drop(1).ttt { !it.containsWordChar() }
    if (remain.isEmpty()) {
        return caret
    }

    val goodLines = skipLines + remain.first()
    val newCaretInLine = run {
        val caretInLine = text.take(caret).reader().readLines().last().length
        minOf(caretInLine, goodLines.last().length)
    }

    val lineSeparator = text.findLineSeparator()!!
    val newCaret = caret + (listOf(lines.first()) + goodLines.dropLast(1)).joinToString("") { it + lineSeparator }.length + newCaretInLine
    return newCaret
}

private fun <E> List<E>.ttt(fn: (E) -> Boolean): Pair<List<E>, List<E>> {
    val taken = this.takeWhile { fn(it) }
    val remain = this.drop(taken.size)
    return taken to remain
}


fun String.containsWordChar(): Boolean {
    return this.any { it.isWordChar() }
}

private fun String.findLineSeparator(): String? = when {
    this.contains("\r\n") -> "\r\n"
    this.contains("\n") -> "\n"
    this.contains("\r") -> "\r"
    else -> null
}

private val wordChars = ("$" + "_" +
        "abcdefghijklmnopqrstuvwxyz" +
        "ABCDEFGHIJKLMNOPQRSTVUWXYZ" +
        "0123456789").toSet()

private fun Char.isWordChar() = wordChars.contains(this)