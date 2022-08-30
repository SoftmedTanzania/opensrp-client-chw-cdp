package org.smartregister.chw.cdp;

import org.smartregister.Context;
import org.smartregister.CoreLibrary;
import org.smartregister.chw.cdp.repository.VisitDetailsRepository;
import org.smartregister.chw.cdp.repository.VisitRepository;
import org.smartregister.repository.Repository;
import org.smartregister.sync.ClientProcessorForJava;
import org.smartregister.sync.helper.ECSyncHelper;

import id.zelory.compressor.Compressor;

public class CdpLibrary {
    private static CdpLibrary instance;

    private final Context context;
    private final Repository repository;

    private int applicationVersion;
    private int databaseVersion;
    private ECSyncHelper syncHelper;

    private VisitRepository visitRepository;
    private VisitDetailsRepository visitDetailsRepository;

    private ClientProcessorForJava clientProcessorForJava;
    private Compressor compressor;

    private CdpLibrary(Context contextArg, Repository repositoryArg, int applicationVersion, int databaseVersion) {
        this.context = contextArg;
        this.repository = repositoryArg;
        this.applicationVersion = applicationVersion;
        this.databaseVersion = databaseVersion;
    }

    public static void init(Context context, Repository repository, int applicationVersion, int databaseVersion) {
        if (instance == null) {
            instance = new CdpLibrary(context, repository, applicationVersion, databaseVersion);
        }
    }

    public static CdpLibrary getInstance() {
        if (instance == null) {
            throw new IllegalStateException(" Instance does not exist!!! Call "
                    + CoreLibrary.class.getName()
                    + ".init method in the onCreate method of "
                    + "your Application class ");
        }
        return instance;
    }

    public Context context() {
        return context;
    }

    public Repository getRepository() {
        return repository;
    }

    public int getApplicationVersion() {
        return applicationVersion;
    }

    public int getDatabaseVersion() {
        return databaseVersion;
    }

    public ECSyncHelper getEcSyncHelper() {
        if (syncHelper == null) {
            syncHelper = ECSyncHelper.getInstance(context().applicationContext());
        }
        return syncHelper;
    }

    public ClientProcessorForJava getClientProcessorForJava() {
        if (clientProcessorForJava == null) {
            clientProcessorForJava = ClientProcessorForJava.getInstance(context().applicationContext());
        }
        return clientProcessorForJava;
    }

    public void setClientProcessorForJava(ClientProcessorForJava clientProcessorForJava) {
        this.clientProcessorForJava = clientProcessorForJava;
    }

    public VisitRepository visitRepository() {
        if (visitRepository == null) {
            visitRepository = new VisitRepository();
        }
        return visitRepository;
    }

    public VisitDetailsRepository visitDetailsRepository() {
        if (visitDetailsRepository == null) {
            visitDetailsRepository = new VisitDetailsRepository();
        }
        return visitDetailsRepository;
    }

}
