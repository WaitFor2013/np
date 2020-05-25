package com.np.design.ui.dialog;


import com.np.design.NoRepeatApp;
import com.np.design.ui.frame.MainFrame;
import com.np.design.NoRepeatApp;
import com.np.design.ui.frame.MainFrame;
import lombok.Getter;

import javax.swing.*;
import java.io.File;


@Getter
public class CommonTipsDialog extends JDialog {
    private JPanel contentPane;
    private JTextField fileText;
    private JButton fileBtn;
    private JTextField pgText;
    private JLabel fileLabel;
    private JLabel packageLabel;
    private JButton okButton;
    private JLabel makF;
    private JLabel makS;

    private static CommonTipsDialog commonTipsDialog;

    private boolean isOk = false;

    private CommonTipsDialog() {
        super(NoRepeatApp.mainFrame, "代码生成");
        setContentPane(contentPane);
        setModal(true);

        //getRootPane().setDefaultButton(okBtn);

        MainFrame.setPreferSizeAndLocateToCenter(this, 0.3, 0.4);
        setResizable(false);

        fileBtn.addActionListener(e -> {
            JFileChooser chooser = new JFileChooser();
            chooser.setMultiSelectionEnabled(false);
            chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            chooser.setDialogTitle("选择上传⽇志⽂件夹");
            int result = chooser.showOpenDialog(this);
            if (result == JFileChooser.APPROVE_OPTION) {
                File file = chooser.getSelectedFile();
                String filepath = file.getAbsolutePath();
                fileText.setText(filepath);// 将⽂件路径设到JTextField
            }
        });

        okButton.addActionListener(e -> {
            if (pgText.getText().isEmpty()) {
                pgText.requestFocus();
                return;
            }

            if (fileText.getText().isEmpty()) {
                fileBtn.requestFocus();
                return;
            }

            isOk = true;
            dispose();
        });
    }


    public String getFilePath() {
        return fileText.getText();
    }

    public String getPackage() {
        return pgText.getText();
    }

    public static CommonTipsDialog getInstance() {
        if (commonTipsDialog == null) {
            commonTipsDialog = new CommonTipsDialog();
        }
        commonTipsDialog.isOk = false;
        return commonTipsDialog;
    }

}
