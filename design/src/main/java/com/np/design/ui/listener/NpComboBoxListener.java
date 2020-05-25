package com.np.design.ui.listener;

import com.np.design.exception.ExceptionHandler;
import lombok.extern.slf4j.Slf4j;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
public class NpComboBoxListener implements ActionListener {

    private final Runnable runnable;

    public NpComboBoxListener(Runnable runnable) {
        this.runnable = runnable;
    }

    @Override
    public void actionPerformed(ActionEvent e) {

        try {
            runnable.run();
        } catch (Exception ex) {
            log.error("发生错误。", ex);
            ExceptionHandler.handle(ex);
        }
    }
}
