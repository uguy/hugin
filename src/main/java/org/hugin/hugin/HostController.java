package org.hugin.hugin;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Map;

@Slf4j
@RestController
@RequestMapping("/api")
public class HostController {

    @GetMapping("/host")
    public Map<String, Object> host() {
        return Map.of(
                "hostname", getHostname(),
                "ip", getIp()
        );
    }

    private static String getIp() {
        String ip = "N/A";
        try (final DatagramSocket socket = new DatagramSocket()) {
            socket.connect(InetAddress.getByName("8.8.8.8"), 10002);
            ip = socket.getLocalAddress().getHostAddress();
        } catch (Exception e) {
            log.error("Failed to read IP address", e);
        }
        return ip;
    }

    private static String getHostname() {
        String hostname = "N/A";
        var process = new ProcessBuilder("hostname");
        try (var reader = new BufferedReader(new InputStreamReader(process.start().getInputStream()))) {
            hostname = reader.readLine();
        } catch (Exception e) {
            log.error("Failed to read hostname", e);
        }
        return hostname;
    }
}
