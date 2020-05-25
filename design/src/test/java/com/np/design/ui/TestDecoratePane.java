package com.np.design.ui;

import com.np.design.ui.editor.DecorateKeyWords;

import javax.swing.*;

public class TestDecoratePane extends JFrame {
    JTextPane tp = new JTextPane();
    public TestDecoratePane() {
        String fileStr = "select a.name,a.age,b.rolename,c.emp_name from t_user a left join t_role b on a.rid = b.id left join t_emp c on a.id = c.id where a.id = ?";
        tp.setText(fileStr);
        this.add(tp);
        DecorateKeyWords dc = new DecorateKeyWords();

        this.setBounds(300, 200, 400, 300);
        this.setVisible(true);
        dc.decorateKeyWords(tp);
    }
    /**
     * @param args
     */
    public static void main(String[] args) {
        new TestDecoratePane();
    }
}