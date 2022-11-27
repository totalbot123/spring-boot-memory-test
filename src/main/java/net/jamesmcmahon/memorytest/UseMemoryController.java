package net.jamesmcmahon.memorytest;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

import static java.lang.Runtime.getRuntime;
import static net.jamesmcmahon.memorytest.Constants.GIGABYTE;
import static net.jamesmcmahon.memorytest.Constants.MEGABYTE;
import static net.jamesmcmahon.memorytest.Stats.printMemoryStats;

@Controller
@RequestMapping("memory")
public class UseMemoryController {

    static List<byte[]> list = new ArrayList<byte[]>();

    @GetMapping("eat-memory")
    public ResponseEntity<Float> eatMemory(
            @RequestParam("steps") int steps,
            @RequestParam("mbs") int mbs,
            @RequestParam("sleep") int sleepSeconds) {
        UseMemoryController.allocateBytes(steps, mbs, sleepSeconds);
        return ResponseEntity.ok().body(getRuntime().freeMemory() / (float) GIGABYTE);
    }

    @GetMapping("free-memory")
    public ResponseEntity<Float> freeMemory(
            @RequestParam("steps") int steps,
            @RequestParam("sleep") int sleepSeconds) {
        UseMemoryController.deallocateBytes(steps, sleepSeconds);
        return ResponseEntity.ok().body(getRuntime().freeMemory() / (float) GIGABYTE);
    }

    @GetMapping("gc")
    public ResponseEntity<Float> gc() {
        System.gc();
        return ResponseEntity.ok().body(getRuntime().freeMemory() / (float) GIGABYTE);
    }

    @GetMapping("print-memory")
    public ResponseEntity printMemory() {
        printMemoryStats();
        return ResponseEntity.ok().build();
    }

    private static List<byte[]> allocateBytes(int steps, int megabytes, int sleepSeconds) {
        for (int i = 0; i < steps; i++) {
            System.out.println(
                    "Free Memory: " +
                            getRuntime().freeMemory() / (float) GIGABYTE +
                            "GB"
            );
            try {
                Thread.sleep(1000 * sleepSeconds);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            UseMemoryController.list.add(new byte[megabytes * MEGABYTE]);
        }
        return UseMemoryController.list;
    }
    
    private static List<byte[]> deallocateBytes(int steps, int sleepSeconds) {
        for (int i = 0; i < Math.min(steps, list.size()); i++) {
            System.out.println(
                "Free Memory: " +
                getRuntime().freeMemory() / (float) GIGABYTE +
                "GB"
                );
            try {
                Thread.sleep(1000 * sleepSeconds);
            } catch (InterruptedException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            UseMemoryController.list.remove(i);
        }
        return UseMemoryController.list;
    }
}
