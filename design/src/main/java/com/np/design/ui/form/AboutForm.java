package com.np.design.ui.form;

import com.np.design.NpConstants;
import com.intellij.uiDesigner.core.GridConstraints;
import com.intellij.uiDesigner.core.GridLayoutManager;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * <pre>
 * AboutForm
 * </pre>
 *
 * @author <a href="https://github.com/rememberber">RememBerBer</a>
 * @since 2019/5/6.
 */
@Getter
public class AboutForm {
    private JPanel aboutPanel;
    private JLabel sloganLabel;
    private JTextPane textPane;

    private JLabel nameLabel;
    private JLabel versionLabel;
    private JLabel copyrightlabel;

    private static AboutForm aboutForm;

    private AboutForm() {
        this.nameLabel.setText(NpConstants.APP_TITLE);
        this.versionLabel.setText(NpConstants.APP_NAME + ":" + NpConstants.APP_VERSION);

        StringBuilder detailBuilder = new StringBuilder();
        detailBuilder.append("NP，No Repeat code。和NP问题一样，完全做到是个大难题(PS:否则大家得多无聊^_^)").append("\n");
        detailBuilder.append("本系统致力于降低重复编码的工作。").append("\n");
        detailBuilder.append("1、NP，更少的BUG产生。").append("\n");
        detailBuilder.append("2、NP，更多的时间放在复杂的业务构建和交互界面优化上。").append("\n");

        textPane.setText(detailBuilder.toString());
    }

    public static AboutForm getInstance() {
        if (aboutForm == null) {
            aboutForm = new AboutForm();
        }
        return aboutForm;
    }

    public static void init() {
        aboutForm.getAboutPanel().updateUI();
    }

}
