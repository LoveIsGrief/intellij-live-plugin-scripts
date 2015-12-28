/**
  * IntelliJ'S "Select Line" doesn't work like SublimeText's and I'm too lazy to find how to create a bug report
  * so here's a script that does it (not create a bug report, dummy :P )
  */

import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.editor.ex.util.EditorUtil

import static liveplugin.PluginUtil.*
import com.intellij.openapi.actionSystem.CommonDataKeys

ks = "ctrl L"

// Selects a line and sets the caret to be on the next one
// Multiple presses will select subsequent lines
// If you have already selected text it will just add the current line to the selection
// !!!!MULTIPLE CARETS = UNDEFINED BEHAVIOR!!!!
registerAction("LineSelectionAction", ks) { AnActionEvent event ->
    editor = event.getData(CommonDataKeys.EDITOR);
    selectionModel = editor.getSelectionModel();

    lines = EditorUtil.calcCaretLineRange(editor);
    lineStart = lines.first;
    nextLineStart = lines.second;

    int start = editor.logicalPositionToOffset(lineStart);
    int end = editor.logicalPositionToOffset(nextLineStart);

    // Extend the existing selection by a line
    if(selectionModel.hasSelection()) {
        selectionStart = selectionModel.getSelectionStart()
        if(start > selectionStart) start = selectionStart

        selectionEnd = selectionModel.getSelectionEnd()
        if(end < selectionEnd) end = selectionEnd
    }


    selectionModel.setSelection(start, end)

    //Caret at the end
    caretModel = editor.getCaretModel()
    caretModel.moveToOffset(end)
}

if (!isIdeStartup) show("Loaded 'LineSelectionAction'<br/>Use " + ks + " to run it")
