package store.helper;

import java.math.BigInteger;

public class CalcHelper {

    public static BigInteger getBigInt(String hashHex){
        BigInteger hashInt = new BigInteger(hashHex, 16);
        return hashInt;
    }

    public static BigInteger calculateRelID(BigInteger nDSuccessor_id, BigInteger nDId) {
        BigInteger ret = nDSuccessor_id.subtract(nDId);
        if(ret.compareTo(BigInteger.ZERO) !=1) ret=ret.add(BigInteger.valueOf(2).pow(256));
        return ret;
    }

    public static long calculateRelID32(long nDSuccessor_id, long nDId) {
        double ret = nDSuccessor_id - nDId;
        if(ret <=0) ret=ret +(Math.pow(2,32));
        return (long)ret;
    }
}
