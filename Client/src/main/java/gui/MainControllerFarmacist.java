package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Random;

public class MainControllerFarmacist implements ISpitalObserver {

    @FXML
    private Button btnVizualizareComanda;
    @FXML
    private TextField txtFldIdMed;
    @FXML
    private Button btnUpdateMeds;
    @FXML
    private Button btnStergeMedicament;
    @FXML
    private TextField txtFldDetaliiMedicament;
    @FXML
    private Button btnAddFarmacist;
    @FXML
    private TextField txtFldDenumireMedicament;
    @FXML
    private Button btnLogOut;
    @FXML
    private TableView<ComandaBasicDTO> tblVwComenzi;
    @FXML
    private TableColumn<ComandaBasicDTO, Integer> tblColIdComanda;
    @FXML
    private TableColumn<ComandaBasicDTO, String> tblColSectie;
    @FXML
    private TableColumn<ComandaBasicDTO, Integer> tblColStatus;
    @FXML
    private TableView<MedicamentBasicDTO> tblVwMedicamente;
    @FXML
    private TableColumn<MedicamentBasicDTO, Integer> tblColId;
    @FXML
    private TableColumn<MedicamentBasicDTO, String> tblColNume;
    @FXML
    private TableColumn<MedicamentBasicDTO, Integer> tblColDescriere;
    @FXML
    private Label lblFarm;
    private Farmacist farmacist;
    private Parent onoreazaParent;

    Parent mainControllerParent;

    private OnoreazaController onoreazaController;

    public void setOnoreazaController(OnoreazaController onoreazaController) {
        this.onoreazaController = onoreazaController;

    }

    public void setOnoreazaParent(Parent onoreazaParent) {
        this.onoreazaParent = onoreazaParent;
    }

    private ISpitalService server;
    private final ObservableList<MedicamentBasicDTO> medicamenteBasicDTOModel = FXCollections.observableArrayList();
    private final ObservableList<ComandaBasicDTO> comandaBasicDTOModel = FXCollections.observableArrayList();

    public MainControllerFarmacist() {
        System.out.println("Constructor main controller farmacist");
    }

    public MainControllerFarmacist(ISpitalService server) {
        this.server = server;
        System.out.println("Constructor main controller farmacist cu parametru server");
    }

    public void setServer(ISpitalService s) {
        server = s;
    }

    public void setFarmacist(Farmacist arb) {
        this.farmacist = arb;
        lblFarm.setText("Welcome, " + arb.getNume());
    }


    public void logout() {
        try {
            server.logoutFarmacist(farmacist, this);
        } catch (SpitalException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void updateComanda() throws SpitalException {
        Platform.runLater(() -> {
            try {
                loadTableDataComenzi();
            } catch (SpitalException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateMedicament() throws SpitalException {
        Platform.runLater(() -> {
            try {
                loadTableDataMedicamente();
                //initModel();
            } catch (SpitalException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateElementComanda() throws SpitalException {

    }



    public void onLogOut(ActionEvent actionEvent) {
        logout();
        System.exit(0);
    }

    public void loadTableDataComenzi() throws SpitalException {
        List<Comanda> comenziFinal = server.findAllComenzi(this);
        List<Comanda> listaParticDTO = new ArrayList<>();
        for(var c:comenziFinal){
            if(c.getStatusComanda() == StatusComanda.IN_ASTEPTARE){
                listaParticDTO.add(c);
            }
        }
        List<ComandaBasicDTO> cmds = new ArrayList<>();

        for (var a : listaParticDTO) {
            ComandaBasicDTO cmd = new ComandaBasicDTO(a.getId(), a.getSectie(), a.getStatusComanda());
            cmds.add(cmd);
        }
        comandaBasicDTOModel.setAll(cmds);

    }

    public void loadTableDataMedicamente() throws SpitalException {
        List<Medicament> listaParticDTO = server.findAllMedicamente(this);

        List<MedicamentBasicDTO> cmds = new ArrayList<>();

        for (var a : listaParticDTO) {
            MedicamentBasicDTO cmd = new MedicamentBasicDTO(a.getId(), a.getDenumire(), a.getDetalii());
            cmds.add(cmd);
        }
        medicamenteBasicDTOModel.setAll(cmds);

    }

    public void initialize(ISpitalService srv) {
        //aici trebuie numele atributelor date in clasa
        tblColIdComanda.setCellValueFactory(new PropertyValueFactory<>("idComanda"));
        tblColSectie.setCellValueFactory(new PropertyValueFactory<>("sectie"));
        tblColStatus.setCellValueFactory(new PropertyValueFactory<>("statusComanda"));
        tblVwComenzi.setItems(comandaBasicDTOModel);
        this.server = srv;

        tblColId.setCellValueFactory(new PropertyValueFactory<>("medicamentId"));
        tblColNume.setCellValueFactory(new PropertyValueFactory<>("denumire"));
        tblColDescriere.setCellValueFactory(new PropertyValueFactory<>("detalii"));
        tblVwMedicamente.setItems(medicamenteBasicDTOModel);

    }

    public void onAddMedicament(ActionEvent actionEvent) throws SpitalException {
        String denumire = txtFldDenumireMedicament.getText();
        String detalii = txtFldDetaliiMedicament.getText();
        Random rand = new Random();
        Integer id = rand.nextInt(50000) + 1;
        Medicament medicament = new Medicament(id, denumire, detalii);
        server.addMedicament(medicament, this);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Medicament adaugat cu succes!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();
    }

    public void onStergeMedicament(ActionEvent actionEvent) throws SpitalException {
        MedicamentBasicDTO selectedMedicament = tblVwMedicamente.getSelectionModel().getSelectedItem();
        List<Medicament> meds = server.findAllMedicamente(this);
        Medicament medicament = null;
        for (var cmd : meds) {
            if (Objects.equals(cmd.getId(), selectedMedicament.getMedicamentId()))
                medicament = cmd;
        }
        server.stergeMedicament(medicament, this);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Medicament sters cu succes!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();
    }

    public void onUpdateMeds(ActionEvent actionEvent) throws SpitalException {
        Integer id = Integer.valueOf(txtFldIdMed.getText());
        String denumire = txtFldDenumireMedicament.getText();
        String detalii = txtFldDetaliiMedicament.getText();

        List<Medicament> meds = server.findAllMedicamente(this);
        Medicament medicament = null;
        for (var cmd : meds) {
            if (Objects.equals(cmd.getId(), id)) {
                medicament = cmd;
            }
        }

        medicament.setDetalii(detalii);
        medicament.setDenumire(denumire);
        server.updateMedicament(medicament, this);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Medicament actualizat cu succes!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();
    }

    public void onVizualizareComanda(ActionEvent actionEvent) {
        ComandaBasicDTO selectedComanda = tblVwComenzi.getSelectionModel().getSelectedItem();
        try {
            Stage stage = new Stage();
            stage.setTitle("Detalii comanda selectata");
            stage.setScene(new Scene(onoreazaParent));

            stage.show();
            onoreazaController.setComanda(selectedComanda);
            onoreazaController.setFarmacist(farmacist);
            onoreazaController.initialize(server);
            onoreazaController.loadTableDataElemCmd();


        } catch (SpitalException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
    }
}
