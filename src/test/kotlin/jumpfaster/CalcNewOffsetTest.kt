package jumpfaster

import org.assertj.core.api.Assertions.assertThat
import org.junit.Test

class JumpLeftTest {

    @Test
    fun `If caret is inside a word, will jump to the start of the word`() {
        val offset = jumpLeft("aa bbb", caretAfter("aa b"))
        assertThat(offset).isEqualTo(caretAfter("aa "))
    }

    @Test
    fun `If caret is in the beginning of text, will not change`() {
        val offset = jumpLeft("abc", 0)
        assertThat(offset).isEqualTo(0)
    }

    @Test
    fun `If caret is in the beginning of a word, it will jump to the beginning of previous word`() {
        val offset = jumpLeft("aa bbb", caretAfter("aa "))
        assertThat(offset).isEqualTo(0)
    }

    @Test
    fun `If caret is inside some spaces, it will jump to the beginning of previous word`() {
        val offset = jumpLeft("aa bb       ccc", caretAfter("aa bb  "))
        assertThat(offset).isEqualTo(caretAfter("aa "))
    }

    @Test
    fun `$ is considered as word char`() {
        val offset = jumpLeft("aa bb\$bbb", caretAfter("aa bb\$b"))
        assertThat(offset).isEqualTo(caretAfter("aa "))
    }

    @Test
    fun `_ is considered as word char`() {
        val offset = jumpLeft("aa bb_bbb", caretAfter("aa bb_b"))
        assertThat(offset).isEqualTo(caretAfter("aa "))
    }

    @Test
    fun `can jump to previous line`() {
        val offset = jumpLeft("aa bb\n   ", caretAfter("aa bb\n  "))
        assertThat(offset).isEqualTo(caretAfter("aa "))
    }

    @Test
    fun `treat other chars as non-word-char`() {
        val offset = jumpLeft("aa bb ( { =    ", caretAfter("aa bb ( { = "))
        assertThat(offset).isEqualTo(caretAfter("aa "))
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

}

class JumpUpTest {

    @Test
    fun `If in the first line, jump up will not change caret`() {
        val offset = jumpUp("abc", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("a"))
    }

    @Test
    fun `Jump to the previous line in the same offset in line`() {
        val offset = jumpUp("abc\n123", caretAfter("abc\n1"))
        assertThat(offset).isEqualTo(caretAfter("a"))
    }

    @Test
    fun `Handle other line separators correctly`() {
        assertThat(jumpUp("abc\r\n123", caretAfter("abc\r\n1"))).isEqualTo(caretAfter("a"))
        assertThat(jumpUp("abc\r123", caretAfter("abc\r1"))).isEqualTo(caretAfter("a"))
    }

    @Test
    fun `If previous line has no word-chars, skip it`() {
        val offset = jumpUp("abc\n{{()}}\n123", caretAfter("abc\n{{()}}\n1"))
        assertThat(offset).isEqualTo(caretAfter("a"))
    }

    @Test
    fun `If all previous lines has no word-chars, no change`() {
        val offset = jumpUp("###\n{{()}}\n123", caretAfter("###\n{{()}}\n1"))
        assertThat(offset).isEqualTo(caretAfter("###\n{{()}}\n1"))
    }

    @Test
    fun `If previous line is short, jump to the end of it`() {
        val offset = jumpUp("aaa\nbbbbbb", caretAfter("aaa\nbbbbb"))
        assertThat(offset).isEqualTo(caretAfter("aaa"))
    }

}

class JumpDownTest {

    @Test
    fun `If in the last line, jump down will not change caret`() {
        assertThat(jumpDown("abc", caretAfter("a"))).isEqualTo(caretAfter("a"))
        assertThat(jumpDown("abc\n123", caretAfter("abc\n1"))).isEqualTo(caretAfter("abc\n1"))
    }

    @Test
    fun `Jump to the next line in the same offset in line`() {
        val offset = jumpDown("abc\n123", caretAfter("a"))
        assertThat(offset).isEqualTo(caretAfter("abc\n1"))
    }

    @Test
    fun `Handle other line separators correctly`() {
        assertThat(jumpDown("abc\r\n123", caretAfter("ab"))).isEqualTo(caretAfter("abc\r\n12"))
        assertThat(jumpDown("abc\r123", caretAfter("ab"))).isEqualTo(caretAfter("abc\r12"))
    }

    @Test
    fun `If next line has no word-chars, skip it`() {
        val offset = jumpDown("abc\n{{()}}\n123", caretAfter("ab"))
        assertThat(offset).isEqualTo(caretAfter("abc\n{{()}}\n12"))
    }

    @Test
    fun `If all previous lines has no word-chars, no change`() {
        val offset = jumpDown("123\n###\n{{()}}", caretAfter("12"))
        assertThat(offset).isEqualTo(caretAfter("12"))
    }

    @Test
    fun `If next line is short, jump to the end of it`() {
        val offset = jumpDown("aaaaaa\nbbb", caretAfter("aaaaa"))
        assertThat(offset).isEqualTo(caretAfter("aaaaaa\nbbb"))
    }

    @Test
    fun `If the caret is in doc start, it will work well`() {
        val offset = jumpDown("aaa\nbbb", 0)
        assertThat(offset).isEqualTo(caretAfter("aaa\n"))
    }
    
}