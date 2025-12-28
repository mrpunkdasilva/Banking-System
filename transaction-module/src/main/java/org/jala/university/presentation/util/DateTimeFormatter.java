package org.jala.university.presentation.util;

import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.util.Callback;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.util.Locale;

/**
 * Formatter for LocalDateTime values in TableView cells.
 * Formats dates in Brazilian format (dd/MM/yyyy HH:mm).
 *
 * @param <T> The type of the TableView items
 */
public class DateTimeFormatter<T> extends TableCell<T, LocalDateTime> {

    private final java.time.format.DateTimeFormatter formatter;

    public DateTimeFormatter() {
        // Create a formatter for Brazilian date format
        formatter = new DateTimeFormatterBuilder()
                .appendPattern("dd/MM/yyyy HH:mm")
                .parseDefaulting(ChronoField.SECOND_OF_MINUTE, 0)
                .toFormatter(new Locale("pt", "BR"));
    }

    @Override
    protected void updateItem(LocalDateTime dateTime, boolean empty) {
        super.updateItem(dateTime, empty);

        if (empty || dateTime == null) {
            setText(null);
            setGraphic(null);
        } else {
            // Format the date and time
            setText(formatter.format(dateTime));
        }
    }

    /**
     * Factory method to create a DateTimeFormatter for a TableColumn.
     *
     * @param <S> The type of the TableView items
     * @return A callback that creates a DateTimeFormatter
     */
    public static <S> Callback<TableColumn<S, LocalDateTime>, TableCell<S, LocalDateTime>> getFactory() {
        return column -> new DateTimeFormatter<>();
    }
}