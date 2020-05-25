package com.np.design.ui.listener;

import com.np.design.exception.ExceptionHandler;
import com.np.design.exception.NpException;
import lombok.extern.slf4j.Slf4j;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

@Slf4j
public class NpTableActionListener implements ActionListener {

    private final Runnable runnable;
    private final JTable jTable;
    private boolean checkSelect = true;

    public NpTableActionListener(JTable jTable, Runnable runnable) {
        this.runnable = runnable;
        this.jTable = jTable;

    }

    public NpTableActionListener(JTable jTable, boolean checkSelect, Runnable runnable) {
        this.runnable = runnable;
        this.jTable = jTable;
        this.checkSelect = checkSelect;
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        try {

            if (checkSelect) {
                int selectedRow = jTable.getSelectedRow();
                if (selectedRow < 0) {
                    throw new NpException("请选择单条记录");
                }
            }
            runnable.run();

        } catch (Exception ex) {
            log.error("发生错误。", ex);
            ExceptionHandler.handle(ex);
        }
    }
}
