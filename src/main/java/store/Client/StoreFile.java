package store.Client;

import chord.Chord;
import chord.MyServiceGrpc;
import chord.SendReceiveGrpc;
import com.google.protobuf.ByteString;
import io.grpc.ManagedChannel;
import io.grpc.ManagedChannelBuilder;
import io.grpc.Server;
import io.grpc.ServerBuilder;
import io.grpc.stub.StreamObserver;
import store.helper.HashSHA256StoreHelper;
import store.helper.NodeHelper;
import store.helper.RPCFunctions;
import store.helper.TreeStructure;
import store.pojo.Chunk;
import store.pojo.Content;
import store.pojo.FileDetails;
import store.pojo.Node;
import store.start.SendReceiveService;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.util.*;
import java.util.stream.Collectors;

public class StoreFile extends SendReceiveGrpc.SendReceiveImplBase{
    static List<Content> cL = new ArrayList<>();
    static Map<String, FileDetails> fileDetailList = new HashMap<>();
    static boolean checker = false;
    public static void main(String[] args) throws IOException, NoSuchAlgorithmException, InterruptedException {

        Thread serverThread = new Thread(() ->{
            Server server = ServerBuilder.forPort(8085).addService(new StoreFile()).build();
            try {
                server.start();
            } catch (IOException e) {
                e.printStackTrace();

                return;
            }

            try {
                server.awaitTermination();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        serverThread.start();


//        Thread thrd = new Thread(()->{
//           while(checker){
//
////               FileWriter fw=null;
////               try {
////                   Thread.sleep(10000);
////                   Collections.sort(cL, (c1,c2)-> (int) (c1.getEndByte()- c2.getEndByte()));
////                    fw = new FileWriter("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources/fileCreated");
////                   FileWriter finalFw = fw;
////                   cL.stream().forEach(x-> {
////                       try {
////                           System.out.println(x.getData().toString());
////                           finalFw.append(x.getData().toString());
////                           finalFw.close();
////                       } catch (IOException e) {
////                           throw new RuntimeException(e);
////                       }
////                   });
////                   fw.close();
////               } catch (InterruptedException e) {
////                   throw new RuntimeException(e);
////               } catch (IOException e) {
////                   throw new RuntimeException(e);
////               }catch(Exception e){
////
////               }
//               try {
//                   Thread.sleep(10000);
//                  // checkRoothash();
//               } catch (NoSuchAlgorithmException e) {
//                   throw new RuntimeException(e);
//               } catch (IOException e) {
//                   throw new RuntimeException(e);
//               } catch (InterruptedException e) {
//                   throw new RuntimeException(e);
//               }
//           }
//        });
//        //thrd.start();
    }

    @Override
    public void sendData(Chord.sendDataToOwner request, StreamObserver<Chord.acknowledgement> responseObserver) {
        FileDetails fD=null;
        checker =true;
        if(fileDetailList.containsKey(request.getRootHash())){
            fD = fileDetailList.get(request.getRootHash());

        }else{
            fD = new FileDetails(request.getFileName(), 0, request.getRootHash(),new ArrayList<>());

        }
        fD.getContentList().add(new Content(request.getContentHash(),request.getEndOfBlock(),request.getData().toByteArray()));
        cL.add(new Content(request.getContentHash(),request.getEndOfBlock(),request.getData().toByteArray()));
        System.out.println(fD.getContentList());
        Chord.acknowledgement.Builder resp = Chord.acknowledgement.newBuilder();
        responseObserver.onNext(resp.build());
        responseObserver.onCompleted();
        try {
            checkRoothash(request.getRootHash(),request.getFileName());
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public static void checkRoothash(String rootHash,String gilename) throws NoSuchAlgorithmException, IOException {
        System.out.println("inside check roothash");
        List<Content> newList = new ArrayList<>();
        newList.addAll(cL);
        StringBuilder sb = new StringBuilder();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream( );

        Collections.sort(cL, (c1,c2)-> (int) (c1.getEndByte()- c2.getEndByte()));
        newList.stream().forEach(x->sb.append(x.getHash()));
        String rHashChecker = HashSHA256StoreHelper.createHashFromFileContent(sb.toString().getBytes(StandardCharsets.UTF_8));
        if(rHashChecker.equals(rootHash)){
            newList.stream().forEach(x-> {
                try {
                    outputStream.write(x.getData());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(new File("/Users/ajinkyarajguru/Documents/Topics_in_DB/DecentralizedStore/src/main/resources/"+gilename));
                outputStream.writeTo(fos);
            } catch(IOException ioe) {
                // Handle exception here
                ioe.printStackTrace();
            } finally {
                fos.close();
            }
            cL=new ArrayList<>();
        }else{
            System.out.println(" root hash exisitng: " +rootHash);
            System.out.println(" root hash exisitng: " +rHashChecker);
        }
    }
}
