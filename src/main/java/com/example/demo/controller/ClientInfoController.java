package com.example.demo.controller;
import com.example.demo.service.GeoLocationService;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.maxmind.geoip2.exception.GeoIp2Exception;

//for java 17
import jakarta.servlet.http.HttpServletRequest;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

//for java 8
//import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.net.Inet6Address;
import java.net.InetAddress;
import java.net.UnknownHostException;


@Component
@RestController
@EnableAutoConfiguration
public class ClientInfoController {

    private final GeoLocationService geoLocationService;

    @Autowired
    public ClientInfoController(GeoLocationService geoLocationService) {
        this.geoLocationService = geoLocationService;
    }


    public String getIpV6(){
        HttpClient httpClient = HttpClientBuilder.create().build();
        HttpGet httpGet = new HttpGet("https://api6.ipify.org?format=json");
        try {
            HttpResponse response = httpClient.execute(httpGet);
            String jsonResponse = EntityUtils.toString(response.getEntity());
            // System.out.println("JSON Response: " + jsonResponse);

            Gson gson = new Gson();
            JsonObject jsonObject = gson.fromJson(jsonResponse, JsonObject.class);

            String ip = jsonObject.get("ip").getAsString();
            // System.out.println("IP: " + ip);
            return ip;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    @GetMapping("/client-info")
    public String getClientInfo(HttpServletRequest request) {
        String ipAddress = getIpV6();
        // System.out.println(ipAddress);
//        String ipAddress = request.getRemoteAddr();

        try {
            return geoLocationService.getGeoLocationInfo(ipAddress, request);
        } catch (IOException | GeoIp2Exception e) {
            e.printStackTrace();
            return "Error retrieving geolocation information";
        }
    }

    @GetMapping("/ipv62")
    public String getIpv6FromXForwardedFor(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
    
        if (xForwardedFor == null || xForwardedFor.isEmpty()) {
            return null; // No header present
        }
    
        // Split the comma-separated values
        String[] addresses = xForwardedFor.split(",");
    
        // Check each address from right to left (most recent proxy first)
        for (int i = addresses.length - 1; i >= 0; i--) {
            String address = addresses[i].trim(); // Remove leading/trailing whitespaces
    
            // Handle IPv6 wrapped in brackets with optional prefix
            if (address.startsWith("[") && address.endsWith("]")) {
                address = address.substring(1, address.length() - 1); // Extract inner content
                if (address.startsWith("::ffff:")) {
                    address = address.substring(7); // Remove prefix for IPv6 compatibility
                }
            }
    
            try {
                // Attempt to parse as IPv6 address (using a validated library)
                InetAddress inetAddress = InetAddress.getByName(address);
                if (inetAddress instanceof Inet6Address) {
                    return address; // Valid IPv6 address found
                }
            } catch (UnknownHostException e) {
                // Ignore parsing errors (could be IPv4 or invalid format)
            }        
            System.out.println(address);
        }
        // No valid IPv6 found in the chain
        return null;
    }

    @GetMapping("/ipv6")
    public String getClientIPv6(HttpServletRequest request) {
        // Lấy giá trị của header X-Forwarded-For
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");
        // System.out.println(request.getRemoteAddr());
        
        // Kiểm tra và xử lý giá trị của header X-Forwarded-For để lấy địa chỉ IPv6
        // String[] ips = xForwardedForHeader.split(",");
        
        String ipv6Address = "";
        String ipAddress = request.getHeader("X-Forwarded-For");
        if(ipAddress == null || ipAddress.isEmpty() || "unknown".equalsIgnoreCase(ipAddress)) {
            ipAddress = request.getRemoteAddr();
            System.out.println(ipAddress);
        }
        
        return ipv6Address;
    }

}

