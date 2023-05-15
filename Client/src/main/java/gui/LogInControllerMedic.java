package gui;

import domain.Medic;
import domain.MedicDTO;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.util.ArrayList;
import java.util.List;

public class LogInControllerMedic {
    @FXML
    private TextField txtFldEmail;
    @FXML
    private TextField txtFldParola;
    @FXML
    private ComboBox<String> cmbBoxSectie;

    Parent mainControllerParent;
    private ISpitalService server;
    private MainControllerMedic mainController;
    private final ObservableList<String> sectiiModel = FXCollections.observableArrayList();

    public void setServer(ISpitalService srv) {
        server = srv;
    }

    public void setMainController(MainControllerMedic mainController) {
        this.mainController = mainController;
    }

    public void setParent(Parent p){
        mainControllerParent=p;
    }

    public void initModel() throws SpitalException {
        List<String> sectii = new ArrayList<>();
        for (Medic medic : server.findAllMedici(mainController)) {
            sectii.add(medic.getSectie());
        }
        sectiiModel.setAll(sectii);
    }

    @FXML
    public void initialize(ISpitalService srv) throws SpitalException {
        server = srv;
        cmbBoxSectie.setItems(sectiiModel);
        initModel();
    }


    public void onLogInPressed(ActionEvent actionEvent) {
        String email = txtFldEmail.getText();
        String parola = txtFldParola.getText();
        String sectie = cmbBoxSectie.getValue();

        if (email.isEmpty() || parola.isEmpty() || sectie.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
        else{
            MedicDTO medicDTO = new MedicDTO(email,parola,sectie);
            try {
                Medic medic = server.loginMedic(medicDTO, mainController);

                Stage stage = new Stage();
                stage.setTitle("Manage comenzi");
                stage.setScene(new Scene(mainControllerParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        mainController.logout();
                        System.exit(0);
                    }
                });
                stage.show();
                mainController.setMedic(medic);
                mainController.loadTableData();
                mainController.initialize(server);
                ((Node) (actionEvent.getSource())).getScene().getWindow().hide();
            }catch (SpitalException e){
                Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
                alert.setTitle("ERROR");
                alert.show();
            }
        }

    }

}
