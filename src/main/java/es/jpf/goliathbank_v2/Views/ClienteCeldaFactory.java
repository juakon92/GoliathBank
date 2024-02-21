package es.jpf.goliathbank_v2.Views;

import es.jpf.goliathbank_v2.Controllers.Admin.ClienteCeldaController;
import es.jpf.goliathbank_v2.Models.Cliente;
import javafx.fxml.FXMLLoader;
import javafx.scene.control.ListCell;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class ClienteCeldaFactory extends ListCell<Cliente> {
    @Override
    protected void updateItem(Cliente cliente, boolean empty) {
        super.updateItem(cliente, empty);
        if (empty){
            setText(null);
            setGraphic(null);
        }else {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Fxml/Admin/ClienteCelda.fxml"));
                ClienteCeldaController controller = new ClienteCeldaController(cliente);
                loader.setController(controller);

                Pane pane = loader.load();

                setGraphic(pane);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
