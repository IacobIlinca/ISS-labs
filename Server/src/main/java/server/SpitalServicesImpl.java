package server;

import Repository.*;
import domain.*;
import service.ISpitalObserver;
import service.ISpitalService;
import service.SpitalException;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SpitalServicesImpl implements ISpitalService {
    private IComandaRepository repoComanda;
    private IDateRepository repoData;
    private IFarmacistRepository repoFarmacist;
    private IElementComandaRepository repoElementComanda;
    private IMedicRepository repoMedic;
    private IMedicamentRepository repoMedicament;
    private Map<Integer, ISpitalObserver> loggedClients;

    public SpitalServicesImpl(IComandaRepository repoComanda, IDateRepository repoData, IFarmacistRepository repoFarmacist, IMedicRepository repoMedic, IMedicamentRepository repoMedicament, IElementComandaRepository repoElementComanda) {
        this.repoComanda = repoComanda;
        this.repoData = repoData;
        this.repoFarmacist = repoFarmacist;
        this.repoMedic = repoMedic;
        this.repoMedicament = repoMedicament;
        this.repoElementComanda = repoElementComanda;
        loggedClients = new ConcurrentHashMap<>();
    }


    @Override
    public synchronized void logoutMedic(Medic medic, ISpitalObserver client) throws SpitalException {
        ISpitalObserver localClient = loggedClients.remove(medic.getId());
        if (localClient == null)
            throw new SpitalException("Medic is not logged in.");
    }

    @Override
    public synchronized void logoutFarmacist(Farmacist farm, ISpitalObserver client) throws SpitalException {
        ISpitalObserver localClient = loggedClients.remove(farm.getId());
        if (localClient == null)
            throw new SpitalException("Farmacist is not logged in.");
    }

    @Override
    public synchronized Medic loginMedic(MedicDTO medicDTO, ISpitalObserver client) throws SpitalException {
        Medic medic = repoMedic.getAccount(medicDTO.getEmail(), medicDTO.getParola(), medicDTO.getSectie());
        if (medic != null) {
            if (loggedClients.get(medic.getId()) != null)
                throw new SpitalException("Medic already logged in.");
            loggedClients.put(medic.getId(), client);
            return medic;
        } else
            throw new SpitalException("Authentication failed.");
    }

    @Override
    public synchronized Farmacist loginFarmacist(FarmacistDTO farmacistDTO, ISpitalObserver client) throws SpitalException {
        Farmacist farmacist = repoFarmacist.getAccount(farmacistDTO.getEmail(), farmacistDTO.getParola());
        if (farmacist != null) {
            if (loggedClients.get(farmacist.getId()) != null)
                throw new SpitalException("Farmacist already logged in.");
            loggedClients.put(farmacist.getId(), client);
            return farmacist;
        } else
            throw new SpitalException("Authentication failed.");
    }

    @Override
    public synchronized List<Comanda> findAllComenzi(ISpitalObserver client) throws SpitalException {
        return repoComanda.findAll();
    }

    @Override
    public List<Medicament> findAllMedicamente(ISpitalObserver client) throws SpitalException {
        return repoMedicament.findAll();
    }

    @Override
    public List<Medic> findAllMedici(ISpitalObserver client) throws SpitalException {
        return repoMedic.findAll();
    }

    @Override
    public Medicament findByDenumire(String denumire, ISpitalObserver client) throws SpitalException {
        return repoMedicament.findByDenumire(denumire);
    }

    @Override
    public synchronized void addComanda(ComandaDTO comanda, ISpitalObserver client) throws SpitalException {
        Random rand = new Random();
        Integer id = rand.nextInt(50000) + 1;
        DateTime date = new DateTime(id, comanda.getZi(), comanda.getLuna(), comanda.getAn(), comanda.getOra(), comanda.getMinut());
        repoData.add(date);

        Random rand2 = new Random();
        Integer id2 = rand.nextInt(50000) + 1;
        Comanda cmd = new Comanda(id2, comanda.getSectie(), comanda.getStatusComanda(), date, comanda.getCreator());
        comanda.getCreator().addToComenziPlasate(cmd);
        boolean result = repoComanda.add(cmd);


        Map<Medicament, ElementComandaDTO> map = comanda.getMedicamentElementComandDTOMap();
        var set = map.entrySet();
        for (var p : set) {
            Medicament medic = p.getKey();
            ElementComandaDTO elemDTO = p.getValue();
            Random rand3 = new Random();
            Integer id3 = rand.nextInt(50000) + 1;
            ElementComanda elementComanda = new ElementComanda(id3, elemDTO.getCantitate(), elemDTO.getObservatii(), medic, cmd);
            repoElementComanda.add(elementComanda);
            cmd.addToElementeComanda(elementComanda);
            medic.addToElementeComanda(elementComanda);
        }

        if (result) {
            ExecutorService executor = Executors.newFixedThreadPool(5);
            for (Integer key : loggedClients.keySet()) {
                ISpitalObserver receiver = loggedClients.get(key);
                executor.execute(() -> {
                    try {
                        receiver.updateComanda();
                    } catch (SpitalException e) {
                        System.out.println("Error notifying referee.");
                    }
                });
            }
        } else throw new SpitalException("Comanda exista deja.");
    }
}
