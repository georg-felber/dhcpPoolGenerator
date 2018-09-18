package dhcpPoolGenerator;

import static java.lang.Integer.parseInt;

/**
 * Klasse dhcpPoolGenerator.NetworkData
 *
 * @author gfelber
 * @version 2018-09-17
 */
public class NetworkData {
    private long ip;
    private int subnetmask;
    private int numberOfStaticIps;

    public String getIp() {
        return intToIp((int) this.ip);
    }

    public void setIp(String ip) throws IllegalArgumentException {
        this.ip = ipToInt(ip);
    }


    public int getSubnetmask() {
        return this.subnetmask;
    }

    public void setSubnetmask(String subnetmask) throws IllegalArgumentException {

        int subnetmaskInt = -1;
        boolean ok = true;

        try {
            subnetmaskInt = Integer.parseInt(subnetmask);
        } catch (NumberFormatException nfe) {
            ok = false;
        }

        if (!ok || subnetmaskInt < 0 || subnetmaskInt > 31) {
            throw new IllegalArgumentException("Invalid Subnetmask");
        }

        this.subnetmask = subnetmaskInt;
    }

    public void setNumberOfStaticIps(String numberOfStaticIps) throws IllegalArgumentException {
        int numberOfStaticIpsLong = -1;
        boolean ok = true;

        try {
            numberOfStaticIpsLong = Integer.parseInt(numberOfStaticIps);
            if (numberOfStaticIpsLong < 0 || numberOfStaticIpsLong > (int) Math.pow(2, 32 - this.subnetmask) - 2) {
                ok = false;
            }
        } catch (NumberFormatException nfe) {
            ok = false;
        } catch (NullPointerException npe) {
            ok = false;
        }

        if (!ok) {
            throw new IllegalArgumentException("Invalid Number of static ip adresses");
        }

        this.numberOfStaticIps = numberOfStaticIpsLong;
    }

    public String getStarterIp() {
        return intToIp((int) (this.ip + this.numberOfStaticIps + 1));
    }

    public int getMaximumNumberOfUsers() {
        return (int) Math.pow(2, 32 - this.subnetmask) - this.numberOfStaticIps - 2;
    }

    public boolean validateNetworkIp() {
        int exponent, is, should;
        byte stage = this.subnetmask % 8 == 0 ? (byte) (3 - (this.subnetmask / 8)) :(byte) (3 - this.subnetmask / 8) ;
        boolean ok;

        exponent = 8 - this.subnetmask % 8;

        for (ok = true;ok && stage >= 0; stage--) {
            is = 0;
            ok = false;

            switch (stage) {
                case 0:
                    is =(int) (this.ip & 0xFF);
                    break;
                case 1:
                    is =(int) ((this.ip >> 8) & 0xFF);
                    break;
                case 2:
                    is =(int) ((this.ip >> 16) & 0xFF);
                    break;
                case 3:
                    is =(int) ((this.ip >> 24) & 0xFF);
                    break;
            }


            should = (int) Math.pow(2, exponent);

            for(int i = 0; !ok && should * i < 256; i++){
                ok = is == should * i;
            }

            exponent = 8;
        }

        return ok;
    }

    public static String intToIp(int i) {
        return ((i >> 24) & 0xFF) + "." +
                ((i >> 16) & 0xFF) + "." +
                ((i >> 8) & 0xFF) + "." +
                (i & 0xFF);
    }

    public String toString() {
        return "Network:\t\t\t" + getIp() + "/" + getSubnetmask() +
                "\nStarter-Ip:\t\t\t" + getStarterIp() +
                "\nMaximum Number of Users:\t" + getMaximumNumberOfUsers();
    }

    public static Long ipToInt(String addr) throws IllegalArgumentException {
        String[] addrArray = addr.split("\\.");

        long num = 0;
        for (int i = 0; i < addrArray.length; i++) {
            int power = 3 - i;
            try {
                num += ((parseInt(addrArray[i]) % 256 * Math.pow(256, power)));
            } catch (NumberFormatException nfe) {
                throw new IllegalArgumentException("The entered ip-address is invalid");
            }
        }
        return num;
    }
}
