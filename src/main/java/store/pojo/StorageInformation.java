package store.pojo;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class StorageInformation {

    String dataOwner;
    String secondOwner;
    String myIp;
//    List<String> primaryFiles;
//    List<String> replica1Files;
//    List<String> replica2Files;
    Map<String, List<String>>  serverStoreInformation;

    public StorageInformation(String myIP){
        List<String> primaryFiles = new ArrayList<>();
        List<String> replica1Files = new ArrayList<>();
        List<String> replica2Files = new ArrayList<>();
        this.serverStoreInformation = new LinkedHashMap<>();
        serverStoreInformation.put("primary",primaryFiles);
        //predecessors rep
        serverStoreInformation.put("replica1",replica1Files);
        //successors rep
        serverStoreInformation.put("replica2",replica2Files);
        this.myIp = myIP;
    }

    public Map<String, List<String>> getServerStoreInformation() {
        return serverStoreInformation;
    }

//    public void setServerStoreInformation(Map<String, List<String>> serverStoreInformation) {
//        serverStoreInformation.put("primary",primaryFiles);
//        serverStoreInformation.put("replica1",replica1Files);
//        serverStoreInformation.put("replica2",replica2Files);
//        this.serverStoreInformation = serverStoreInformation;
//    }

    public void addFilesToMap(String type, List<String> files){

        this.serverStoreInformation.get(type).addAll(files);

    }

    public void addFilesAndRemoveOld(String type, List<String> files){

        this.serverStoreInformation.put(type,files);

    }

    @Override
    public String toString() {
        return "StorageInformation{" +
                "data server='" + myIp + '\'' +
                ", primaryFiles=" + serverStoreInformation.get("primary").toString() +
                ", replica1Files=" + serverStoreInformation.get("replica1").toString() +
                ", replica2Files=" + serverStoreInformation.get("replica2").toString() +

                '}';

    }
    public void printMap(){
        serverStoreInformation.entrySet().stream().forEach(x-> System.out.println(x.getKey() + x.getValue().stream().toString()));
    }


}
