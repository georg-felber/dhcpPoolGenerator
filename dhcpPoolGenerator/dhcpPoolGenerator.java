package dhcpPoolGenerator;

/**
 * Klasse dhcpPoolGenerator.dhcpPoolGenerator
 *
 * @author gfelber
 * @version 2018-09-17
 */
public class dhcpPoolGenerator {
    public static void main(String[] args) {
        NetworkData networkData = run(args);
        if (networkData != null) {
            System.out.println(networkData.toString());
        }
    }

    public static NetworkData run(String[] args) {
        NetworkData networkData = new NetworkData();
        boolean ok = false;

        try {
            networkData.setIp(args[0]);
            networkData.setSubnetmask(args[1]);
            networkData.setNumberOfStaticIps(args[2]);
            ok = true;
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            System.out.println("You need to enter a network ip, a subnetmask and the maximum number of useres");
        } catch (IllegalArgumentException iae) {
            System.out.println(iae.getMessage());
        }
        if(!networkData.validateNetworkIp()){
            System.out.println("Invalid Network Ip and Subnetmask combination");
            ok = false;
        }

        if (ok) {
            return networkData;
        } else {
            return null;
        }
    }
}
