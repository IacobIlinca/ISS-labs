package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.net.URL;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.ResourceBundle;

public class MainControllerMedic implements  ISpitalObserver {

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
    private Medic medic;
    private final ObservableList<String> denumiriModel = FXCollections.observableArrayList();

    private ComandaDTO comandaDTO = new ComandaDTO("", StatusComanda.IN_ASTEPTARE, medic);
    private final ObservableList<ComandaBasicDTO> comandaBasicDTOModel = FXCollections.observableArrayList();


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
}
