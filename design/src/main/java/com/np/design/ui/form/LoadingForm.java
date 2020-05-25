package com.np.design.ui.form;

import com.np.design.NpConstants;
import lombok.Getter;

import javax.swing.*;


@Getter
public class LoadingForm {
    private JLabel logo;
    private JLabel message;
    private JLabel version;
    private JPanel loadPanel;

    private static LoadingForm loadingForm;

    private LoadingForm() {
        this.version.setText(NpConstants.APP_TITLE + "(" + NpConstants.APP_NAME + ":" + NpConstants.APP_VERSION + ")");
    }

    public static LoadingForm getInstance() {
        if (loadingForm == null) {
            loadingForm = new LoadingForm();
        }
        return loadingForm;
    }

}
