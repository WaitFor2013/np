package com.np.design.ui;

import com.np.design.exception.NpException;

import javax.swing.*;
import javax.swing.event.ListDataListener;
import java.util.List;

public class NpComboBoxModel<T> implements ComboBoxModel {

    private final List<T> data;
    private T selectItem;


    public NpComboBoxModel(List<T> data) {
        this.data = data;
    }

    @Override
    public void setSelectedItem(Object anItem) {
        if (null != anItem) {
            selectItem = (T) anItem;
        } else {
            throw new NpException("can't set null.");
        }
    }

    @Override
    public Object getSelectedItem() {
        return selectItem;
    }

    @Override
    public int getSize() {
        return data.size();
    }

    @Override
    public Object getElementAt(int index) {
        return data.get(index);
    }

    @Override
    public void addListDataListener(ListDataListener l) {

    }

    @Override
    public void removeListDataListener(ListDataListener l) {

    }
}
