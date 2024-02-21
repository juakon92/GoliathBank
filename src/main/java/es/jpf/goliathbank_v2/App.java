package es.jpf.goliathbank_v2;

import es.jpf.goliathbank_v2.Models.Model;
import javafx.application.Application;
import javafx.stage.Stage;

public class App extends Application {
    @Override
    public void start(Stage stage){
        Model.getInstance().getFactoriaView().mostrarVentanaLogin();
    }
}
