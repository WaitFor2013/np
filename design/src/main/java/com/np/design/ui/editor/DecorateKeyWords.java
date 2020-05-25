package com.np.design.ui.editor;

import com.np.database.sql.util.PGUtils;

import javax.swing.*;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class DecorateKeyWords {

    public final static Color keyColor = new Color(58, 195, 92);

    public final static Color normalColor = new Color(187, 187, 187);

    public DecorateKeyWords() {
    }

    public void decorateKeyWords(JTextPane tp) {

        String text = tp.getText();
        StyledDocument doc = tp.getStyledDocument();

        SimpleAttributeSet keyColorAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(keyColorAttribute, keyColor);

        SimpleAttributeSet normalColorAttribute = new SimpleAttributeSet();
        StyleConstants.setForeground(normalColorAttribute, normalColor);

        ListIterator<WordNode> iterator = split(text, ' ', '\n', '\t');
        while (iterator.hasNext()) {
            WordNode wn = iterator.next();
            if (PGUtils.isKeyword(wn.getWord())) {
                doc.setCharacterAttributes(wn.getLocation(), wn.getWord()
                        .length(), keyColorAttribute, true);
            } else {
                doc.setCharacterAttributes(wn.getLocation(), wn.getWord()
                        .length(), normalColorAttribute, true);
            }
        }
    }

    public ListIterator<WordNode> split(String str, char... regex) {
        StringBuilder strBuilder = new StringBuilder();
        for (int i = 0; i < regex.length; i++) {
            if (i == regex.length - 1) {
                strBuilder.append("[").append(regex[i]).append("]");
            } else {
                strBuilder.append("[").append(regex[i]).append("]|");
            }
        }
        Pattern p = Pattern.compile(strBuilder.toString());
        Matcher m = p.matcher(str);
        List<WordNode> nodeList = new ArrayList();
        int strStart = 0;
        String s;
        WordNode sn;
        while (m.find()) {
            s = str.substring(strStart, m.start());
            if (!s.equals(new String())) {
                sn = new WordNode(strStart, s);
                nodeList.add(sn);
            }
            strStart = m.start() + 1;
        }
        s = str.substring(strStart, str.length());
        sn = new WordNode(strStart, s);
        nodeList.add(sn);
        return nodeList.listIterator();
    }

}


