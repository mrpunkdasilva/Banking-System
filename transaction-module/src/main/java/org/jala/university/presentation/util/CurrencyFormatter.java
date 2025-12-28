package org.jala.university.presentation.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

/**
 * Formatter for currency values in TableView cells.
 * Formats BigDecimal values as Brazilian Real currency.
 *
 * @param <T> The type of the TableView items
 */
public class CurrencyFormatter<T> extends TableCell<T, BigDecimal> {

    private final NumberFormat currencyFormat;

    public CurrencyFormatter() {
        // Use Brazilian locale for currency formatting
        currencyFormat = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
    }

    @Override
    protected void updateItem(BigDecimal amount, boolean empty) {
        super.updateItem(amount, empty);

        if (empty || amount == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Format the amount as currency
            setText(currencyFormat.format(amount));
            
            // Add styling based on positive/negative values
            if (amount.compareTo(BigDecimal.ZERO) < 0) {
                // Negative amount (debit)
                getStyleClass().add("negative-amount");
                getStyleClass().remove("positive-amount");
            } else {
                // Positive amount (credit)
                getStyleClass().add("positive-amount");
                getStyleClass().remove("negative-amount");
            }
        }
    }

    /**
     * Factory method to create a CurrencyFormatter for a TableColumn.
     *
     * @param <S> The type of the TableView items
     * @return A callback that creates a CurrencyFormatter
     */
    public static <S> Callback<TableColumn<S, BigDecimal>, TableCell<S, BigDecimal>> getFactory() {
        return column -> new CurrencyFormatter<>();
    }
}