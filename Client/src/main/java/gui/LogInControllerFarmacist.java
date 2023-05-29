package gui;

import domain.Farmacist;
import domain.FarmacistDTO;
import domain.Medic;
import domain.MedicDTO;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.ISpitalService;
import service.SpitalException;

public class LogInControllerFarmacist {

    @FXML
    private Button btnLogInFarmacist;
    @FXML
    private TextField txtFldParola;
    @FXML
    private TextField txtFldEmail;

    Parent mainControllerParent;
    private ISpitalService server;
    private MainControllerFarmacist mainController;

    public void setServer(ISpitalService srv) {
        server = srv;
    }

    public void setMainController(MainControllerFarmacist mainController) {
        this.mainController = mainController;
    }

    public void setParent(Parent p) {
        mainControllerParent = p;
    }

    @FXML
    public void initialize(ISpitalService srv) throws SpitalException {
        server = srv;
    }

    public void onLogInPressed(ActionEvent actionEvent) {
        String email = txtFldEmail.getText();
        String parola = txtFldParola.getText();
        if (email.isEmpty() || parola.isEmpty() ) {
            Alert alert = new Alert(Alert.AlertType.NONE, "Date invalide", ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
        else{
            FarmacistDTO farmacistDTO = new FarmacistDTO(email,parola);
            try {
                Farmacist farmacist = server.loginFarmacist(farmacistDTO, mainController);

                Stage stage = new Stage();
                stage.setTitle("Manage comenzi si medicamente");
                stage.setScene(new Scene(mainControllerParent));

                stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                    @Override
                    public void handle(WindowEvent event) {
                        mainController.logout();
                        System.exit(0);
                    }
                });
                stage.show();
                mainController.setFarmacist(farmacist);
                mainController.loadTableDataMedicamente();
                mainController.loadTableDataComenzi();
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
