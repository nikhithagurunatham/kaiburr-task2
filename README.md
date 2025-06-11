
# Kaiburr Task 2 – Docker + Kubernetes Deployment

This is **Task 2** of the Kaiburr recruitment challenge. It extends the Spring Boot API from Task 1 by containerizing the application with Docker and deploying it to a Kubernetes cluster along with MongoDB using persistent volumes.

The `/tasks/{id}/execute` endpoint is now Kubernetes-native — it launches a pod to execute the command using the Kubernetes Java client.

---

## 🛠️ Tech Stack

- **Java 17**
- **Spring Boot**
- **MongoDB**
- **Docker**
- **Kubernetes**
- **Kubernetes Java Client**
- **YAML (for K8s configurations)**

---

## 📦 Features

- Dockerized the Spring Boot backend.
- Created Kubernetes deployments for:
  - Spring Boot app
  - MongoDB (with persistent volume)
- Configured services and volume claims.
- Updated `/tasks/{id}/execute` to:
  - Create a temporary pod dynamically.
  - Run the command inside the pod.
  - Capture and return output via the API.
- Full K8s YAML manifests included.

---

## 📁 Folder Structure

```
task2/
├── src/...
├── Dockerfile
├── k8s/
│   ├── mongo-deployment.yaml
│   ├── mongo-service.yaml
│   ├── mongo-pvc.yaml
│   ├── app-deployment.yaml
│   └── app-service.yaml
├── application.properties
├── README.md
└── demo/
    └── screenshots/
```

---

## ⚙️ How to Build and Deploy

### ✅ 1. Build Docker Image

```bash
docker build -t kaiburr-task2-app .
```

### ✅ 2. Push Docker Image (Optional for Minikube: skip this step)

```bash
docker tag kaiburr-task2-app <your-dockerhub-username>/kaiburr-task2-app
docker push <your-dockerhub-username>/kaiburr-task2-app
```

### ✅ 3. Deploy to Kubernetes

```bash
kubectl apply -f k8s/mongo-pvc.yaml
kubectl apply -f k8s/mongo-deployment.yaml
kubectl apply -f k8s/mongo-service.yaml

kubectl apply -f k8s/app-deployment.yaml
kubectl apply -f k8s/app-service.yaml
```

---

## 🌐 API Endpoint

| Method | Endpoint                 | Description                        |
|--------|--------------------------|------------------------------------|
| POST   | `/tasks/{id}/execute`    | Executes the shell command in a **K8s pod** using the Kubernetes Java API |

---

## 🧪 Sample Execution Flow

1. `POST /tasks` → Add a task with a shell command.
2. `POST /tasks/{id}/execute` → Creates a Kubernetes pod.
3. The pod runs the shell command and is auto-deleted after execution.
4. API returns the command output in the response.

---

## 🖼️ Screenshots

Screenshots of working Kubernetes deployment and API execution are available under:

```
demo/screenshots/
```

Sample:
- Pod creation logs
- Command output via API
- K8s dashboard (if applicable)

---

## 📝 Notes

- Ensure Kubernetes cluster is running (`minikube` or cloud).
- MongoDB volume persists even after pod restarts.
- Kubernetes Java client must be properly configured (`kubeconfig`).

---

## 📬 Author

**Nikhitha Gurunatham**  
Kaiburr Recruitment Challenge – Task 2  
Date: June 2025

---

## 🪄 License

Open-source project under the [MIT License](LICENSE).
