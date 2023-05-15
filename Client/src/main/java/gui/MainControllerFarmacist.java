package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.util.ArrayList;
import java.util.List;

public class MainControllerFarmacist implements ISpitalObserver {
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
    private TableColumn<MedicamentBasicDTO, String > tblColNume;
    @FXML
    private TableColumn<MedicamentBasicDTO, Integer> tblColDescriere;
    @FXML
    private Label lblFarm;
    private Farmacist farmacist;
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
                //initModel();
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

    public void onLogOut(ActionEvent actionEvent) {
        logout();
        System.exit(0);
    }

    public void loadTableDataComenzi() throws SpitalException {
        List<Comanda> listaParticDTO = server.findAllComenzi(this);
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
}
