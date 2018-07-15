package jumpfaster

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class JumpLeftTest {

    @Test
    fun `If caret is inside a word, will jump to the end of the previous word`() {
        val offset = jumpLeft("aa bbb", caretAfter("aa b"))
        assertThat(offset).isEqualTo(caretAfter("aa"))
    }

    @Test
    fun `If caret is at the start of text, will not change`() {
        val offset = jumpLeft("abc", 0)
        assertThat(offset).isEqualTo(0)
    }

    @Test
    fun `If caret is inside some spaces, it will jump to the end of previous word`() {
        val offset = jumpLeft("aa bb       ccc", caretAfter("aa bb  "))
        assertThat(offset).isEqualTo(caretAfter("aa bb"))
    }

    @Test
    fun `$ is considered as word char`() {
        val offset = jumpLeft("aa bb\$bbb", caretAfter("aa bb\$b"))
        assertThat(offset).isEqualTo(caretAfter("aa"))
    }

    @Test
    fun `_ is considered as word char`() {
        val offset = jumpLeft("aa bb_bbb", caretAfter("aa bb_b"))
        assertThat(offset).isEqualTo(caretAfter("aa"))
    }

    @Test
    fun `can jump to previous line`() {
        val offset = jumpLeft("aa bb\n   ", caretAfter("aa bb\n  "))
        assertThat(offset).isEqualTo(caretAfter("aa bb"))
    }

    @Test
    fun `treat other chars as non-word-char`() {
        val offset = jumpLeft("aa bb ( { =    ", caretAfter("aa bb ( { = "))
        assertThat(offset).isEqualTo(caretAfter("aa bb"))
    }

}

class JumpRightTest {

    @Test
    fun `If caret is inside a word, will jump to the start of next word`() {
        val offset = jumpRight("aa bbb", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("aa "))
    }

    @Test
    fun `If caret is in the end of text, will not change`() {
        val offset = jumpRight("abc", caretAfter("abc"))
        assertThat(offset).isEqualTo(caretAfter("abc"))
    }

    @Test
    fun `If caret is in the end of a word, it will jump to the beginning of next word`() {
        val offset = jumpRight("aa bbb", caretAfter("aa"))
        assertThat(offset).isEqualTo(caretAfter("aa "))
    }

    @Test
    fun `If caret is inside some spaces, it will jump to the beginning of next word`() {
        val offset = jumpRight("aa bb       ccc", caretAfter("aa bb  "))
        assertThat(offset).isEqualTo(caretAfter("aa bb       "))
    }

    @Test
    fun `$ is considered as word char`() {
        val offset = jumpRight("aa\$aa bbb", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("aa\$aa "))
    }

    @Test
    fun `_ is considered as word char`() {
        val offset = jumpRight("aa_aa bb", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("aa_aa "))
    }

    @Test
    fun `can jump to previous line`() {
        val offset = jumpRight("aa \n  bb ", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("aa \n  "))
    }

    @Test
    fun `treat other chars as non-word-char`() {
        val offset = jumpRight("aa ( { =   cc ", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("aa ( { =   "))
    }

    @Test
    fun `if in the last word of last line, will jump to the end of the word`() {
        val offset = jumpRight("aa \n 111 222 ", caretAfter("aa \n 111 2"))
        assertThat(offset).isEqualTo(caretAfter("aa \n 111 222"))
    }
}

class JumpUpTest {

    @Test
    fun `If in the first line, will jump to the START of the first word`() {
        val offset = jumpUp("  abc 123", caretAfter("  abc 12"))
        assertThat(offset).isEqualTo(caretAfter("  "))
    }

    @Test
    fun `If in the spaces of the first line, and no words before it, will jump to the START of the first word`() {
        val offset = jumpUp("      abc 123", caretAfter("  "))
        assertThat(offset).isEqualTo(caretAfter("      "))
    }

    @Test
    fun `Jump to the previous line will jump to the word end before current column index`() {
        val offset = jumpUp("ab def\n123456", caretAfter("ab def\n1234"))
        assertThat(offset).isEqualTo(caretAfter("ab"))
    }

    @Test
    fun `Jump to the previous line will jump to the end of next word if no word before current column`() {
        val offset = jumpUp("    def\n123456", caretAfter("    def\n12"))
        assertThat(offset).isEqualTo(caretAfter("    def"))
    }

    @Test
    fun `Jump to the previous line will stay in the same column if it's just the end of a word`() {
        val offset = jumpUp("ab def\n123456", caretAfter("ab def\n12"))
        assertThat(offset).isEqualTo(caretAfter("ab"))
    }

    @Test
    fun `Handle other line separators correctly`() {
        assertThat(jumpUp("abc\r\n123", caretAfter("abc\r\n123"))).isEqualTo(caretAfter("abc"))
        assertThat(jumpUp("abc\r123", caretAfter("abc\r123"))).isEqualTo(caretAfter("abc"))
    }

    @Test
    fun `If previous line has no word-chars, skip it`() {
        val offset = jumpUp("abc\n{{()}}\n123", caretAfter("abc\n{{()}}\n123"))
        assertThat(offset).isEqualTo(caretAfter("abc"))
    }

    @Test
    fun `If all previous lines has no word-chars, jump to the start of the first word`() {
        val offset = jumpUp("###\n{{()}}\n123", caretAfter("###\n{{()}}\n1"))
        assertThat(offset).isEqualTo(caretAfter("###\n{{()}}\n"))
    }

    @Test
    fun `If previous line is short, jump to end of the last word`() {
        val offset = jumpUp("aa bb\nccccccccc", caretAfter("aa bb\nccccccc"))
        assertThat(offset).isEqualTo(caretAfter("aa bb"))
    }

}

class JumpDownTest {

    @Test
    fun `If in the last line, jump to the end of the last word`() {
        assertThat(jumpDown("abc", caretAfter("a"))).isEqualTo(caretAfter("abc"))
        assertThat(jumpDown("abc\n123", caretAfter("abc\n1"))).isEqualTo(caretAfter("abc\n123"))
    }

    @Test
    fun `Jump to the next line will stay in the same column if it happens to be the start of a word`() {
        val offset = jumpDown("aa bb\n11 222", caretAfter("aa "))
        assertThat(offset).isEqualTo(caretAfter("aa bb\n11 "))
    }

    @Test
    fun `Jump to the next line will jump to the start of the word before current column if it's not the start of a word`() {
        val offset = jumpDown("abc\n123", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("abc\n"))
    }

    @Test
    fun `Jump to the next line will jump to the start of the word after current column if no word before the column`() {
        val offset = jumpDown("abc\n       123", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("abc\n       "))
    }

    @Test
    fun `Handle other line separators correctly`() {
        assertThat(jumpDown("abc\r\n123", caretAfter("abc"))).isEqualTo(caretAfter("abc\r\n"))
        assertThat(jumpDown("abc\r123", caretAfter("abc"))).isEqualTo(caretAfter("abc\r"))
    }

    @Test
    fun `If next line has no word-chars, skip it`() {
        val offset = jumpDown("abc\n{{()}}\n123", caretAfter("ab"))
        assertThat(offset).isEqualTo(caretAfter("abc\n{{()}}\n"))
    }

    @Test
    fun `If all later lines has no word-chars, just to the end of the last word`() {
        val offset = jumpDown("123\n###\n{{()}}", caretAfter("12"))
        assertThat(offset).isEqualTo(caretAfter("123"))
    }

    @Test
    fun `If next line is short, jump to the start of the last word`() {
        val offset = jumpDown("aaaaaaaaa\n111 222", caretAfter("aaaaaaaa"))
        assertThat(offset).isEqualTo(caretAfter("aaaaaaaaa\n111 "))
    }

    @Test
    fun `If the caret is at doc start, it will work well`() {
        val offset = jumpDown("aaa\nbbb", 0)
        assertThat(offset).isEqualTo(caretAfter("aaa\n"))
    }

    @Test
    fun `If the caret is at doc end, the caret will not change`() {
        val offset = jumpDown("aaaaaa", caretAfter("aaaaaa"))
        assertThat(offset).isEqualTo(caretAfter("aaaaaa"))
    }

    @Test
    fun `If the caret is in the spaces of doc end, the caret will jump to the end of the last word`() {
        val offset = jumpDown("aaa     ", caretAfter("aaa   "))
        assertThat(offset).isEqualTo(caretAfter("aaa"))
    }

    @Test
    fun `If the caret is in an empty line, it should work well`() {
        val offset = jumpDown("aaa\n\nbbb", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("aaa\n\n"))
    }

    @Test
    fun `work well with many kinds of line separators`() {
        assertThat(jumpDown("aaa\rbbb", caretAfter("a"))).isEqualTo(caretAfter("aaa\r"))
        assertThat(jumpDown("aaa\nbbb", caretAfter("a"))).isEqualTo(caretAfter("aaa\n"))
        assertThat(jumpDown("aaa\r\nbbb", caretAfter("a"))).isEqualTo(caretAfter("aaa\r\n"))
        assertThat(jumpDown("aaa\n\rbbb", caretAfter("a"))).isEqualTo(caretAfter("aaa\n\r"))
        assertThat(jumpDown("aaa\r\rbbb", caretAfter("a"))).isEqualTo(caretAfter("aaa\r\r"))
        assertThat(jumpDown("aaa\n\nbbb", caretAfter("a"))).isEqualTo(caretAfter("aaa\n\n"))
    }
}