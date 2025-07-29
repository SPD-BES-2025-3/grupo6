package br.com.ufg.odm.dataSync.queue;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/queue")
@RequiredArgsConstructor
public class QueueManagementController {

    private final OperationQueueService queueService;

    @GetMapping("/status")
    public ResponseEntity<Map<String, Object>> getQueueStatus() {
        Map<String, Object> status = new HashMap<>();
        status.put("queueSize", queueService.getQueueSize());
        status.put("processingSize", queueService.getProcessingSize());
        status.put("failedSize", queueService.getFailedSize());

        return ResponseEntity.ok(status);
    }

    @GetMapping("/failed")
    public ResponseEntity<List<String>> getFailedOperations() {
        return ResponseEntity.ok(queueService.getFailedOperations());
    }

    @PostMapping("/retry-failed")
    public ResponseEntity<Map<String, String>> retryFailedOperations() {
        queueService.requeueFailedOperations();

        Map<String, String> response = new HashMap<>();
        response.put("message", "Operações falhadas recolocadas na fila com sucesso");

        return ResponseEntity.ok(response);
    }
}
