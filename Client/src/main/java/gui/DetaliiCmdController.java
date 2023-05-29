package gui;

import domain.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class DetaliiCmdController implements ISpitalObserver {

    @FXML
    private TextField txtFldlId;
    @FXML
    private TextField txtFldCantitateNoua;
    @FXML
    private Button btnUpdateCant;
    @FXML
    private TableColumn<ElemCmd, Integer> tblColId;
    @FXML
    private TextField txtFlxdCmd;
    @FXML
    private TableView<ElemCmd> tblVwElemCmd;
    @FXML
    private TableColumn<ElemCmd, String> tblColMed;
    @FXML
    private TableColumn<ElemCmd, Integer> tblcColCantitate;
    @FXML
    private TableColumn<ElemCmd, String > tblColObservatii;
    @FXML
    private TableColumn<ElemCmd, Integer> tblColCoamnadaRef;

    private ISpitalService server;

    private ComandaBasicDTO comandaDTO;
    private final ObservableList<ElemCmd> elemCmdModel = FXCollections.observableArrayList();

    public DetaliiCmdController() {
        System.out.println("Constructor main controller farmacist");
    }

    public DetaliiCmdController(ISpitalService server) {
        this.server = server;
        System.out.println("Constructor detalii comanda controller cu parametru server");
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

    public void loadTableDataElemCmd() throws SpitalException {
        List<ElemCmd> elems = new ArrayList<>();
        List<ElementComanda> elementeComanda = server.findAllElementeComanda(this);
        for(var el:elementeComanda){
            if(Objects.equals(el.getComadaReferita().getId(), comandaDTO.getIdComanda())){
                ElemCmd e = new ElemCmd(el.getId(),el.getCantitate(),el.getObservatii(), el.getMedicament().getDenumire(), el.getComadaReferita().getId());
                elems.add(e);
            }
        }

        elemCmdModel.setAll(elems);
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



    public void onUpdateCantitate(ActionEvent actionEvent) throws SpitalException {
        Integer id = Integer.valueOf(txtFldlId.getText());
        Integer cantitate = Integer.valueOf(txtFldCantitateNoua.getText());

        List<ElementComanda> elems = server.findAllElementeComanda(this);
        ElementComanda elementComanda = null;
        for (var cmd : elems) {
            if(Objects.equals(cmd.getId(), id)){
                elementComanda = cmd;
            }
        }

        elementComanda.setCantitate(cantitate);
        server.updateElementComanda(elementComanda, this);

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Elementul comenzii actualizat cu succes!", ButtonType.OK);
        alert.setTitle("OK");
        alert.show();
    }
}
