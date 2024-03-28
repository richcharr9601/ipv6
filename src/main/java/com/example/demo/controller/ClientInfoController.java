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
    public String getClientIPv6FromXForwardedFor(HttpServletRequest request) {
        String ipv6Address = null;

        // Get the X-Forwarded-For header value
        String xForwardedForHeader = request.getHeader("X-Forwarded-For");

        // If the header value is not null and not empty
        if (xForwardedForHeader != null && !xForwardedForHeader.isEmpty()) {
            // Split the header value into individual IP addresses
            String[] ipAddresses = xForwardedForHeader.split(",");

            // Iterate through each IP address
            for (String ipAddress : ipAddresses) {
                ipAddress = ipAddress.trim();
                System.out.println(ipAddresses);
                // Check if the IP address is IPv6
                if (isIPv6Address(ipAddress)) {
                    ipv6Address = ipAddress;
                    break; // Break the loop if an IPv6 address is found
                }
            }
        }

        return ipv6Address;
    }

    // Utility method to check if a string represents an IPv6 address
    private boolean isIPv6Address(String ipAddress) {
        try {
            // Try parsing the IP address
            java.net.InetAddress inetAddress = java.net.InetAddress.getByName(ipAddress);
            return inetAddress instanceof java.net.Inet6Address; // If parsing succeeds, it's IPv6
        } catch (java.net.UnknownHostException e) {
            return false; // If parsing fails, it's not IPv6
        }
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

