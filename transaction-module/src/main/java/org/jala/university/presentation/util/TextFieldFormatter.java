package org.jala.university.presentation.util;

import javafx.scene.control.TextField;

public final class TextFieldFormatter {

    private TextFieldFormatter() {
        throw new UnsupportedOperationException("Classe utilitária");
    }

    public static void applyMask(TextField textField, String mask) {
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue == null) {
                return;
            }

            // Se estiver apagando, permite a operação
            if (oldValue != null && oldValue.length() > newValue.length()) {
                return;
            }

            String value = newValue.replaceAll("[^0-9]", "");
            StringBuilder formatted = new StringBuilder();
            
            int maskIndex = 0;
            int valueIndex = 0;
            
            while (maskIndex < mask.length() && valueIndex < value.length()) {
                char maskChar = mask.charAt(maskIndex);
                
                if (maskChar == '#') {
                    formatted.append(value.charAt(valueIndex));
                    valueIndex++;
                } else {
                    formatted.append(maskChar);
                }
                
                maskIndex++;
            }

            String formattedText = formatted.toString();
            if (!formattedText.equals(newValue)) {
                textField.setText(formattedText);
            }
        });

        // Adiciona um filtro para limitar o tamanho máximo
        textField.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null && newValue.length() > mask.length()) {
                textField.setText(oldValue);
            }
        });
    }
}
