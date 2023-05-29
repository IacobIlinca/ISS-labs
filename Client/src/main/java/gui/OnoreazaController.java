package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class OnoreazaController implements ISpitalObserver {

    @FXML
    public TableView<ElemCmd> tblVwElemCmd;
    @FXML
    public TableColumn<ElemCmd, Integer> tblColId;
    @FXML
    public TableColumn<ElemCmd, String> tblColMed;
    @FXML
    public TableColumn<ElemCmd, Integer> tblcColCantitate;
    @FXML
    public TableColumn<ElemCmd, String> tblColObservatii;
    @FXML
    public TableColumn<ElemCmd, String> tblColCoamnadaRef;
    @FXML
    public Button btnOnoreazaCmd;

    public Farmacist farmacist;

    private ISpitalService server;

    private ComandaBasicDTO comandaDTO;
    private final ObservableList<ElemCmd> elemCmdModel = FXCollections.observableArrayList();

    public OnoreazaController() {
        System.out.println("Constructor onoreaza");
    }

    public OnoreazaController(ISpitalService server) {
        this.server = server;
        System.out.println("Constructor onoreaza controller cu parametru server");
    }

    public void setFarmacist(Farmacist farmacist) {
        this.farmacist = farmacist;
    }

    public void setServer(ISpitalService srv) {
        server = srv;
    }

    public void setComanda(ComandaBasicDTO arb) {
        this.comandaDTO = arb;
    }

    @FXML
    public void initialize(ISpitalService srv) throws SpitalException {
        this.server = srv;
        //aici trebuie numele atributelor date in clasa
        tblColId.setCellValueFactory(new PropertyValueFactory<>("id"));
        tblcColCantitate.setCellValueFactory(new PropertyValueFactory<>("cantitate"));
        tblColObservatii.setCellValueFactory(new PropertyValueFactory<>("observatii"));
        tblColMed.setCellValueFactory(new PropertyValueFactory<>("numeMedicament"));
        tblColCoamnadaRef.setCellValueFactory(new PropertyValueFactory<>("comandaReferita"));
        tblVwElemCmd.setItems(elemCmdModel);
    }

    @Override
    public void updateComanda() throws SpitalException {

    }

    @Override
    public void updateMedicament() throws SpitalException {

    }

    @Override
    public void updateElementComanda() throws SpitalException {
        Platform.runLater(() -> {
            try {
                loadTableDataElemCmd();
                //initModel();
            } catch (SpitalException e) {
                throw new RuntimeException(e);
            }
        });
    }

    public void loadTableDataElemCmd() throws SpitalException {
        List<ElemCmd> elems = new ArrayList<>();
        List<ElementComanda> elementeComanda = server.findAllElementeComanda(this);
        for (var el : elementeComanda) {
            if (Objects.equals(el.getComadaReferita().getId(), comandaDTO.getIdComanda())) {
                ElemCmd e = new ElemCmd(el.getId(), el.getCantitate(), el.getObservatii(), el.getMedicament().getDenumire(), el.getComadaReferita().getId());
                elems.add(e);
            }
        }

        elemCmdModel.setAll(elems);
    }

    public void onOnoreazaComanda(ActionEvent actionEvent) throws SpitalException {
        comandaDTO.setStatusComanda(StatusComanda.ONORATA);
        List<Comanda> comenzi = server.findAllComenzi(this);
        for (var c : comenzi) {
            if (Objects.equals(c.getId(), comandaDTO.getIdComanda())) {
                c.setFarmacist(farmacist);
                c.setStatusComanda(StatusComanda.ONORATA);
                farmacist.addToComenziOnorate(c);
                server.updateComanda(c, this);
            }
        }
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Comanda onorata!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();
        ((Node) (actionEvent.getSource())).getScene().getWindow().hide();

    }


}
