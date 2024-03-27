package com.example.demo.service;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.Location;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;

@Service
public class GeoLocationService {

    private final DatabaseReader reader;

    public GeoLocationService() throws IOException {
        // Load the GeoLite2 City database file
        File database = new File("src//main//resources/GeoLite2-City.mmdb");
        this.reader = new DatabaseReader.Builder(database).build();
    }

    public String getGeoLocationInfo(String ipAddress, HttpServletRequest request) throws IOException, GeoIp2Exception {
        InetAddress ip = Inet6Address.getByName(ipAddress);
        CityResponse response = reader.city(ip);
        Location location = response.getLocation();
        String realIP = request.getHeader("X-Real-IP");
        String forwardedProto = request.getHeader("X-Forwarded-Proto");
        String forwardedFor = request.getHeader("X-Forwarded-For");

        // Retrieve time zone
        String timeZone = location.getTimeZone();

        // Construct and return information
        return  "Ip: " + ip + ",\n City: "  +
                "Country: " + response.getCountry().getName() +
                ",\n City: " + response.getCity().getName() +
                ",\n Latitude: " + response.getLocation().getLatitude() +
                ",\n Longitude: " + response.getLocation().getLongitude() +
                ",\n Time zone: " + timeZone +
                ",\n Real IP: " + realIP +
                ",\n Forwarded Proto: " + forwardedProto +
                ",\n Forwarded For: " + forwardedFor;
    }
}
