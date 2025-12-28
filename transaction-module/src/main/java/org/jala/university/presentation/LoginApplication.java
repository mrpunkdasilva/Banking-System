package org.jala.university.presentation;

/*
* ⠀⠀⠀⠀⠀⠀⠀⢀⣠⣤⠶⠒⠒⠞⠛⠒⠶⠶⠦⣄⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⢠⡴⡺⠟⠓⠲⣍⠀⠀⠀⠀⠀⠀⠀⠀⠀⠙⡛⢦⡀⠀⠀⠀⠀⠀⠀
⠀⢠⣶⠶⠋⢸⠥⠤⣤⣀⠈⡆⠀⠀⢀⠀⠀⠀⠀⠀⣀⠬⢍⡛⢢⣄⠀⠀⠀⠀
⠀⠘⡏⠀⠀⠘⢆⠀⠀⢀⡽⠁⠀⠰⡁⠀⠀⠀⢀⡎⠀⠀⠀⠈⢆⠙⣆⠀⠀⠀
⠀⠀⢷⠀⠀⠀⠀⠉⠉⠉⠀⠀⢺⠤⠤⠤⡆⠀⠸⡍⠉⠙⠓⢤⡜⠀⠉⢷⠀⠀
⠀⠀⠘⡆⠀⠀⠀⠀⠀⠀⠀⠀⠟⠢⠬⠋⠀⠀⠀⠈⠢⠤⠤⠚⠁⠀⠀⠚⣧⠀
⠀⠀⠀⠘⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢿⢦
⠀⠀⢀⡀⠈⢦⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⣦⣭⠟
⢠⣴⠟⢱⡤⠤⢝⣦⣄⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢀⡴⠋⠀⠀⠀
⣿⣒⣀⣟⠀⠀⠀⠀⢨⢹⣶⣦⣤⣀⣀⠀⠀⠀⠀⢀⣀⣠⡴⠾⢭⡤⠞⣹⠀⠀
⠀⠀⠈⠉⠛⠲⠤⢤⣯⡞⣿⡃⠀⠈⣹⣿⡟⠉⠉⠉⠀⠀⠀⠀⢸⠑⠄⠽⣶⠀
⠀⠀⠀⠀⠀⠀⠀⢸⣿⣿⣿⣿⣶⣶⣏⣿⠁⠀⣀⣠⡤⠤⠤⠴⠾⠷⢮⡿⠟⠀
⠀⠀⠀⠀⠀⠀⢀⣸⣿⣿⣿⣿⣿⣿⣿⣿⣷⣤⣽⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⣴⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣷⡀⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⣿⡇⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⢀⡞⠙⠻⠿⠿⢿⡏⠛⠻⢿⣿⣿⣿⣿⣿⣿⣧⠀⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠈⠳⠦⠤⠴⠖⠛⠁⠀⠀⠀⠈⠻⣿⣿⣿⣿⣿⡆⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠈⣿⣿⡿⠟⡇⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⢰⠋⠀⠀⢰⠇⠀⠀⠀⠀⠀⠀⠀⠀
⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠀⠸⢦⣤⡴⠋⠀⠀⠀⠀⠀⠀⠀⠀⠀
*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import org.jala.university.application.service.impl.CronService;
import org.jala.university.infrastructure.utils.DatabaseEventInitializer;
import org.jala.university.presentation.util.ViewSwitcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Timer;

public class LoginApplication extends Application {

    private static final double SCENE_WIDTH = 750.0;
    private static final double SCENE_HEIGHT = 550.0;

    @Override
    public final void start(Stage primaryStage) throws Exception {
        // Inicializar o evento de limpeza de códigos expirados
        DatabaseEventInitializer.initializeCleanupEvent();

        // Carrega o arquivo FXML
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/fxml/login-view.fxml"));
        Parent root = loader.load();

        // Aplica o CSS
        root.getStylesheets().add(getClass().getResource("/styles/styles.css").toExternalForm());

        // Configura a cena
        Scene scene = new Scene(root, SCENE_WIDTH, SCENE_HEIGHT);
        scene.setFill(Color.TRANSPARENT);

        ViewSwitcher.setScene(scene);

        // Configura o palco
        primaryStage.setTitle("Login");
        primaryStage.setScene(scene);
        primaryStage.initStyle(StageStyle.TRANSPARENT);
        primaryStage.show();
    }

    public static void main(String[] args) {

        Timer timer = new Timer();
        CronService cronService = new CronService();

        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        // If midnight has passed today, schedule for tomorrow
        if (calendar.getTime().before(new Date())) {
            calendar.add(Calendar.DAY_OF_MONTH, 1);
        }

        long delay = calendar.getTimeInMillis() - System.currentTimeMillis();

        try {
            timer.scheduleAtFixedRate(cronService, delay, 24 * 60 * 60 * 1000);
            launch(args);
        } catch (Exception e) {
            System.out.println("Error scheduling cron service" + e);
            System.exit(1);
        }

        // Cleanup hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            timer.cancel();
            System.out.println("Timer cancelled on shutdown");
        }));
    }
}
