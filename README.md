# Environment Build Process
_____

1. **Download and install Docker & Android Studio**
    + **Docker:** https://www.docker.com/products/docker-desktop
    + **Android Studio:** https://developer.android.com/studio

2. **Start Docker containers**
    ```bash
    cd docker
    docker-compose up -d
    ```
    ```bash
    Creating network "docker_default" with the default driver
    Creating docker_influxdb_1 ... done
    Creating docker_grafana_1  ... done
    Creating docker_python_1   ... done
    Creating docker_app_1      ... done
    ```

3. **Accessing Grafana Dashboards**
    + **URL:** http://localhost:3000
    + **Username:** demo
    + **Password:** demo

4. **Stop Docker containers**
    ```bash
    docker-compose down
    ```
    ```bash
    Stopping docker_app_1      ... done
    Stopping docker_python_1   ... done
    Stopping docker_grafana_1  ... done
    Stopping docker_influxdb_1 ... done
    Removing docker_app_1      ... done
    Removing docker_python_1   ... done
    Removing docker_grafana_1  ... done
    Removing docker_influxdb_1 ... done
    Removing network docker_default
    ```



