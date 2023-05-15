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

public class SpitalClientRpcWorker implements Runnable, ISpitalObserver {
    private ISpitalService server;
    private Socket connection;

    private ObjectInputStream input;
    private ObjectOutputStream output;
    private volatile boolean connected;
    public SpitalClientRpcWorker(ISpitalService server, Socket connection) {
        this.server = server;
        this.connection = connection;
        try{
            output=new ObjectOutputStream(connection.getOutputStream());
            output.flush();
            input=new ObjectInputStream(connection.getInputStream());
            connected=true;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public void run() {
        while(connected){
            try {
                Object request=input.readObject();
                Response response=handleRequest((Request)request);
                if (response!=null){
                    sendResponse(response);
                }
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        try {
            input.close();
            output.close();
            connection.close();
        } catch (IOException e) {
            System.out.println("Error "+e);
        }
    }

    private void sendResponse(Response response) throws IOException{
        System.out.println("sending response "+response);
        synchronized (output) {
            output.writeObject(response);
            output.flush();
        }
    }


    private static Response okResponse=new Response.Builder().type(ResponseType.OK).build();

    @Override
    public void updateComanda() throws SpitalException {
        Response resp = new Response.Builder().type(ResponseType.ADDED_COMANDA).build();
        System.out.println("Added result");
        try{
            sendResponse(resp);
        } catch (IOException e){
            e.printStackTrace();
        }    }

    @Override
    public void updateMedicament() throws SpitalException {

    }

    private Response handleRequest(Request request){
        Response response=null;
        if (request.type()== RequestType.LOGIN_MEDIC){
            System.out.println("Login medic request ..."+request.type());
            MedicDTO dto = (MedicDTO) request.data();
            try {
                Medic medic = server.loginMedic(dto, this);
                return new Response.Builder().type(ResponseType.LOGGED_IN_MEDIC).data(medic).build();
            }
            catch (SpitalException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGIN_FARMACIST){
            System.out.println("Login farmacist request ..."+request.type());
            FarmacistDTO dto = (FarmacistDTO) request.data();
            try {
                Farmacist farm = server.loginFarmacist(dto, this);
                return new Response.Builder().type(ResponseType.LOGGED_IN_FARMACIST).data(farm).build();
            }
            catch (SpitalException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if (request.type()== RequestType.LOGOUT_MEDIC){
            System.out.println("Logout medic request");
            Medic md = (Medic) request.data();
            try{
                server.logoutMedic(md, this);
                connected = false;
                return okResponse;
            }
            catch (SpitalException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if (request.type()== RequestType.LOGOUT_FARMACIST){
            System.out.println("Logout farmacist request");
            Farmacist md = (Farmacist) request.data();
            try{
                server.logoutFarmacist(md, this);
                connected = false;
                return okResponse;
            }
            catch (SpitalException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }

        if(request.type() == RequestType.FIND_ALL_COMENZI) {
//            int pid=(int)request.data();
            System.out.println("Get all comenzi");
            try {
                List<Comanda> comenzi = server.findAllComenzi(this);
                return new Response.Builder().type(ResponseType.FOUND_ALL_COMANDA).data(comenzi).build();
            } catch (SpitalException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.FIND_ALL_MEDICAMENTE) {
//            int pid=(int)request.data();
            System.out.println("Get all medicamente");
            try {
                List<Medicament> medicamente = server.findAllMedicamente(this);
                return new Response.Builder().type(ResponseType.FOUND_ALL_MEDICAMENTE).data(medicamente).build();
            } catch (SpitalException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.FIND_ALL_MEDICI) {
            System.out.println("Get all medici");
            try {
                List<Medic> medici = server.findAllMedici(this);
                return new Response.Builder().type(ResponseType.FOUND_ALL_MEDICI).data(medici).build();
            } catch (SpitalException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.FIND_MEDICAMENT_BY_DENUMIRE) {
            String  denumie = request.data().toString();
            System.out.println("Find medicament by denumire");
            try {
                Medicament medicament = server.findByDenumire(denumie,this);
                return new Response.Builder().type(ResponseType.FOUND_MEDICAMENT_BY_DENUMIRE).data(medicament).build();
            } catch (SpitalException e) {
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        if(request.type() == RequestType.ADD_COMANDA) {
            ComandaDTO cmd = (ComandaDTO) request.data();
            System.out.println("Adding comanda");
            try{
                server.addComanda(cmd, this);
                return new Response.Builder().type(ResponseType.OK).build();
            } catch (SpitalException e){
                return new Response.Builder().type(ResponseType.ERROR).data(e.getMessage()).build();
            }
        }
        return response;
    }
}
