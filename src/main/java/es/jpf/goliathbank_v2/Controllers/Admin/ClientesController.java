package es.jpf.goliathbank_v2.Controllers.Admin;

import es.jpf.goliathbank_v2.Models.Cliente;
import es.jpf.goliathbank_v2.Models.Model;
import es.jpf.goliathbank_v2.Views.ClienteCeldaFactory;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class ClientesController implements Initializable {
    public ListView<Cliente> listado_clientes;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        List<Cliente> clientes = Model.getInstance().getDatabaseDriver().obtenerClientes();
        listado_clientes.setCellFactory(clienteListView -> new ClienteCeldaFactory());
        listado_clientes.getItems().addAll(clientes);
    }
}
