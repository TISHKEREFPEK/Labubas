package com.example.labubas;

import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

public class PhoneTextWatcher implements TextWatcher {
    private final EditText editText;
    private boolean formatting;
    public PhoneTextWatcher(EditText editText) {
        this.editText = editText;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {}

    @Override
    public void afterTextChanged(Editable s) {
        if (formatting) return;
        formatting = true;

        String digits = s.toString().replaceAll("\\D", "");

        if (digits.isEmpty()) {
            formatting = false;
            return;
        }

        if (digits.startsWith("7") || digits.startsWith("8")) {
            digits = digits.substring(1);
        }

        StringBuilder formatted = new StringBuilder("+7");
        if (digits.length() > 0) {
            formatted.append(" (");
            int len = Math.min(digits.length(), 3);
            formatted.append(digits.substring(0, len));
            digits = digits.substring(len);
        }
        if (digits.length() > 0) {
            formatted.append(") ");
            int len = Math.min(digits.length(), 3);
            formatted.append(digits.substring(0, len));
            digits = digits.substring(len);
        }
        if (digits.length() > 0) {
            formatted.append("-");
            int len = Math.min(digits.length(), 2);
            formatted.append(digits.substring(0, len));
            digits = digits.substring(len);
        }
        if (digits.length() > 0) {
            formatted.append("-");
            int len = Math.min(digits.length(), 2);
            formatted.append(digits.substring(0, len));
        }

        editText.setText(formatted.toString());
        editText.setSelection(editText.getText().length());

        formatting = false;
    }
}