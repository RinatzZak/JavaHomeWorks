package com.javarush.task.task32.task3209.actions;

import javax.swing.*;
import javax.swing.text.MutableAttributeSet;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledEditorKit;
import java.awt.event.ActionEvent;

public class SubscriptAction extends StyledEditorKit.StyledTextAction {
    
    public SubscriptAction() {
        super(StyleConstants.Subscript.toString());
    }

    @Override
    public void actionPerformed(ActionEvent actionEvent) {
        JEditorPane editor = getEditor(actionEvent);
        if (editor != null){
            MutableAttributeSet mas = getStyledEditorKit(editor).getInputAttributes();
            SimpleAttributeSet sas = new SimpleAttributeSet();
            StyleConstants.setSubscript(sas, !StyleConstants.isSubscript(mas));
            setCharacterAttributes(editor, sas, false);
        }
    }
    
}
