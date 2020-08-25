package com.np.design.ui.dialog;


import com.np.design.NoRepeatApp;
import com.np.design.ui.frame.MainFrame;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SqlExportDialog extends JDialog {
    private JPanel contentPane;
    private JButton buttonOK;
    private JButton buttonCancel;
    private JLabel fileLabel;
    private JTextField fileText;
    private JLabel makS;
    private JButton fileBtn;

    public SqlExportDialog() {
        super(NoRepeatApp.mainFrame, "表定义目录");
        setContentPane(contentPane);
        setModal(true);

        MainFrame.setPreferSizeAndLocateToCenter(this, 0.3, 0.2);

        getRootPane().setDefaultButton(buttonOK);

        buttonOK.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onOK();
            }
        });

        buttonCancel.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                onCancel();
            }
        });

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

    }

    public String getFilePath() {
        return fileText.getText();
    }

    private void onOK() {
        if (fileText.getText().isEmpty()) {
            fileBtn.requestFocus();
            return;
        }
        // add your code here
        dispose();
    }

    private void onCancel() {
        // add your code here if necessary
        dispose();
    }

    public static SqlExportDialog getInstance() {
        if (dialog == null) {
            dialog = new SqlExportDialog();
            dialog.pack();
        }

        dialog.fileText.setText(null);
        return dialog;
    }

    private static SqlExportDialog dialog;


}
