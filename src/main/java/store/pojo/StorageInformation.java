package store.pojo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StorageInformation {

    String dataOwner;
    String secondOwner;
    List<String> primaryFiles;
    List<String> replica1Files;
    List<String> replica2Files;
    Map<String, List<String>>  serverStoreInformation;

    public StorageInformation(){
        this.primaryFiles = new ArrayList<>();
        this.replica1Files = new ArrayList<>();
        this.replica2Files = new ArrayList<>();
        this.serverStoreInformation = new LinkedHashMap<>();
        serverStoreInformation.put("primary",primaryFiles);
        serverStoreInformation.put("replica1",replica1Files);
        serverStoreInformation.put("replica2",replica2Files);
    }

    public Map<String, List<String>> getServerStoreInformation() {
        return serverStoreInformation;
    }

    public void setServerStoreInformation(Map<String, List<String>> serverStoreInformation) {
        this.serverStoreInformation = serverStoreInformation;
    }
}
