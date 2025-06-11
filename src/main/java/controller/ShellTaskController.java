package com.example.demo.controller;

import com.example.demo.model.ShellTaskRequest;
import com.example.demo.model.ShellTaskResult;
import io.kubernetes.client.openapi.ApiClient;
import io.kubernetes.client.openapi.ApiException;
import io.kubernetes.client.openapi.apis.CoreV1Api;
import io.kubernetes.client.openapi.models.*;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.FileReader;
import java.io.IOException;
import java.util.Collections;
import java.util.UUID;

@RestController
@RequestMapping("/api/shell")
public class ShellTaskController {

    private CoreV1Api api;

    public ShellTaskController() throws IOException {
        // Load kubeconfig inside the cluster or from default location (for dev)
        // Use in-cluster config if available, else fallback to ~/.kube/config
        ApiClient client;
        try {
            client = ClientBuilder.cluster().build();  // in-cluster config
        } catch (Exception e) {
            // fallback for local testing
            String kubeConfigPath = System.getenv("KUBECONFIG");
            if (kubeConfigPath == null) {
                kubeConfigPath = System.getProperty("user.home") + "/.kube/config";
            }
            client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
        }
        io.kubernetes.client.openapi.Configuration.setDefaultApiClient(client);
        api = new CoreV1Api(client);
    }

    @PostMapping("/execute")
    public ResponseEntity<ShellTaskResult> executeShellCommand(@RequestBody ShellTaskRequest request) {
        String command = request.getCommand();
        if (command == null || command.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(new ShellTaskResult(null, "Command cannot be empty", false));
        }

        String podName = "shell-cmd-" + UUID.randomUUID().toString().substring(0, 8);

        try {
            // Create a pod spec to run the shell command once and exit
            V1Pod pod = new V1Pod()
                    .metadata(new V1ObjectMeta().name(podName).labels(Collections.singletonMap("app", "shell-cmd")))
                    .spec(new V1PodSpec()
                            .containers(Collections.singletonList(
                                    new V1Container()
                                            .name("shell")
                                            .image("busybox")  // lightweight image with sh shell
                                            .command(Collections.singletonList("sh"))
                                            .args(Collections.singletonList("-c"))
                                            .addArgsItem(command) // command to run
                            ))
                            .restartPolicy("Never")
                    );

            // Create pod in default namespace
            api.createNamespacedPod("default", pod, null, null, null);

            String message = "Pod " + podName + " created to execute command.";
            return ResponseEntity.ok(new ShellTaskResult(podName, message, true));
        } catch (ApiException e) {
            return ResponseEntity.status(500).body(new ShellTaskResult(null, "Kubernetes API error: " + e.getResponseBody(), false));
        }
    }
}
