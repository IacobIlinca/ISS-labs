import gui.*;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import rpcprotocol.SpitalServicesRpcProxy;
import service.ISpitalService;

import java.io.IOException;
import java.util.Properties;

public class StartRpcClientFX extends Application {
    private Stage primaryStage;

    private static int defaultChatPort = 55555;
    private static String defaultServer = "localhost";

    public static void main(String[] args) {
        launch(args);
    }

    public void start(Stage primaryStage) throws Exception {
        System.out.println("In start");
        Properties clientProps = new Properties();
        try {
            clientProps.load(StartRpcClientFX.class.getResourceAsStream("/client.properties"));
            System.out.println("Client properties set. ");
            clientProps.list(System.out);
        } catch (IOException e) {
            System.err.println("Cannot find client.properties " + e);
            return;
        }
        String serverIP = clientProps.getProperty("spital.server.host", defaultServer);
        int serverPort = defaultChatPort;

        try {
            serverPort = Integer.parseInt(clientProps.getProperty("spital.server.port"));
        } catch (NumberFormatException ex) {
            System.err.println("Wrong port number " + ex.getMessage());
            System.out.println("Using default port: " + defaultChatPort);
        }
        System.out.println("Using server IP " + serverIP);
        System.out.println("Using server port " + serverPort);

        ISpitalService server = new SpitalServicesRpcProxy(serverIP, serverPort);


        FXMLLoader loader = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/login-medic.fxml"));
        Parent root = loader.load();


        LogInControllerMedic ctrlMedic =
                loader.<LogInControllerMedic>getController();
        ctrlMedic.initialize(server);
        ctrlMedic.setServer(server);

        FXMLLoader cloader = new FXMLLoader(
                getClass().getClassLoader().getResource("terminal_medic.fxml"));
        Parent croot = cloader.load();


        MainControllerMedic mainCtrlMedic =
                cloader.<MainControllerMedic>getController();
        mainCtrlMedic.setServer(server);

        ctrlMedic.setMainController(mainCtrlMedic);
        ctrlMedic.setParent(croot);

        primaryStage.setTitle("Log in medic");
        primaryStage.setScene(new Scene(root, 600, 600));
        primaryStage.show();


        /////
        FXMLLoader loader2 = new FXMLLoader(
                getClass().getClassLoader().getResource("scenes/login-farmacist.fxml"));
        Parent root2 = loader2.load();


        LogInControllerFarmacist ctrlFarm = loader2.<LogInControllerFarmacist>getController();
        ctrlFarm.initialize(server);
        ctrlFarm.setServer(server);

        FXMLLoader cloader2 = new FXMLLoader(
                getClass().getClassLoader().getResource("terminal_farmacist.fxml"));
        Parent croot2 = cloader2.load();


        MainControllerFarmacist mainCtrlFarm =
                cloader2.<MainControllerFarmacist>getController();
        mainCtrlFarm.setServer(server);

        ctrlFarm.setMainController(mainCtrlFarm);
        ctrlFarm.setParent(croot2);

        FXMLLoader loaderDetalii = new FXMLLoader(
                getClass().getClassLoader().getResource("detaliiCmd.fxml"));
        Parent detaliiParent = loaderDetalii.load();

        mainCtrlMedic.setParentDetalii(detaliiParent);
        DetaliiCmdController detaliiCmdController =
                loaderDetalii.<DetaliiCmdController>getController();

        mainCtrlMedic.setDetaliiCmdController(detaliiCmdController);
        detaliiCmdController.setServer(server);

        FXMLLoader loaderOnoreaza = new FXMLLoader(
                getClass().getClassLoader().getResource("onoreaza.fxml"));
        Parent onoreazaParent = loaderOnoreaza.load();

        mainCtrlFarm.setOnoreazaParent(onoreazaParent);
        OnoreazaController onoreazaController =
                loaderOnoreaza.<OnoreazaController>getController();

        mainCtrlFarm.setOnoreazaController(onoreazaController);
        onoreazaController.setServer(server);


        Stage stage = new Stage();
        stage.setTitle("Log in farmacist");
        stage.setScene(new Scene(root2, 600, 600));
        stage.show();


    }
}
