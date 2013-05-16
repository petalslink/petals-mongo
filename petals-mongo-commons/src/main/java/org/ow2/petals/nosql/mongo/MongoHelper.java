package org.ow2.petals.nosql.mongo;

import com.mongodb.*;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

/**
 * chamerling - chamerling@linagora.com
 */
public class MongoHelper {

    private MongoHelper() {
    }

    /**
     * Returns a List of ServerAddress objects for each host specified in the
     * hostname property. Returns an empty list if configuration is detected to
     * be invalid, e.g.:
     * <ul>
     * <li>Port property doesn't contain either one port or one port per host</li>
     * <li>After parsing port property to integers, there isn't either one port
     * or one port per host</li>
     * </ul>
     *
     * @param hostname
     *            Blank space delimited hostnames
     * @param port
     *            Blank space delimited ports. Must specify one port for all
     *            hosts or a port per host.
     * @return List of ServerAddresses to connect to
     */
    public static List<ServerAddress> getServerAddresses(String hostname, String port) {
        List<ServerAddress> addresses = new ArrayList<ServerAddress>();

        String[] hosts = hostname.split(" ");
        String[] ports = port.split(" ");

        if (ports.length != 1 && ports.length != hosts.length) {
            // errorHandler
            // .error("MongoDB appender port property must contain one port or a port per host",
            // null, ErrorCode.ADDRESS_PARSE_FAILURE);
        } else {
            List<Integer> portNums = getPortNums(ports);
            // Validate number of ports again after parsing
            if (portNums.size() != 1 && portNums.size() != hosts.length) {
                // error("MongoDB appender port property must contain one port or a valid port per host",
                // null, ErrorCode.ADDRESS_PARSE_FAILURE);
            } else {
                boolean onePort = (portNums.size() == 1);

                int i = 0;
                for (String host : hosts) {
                    int portNum = (onePort) ? portNums.get(0) : portNums.get(i);
                    try {
                        addresses.add(new ServerAddress(host.trim(), portNum));
                    } catch (UnknownHostException e) {
                        // errorHandler
                        // .error("MongoDB appender hostname property contains unknown host",
                        // e, ErrorCode.ADDRESS_PARSE_FAILURE);
                    }
                    i++;
                }
            }
        }
        return addresses;
    }

    /**
     * Get ports
     * @param ports
     * @return
     */
    public static List<Integer> getPortNums(String[] ports) {
        List<Integer> portNums = new ArrayList<Integer>();

        for (String port : ports) {
            try {
                Integer portNum = Integer.valueOf(port.trim());
                if (portNum < 0) {
                    // errorHandler
                    // .error("MongoDB appender port property can't contain a negative integer",
                    // null, ErrorCode.ADDRESS_PARSE_FAILURE);
                } else {
                    portNums.add(portNum);
                }
            } catch (NumberFormatException e) {
                // errorHandler
                // .error("MongoDB appender can't parse a port property value into an integer",
                // e, ErrorCode.ADDRESS_PARSE_FAILURE);
            }

        }
        return portNums;
    }

    public static void clearCollection(DBCollection collection) {
        WriteResult wr = collection.remove(new BasicDBObject());
    }

    /**
     *
     * @param addresses
     * @return
     */
    public static Mongo getMongo(List<ServerAddress> addresses) {
        if (addresses.size() == 1) {
            return new Mongo(addresses.get(0));
        } else {
            // Replica set
            return new Mongo(addresses);
        }
    }
}
