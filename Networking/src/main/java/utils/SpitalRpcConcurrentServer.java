package utils;

import rpcprotocol.SpitalClientRpcWorker;
import service.ISpitalService;

import java.net.Socket;

public class SpitalRpcConcurrentServer extends AbsConcurrentServer {
    private ISpitalService triatlonServer;

    public SpitalRpcConcurrentServer(int port, ISpitalService triatlonServer) {
        super(port);
        this.triatlonServer = triatlonServer;
        System.out.println("Chat- TriatlonRpcConcurrentServer");
    }

    @Override
    protected Thread createWorker(Socket client) {
        SpitalClientRpcWorker worker =new SpitalClientRpcWorker(triatlonServer, client);

        Thread tw= new Thread(worker);
        return tw;
    }

    @Override
    public void stop(){
        System.out.println("Stopping services ...");
    }
}
