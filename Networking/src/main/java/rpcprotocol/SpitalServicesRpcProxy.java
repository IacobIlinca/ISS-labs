package rpcprotocol;

import domain.*;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public class SpitalServicesRpcProxy implements ISpitalService {
    private String host;
    private int port;

    private ISpitalObserver client;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private Socket connection;

    private BlockingQueue<Response> qresponses;
    private volatile boolean finished;

    public SpitalServicesRpcProxy(String host, int port) throws SpitalException {
        this.host = host;
        this.port = port;
        qresponses = new LinkedBlockingQueue<Response>();
        initializeConnection();
    }

    @Override
    public void logoutMedic(Medic medic, ISpitalObserver client) throws SpitalException {
        Request req = new Request.Builder().type(RequestType.LOGOUT_MEDIC).data(medic).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
    }

    @Override
    public void logoutFarmacist(Farmacist dto, ISpitalObserver client) throws SpitalException {
        Request req = new Request.Builder().type(RequestType.LOGOUT_FARMACIST).data(dto).build();
        sendRequest(req);
        Response response = readResponse();
        closeConnection();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
    }

    @Override
    public Medic loginMedic(MedicDTO medicDTO, ISpitalObserver client) throws SpitalException {

        Request req = new Request.Builder().type(RequestType.LOGIN_MEDIC).data(medicDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
        this.client = client;
        Medic arb = (Medic) response.data();
        return arb;
    }

    @Override
    public Farmacist loginFarmacist(FarmacistDTO farmacistDTO, ISpitalObserver client) throws SpitalException {

        Request req = new Request.Builder().type(RequestType.LOGIN_FARMACIST).data(farmacistDTO).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
        this.client = client;
        Farmacist arb = (Farmacist) response.data();
        return arb;
    }

    @Override
    public List<Comanda> findAllComenzi(ISpitalObserver client) throws SpitalException {
        Request req = new Request.Builder().type(RequestType.FIND_ALL_COMENZI).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
        List<Comanda> comenzi = (List<Comanda>) response.data();
        return comenzi;
    }

    @Override
    public List<Medicament> findAllMedicamente(ISpitalObserver client) throws SpitalException {
        Request req = new Request.Builder().type(RequestType.FIND_ALL_MEDICAMENTE).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
        List<Medicament> medicaments = (List<Medicament>) response.data();
        return medicaments;
    }

    @Override
    public List<Medic> findAllMedici(ISpitalObserver client) throws SpitalException {
        Request req = new Request.Builder().type(RequestType.FIND_ALL_MEDICI).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
        List<Medic> medici = (List<Medic>) response.data();
        return medici;
    }

    @Override
    public Medicament findByDenumire(String denumire, ISpitalObserver client) throws SpitalException {
        Request req = new Request.Builder().type(RequestType.FIND_MEDICAMENT_BY_DENUMIRE).data(denumire).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
        this.client = client;
        Medicament medicament = (Medicament) response.data();
        return medicament;
    }

    @Override
    public void addComanda(ComandaDTO comanda, ISpitalObserver client) throws SpitalException {
        Request req = new Request.Builder().type(RequestType.ADD_COMANDA).data(comanda).build();
        sendRequest(req);
        Response response = readResponse();
        if (response.type() == ResponseType.ERROR) {
            String err = response.data().toString();
            throw new SpitalException(err);
        }
    }

    private void closeConnection() {
        finished = true;
        try {
            input.close();
            output.close();
            connection.close();
            client = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sendRequest(Request request) throws SpitalException {
        try {
            output.writeObject(request);
            output.flush();
        } catch (IOException e) {
            throw new SpitalException("Error sending object " + e);
        }
    }

    private Response readResponse() throws SpitalException {
        Response response = null;
        try {

            response = qresponses.take();

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response;
    }

    private void initializeConnection() throws SpitalException {
        try {
            connection = new Socket(host, port);
            output = new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input = new ObjectInputStream(connection.getInputStream());
            finished = false;
            startReader();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void startReader() {
        Thread tw = new Thread(new ReaderThread());
        tw.start();
    }

    private void handleUpdateComenzi(Response response) {
        System.out.println("Proxy added result comenzi");
        try {
            client.updateComanda();
        } catch (SpitalException e) {
            e.printStackTrace();
        }

    }

    private void handleUpdateMedicamente(Response response) {

        System.out.println("Proxy added result medicamente");
        try {
            client.updateMedicament();
        } catch (SpitalException e) {
            e.printStackTrace();
        }

    }

    private boolean isUpdateComenzi(Response response) {
        return response.type() == ResponseType.ADDED_COMANDA;
    }

    private boolean isUpdateMedicamente(Response response) {
        return false;
    }

    private class ReaderThread implements Runnable {
        public void run() {
            while (!finished) {
                try {
                    Object response = input.readObject();
                    System.out.println("response received " + response);
                    if (isUpdateComenzi((Response) response)) {
                        handleUpdateComenzi((Response) response);
                    } else if (isUpdateMedicamente((Response) response)) {
                        handleUpdateMedicamente((Response) response);
                    } else {

                        try {
                            qresponses.put((Response) response);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Reading error " + e);
                } catch (ClassNotFoundException e) {
                    System.out.println("Reading error " + e);
                }
            }
        }
    }
}
