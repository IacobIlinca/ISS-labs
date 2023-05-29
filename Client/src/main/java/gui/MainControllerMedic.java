package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.*;

public class MainControllerMedic implements ISpitalObserver {

    @FXML
    private Button btnStergeComanda;
    @FXML
    private Button btnDetaliiComanda;
    @FXML
    private Button btnPlaseazaComanda;
    @FXML
    private TextField txtFldObservatii;
    @FXML
    private Button btnAddElemComanda;
    @FXML
    private Button btnLogOut;
    @FXML
    private Label lblMedic;
    @FXML
    private TableView<ComandaBasicDTO> tblVwComenzi;
    @FXML
    private TableColumn<ComandaBasicDTO, Integer> tblColId;
    @FXML
    private TableColumn<ComandaBasicDTO, String> tblColSectie;
    @FXML
    private TableColumn<ComandaBasicDTO, String> tblColStatus;
    @FXML
    private ComboBox<String> cmbBoxMedicament;
    @FXML
    private TextField txtFldCantitate;

    private List<ElementComandaDTO> elementComandaDTOS;

    private ISpitalService server;
    Parent mainControllerParent;

    private DetaliiCmdController detaliiCmdController;

    public void setDetaliiCmdController(DetaliiCmdController detaliiCmdController) {
        this.detaliiCmdController = detaliiCmdController;
    }

    private Parent detaliiParent;
    private Medic medic;
    private final ObservableList<String> denumiriModel = FXCollections.observableArrayList();

    private ComandaDTO comandaDTO = new ComandaDTO("", StatusComanda.IN_ASTEPTARE, medic);
    private final ObservableList<ComandaBasicDTO> comandaBasicDTOModel = FXCollections.observableArrayList();

    public void setParentDetalii(Parent detaliiParent) {
        this.detaliiParent = detaliiParent;
    }

    public void setParent(Parent p) {
        mainControllerParent = p;
    }

    public MainControllerMedic() {
        System.out.println("Constructor main controller medic");
    }

    public MainControllerMedic(ISpitalService server) {
        this.server = server;
        System.out.println("Constructor main controller medic cu parametru server");
    }

    public void setServer(ISpitalService s) {
        server = s;
    }

    public void setMedic(Medic arb) {
        this.medic = arb;
        this.comandaDTO.setCreator(arb);
        this.comandaDTO.setSectie(medic.getSectie());
        lblMedic.setText("Welcome, " + arb.getNume());
    }

    public void initModel() throws SpitalException {
        List<String> denumiriMedicamente = new ArrayList<>();
        for (Medicament medicament : server.findAllMedicamente(this)) {
            denumiriMedicamente.add(medicament.getDenumire());
        }
        denumiriModel.setAll(denumiriMedicamente);
    }


    //@Override
    public void initialize(ISpitalService srv) {
        //aici trebuie numele atributelor date in clasa
        tblColId.setCellValueFactory(new PropertyValueFactory<>("idComanda"));
        tblColSectie.setCellValueFactory(new PropertyValueFactory<>("sectie"));
        tblColStatus.setCellValueFactory(new PropertyValueFactory<>("statusComanda"));
        tblVwComenzi.setItems(comandaBasicDTOModel);
        this.server = srv;
        cmbBoxMedicament.setItems(denumiriModel);
        try {
            initModel();
        } catch (SpitalException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void updateComanda() throws SpitalException {
        Platform.runLater(() -> {
            try {
                loadTableData();
                //initModel();
            } catch (SpitalException e) {
                throw new RuntimeException(e);
            }
        });
    }

    @Override
    public void updateMedicament() throws SpitalException {

    }

    @Override
    public void updateElementComanda() throws SpitalException {

    }

    public void logout() {
        try {
            server.logoutMedic(medic, this);
        } catch (SpitalException e) {
            e.printStackTrace();
        }
    }

    public void onLogOut(ActionEvent actionEvent) {
        logout();
        System.exit(0);
    }

    public void loadTableData() throws SpitalException {
        List<Comanda> listaParticDTO = server.findAllComenzi(this);
        List<ComandaBasicDTO> cmds = new ArrayList<>();

        for (var a : listaParticDTO) {
            ComandaBasicDTO cmd = new ComandaBasicDTO(a.getId(), a.getSectie(), a.getStatusComanda());
            cmds.add(cmd);
        }
        comandaBasicDTOModel.setAll(cmds);

    }

    public void onAdaugaElemComanda(ActionEvent actionEvent) throws SpitalException {
        String denumire = cmbBoxMedicament.getValue();
        Medicament medicament = server.findByDenumire(denumire, this);
        Integer cantitate = Integer.valueOf(txtFldCantitate.getText());
        String observatii = txtFldObservatii.getText();

        ElementComandaDTO elemCmd = new ElementComandaDTO(cantitate, observatii);
        var map = comandaDTO.getMedicamentElementComandDTOMap();
        map.put(medicament, elemCmd);
        txtFldCantitate.clear();
        txtFldObservatii.clear();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Element adaugat cu succes la comanda!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();

    }

    public void onPlaseazaComanda(ActionEvent actionEvent) throws SpitalException {
        comandaDTO.setAn(LocalDateTime.now().getYear());
        comandaDTO.setLuna(LocalDateTime.now().getMonthValue());
        comandaDTO.setZi(LocalDateTime.now().getDayOfMonth());
        comandaDTO.setOra(LocalDateTime.now().getHour());
        comandaDTO.setMinut(LocalDateTime.now().getMinute());

        server.addComanda(comandaDTO, this);
        this.comandaDTO = new ComandaDTO(medic.getSectie(), StatusComanda.IN_ASTEPTARE, medic);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Comanda inregistarata cu succes!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();

    }

    public void onStergeComanda(ActionEvent actionEvent) throws SpitalException {
        ComandaBasicDTO selectedComanda = tblVwComenzi.getSelectionModel().getSelectedItem();
        List<Comanda> comenzi = server.findAllComenzi(this);
        Comanda comanda = null;
        for (var cmd : comenzi) {
            if (Objects.equals(cmd.getId(), selectedComanda.getIdComanda()))
                comanda = cmd;
        }
        server.stergeComanda(comanda, this);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Comanda stearsa cu succes!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();
    }

    public void onDetaliiComanda(ActionEvent actionEvent) {
        ComandaBasicDTO selectedComanda = tblVwComenzi.getSelectionModel().getSelectedItem();
        try {
            Stage stage = new Stage();
            stage.setTitle("Detalii comanda selectata");
            stage.setScene(new Scene(detaliiParent));

            stage.show();
            detaliiCmdController.setComanda(selectedComanda);
            detaliiCmdController.initialize(server);
            detaliiCmdController.loadTableDataElemCmd();

        } catch (SpitalException e) {
            Alert alert = new Alert(Alert.AlertType.NONE, e.getMessage(), ButtonType.OK);
            alert.setTitle("ERROR");
            alert.show();
        }
    }
}

