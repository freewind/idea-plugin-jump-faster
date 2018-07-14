package jumpfaster.actions

import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.application.ApplicationManager
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.project.impl.ProjectImpl
import jumpfaster.jumpDown
import jumpfaster.jumpLeft
import jumpfaster.jumpRight
import jumpfaster.jumpUp

enum class Direction { Up, Down, Left, Right }

abstract class JumpAction(private val direction: Direction) : AnAction() {
    override fun actionPerformed(event: AnActionEvent) {
        (event.project as? ProjectImpl)?.let { project ->
            val editor = getCurrentTextEditor(project)
            if (editor != null) {
                val text = editor.editor.document.text
                val caret = editor.editor.caretModel.offset
                val newOffset = when (direction) {
                    Direction.Left -> jumpLeft(text, caret)
                    Direction.Right -> jumpRight(text, caret)
                    Direction.Up -> jumpUp(text, caret)
                    Direction.Down -> jumpDown(text, caret)
                }
                ApplicationManager.getApplication().runWriteAction {
                    editor.editor.caretModel.moveToOffset(newOffset)
                }
            }
        }
    }

    private fun getCurrentTextEditor(project: ProjectImpl) = FileEditorManager.getInstance(project)?.selectedEditors?.filterIsInstance<TextEditor>()?.firstOrNull()
}

class JumpLeftAction : JumpAction(Direction.Left)
class JumpRightAction : JumpAction(Direction.Right)
class JumpUpAction : JumpAction(Direction.Up)
class JumpDownAction : JumpAction(Direction.Down)



