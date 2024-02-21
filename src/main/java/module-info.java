module es.jpf.goliathbank_v2 {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;
    requires org.kordamp.bootstrapfx.core;
    requires com.almasb.fxgl.all;
    requires de.jensd.fx.glyphs.fontawesome;

    opens es.jpf.goliathbank_v2 to javafx.fxml;
    exports es.jpf.goliathbank_v2;
    exports es.jpf.goliathbank_v2.Controllers;
    exports es.jpf.goliathbank_v2.Controllers.Admin;
    exports es.jpf.goliathbank_v2.Controllers.Client;
    exports es.jpf.goliathbank_v2.Models;
    exports es.jpf.goliathbank_v2.Views;
}