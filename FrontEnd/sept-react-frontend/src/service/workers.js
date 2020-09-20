import axios from "axios";
import authHeader from "./auth-header";

// For local development, this is hardcoded in at the moment
const API_URL = "http://localhost:8080/api/v1/worker/";

class Workers {
    viewWorkers() {
        return axios
            .get(API_URL, {
                headers: authHeader()
            })
            .then(response => {
                return response.data;
            })
            .catch(error => {
                console.log(error);
            });
    }
}

export default new Workers();